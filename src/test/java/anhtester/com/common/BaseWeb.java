package anhtester.com.common;

import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.listeners.TestListener;
import anhtester.com.driver.DriverManager;
import anhtester.com.driver.TargetFactory;
import anhtester.com.report.AllureManager;
import anhtester.com.utils.WebUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.*;

import static anhtester.com.config.ConfigurationManager.configuration;

@Listeners({TestListener.class})
public class BaseWeb {

    public WebUI webUI = null;

    @BeforeSuite
    public void beforeSuite() {
        AllureManager.setAllureEnvironmentInformation();
        PropertiesHelpers.loadAllFiles();
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void createDriver(@Optional("chrome") String browser) {
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);

        webUI = new WebUI();
    }

    public WebDriver createBrowser(@Optional("chrome") String browser) {
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);
        return DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        DriverManager.quit();
    }
}
