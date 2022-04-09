/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package anhtester.com.utils;

import anhtester.com.driver.DriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import static anhtester.com.constants.FrameworkConstants.BROWSER;

//final -> We do not want any class to extend this class
public final class BrowserInfoUtils {

    //private -> We do not want anyone to create the object of this class
    //Private constructor to avoid external instantiation
    private BrowserInfoUtils() {
    }

    public static String getBrowserInfo() {
//        Capabilities capabilities = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
//        return capabilities.getBrowserName().toUpperCase();
        String browser = "";
        if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser") == null) {
            browser = BROWSER.toUpperCase();
        } else {
            browser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser").trim().toUpperCase();
        }
        return browser;
    }

    public static String getBrowserVersionInfo() {
//        Capabilities capabilities = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
//        return capabilities.getBrowserVersion();
        return "";
    }

}
