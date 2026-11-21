/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.utils.LogUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Take screenshots (whole page or screen). Video recording is done by {@link ScreenRecorderHelpers}.
 */
public class CaptureHelpers {

    // DateTimeFormatter is thread-safe (SimpleDateFormat is not: screenshots are taken by parallel tests)
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");

    private CaptureHelpers() {
    }

    public static void captureScreenshot(WebDriver driver, String screenName) {
        if (driver == null) {
            LogUtils.debug("Skip screenshot '" + screenName + "': no browser on this thread.");
            return;
        }
        try {
            String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXPORT_CAPTURE_PATH;
            File file = new File(path);
            if (!file.exists()) {
                LogUtils.info("No Folder: " + path);
                file.mkdirs();
                LogUtils.info("Folder created: " + file);
            }

            LogUtils.debug("Driver for Screenshot: " + driver);
            // Create reference of TakesScreenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            // Call the capture screenshot function - getScreenshotAs
            File source = ts.getScreenshotAs(OutputType.FILE);
            // result.getName() gets the name of the test case and assigns it to the screenshot file name
            FileUtils.copyFile(source, new File(path + "/" + screenName + "_" + dateFormat.format(LocalDateTime.now()) + ".png"));
            LogUtils.info("Screenshot taken: " + screenName);
            LogUtils.info("Screenshot taken current URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            LogUtils.warn("Exception while taking screenshot: " + e.getMessage(), e);
        }
    }

    public static File getScreenshotFile(String screenshotName) {
        Rectangle allScreenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SSS").format(new Date());
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(allScreenBounds);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            LogUtils.info("Folder created: " + folder);
        }

        String filePath = path + File.separator + screenshotName + dateName + ".png";
        File file = new File(filePath);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static String getScreenshotRelativePath(String screenshotName) {
        Rectangle allScreenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SSS").format(new Date());
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(allScreenBounds);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images";

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            LogUtils.info("Folder created: " + folder);
        }

        String filePath = path + File.separator + screenshotName + dateName + ".png";

        File file = new File(filePath);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filePathRelative = FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images" + File.separator + screenshotName + dateName + ".png";
        return filePathRelative;
    }

    public static String getScreenshotAbsolutePath(String screenshotName) {
        Rectangle allScreenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SSS").format(new Date());
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(allScreenBounds);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images";

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            LogUtils.info("Folder created: " + folder);
        }

        String filePath = path + File.separator + screenshotName + dateName + ".png";

        File file = new File(filePath);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filePath;
    }


}
