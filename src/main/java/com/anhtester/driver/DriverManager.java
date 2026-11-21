package com.anhtester.driver;

import com.anhtester.utils.LogUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

/**
 * Hold one WebDriver per thread (parallel safe).
 */
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

    /**
     * Quit the driver of the current thread and always clear the ThreadLocal,
     * so a pooled thread never reuses a dead session.
     */
    public static void quit() {
        WebDriver current = driver.get();
        if (current == null) {
            return;
        }
        try {
            current.quit();
            LogUtils.info("Closed the browser.");
        } catch (Exception e) {
            LogUtils.warn("Error while quitting the browser (session may already be closed): " + e.getMessage(), e);
        } finally {
            driver.remove();
        }
    }

    /**
     * @return "browser version on platform" of the current driver, or "unknown"
     */
    public static String getInfo() {
        WebDriver current = driver.get();
        if (current instanceof HasCapabilities) {
            Capabilities cap = ((HasCapabilities) current).getCapabilities();
            return String.format("%s %s on %s", cap.getBrowserName(), cap.getBrowserVersion(), cap.getPlatformName());
        }
        return "unknown";
    }
}
