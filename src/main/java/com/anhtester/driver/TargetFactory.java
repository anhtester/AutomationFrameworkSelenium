/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package com.anhtester.driver;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.enums.Target;
import com.anhtester.exceptions.FrameworkException;
import com.anhtester.exceptions.InvalidRemoteWebDriverURLException;
import com.anhtester.exceptions.TargetNotValidException;
import com.anhtester.utils.LogUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

/**
 * Create the WebDriver for local or remote (Selenium Grid / Docker / Cloud) execution.
 * <p>
 * Browser priority: -DBROWSER / env BROWSER &gt; parameter BROWSER of the suite XML &gt; BROWSER in config.properties &gt; chrome
 */
public class TargetFactory {

    private static final String DEFAULT_BROWSER = "chrome";

    public WebDriver createInstance() {
        return createInstance(null);
    }

    public WebDriver createInstance(String browser) {
        String browserName = resolveBrowser(browser);
        Target target = resolveTarget();

        Allure.step("🤖 Run on browser: " + browserName);
        LogUtils.info(String.format("Creating driver: browser=%s, target=%s, headless=%s", browserName, target, FrameworkConstants.HEADLESS));

        BrowserFactory browserFactory;
        try {
            browserFactory = BrowserFactory.valueOf(browserName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new FrameworkException("Browser '" + browserName + "' is not supported. Use: chrome, edge, firefox, safari", e);
        }

        int maxAttempts = Math.max(1, FrameworkConstants.DRIVER_CREATE_RETRY + 1);
        RuntimeException lastError = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                long start = System.currentTimeMillis();
                WebDriver webdriver;
                switch (target) {
                    case LOCAL:
                        webdriver = browserFactory.createDriver();
                        break;
                    case REMOTE:
                        webdriver = createRemoteInstance(browserFactory.getOptions());
                        break;
                    default:
                        throw new TargetNotValidException(target.toString());
                }
                logDriverInfo(webdriver, System.currentTimeMillis() - start);
                return decorate(webdriver);
            } catch (TargetNotValidException | InvalidRemoteWebDriverURLException e) {
                throw e; // configuration errors: retrying does not help
            } catch (RuntimeException e) {
                lastError = e;
                LogUtils.warn(String.format("Can not create driver %s (attempt %d/%d): %s", browserName, attempt, maxAttempts, e.getMessage()), e);
                if (attempt < maxAttempts) {
                    sleepQuietly(2000L * attempt);
                }
            }
        }
        throw new FrameworkException("Can not create driver for browser '" + browserName + "' on target '" + target + "' after " + maxAttempts + " attempt(s)", lastError);
    }

    /**
     * Resolve the browser name following the priority described in the class doc.
     *
     * @param suiteBrowser value of the parameter BROWSER from the suite XML (can be null or empty)
     * @return the browser name to use
     */
    public static String resolveBrowser(String suiteBrowser) {
        String override = firstNonBlank(System.getProperty("BROWSER"), System.getenv("BROWSER"));
        String fileValue = FrameworkConstants.BROWSER_FROM_FILE;
        String browserName = firstNonBlank(override, suiteBrowser, fileValue, DEFAULT_BROWSER);
        return browserName.trim().toLowerCase(Locale.ROOT);
    }

    private static Target resolveTarget() {
        String value = firstNonBlank(FrameworkConstants.TARGET, "local");
        try {
            return Target.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new TargetNotValidException(value);
        }
    }

    private WebDriver decorate(WebDriver webdriver) {
        if (!FrameworkConstants.YES.equalsIgnoreCase(FrameworkConstants.WEBDRIVER_EVENT_LOG)) {
            return webdriver;
        }
        try {
            return new EventFiringDecorator<>(new DriverEventListener()).decorate(webdriver);
        } catch (Exception e) {
            LogUtils.warn("Can not attach WebDriver event listener, continue without it: " + e.getMessage(), e);
            return webdriver;
        }
    }

    private static void logDriverInfo(WebDriver webdriver, long elapsedMs) {
        try {
            if (webdriver instanceof HasCapabilities) {
                Capabilities cap = ((HasCapabilities) webdriver).getCapabilities();
                LogUtils.info(String.format("Driver created in %d ms: %s %s on %s", elapsedMs, cap.getBrowserName(), cap.getBrowserVersion(), cap.getPlatformName()));
            }
        } catch (Exception e) {
            LogUtils.debug("Can not read driver capabilities: " + e.getMessage());
        }
    }

    /**
     * Build the Grid URL.
     * REMOTE_URL can be a host (localhost) combined with REMOTE_PORT, or a full URL (https://hub.example.com/wd/hub).
     */
    static URL buildGridUrl() {
        String remoteUrl = firstNonBlank(FrameworkConstants.REMOTE_URL, "localhost").trim();
        String remotePort = firstNonBlank(FrameworkConstants.REMOTE_PORT, "").trim();
        String gridUrl;
        if (remoteUrl.contains("://")) {
            gridUrl = remoteUrl;
        } else {
            gridUrl = "http://" + remoteUrl + (remotePort.isEmpty() ? "" : ":" + remotePort);
        }
        try {
            return URI.create(gridUrl).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new InvalidRemoteWebDriverURLException("Grid URL is invalid: " + gridUrl + " (check REMOTE_URL and REMOTE_PORT)", e);
        }
    }

    private RemoteWebDriver createRemoteInstance(MutableCapabilities capability) {
        URL gridUrl = buildGridUrl();
        LogUtils.info("Remote URL: " + gridUrl + " - Browser: " + capability.getBrowserName());
        return new RemoteWebDriver(gridUrl, capability);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }
        }
        return null;
    }

    private static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
