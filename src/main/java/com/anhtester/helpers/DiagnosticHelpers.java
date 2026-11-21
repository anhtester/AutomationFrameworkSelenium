/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import com.anhtester.driver.DriverManager;
import com.anhtester.utils.LogUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.time.Instant;
import java.util.logging.Level;

/**
 * Collect information about the browser state when a test fails:
 * current URL, page title, browser info, page source and browser console logs.
 * <p>
 * Browser console logs can be read only once (the driver clears its buffer), so the result is
 * collected once per test and cached on the current thread. Call {@link #reset()} when a test starts.
 * Every method is safe: when the session is dead or the browser does not support it, an empty value is returned.
 */
public final class DiagnosticHelpers {

    private static final ThreadLocal<Diagnostics> CACHE = new ThreadLocal<>();

    private DiagnosticHelpers() {
    }

    /**
     * Snapshot of the browser state.
     */
    public static final class Diagnostics {
        public final String url;
        public final String title;
        public final String browser;
        public final String pageSource;
        public final String consoleLogs;
        public final int consoleErrorCount;

        Diagnostics(String url, String title, String browser, String pageSource, String consoleLogs, int consoleErrorCount) {
            this.url = url;
            this.title = title;
            this.browser = browser;
            this.pageSource = pageSource;
            this.consoleLogs = consoleLogs;
            this.consoleErrorCount = consoleErrorCount;
        }

        /**
         * @return short multi-line summary for logs and reports
         */
        public String summary() {
            return "URL: " + url + System.lineSeparator()
                    + "Title: " + title + System.lineSeparator()
                    + "Browser: " + browser + System.lineSeparator()
                    + "Browser console errors: " + consoleErrorCount;
        }
    }

    /**
     * Forget the cached diagnostics of the current thread (call it when a test starts).
     */
    public static void reset() {
        CACHE.remove();
    }

    /**
     * Collect diagnostics of the current driver once per test (cached on the current thread).
     *
     * @return the diagnostics, or null when there is no driver
     */
    public static Diagnostics collectOnce() {
        Diagnostics cached = CACHE.get();
        if (cached != null) {
            return cached;
        }
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return null;
        }
        String url = safe(driver::getCurrentUrl);
        String title = safe(driver::getTitle);
        String pageSource = safe(driver::getPageSource);
        String browser = safe(DriverManager::getInfo);

        StringBuilder console = new StringBuilder();
        int errors = 0;
        try {
            LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
            for (LogEntry entry : entries) {
                console.append('[').append(Instant.ofEpochMilli(entry.getTimestamp())).append("] ")
                        .append(entry.getLevel()).append(' ')
                        .append(entry.getMessage()).append(System.lineSeparator());
                if (entry.getLevel().intValue() >= Level.SEVERE.intValue()) {
                    errors++;
                }
            }
        } catch (Exception e) {
            // Firefox/Safari do not support reading console logs through WebDriver
            LogUtils.debug("Browser console logs are not available: " + e.getClass().getSimpleName());
        }

        Diagnostics diagnostics = new Diagnostics(url, title, browser, pageSource, console.toString(), errors);
        CACHE.set(diagnostics);
        return diagnostics;
    }

    private interface Supplier {
        String get() throws Exception;
    }

    private static String safe(Supplier supplier) {
        try {
            String value = supplier.get();
            return value == null ? "" : value;
        } catch (Exception e) {
            return "N/A (" + e.getClass().getSimpleName() + ")";
        }
    }
}
