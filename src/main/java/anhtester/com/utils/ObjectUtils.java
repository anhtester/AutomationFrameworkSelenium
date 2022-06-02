/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import anhtester.com.helpers.PropertiesHelpers;
import org.openqa.selenium.By;

public class ObjectUtils {

    public static By getObject(String elementName) {

        // retrieve the specified object from the object list in properties file
        String locator = PropertiesHelpers.getValue(elementName);

        if (locator.equals("") || locator.isEmpty()) {
            Log.info("The Locator " + elementName + " does not exist !!");
            try {
                throw new Exception("The Locator " + elementName + " does not exist !!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // extract the locator type and value from the object
        String locatorType = locator.split(":")[0];
        String locatorValue = locator.split(":")[1];

        Log.info("Retrieving object of type '" + locatorType + "' and locator '" + locatorValue + "' from the object repository");

        // Trả về một thể hiện của lớp By dựa trên loại định vị (id, name, xpath, css,...)
        // Đối tượng By có thể được sử dụng bởi driver.findElement (WebElement)
        if (locatorType.toLowerCase().equals("id"))
            return By.id(locatorValue);
        else if (locatorType.toLowerCase().equals("name"))
            return By.name(locatorValue);
        else if (locatorType.toLowerCase().equals("xpath"))
            return By.xpath(locatorValue);
        else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
            return By.cssSelector(locatorValue);
        else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
            return By.className(locatorValue);
        else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
            return By.tagName(locatorValue);
        else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
            return By.linkText(locatorValue);
        else if ((locatorType.toLowerCase().equals("partiallinktext")) || (locatorType.toLowerCase().equals("partial")))
            return By.partialLinkText(locatorValue);
        else
            try {
                throw new Exception("Unknown locator type '" + locatorType + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    public static String getXpathValue(String elementName) {

        // retrieve the specified object from the object list in properties file
        String locator = PropertiesHelpers.getValue(elementName);

        if (locator.equals("") || locator.isEmpty()) {
            try {
                Log.info("The Locator " + elementName + " does not exist !!");
                throw new Exception("The Locator " + elementName + " does not exist !!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // extract the locator type and value from the object
            String locatorType = locator.split(":")[0];
            String locatorValue = locator.split(":")[1];

            if (!locatorType.toLowerCase().trim().equals("xpath")) {
                try {
                    Log.info(locatorType.toLowerCase());
                    Log.info("The Locator Type of " + elementName + " does not XPATH !!");
                    throw new Exception("The Locator Type of " + elementName + " does not XPATH !!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.info("Retrieving Xpath with value '" + locatorValue + "' from the object repository");
                return locatorValue;
            }
        }

        return null;
    }


    /**
     * Receives a wildcard string, replace the wildcard with the value and return to the caller
     *
     * @param xpath Xpath with wildcard string
     *              VD: //a[text()='%s']   =>  %s is String, %d is int
     * @param value value to be replaced in place of wildcard
     * @return dynamic xpath string
     * @author Anh Tester
     */
    public static String getXpathDynamic(String xpath, Object value) {
        if (xpath == null || xpath == "") {
            try {
                Log.info("Parameter passing error. The 'xpath' parameter is null.");
                throw new Exception("Warning !! The xpath is null.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return String.format(xpath, value);
        }
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
        if (xpath == null || xpath == "") {
            try {
                Log.info("Parameter passing error. The 'xpath' parameter is null.");
                throw new Exception("Warning !! The xpath is null.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return String.format(xpath, value);
        }
    }

}
