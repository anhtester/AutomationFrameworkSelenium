/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    //Initialize Log4j2 instance
    private static final Logger Log =  LogManager.getLogger(Log.class);

    //Info Level Logs
    public static void info (String message) {
        Log.info(message);
    }
    public static void info (Object message) {
        Log.info(message);
    }
    public static void info (String message, Throwable throwable) {
        Log.info(message);
    }

    //Warn Level Logs
    public static void warn (String message) {
        Log.warn(message);
    }
    public static void warn (Object message) {
        Log.warn(message);
    }

    //Error Level Logs
    public static void error (String message) {
        Log.error(message);
    }
    public static void error (String message, Throwable throwable) {
        Log.error(message);
    }
    public static void error (Object message) {
        Log.error(message);
    }
    public static void error (Object message, Throwable throwable) {
        Log.error(message);
    }

    //Fatal Level Logs
    public static void fatal (String message) {
        Log.fatal(message);
    }
    public static void fatal (Object message) {
        Log.fatal(message);
    }

    //Debug Level Logs
    public static void debug (String message) {
        Log.debug(message);
    }
    public static void debug (Object message) {
        Log.debug(message);
    }
}
