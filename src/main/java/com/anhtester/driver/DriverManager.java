package com.anhtester.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        super();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driver) {
        DriverManager.driver.set(driver);
    }

    public static void quit() {
        if (DriverManager.getDriver() != null){
            DriverManager.getDriver().quit();
        }
    }

//    public static String getInfo() {
//        Capabilities cap = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
//        String browserName = cap.getBrowserName();
//        String platform = cap.getPlatformName().toString();
//        String version = cap.getBrowserVersion();
//        return String.format("browser: %s v: %s platform: %s", browserName, version, platform);
//    }
}
