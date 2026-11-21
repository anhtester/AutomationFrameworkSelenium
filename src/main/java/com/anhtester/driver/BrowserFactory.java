/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package com.anhtester.driver;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.exceptions.HeadlessNotSupportedException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static java.lang.Boolean.TRUE;

public enum BrowserFactory {

   CHROME {
      @Override
      public WebDriver createDriver() {
         return new ChromeDriver(getOptions());
      }

      @Override
      public ChromeOptions getOptions() {
         ChromeOptions options = new ChromeOptions();

         // Keep browser console logs so they can be attached to reports when a test fails
         options.setCapability("goog:loggingPrefs", browserLoggingPrefs());

         Map<String, Object> prefs = new HashMap<String, Object>();
         prefs.put("profile.default_content_setting_values.notifications", 2);
         prefs.put("profile.password_manager_leak_detection", false); // Turn off change your password
         prefs.put("credentials_enable_service", false);
         prefs.put("profile.password_manager_enabled", false);
         prefs.put("autofill.profile_enabled", false); //Turn off Save Address popup
         // Downloads go into the project (exports/Downloads), without the "Save as" dialog
         prefs.put("download.default_directory", downloadDirectory());
         prefs.put("download.prompt_for_download", false);
         options.setExperimentalOption("prefs", prefs);

         options.addArguments("--disable-extensions");
         options.addArguments("--disable-infobars");
         options.addArguments("--disable-notifications");
         options.addArguments("--remote-allow-origins=*");

         options.setAcceptInsecureCerts(true);

         if (Boolean.parseBoolean(FrameworkConstants.HEADLESS)) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--high-dpi-support=1");
            options.addArguments("--force-device-scale-factor=1");
         } else {
            options.addArguments(START_MAXIMIZED);
         }

         return options;
      }
   }, EDGE {
      @Override
      public WebDriver createDriver() {
         return new EdgeDriver(getOptions());
      }

      @Override
      public EdgeOptions getOptions() {
         EdgeOptions options = new EdgeOptions();

         // Keep browser console logs so they can be attached to reports when a test fails
         options.setCapability("ms:loggingPrefs", browserLoggingPrefs());

         Map<String, Object> prefs = new HashMap<String, Object>();
         prefs.put("profile.default_content_setting_values.notifications", 2);
         prefs.put("profile.password_manager_leak_detection", false); // Turn off change your password
         prefs.put("credentials_enable_service", false);
         prefs.put("profile.password_manager_enabled", false);
         prefs.put("autofill.profile_enabled", false);
         // Downloads go into the project (exports/Downloads), without the "Save as" dialog
         prefs.put("download.default_directory", downloadDirectory());
         prefs.put("download.prompt_for_download", false);
         options.setExperimentalOption("prefs", prefs);

         options.addArguments("--disable-extensions");
         options.addArguments("--disable-infobars");
         options.addArguments("--disable-notifications");
         options.addArguments("--remote-allow-origins=*");

         options.setAcceptInsecureCerts(true);

         if (Boolean.parseBoolean(FrameworkConstants.HEADLESS)) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--high-dpi-support=1");
            options.addArguments("--force-device-scale-factor=1");
         } else {
            options.addArguments(START_MAXIMIZED);
         }

         return options;
      }
   }, FIREFOX {
      @Override
      public WebDriver createDriver() {
         return new FirefoxDriver(getOptions());
      }

      @Override
      public FirefoxOptions getOptions() {
         FirefoxOptions options = new FirefoxOptions();
         // Downloads go into the project (exports/Downloads)
         options.addPreference("browser.download.folderList", 2);
         options.addPreference("browser.download.dir", downloadDirectory());
         options.addPreference("browser.download.useDownloadDir", true);

         options.setAcceptInsecureCerts(true);

         if (Boolean.parseBoolean(FrameworkConstants.HEADLESS)) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
         }

         return options;
      }
   }, SAFARI {
      @Override
      public WebDriver createDriver() {
         return new SafariDriver(getOptions());
      }

      @Override
      public SafariOptions getOptions() {
         SafariOptions options = new SafariOptions();
         options.setAutomaticInspection(false);

         if (TRUE.equals(Boolean.valueOf(FrameworkConstants.HEADLESS)))
            throw new HeadlessNotSupportedException(options.getBrowserName());

         return options;
      }
   };

   private static final String START_MAXIMIZED = "--start-maximized";

   /**
    * @return absolute path of the download folder (created if it does not exist)
    */
   public static String downloadDirectory() {
      File folder = new File(FrameworkConstants.EXPORT_DOWNLOAD_PATH).getAbsoluteFile();
      folder.mkdirs();
      return folder.getPath();
   }

   private static LoggingPreferences browserLoggingPrefs() {
      LoggingPreferences logPrefs = new LoggingPreferences();
      logPrefs.enable(LogType.BROWSER, Level.ALL);
      return logPrefs;
   }

   public abstract WebDriver createDriver();

   public abstract MutableCapabilities getOptions();
}
