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
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import static com.anhtester.constants.FrameworkConstants.EXPORT_VIDEO_PATH;
import static org.openqa.selenium.OutputType.BYTES;

public class AllureManager {

    private AllureManager() {
    }

    /**
     * Folder of the Allure results (surefire sets allure.results.directory=target/allure-results)
     */
    public static Path getResultsDirectory() {
        return Paths.get(System.getProperty("allure.results.directory", "target/allure-results"));
    }

    /**
     * Base environment information shown in the "Environment" widget of the Allure report.
     */
    public static Map<String, String> getEnvironmentInformation() {
        Map<String, String> environment = new LinkedHashMap<>();
        environment.put("Test URL", FrameworkConstants.URL_CMS_USER);
        environment.put("Target Execution", FrameworkConstants.TARGET);
        environment.put("Browser", TargetFactory.resolveBrowser(null));
        environment.put("Headless Mode", FrameworkConstants.HEADLESS);
        environment.put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT));
        environment.put("Page Load Timeout", String.valueOf(FrameworkConstants.WAIT_PAGE_LOADED));
        environment.put("Remote URL", String.valueOf(FrameworkConstants.REMOTE_URL));
        environment.put("Remote Port", String.valueOf(FrameworkConstants.REMOTE_PORT));
        environment.put("Java", System.getProperty("java.version"));
        environment.put("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        return environment;
    }

    /**
     * Write environment.properties into the Allure results folder (replaces the old allure-environment-writer library).
     *
     * @param environment key/value pairs, kept in insertion order
     */
    public static void writeEnvironment(Map<String, String> environment) {
        Path file = getResultsDirectory().resolve("environment.properties");
        // java.util.Properties escapes keys/values correctly (spaces in keys, backslashes, line breaks)
        Properties properties = new Properties() {
            // keep the insertion order of the map in the file
            private final Set<Object> orderedKeys = new LinkedHashSet<>();

            @Override
            public synchronized Object put(Object key, Object value) {
                orderedKeys.add(key);
                return super.put(key, value);
            }

            @Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(orderedKeys);
            }

            @Override
            public Set<Map.Entry<Object, Object>> entrySet() {
                Set<Map.Entry<Object, Object>> entries = new LinkedHashSet<>();
                orderedKeys.forEach(key -> entries.add(new AbstractMap.SimpleEntry<>(key, get(key))));
                return entries;
            }
        };
        environment.forEach((key, value) -> properties.put(key, value == null ? "" : value));
        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                properties.store(writer, "Allure environment");
            }
        } catch (IOException e) {
            LogUtils.warn("Can not write Allure environment file " + file + ": " + e.getMessage(), e);
        }
    }

    public static void setAllureEnvironmentInformation() {
        writeEnvironment(getEnvironmentInformation());
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
