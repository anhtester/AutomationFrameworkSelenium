package com.anhtester.common;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.driver.TargetFactory;
import com.anhtester.helpers.PropertiesHelpers;
import com.anhtester.listeners.TestListener;
import com.anhtester.utils.LogUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.*;

/**
 * Generic base class of all test cases: create/close the browser for every test method.
 * Project base classes (ex: BaseTestCMS) add access to their pages.
 */
@Listeners({TestListener.class})
public class BaseTest {

   /**
    * Create the browser for every test method.
    * The suite parameter BROWSER is optional: when it is missing, -DBROWSER / config.properties / chrome is used.
    */
   @Parameters("BROWSER")
   @BeforeMethod(alwaysRun = true)
   public void createDriver(@Optional("") String browser) {
      initDriver(browser);
   }

   /**
    * Always close the browser, even when the test fails.
    * Soft assertions are verified by TestListener right after the test method (not here),
    * so a soft assert failure marks the test as FAILED instead of breaking the configuration method.
    */
   @AfterMethod(alwaysRun = true)
   public void closeDriver() {
      DriverManager.quit();
   }

   /**
    * Create a browser outside the TestNG lifecycle (main method, utilities...)
    *
    * @param browser browser name: chrome, edge, firefox, safari
    * @return the driver
    */
   public WebDriver createBrowser(@Optional("") String browser) {
      PropertiesHelpers.loadAllFiles();
      return initDriver(browser);
   }

   private WebDriver initDriver(String browser) {
      WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
      try {
         if (Boolean.parseBoolean(FrameworkConstants.HEADLESS)) {
            driver.manage().window().setSize(new Dimension(1920, 1080)); // ép Selenium resize
         } else {
            driver.manage().window().maximize();
         }
         LogUtils.debug("Window size: " + driver.manage().window().getSize());
      } catch (Exception e) {
         LogUtils.warn("Can not resize the browser window: " + e.getMessage(), e);
      }
      DriverManager.setDriver(driver);
      return driver;
   }

}
