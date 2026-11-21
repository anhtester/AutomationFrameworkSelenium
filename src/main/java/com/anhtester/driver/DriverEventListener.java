/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.driver;

import com.anhtester.utils.LogUtils;
import com.anhtester.utils.MaskUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Log every important WebDriver action automatically (navigation, click, typing, alert, quit, errors)
 * with its duration. Attached in {@link TargetFactory} through EventFiringDecorator.
 * <p>
 * Events are logged at DEBUG level: they go into the per-test log file (exports/logs/tests)
 * without flooding the console. A slow action (more than {@link #SLOW_ACTION_MS} ms) is logged at WARN.
 * <p>
 * A listener must never throw, otherwise it would break the test: every method is protected.
 */
public class DriverEventListener implements WebDriverListener {

    private static final long SLOW_ACTION_MS = 5000;

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    private static void start() {
        startTime.set(System.currentTimeMillis());
    }

    private static long elapsed() {
        Long start = startTime.get();
        startTime.remove();
        return start == null ? -1 : System.currentTimeMillis() - start;
    }

    private static void logDone(String action) {
        long ms = elapsed();
        if (ms > SLOW_ACTION_MS) {
            LogUtils.warn("[WebDriver] SLOW " + action + " took " + ms + " ms");
        } else {
            LogUtils.debug("[WebDriver] " + action + " done in " + ms + " ms");
        }
    }

    /**
     * Short description of an element: the locator part of RemoteWebElement.toString()
     */
    private static String describe(WebElement element) {
        try {
            String text = String.valueOf(element);
            int index = text.indexOf("-> ");
            if (index >= 0) {
                String locator = text.substring(index + 3);
                return locator.endsWith("]") ? locator.substring(0, locator.length() - 1) : locator;
            }
            return text;
        } catch (Exception e) {
            return "element";
        }
    }

    // ------------------------------- Navigation -------------------------------

    @Override
    public void beforeGet(WebDriver driver, String url) {
        LogUtils.debug("[WebDriver] Open URL: " + url);
        start();
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        logDone("Open URL " + url);
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        LogUtils.debug("[WebDriver] Navigate to: " + url);
        start();
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, String url) {
        logDone("Navigate to " + url);
    }

    @Override
    public void beforeRefresh(WebDriver.Navigation navigation) {
        LogUtils.debug("[WebDriver] Refresh page");
    }

    @Override
    public void beforeBack(WebDriver.Navigation navigation) {
        LogUtils.debug("[WebDriver] Navigate back");
    }

    // ------------------------------- Element actions -------------------------------

    @Override
    public void beforeClick(WebElement element) {
        LogUtils.debug("[WebDriver] Click: " + describe(element));
        start();
    }

    @Override
    public void afterClick(WebElement element) {
        logDone("Click");
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        try {
            String value = keysToSend == null ? "" : Arrays.stream(keysToSend).map(String::valueOf).collect(Collectors.joining());
            if (MaskUtils.isSensitive(element)) {
                value = MaskUtils.MASK;
            }
            LogUtils.debug("[WebDriver] Type '" + value + "' into: " + describe(element));
        } catch (Exception ignored) {
            // never break the test because of logging
        }
    }

    @Override
    public void beforeClear(WebElement element) {
        LogUtils.debug("[WebDriver] Clear: " + describe(element));
    }

    @Override
    public void beforeSubmit(WebElement element) {
        LogUtils.debug("[WebDriver] Submit: " + describe(element));
    }

    // ------------------------------- Alert / window / session -------------------------------

    @Override
    public void beforeAccept(Alert alert) {
        LogUtils.debug("[WebDriver] Accept alert");
    }

    @Override
    public void beforeDismiss(Alert alert) {
        LogUtils.debug("[WebDriver] Dismiss alert");
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        LogUtils.debug("[WebDriver] Quit browser");
    }

    // ------------------------------- Errors -------------------------------

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        try {
            Throwable cause = e.getTargetException();
            // These are expected while waiting/polling: keep them at DEBUG to avoid noise
            boolean expected = cause instanceof NoSuchElementException
                    || cause instanceof StaleElementReferenceException
                    || cause instanceof TimeoutException;
            String message = "[WebDriver] Error on " + method.getName() + ": "
                    + cause.getClass().getSimpleName() + " - " + firstLine(cause.getMessage());
            if (expected) {
                LogUtils.debug(message);
            } else {
                LogUtils.warn(message);
            }
        } catch (Exception ignored) {
            // never break the test because of logging
        }
    }

    private static String firstLine(String text) {
        if (text == null) {
            return "";
        }
        int index = text.indexOf('\n');
        return index > 0 ? text.substring(0, index) : text;
    }
}
