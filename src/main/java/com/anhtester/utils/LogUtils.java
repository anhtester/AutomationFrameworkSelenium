/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    //Initialize Log4j2 instance
    private static final Logger LOGGER = LogManager.getLogger(LogUtils.class);

    //Info Level Logs
    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(Object message) {
        LOGGER.info(message);
    }

    public static void info(String message, Throwable throwable) {
        LOGGER.info(message);
    }

    //Warn Level Logs
    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void warn(Object message) {
        LOGGER.warn(message);
    }

    //Error Level Logs
    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.error(message);
    }

    public static void error(Object message) {
        LOGGER.error(message);
    }

    public static void error(Object message, Throwable throwable) {
        LOGGER.error(message);
    }

    //Fatal Level Logs
    public static void fatal(String message) {
        LOGGER.fatal(message);
    }

    public static void fatal(Object message) {
        LOGGER.fatal(message);
    }

    //Debug Level Logs
    public static void debug(String message) {
        LOGGER.debug(message);
    }

    public static void debug(Object message) {
        LOGGER.debug(message);
    }
}
