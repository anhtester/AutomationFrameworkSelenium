/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.regex.Pattern;

/**
 * Hide sensitive values (passwords, tokens, ...) before they are written to logs or reports.
 */
public final class MaskUtils {

    public static final String MASK = "******";

    private static final Pattern SENSITIVE_NAME = Pattern.compile("(?i)(pass(word)?|pwd|secret|token|otp|pin|cvv|credit.?card)");

    private MaskUtils() {
    }

    /**
     * @param by locator of the element
     * @return true if the locator looks like a sensitive field (password, token, ...)
     */
    public static boolean isSensitive(By by) {
        return by != null && SENSITIVE_NAME.matcher(by.toString()).find();
    }

    /**
     * @param element the element (input)
     * @return true if the element is a password input or its id/name looks sensitive
     */
    public static boolean isSensitive(WebElement element) {
        if (element == null) {
            return false;
        }
        try {
            String type = element.getDomAttribute("type");
            if ("password".equalsIgnoreCase(type)) {
                return true;
            }
            String id = element.getDomAttribute("id");
            String name = element.getDomAttribute("name");
            return (id != null && SENSITIVE_NAME.matcher(id).find()) || (name != null && SENSITIVE_NAME.matcher(name).find());
        } catch (Exception e) {
            // Element is stale/detached: we can not check it -> do not mask
            return false;
        }
    }

    /**
     * Return the value or the mask when the field is sensitive.
     *
     * @param by      locator of the element
     * @param element the element (can be null)
     * @param value   the value typed into the element
     * @return value safe to log
     */
    public static String maskIfSensitive(By by, WebElement element, String value) {
        if (value == null) {
            return null;
        }
        return (isSensitive(by) || isSensitive(element)) ? MASK : value;
    }
}
