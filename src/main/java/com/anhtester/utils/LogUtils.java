/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spi.ExtendedLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Central logging facade of the framework (Log4j2).
 * <p>
 * - Every overload that receives a {@link Throwable} writes the full stack trace.<br>
 * - The caller location (%C.%M:%L) points to the real caller, not to LogUtils.<br>
 * - {@link #setTestContext(String)} tags every log line of the current thread with the test name
 * (ThreadContext key {@value #TEST_NAME_KEY}) and routes it into a separate file per test
 * (see log4j2.xml, folder exports/logs/tests).
 */
public final class LogUtils {

    /**
     * Fully qualified class name used by Log4j2 to compute the real caller location.
     */
    private static final String FQCN = LogUtils.class.getName();
    private static final ExtendedLogger LOGGER = (ExtendedLogger) LogManager.getLogger(LogUtils.class);

    /**
     * ThreadContext (MDC) key holding the current test name.
     */
    public static final String TEST_NAME_KEY = "testName";

    /**
     * Folder that contains one log file per test case. Must match log4j2.xml.
     */
    public static final String TEST_LOG_DIR = System.getProperty("log.dir", "exports/logs") + "/tests";

    private static final Pattern UNSAFE_FILE_CHARS = Pattern.compile("[^A-Za-z0-9._-]");

    private LogUtils() {
    }

    // ---------------------------------------------------------------------------------------------
    // Test context (MDC)
    // ---------------------------------------------------------------------------------------------

    /**
     * Tag all following log lines of the current thread with the given test name.
     *
     * @param testName test name, usually SimpleClassName.methodName
     */
    public static void setTestContext(String testName) {
        if (testName == null || testName.isBlank()) {
            clearTestContext();
            return;
        }
        ThreadContext.put(TEST_NAME_KEY, toSafeFileName(testName));
    }

    /**
     * Remove the test name from the current thread.
     */
    public static void clearTestContext() {
        ThreadContext.remove(TEST_NAME_KEY);
    }

    /**
     * @return test name of the current thread or null
     */
    public static String getTestContext() {
        return ThreadContext.get(TEST_NAME_KEY);
    }

    /**
     * Convert a test name to a value that can be used as file name.
     *
     * @param name the raw name
     * @return a safe file name
     */
    public static String toSafeFileName(String name) {
        return UNSAFE_FILE_CHARS.matcher(name).replaceAll("_");
    }

    /**
     * @param testName test name (raw or already safe)
     * @return path of the log file of that test
     */
    public static Path getTestLogFile(String testName) {
        return Paths.get(TEST_LOG_DIR, toSafeFileName(testName) + ".log");
    }

    /**
     * Read the last lines of the log file of a test, used to attach logs to reports on failure.
     *
     * @param testName test name
     * @param maxLines maximum number of lines returned from the end of the file
     * @return the log content or an empty string if the file does not exist
     */
    public static String readTestLog(String testName, int maxLines) {
        if (testName == null) {
            return "";
        }
        Path file = getTestLogFile(testName);
        if (!Files.exists(file)) {
            return "";
        }
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            int from = Math.max(0, lines.size() - maxLines);
            return String.join(System.lineSeparator(), lines.subList(from, lines.size()));
        } catch (IOException e) {
            warn("Can not read test log file: " + file, e);
            return "";
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Log methods
    // ---------------------------------------------------------------------------------------------

    private static void log(Level level, Object message, Throwable throwable) {
        LOGGER.logIfEnabled(FQCN, level, null, message, throwable);
    }

    //Info Level Logs
    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    public static void info(Object message) {
        log(Level.INFO, message, null);
    }

    public static void info(String message, Throwable throwable) {
        log(Level.INFO, message, throwable);
    }

    //Warn Level Logs
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }

    public static void warn(Object message) {
        log(Level.WARN, message, null);
    }

    public static void warn(String message, Throwable throwable) {
        log(Level.WARN, message, throwable);
    }

    //Error Level Logs
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    public static void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    /**
     * Log a throwable with its full stack trace.
     *
     * @param throwable the error
     */
    public static void error(Throwable throwable) {
        log(Level.ERROR, throwable == null ? "null" : throwable.toString(), throwable);
    }

    public static void error(Object message) {
        if (message instanceof Throwable) {
            error((Throwable) message);
        } else {
            log(Level.ERROR, message, null);
        }
    }

    public static void error(Object message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    //Fatal Level Logs
    public static void fatal(String message) {
        log(Level.FATAL, message, null);
    }

    public static void fatal(Object message) {
        log(Level.FATAL, message, null);
    }

    public static void fatal(String message, Throwable throwable) {
        log(Level.FATAL, message, throwable);
    }

    //Debug Level Logs
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    public static void debug(Object message) {
        log(Level.DEBUG, message, null);
    }

    public static void debug(String message, Throwable throwable) {
        log(Level.DEBUG, message, throwable);
    }

    /**
     * @return true if debug logs are enabled (avoid building expensive messages)
     */
    public static boolean isDebugEnabled() {
        return LOGGER.isDebugEnabled();
    }
}
