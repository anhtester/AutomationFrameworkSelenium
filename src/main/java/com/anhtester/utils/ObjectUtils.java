/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import com.anhtester.helpers.PropertiesHelpers;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectUtils {

    private static final String BY = "by";
    private static final String H = "h";
    private static final String LOCATOR = "locator";
    private static final String FOUND_BY = "foundBy";

    public static By getByFromWebElement(WebElement element) {
        if (element instanceof DefaultElementLocator) {
            return getByLocator(element);
        } else if (element instanceof Proxy) {
            Object proxyOrigin = getField(element, H);
            Object locator = getField(proxyOrigin, LOCATOR);

            return getByLocator(locator);

        } else /* if WebElement is RemoteWebElement */ {
            String foundByString = getFoundBy(element);
            String foundByPattern = "(?<=\\-> ).*";

            Pattern pattern = Pattern.compile(foundByPattern);
            Matcher matcher = pattern.matcher(foundByString);

            if (matcher.find()) {
                int locatorDefinitionIndex = 0;
                String locatorDefinition = matcher.group(locatorDefinitionIndex);

                return getByLocatorFromString(locatorDefinition);

            } else {
                throw new IllegalStateException("Failed to get locator from RemoteWebElement. Please check if the Regex pattern is valid.");
            }

        }
    }

    private static Object getField(Object element, String fieldName) {
        try {
            return FieldUtils.readField(element, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFoundBy(Object element) {
        try {
            return (String) FieldUtils.readField(element, FOUND_BY, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static By getByLocator(Object element) {
        try {
            return (By) FieldUtils.readField(element, BY, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static By getByLocatorFromString(String locatorToString) {
        String[] locatorSplit = locatorToString.split(": ");

        if (locatorSplit.length != 2)
            throw new IllegalStateException(String.format("Locator definition does not had 2 elements for %s locator", locatorToString));

        String locatorType = locatorSplit[0];
        String locatorValue = locatorSplit[1];

        switch (locatorType) {
            case "css selector":
                return By.cssSelector(locatorValue);
            case "id":
                return By.id(locatorValue);
            case "link text":
                return By.linkText(locatorValue);
            case "partial link text":
                return By.partialLinkText(locatorValue);
            case "tag name":
                return By.tagName(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "class":
                return By.className(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            default:
                throw new IllegalStateException("Cannot define locator for WebElement definition: " + locatorToString);
        }
    }

    public static By getByLocatorFromConfig(String elementName) {

        // retrieve the specified object from the object list in properties file
        String locator = PropertiesHelpers.getValue(elementName);

        if (locator.isEmpty()) {
            LogUtils.info("The Locator string " + elementName + " does not exist !!");
            try {
                throw new Exception("The Locator " + elementName + " does not exist !!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (locator.split("&&").length != 2) {
            throw new IllegalStateException(String.format("Locator definition does not had 2 elements for %s locator", elementName));
        }

        // extract the locator type and value from the object
        String locatorType = locator.split("&&")[0];
        String locatorValue = locator.split("&&")[1];

        LogUtils.info("Retrieving object of type '" + locatorType + "' and locator '" + locatorValue + "' from the object repository");

        // Returns an instance of the By class based on the locator type (id, name, xpath, css,...)
        // By object can be used by driver.findElement (WebElement)
        switch (locatorType.toLowerCase().trim()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "cssselector":
            case "css":
                return By.cssSelector(locatorValue);
            case "classname":
            case "class":
                return By.className(locatorValue);
            case "tagname":
            case "tag":
                return By.tagName(locatorValue);
            case "linktext":
            case "link":
                return By.linkText(locatorValue);
            case "partiallinktext":
            case "partial":
                return By.partialLinkText(locatorValue);
            default:
                try {
                    throw new Exception("Unknown locator type '" + locatorType + "'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    public static String getXpathValue(String elementName) {

        // retrieve the specified object from the object list in properties file
        String locator = PropertiesHelpers.getValue(elementName);

        if (locator.isEmpty()) {
            try {
                LogUtils.info("The Locator " + elementName + " does not exist !!");
                throw new Exception("The Locator " + elementName + " does not exist !!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if (locator.split("&&").length != 2) {
                throw new IllegalStateException(String.format("Locator definition does not had 2 elements for %s locator", elementName));
            }
            
            // extract the locator type and value from the object
            String locatorType = locator.split("&&")[0];
            String locatorValue = locator.split("&&")[1];

            if (!locatorType.toLowerCase().trim().equals("xpath")) {
                try {
                    LogUtils.info("The Locator Type of " + elementName + " does not XPATH !! => " + locatorType);
                    throw new Exception("The Locator Type of " + elementName + " does not XPATH !! => " + locatorType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                LogUtils.info("Retrieving Xpath with value '" + locatorValue + "' from the object repository");
                return locatorValue;
            }
        }

        return null;
    }


    /**
     * Receives a wildcard string, replace the wildcard with the value and return to the caller
     *
     * @param xpath Xpath with wildcard string
     * @param value multi value to be replaced in place of wildcard
     *              VD: ObjectUtils.getXpathDynamic("//button[normalize-space()='%s']//div[%d]//span[%d]", "Login", 2, 10);
     * @return dynamic xpath string
     * @author Anh Tester
     */
    public static String getXpathDynamic(String xpath, Object... value) {
        if (xpath == null || xpath.equals("")) {
            try {
                LogUtils.info("Parameter passing error. The 'xpath' parameter is null.");
                throw new Exception("Warning. The xpath is null.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return String.format(xpath, value);
        }
    }

}
