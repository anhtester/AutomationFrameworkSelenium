/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package anhtester.com.utils;

//final -> We do not want any class to extend this class
public final class OSInfoUtils {

    /**
     * Private constructor to avoid external instantiation
     * private -> We do not want anyone to create the object of this class
     */
    private OSInfoUtils() {
    }

    public static String getOSInfo() {
        return System.getProperty("os.name").replace(" ", "_");
    }

}
