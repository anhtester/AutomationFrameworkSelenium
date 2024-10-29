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
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.TRUE;

public enum BrowserFactory {

    CHROME {
        @Override
        public WebDriver createDriver() {
            //WebDriverManager.getInstance(DriverManagerType.CHROME).setup();

            return new ChromeDriver(getOptions());
        }

        @Override
        public ChromeOptions getOptions() {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("autofill.profile_enabled", false);
            options.setExperimentalOption("prefs", prefs);

            options.addArguments("--disable-extensions");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");

            options.setAcceptInsecureCerts(true);

            if (Boolean.valueOf(FrameworkConstants.HEADLESS) == true) {
                options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1880,1000");
            }

            return options;
        }
    }, EDGE {
        @Override
        public WebDriver createDriver() {
            //WebDriverManager.getInstance(DriverManagerType.EDGE).setup();

            return new EdgeDriver(getOptions());
        }

        @Override
        public EdgeOptions getOptions() {
            EdgeOptions options = new EdgeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("autofill.profile_enabled", false);
            options.setExperimentalOption("prefs", prefs);

            options.addArguments("--disable-extensions");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");

            options.setAcceptInsecureCerts(true);

            if (Boolean.valueOf(FrameworkConstants.HEADLESS) == true) {
                options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1880,1000");
            }

            return options;
        }
    }, FIREFOX {
        @Override
        public WebDriver createDriver() {
            //WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();

            return new FirefoxDriver(getOptions());
        }

        @Override
        public FirefoxOptions getOptions() {
            FirefoxOptions options = new FirefoxOptions();

            options.setAcceptInsecureCerts(true);

            if (Boolean.valueOf(FrameworkConstants.HEADLESS) == true) {
                options.addArguments("-headless");
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");
            }

            return options;
        }
    }, SAFARI {
        @Override
        public WebDriver createDriver() {
            //WebDriverManager.getInstance(DriverManagerType.SAFARI).setup();

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

    public abstract WebDriver createDriver();

    public abstract MutableCapabilities getOptions();
}
