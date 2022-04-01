package anhtester.com.manager;

import anhtester.com.config.Constants;
import anhtester.com.driver.DriverManager;
import com.google.common.base.Supplier;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BrowserFactory {

    //  Driver Global

    /**
     * Khai báo biến driverInstance thuộc đối tượng WebDriver từ TheadLocal
     */
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    public static final int IMPLICIT_WAIT = Constants.IMPLICIT_WAIT;
    public static final int PAGE_LOAD_TIMEOUT = Constants.PAGE_LOAD_TIMEOUT;

    private static Supplier<WebDriver> chromeDriver = () -> {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    };

    private static Supplier<WebDriver> firefoxDriver = () -> {
        WebDriverManager.firefoxdriver().setup();
        driver.set(new FirefoxDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    };

    private static Supplier<WebDriver> edgeDriver = () -> {
        WebDriverManager.edgedriver().setup();
        driver.set(new EdgeDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    };

    private static Supplier<WebDriver> operaDriver = () -> {
        WebDriverManager.operadriver().setup();
        driver.set(new OperaDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    };

    private static Map<String, Supplier<WebDriver>> driverMap = new HashMap<>();

    public BrowserFactory() {
        driverMap.put("chrome", chromeDriver);
        driverMap.put("firefox", firefoxDriver);
        driverMap.put("edge", edgeDriver);
        driverMap.put("opera", operaDriver);
    }

    /**
     * Khởi tạo giá trị theo loại trình duyệt cho WebDriver.
     *
     * @param browser loại trình duyệt cần khởi tạo
     */
    public static WebDriver createDriver(String browser) {
        System.out.println(browser + " browser running...");
        return driverMap.getOrDefault(browser.toLowerCase().trim(), chromeDriver).get();
    }

    /**
     * Lấy giá trị driver từ WebDriver trong ThreadLocal
     *
     * @return một giá trị driver thuộc đối tượng WebDriver
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            return DriverManager.getDriver();
        } else {
            return driver.get();
        }
    }

    /**
     * Removes the driver.
     */
    public static void closeDriver() {
        driver.get().quit();
        driver.remove();
    }

    public static String getInfo() {
        Capabilities cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        String browserName = cap.getBrowserName();
        String platform = cap.getPlatform().toString();
        String version = cap.getVersion();
        return String.format("Browser: %s, Version: %s, Platform: %s", browserName, version, platform);
    }
}