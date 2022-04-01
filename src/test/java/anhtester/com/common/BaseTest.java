package anhtester.com.common;

import java.time.Duration;

import anhtester.com.config.Constants;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static final int IMPLICIT_WAIT = Constants.IMPLICIT_WAIT;
    public static final int PAGE_LOAD_TIMEOUT = Constants.PAGE_LOAD_TIMEOUT;

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static WebDriver createDriver(String browserType) {
        switch (browserType.toLowerCase().trim()) {
            case "chrome":
                Log.info("Launching " + browserType + " browser...");
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                driver.get().manage().window().maximize();
                driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
                driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
                break;
            case "firefox":
                Log.info("Launching " + browserType + " browser...");
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                driver.get().manage().window().maximize();
                driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
                driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
                break;
            case "opera":
                Log.info("Launching " + browserType + " browser...");
                WebDriverManager.operadriver().setup();
                driver.set(new OperaDriver());
                driver.get().manage().window().maximize();
                driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
                driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
                break;
            case "edge":
                Log.info("Launching " + browserType + " browser...");
                WebDriverManager.edgedriver().setup();
                driver.set(new EdgeDriver());
                driver.get().manage().window().maximize();
                driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
                driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
                break;
            default:
                Log.info("Browser: " + browserType + " is invalid, Launching Chrome as browser of choice...");
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                driver.get().manage().window().maximize();
                driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
                driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
                break;
        }

        return driver.get();
    }

    // Khởi tạo cấu hình của các Browser để đưa vào Switch Case setDriver

    public static WebDriver initChromeDriver() {
        Log.info("Launching Chrome browser...");
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    }

    public static WebDriver initFirefoxDriver() {
        Log.info("Launching Firefox browser...");
        WebDriverManager.firefoxdriver().setup();
        driver.set(new FirefoxDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    }

    public static WebDriver initOperaDriver() {
        Log.info("Launching Opera browser...");
        WebDriverManager.operadriver().setup();
        driver.set(new OperaDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    }

    public static WebDriver initEdgeDriver() {
        Log.info("Launching Edge browser...");
        WebDriverManager.edgedriver().setup();
        driver.set(new EdgeDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        return driver.get();
    }

    // Hàm initBaseTestSetup sẽ chạy trước nhất trong project này nếu có kế thừa class này
    //@Parameters({"browserType"})
    @BeforeTest
    public void initBaseTestSetup() {
        try {
            // Thực thi để khởi tạo driver với browser tương ứng
            createDriver(PropertiesHelpers.getValue("browser"));
            Log.info("Tests are starting!");
        } catch (Exception e) {
            Log.info("Error initialize driver..." + e.getStackTrace());
            Log.error("Error initialize driver");
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.get().quit();
            driver.remove();
            Log.info("Tests are ending!");
        }
    }
}
