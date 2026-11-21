/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import com.anhtester.driver.TargetFactory;
import org.testng.ITestResult;
import org.testng.Reporter;


public final class BrowserInfoUtils {

    private BrowserInfoUtils() {
        super();
    }

    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * @return browser name of the current test in upper case (same priority as TargetFactory)
     */
    public static String getBrowserInfo() {
        String suiteBrowser = null;
        ITestResult result = Reporter.getCurrentTestResult();
        if (result != null && result.getTestContext() != null && result.getTestContext().getCurrentXmlTest() != null) {
            suiteBrowser = result.getTestContext().getCurrentXmlTest().getParameter("BROWSER");
        }
        return TargetFactory.resolveBrowser(suiteBrowser).toUpperCase();
    }

    public static String getOSInfo() {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }

}
