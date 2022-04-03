package anhtester.com.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driver) {
        DriverManager.driver.set(driver);
    }

    public static void quit() {
        DriverManager.getDriver().quit();
        DriverManager.driver.remove();
    }

//    public static String getInfo() {
//        Capabilities cap = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
//        String browserName = cap.getBrowserName();
//        String platform = cap.getPlatformName().toString();
//        String version = cap.getBrowserVersion();
//        return String.format("browser: %s v: %s platform: %s", browserName, version, platform);
//    }
}
