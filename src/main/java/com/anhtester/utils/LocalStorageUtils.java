/*
 * Copyright (c) 2025 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import com.anhtester.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;

public class LocalStorageUtils {

   private LocalStorageUtils() {
      super();
   }

   private static JavascriptExecutor js() {
      return (JavascriptExecutor) DriverManager.getDriver();
   }

   public static String getItem(String key) {
      return (String) js().executeScript("return window.localStorage.getItem(arguments[0]);", key);
   }

   public static void setItem(String key, String value) {
      js().executeScript("window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
   }

   public static void removeItem(String key) {
      js().executeScript("window.localStorage.removeItem(arguments[0]);", key);
   }

   public static void clear() {
      js().executeScript("window.localStorage.clear();");
   }

   public static Long size() {
      return (Long) js().executeScript("return window.localStorage.length;");
   }
}