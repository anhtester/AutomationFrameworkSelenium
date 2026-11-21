/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.reports;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.driver.TargetFactory;
import com.anhtester.helpers.DiagnosticHelpers;
import com.anhtester.helpers.FileHelpers;
import com.anhtester.utils.BrowserInfoUtils;
import com.anhtester.utils.LogUtils;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.anhtester.constants.FrameworkConstants.EXPORT_VIDEO_PATH;
import static org.openqa.selenium.OutputType.BYTES;

public class AllureManager {

    private AllureManager() {
    }

    public static void setAllureEnvironmentInformation() {
        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder().
                        put("Test URL", FrameworkConstants.URL_CRM).
                        put("Target Execution", FrameworkConstants.TARGET).
                        put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT)).
                        put("Page Load Timeout", String.valueOf(FrameworkConstants.WAIT_PAGE_LOADED)).
                        put("Headless Mode", FrameworkConstants.HEADLESS).
                        put("Local Browser", TargetFactory.resolveBrowser(null)).
                        put("Remote URL", FrameworkConstants.REMOTE_URL).
                        put("Remote Port", FrameworkConstants.REMOTE_PORT).
                        build());

        LogUtils.info("Allure Reports is installed.");

    }

    @Attachment(value = "Failed test Screenshot", type = "image/png")
    public static byte[] takeScreenshotToAttachOnAllureReport() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(BYTES);
        } catch (Exception ex) {
            LogUtils.warn("Can not take screenshot for Allure: " + ex.getMessage());
        }
        return new byte[0];
    }

    @Attachment(value = "Step Screenshot", type = "image/png")
    public static byte[] takeScreenshotStep() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(BYTES);
        } catch (Exception ex) {
            LogUtils.warn("Can not take step screenshot for Allure: " + ex.getMessage());
        }
        return new byte[0];
    }

    @Attachment(value = "Browser Information", type = "text/plain")
    public static String addBrowserInformationOnAllureReport() {
        return BrowserInfoUtils.getOSInfo();
    }


    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    //HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    public static void addAttachmentVideoAVI() {
        if (FrameworkConstants.VIDEO_RECORD.toLowerCase().trim().equals(FrameworkConstants.YES)) {
            try {
                //Get file Last Modified in folder
                File video = FileHelpers.getFileLastModified(EXPORT_VIDEO_PATH);
                if (video != null) {
                    Allure.addAttachment("Video record AVI", "video/avi", new FileInputStream(video), "avi");
                } else {
                    LogUtils.warn("Video record not found.");
                    LogUtils.warn("Can not attachment Video in Allure reports");
                }

            } catch (IOException e) {
                LogUtils.error("Can not attachment Video in Allure reports", e);
            }
        }
    }

    public static void addAttachmentVideoMP4() {
        try {
            //Get file Last Modified in folder
            File video = FileHelpers.getFileLastModified(EXPORT_VIDEO_PATH);
            //File video = new File("ExportData/Videos/SampleVideo.mp4");
            if (video != null) {
                Allure.addAttachment("Video record MP4", "video/mp4", new FileInputStream(video), "mp4");
            } else {
                LogUtils.warn("Video record not found.");
                LogUtils.warn("Can not attachment Video in Allure reports");
            }

        } catch (IOException e) {
            LogUtils.error("Can not attachment Video in Allure reports", e);
        }
    }

    /**
     * Attach the browser state (URL, page source, console logs) and the test log to the current Allure test.
     *
     * @param diagnostics browser diagnostics, can be null when there is no driver
     * @param testLog     content of the per-test log file, can be empty
     */
    public static void attachFailureDiagnostics(DiagnosticHelpers.Diagnostics diagnostics, String testLog) {
        try {
            if (diagnostics != null) {
                Allure.addAttachment("Failure info", "text/plain", diagnostics.summary(), ".txt");
                if (!diagnostics.pageSource.isEmpty()) {
                    Allure.addAttachment("Page source", "text/html", diagnostics.pageSource, ".html");
                }
                if (!diagnostics.consoleLogs.isEmpty()) {
                    Allure.addAttachment("Browser console logs", "text/plain", diagnostics.consoleLogs, ".txt");
                }
            }
            if (testLog != null && !testLog.isEmpty()) {
                Allure.addAttachment("Test log", "text/plain", testLog, ".log");
            }
        } catch (Exception e) {
            LogUtils.warn("Can not attach failure diagnostics to Allure: " + e.getMessage(), e);
        }
    }

}
