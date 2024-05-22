package com.anhtester.helpers;

import com.anhtester.constants.FrameworkConstants;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecorderHelpers extends ScreenRecorder {

    private String fileName;
    private File currentFile;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

    public ScreenRecorderHelpers() throws IOException, AWTException {
        super(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(),
                new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height),
                new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null,
                new File("./" + FrameworkConstants.EXPORT_VIDEO_PATH + "/"));
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }

        currentFile = getFileWithUniqueName(movieFolder.getAbsolutePath() + File.separator + fileName + "_" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
        return currentFile;
    }

    private File getFileWithUniqueName(String fileName) {
        String extension = "";
        String name = "";

        int idxOfDot = fileName.lastIndexOf('.'); // Get the last index of . to separate extension
        extension = fileName.substring(idxOfDot + 1);
        name = fileName.substring(0, idxOfDot);

        Path path = Paths.get(fileName);
        int counter = 1;
        while (Files.exists(path)) {
            fileName = name + "-" + counter + "." + extension;
            path = Paths.get(fileName);
            counter++;
        }
        return new File(fileName);
    }

    public void startRecording(String fileName) {
        this.fileName = fileName;
        try {
            start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopRecording(boolean keepFile) {
        try {
            stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!keepFile) {
            deleteRecording();
        }
    }

    private void deleteRecording() {
        boolean deleted = false;
        try {
            if (currentFile.exists()) {
                deleted = currentFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (deleted)
            currentFile = null;
        else
            System.out.println("Could not delete the screen record!");
    }


}
