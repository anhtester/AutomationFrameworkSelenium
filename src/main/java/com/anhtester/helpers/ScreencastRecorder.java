/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.utils.LogUtils;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.Event;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.json.Json;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Record a video of the browser tab with the Chrome DevTools Protocol (Page.startScreencast) and save it as MP4.
 * <p>
 * Works with Chrome and Edge, also in headless mode, on CI and on Selenium Grid: no screen and no ffmpeg needed
 * (frames are encoded in Java with JCodec). One recorder per thread, so parallel tests have their own video.
 * <p>
 * Usage: {@link #start(String)} when the test starts, {@link #stop(boolean)} when it ends. {@code stop} can be called
 * several times (TestNG listener and Allure listener): the first call saves the video, the next ones return the same file.
 */
public final class ScreencastRecorder {

    private static final int FPS = 5;
    private static final int MAX_WIDTH = 1280;
    private static final int MAX_HEIGHT = 720;
    /**
     * Longest video (seconds): longer recordings are sampled to keep the encoding time small
     */
    private static final int MAX_SECONDS = 600;

    private static final ThreadLocal<ScreencastRecorder> CURRENT = new ThreadLocal<>();
    private static final ThreadLocal<File> LAST_VIDEO = new ThreadLocal<>();
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");

    private final String name;
    private final DevTools devTools;
    private final List<Frame> frames = Collections.synchronizedList(new ArrayList<>());
    private final ExecutorService ackExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "screencast-ack");
        thread.setDaemon(true);
        return thread;
    });
    private volatile boolean recording;

    private static final class Frame {
        private final long timestampMillis;
        private final byte[] jpeg;

        private Frame(long timestampMillis, byte[] jpeg) {
            this.timestampMillis = timestampMillis;
            this.jpeg = jpeg;
        }
    }

    private ScreencastRecorder(String name, DevTools devTools) {
        this.name = name;
        this.devTools = devTools;
    }

    /**
     * @param driver the driver
     * @return true for Chrome / Edge drivers with DevTools (local or Grid)
     */
    public static boolean isSupported(WebDriver driver) {
        if (!(driver instanceof HasDevTools) || !(driver instanceof HasCapabilities)) {
            return false;
        }
        Capabilities capabilities = ((HasCapabilities) driver).getCapabilities();
        String browser = String.valueOf(capabilities.getBrowserName()).toLowerCase(Locale.ROOT);
        return browser.contains("chrome") || browser.contains("edge");
    }

    /**
     * Start recording the browser of the current thread.
     *
     * @param testName used for the video file name
     * @return true when the recording started
     */
    public static boolean start(String testName) {
        stopQuietly();
        LAST_VIDEO.remove();
        WebDriver driver = DriverManager.getDriver();
        if (!isSupported(driver)) {
            LogUtils.info("Video by screencast is not supported for this browser: " + DriverManager.getInfo());
            return false;
        }
        try {
            DevTools devTools = ((HasDevTools) driver).getDevTools();
            devTools.createSessionIfThereIsNotOne();
            ScreencastRecorder recorder = new ScreencastRecorder(SystemHelpers.makeSlug(testName), devTools);
            recorder.begin();
            CURRENT.set(recorder);
            LogUtils.info("Video recording started (screencast): " + testName);
            return true;
        } catch (Exception e) {
            LogUtils.warn("Can not start the video recording: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Stop the recording of the current thread.
     *
     * @param keep true: encode and save the MP4 file, false: discard the frames
     * @return the MP4 file, or null when nothing was recorded / kept
     */
    public static File stop(boolean keep) {
        ScreencastRecorder recorder = CURRENT.get();
        if (recorder == null) {
            return LAST_VIDEO.get();
        }
        CURRENT.remove();
        File video = recorder.end(keep);
        if (video != null) {
            LAST_VIDEO.set(video);
        }
        return video;
    }

    /**
     * @return true when a recording is running on the current thread
     */
    public static boolean isRecording() {
        return CURRENT.get() != null;
    }

    /**
     * Stop a running recording without saving it and forget the last video of the current thread
     * (called before quitting the browser, so the next test never gets the video of this one).
     */
    public static void reset() {
        stopQuietly();
        LAST_VIDEO.remove();
    }

    /**
     * Stop a running recording without saving it (before quitting the browser).
     */
    public static void stopQuietly() {
        if (CURRENT.get() != null) {
            stop(false);
        }
    }

    private void begin() {
        devTools.addListener(new Event<Map<String, Object>>("Page.screencastFrame", input -> input.read(Json.MAP_TYPE)), this::onFrame);
        devTools.send(new Command<Void>("Page.startScreencast", Map.of(
                "format", "jpeg",
                "quality", 60,
                "maxWidth", MAX_WIDTH,
                "maxHeight", MAX_HEIGHT,
                "everyNthFrame", 1)));
        recording = true;
    }

    @SuppressWarnings("unchecked")
    private void onFrame(Map<String, Object> params) {
        if (!recording) {
            return;
        }
        try {
            Object sessionId = params.get("sessionId");
            // The browser sends the next frame only after the acknowledgment: never block the event thread
            ackExecutor.submit(() -> {
                try {
                    devTools.send(new Command<Void>("Page.screencastFrameAck", Map.of("sessionId", sessionId)));
                } catch (Exception ignored) {
                    // the session may be closed at the end of the test
                }
            });
            Map<String, Object> metadata = (Map<String, Object>) params.get("metadata");
            long timestamp = metadata != null && metadata.get("timestamp") instanceof Number
                    ? (long) (((Number) metadata.get("timestamp")).doubleValue() * 1000)
                    : System.currentTimeMillis();
            frames.add(new Frame(timestamp, Base64.getDecoder().decode((String) params.get("data"))));
        } catch (Exception e) {
            LogUtils.debug("Can not read a screencast frame: " + e.getMessage());
        }
    }

    private File end(boolean keep) {
        recording = false;
        try {
            devTools.send(new Command<Void>("Page.stopScreencast", Map.of()));
        } catch (Exception e) {
            LogUtils.debug("Can not stop the screencast (session closed?): " + e.getMessage());
        }
        try {
            devTools.clearListeners();
        } catch (Exception ignored) {
            // nothing to clean
        }
        ackExecutor.shutdownNow();

        List<Frame> snapshot;
        synchronized (frames) {
            snapshot = new ArrayList<>(frames);
            frames.clear();
        }
        if (!keep) {
            LogUtils.debug("Video not kept (" + snapshot.size() + " frames discarded): " + name);
            return null;
        }
        if (snapshot.isEmpty()) {
            LogUtils.warn("No frame recorded for the video: " + name);
            return null;
        }
        try {
            long start = System.currentTimeMillis();
            File file = encode(snapshot);
            LogUtils.info(String.format("Video saved: %s (%d frames, encoded in %d ms)", file.getPath(), snapshot.size(), System.currentTimeMillis() - start));
            return file;
        } catch (Exception e) {
            LogUtils.warn("Can not encode the video " + name + ": " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Build a constant frame rate MP4: for every 1/FPS second, use the last frame received before that time.
     */
    private File encode(List<Frame> snapshot) throws IOException {
        File folder = new File(SystemHelpers.getCurrentDir() + FrameworkConstants.EXPORT_VIDEO_PATH);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Can not create folder " + folder);
        }
        File file = new File(folder, name + "_" + LocalDateTime.now().format(FILE_DATE) + ".mp4");

        snapshot.sort((a, b) -> Long.compare(a.timestampMillis, b.timestampMillis));
        long first = snapshot.get(0).timestampMillis;
        long last = snapshot.get(snapshot.size() - 1).timestampMillis + 1000; // keep the last state 1 second
        long step = 1000L / FPS;
        long totalSteps = (last - first) / step + 1;
        if (totalSteps > (long) MAX_SECONDS * FPS) {
            step = (last - first) / ((long) MAX_SECONDS * FPS) + 1;
        }

        AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(file, FPS);
        try {
            int index = 0;
            BufferedImage current = null;
            int width = 0;
            int height = 0;
            for (long time = first; time <= last; time += step) {
                boolean changed = false;
                while (index < snapshot.size() && snapshot.get(index).timestampMillis <= time) {
                    index++;
                    changed = true;
                }
                if (changed || current == null) {
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(snapshot.get(Math.max(0, index - 1)).jpeg));
                    if (image == null) {
                        continue;
                    }
                    if (width == 0) {
                        // H.264 (YUV 4:2:0) needs even dimensions
                        width = image.getWidth() & ~1;
                        height = image.getHeight() & ~1;
                    }
                    current = toEncoderImage(image, width, height);
                }
                encoder.encodeImage(current);
            }
        } finally {
            encoder.finish();
        }
        return file;
    }

    /**
     * Copy into an RGB image with the size of the video (the tab can be resized during the test)
     */
    private static BufferedImage toEncoderImage(BufferedImage source, int width, int height) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        target.getGraphics().drawImage(source, 0, 0, width, height, null);
        return target;
    }
}
