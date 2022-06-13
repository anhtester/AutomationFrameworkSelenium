package anhtester.com.common;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.listeners.TestListener;
import anhtester.com.driver.DriverManager;
import anhtester.com.driver.TargetFactory;
import anhtester.com.report.AllureManager;
import anhtester.com.utils.WebUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.*;

@Listeners({TestListener.class})
public class BaseTest {

    @BeforeSuite
    public void beforeSuite() {
        AllureManager.setAllureEnvironmentInformation();
        PropertiesHelpers.loadAllFiles(); //Config and Locators
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void createDriver(@Optional("chrome") String browser) {
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        DriverManager.quit();
        WebUI.stopSoftAssertAll();
    }


    public WebDriver createBrowser(@Optional("chrome") String browser) {
        PropertiesHelpers.loadAllFiles();
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);
        return DriverManager.getDriver();

    }

}
