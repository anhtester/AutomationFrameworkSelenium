package anhtester.com.common;

import anhtester.com.driver.DriverManager;
import anhtester.com.driver.TargetFactory;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.listeners.TestListener;
import anhtester.com.projects.website.crm.pages.CommonPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners({TestListener.class})
public class BaseTest extends CommonPage{

    @Parameters("BROWSER")
    @BeforeMethod(alwaysRun = true)
    public void createDriver(@Optional("chrome") String browser, Method method) {
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        driver.manage().window().maximize();
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        DriverManager.quit();
    }

    public WebDriver createBrowser(@Optional("chrome") String browser) {
        PropertiesHelpers.loadAllFiles();
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        driver.manage().window().maximize();
        DriverManager.setDriver(driver);
        return DriverManager.getDriver();
    }

}
