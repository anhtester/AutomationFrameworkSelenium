/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import com.anhtester.utils.LanguageUtils;
import com.anhtester.utils.LogUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Read/write values from properties files.
 * <p>
 * Value priority of {@link #getValue(String)}:
 * <ol>
 *     <li>JVM system property: mvn test -DBROWSER=firefox -DHEADLESS=false</li>
 *     <li>Environment variable with the same name (CI: env BROWSER=firefox)</li>
 *     <li>Properties files (config.properties, data.properties, ...)</li>
 * </ol>
 */
public class PropertiesHelpers {

    private static volatile Properties properties;
    private static String linkFile;
    private static final String relPropertiesFilePathDefault = "src/test/resources/config/config.properties";

    public static synchronized Properties loadAllFiles() {
        LinkedList<String> files = new LinkedList<>();
        // Add tất cả file Properties vào đây theo mẫu
        files.add("src/test/resources/config/config.properties");
        files.add("src/test/resources/config/data.properties");
        files.add("src/test/resources/objects/locators.properties");

        Properties loaded = new Properties();
        for (String f : files) {
            String path = SystemHelpers.getCurrentDir() + f;
            try (InputStream input = new FileInputStream(path)) {
                Properties tempProp = new Properties();
                tempProp.load(input);
                loaded.putAll(tempProp);
                linkFile = path;
            } catch (IOException e) {
                LogUtils.warn("Warning !! Can not load properties file: " + path, e);
            }
        }

        // Environment file (ENV=staging -> config/env/staging.properties) overrides the base files
        String env = getEnvironmentName(loaded);
        if (env != null) {
            String path = SystemHelpers.getCurrentDir() + ENV_FOLDER + env + ".properties";
            try (InputStream input = new FileInputStream(path)) {
                Properties envProp = new Properties();
                envProp.load(input);
                loaded.putAll(envProp);
                LogUtils.info("Loaded environment '" + env + "': " + ENV_FOLDER + env + ".properties (" + envProp.size() + " keys)");
            } catch (IOException e) {
                LogUtils.error("ENV=" + env + " but the file " + path + " does not exist: only the base configuration is used.");
            }
        }

        properties = loaded;
        LogUtils.info("Loaded all properties files.");
        return properties;
    }

    /**
     * Folder of the environment files: ENV=staging loads config/env/staging.properties
     */
    public static final String ENV_FOLDER = "src/test/resources/config/env/";

    /**
     * @return the environment name from -DENV / env variable ENV / config.properties, or null when not set
     */
    public static String getEnvironmentName() {
        return getEnvironmentName(properties);
    }

    private static String getEnvironmentName(Properties base) {
        String env = getOverride("ENV");
        if (env == null && base != null) {
            String fromFile = base.getProperty("ENV");
            env = (fromFile == null || fromFile.trim().isEmpty()) ? null : fromFile.trim();
        }
        return env;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static synchronized void setFile(String relPropertiesFilePath) {
        Properties loaded = new Properties();
        linkFile = SystemHelpers.getCurrentDir() + relPropertiesFilePath;
        try (InputStream input = new FileInputStream(linkFile)) {
            loaded.load(input);
        } catch (Exception e) {
            LogUtils.error("Can not load properties file: " + linkFile, e);
        }
        properties = loaded;
    }

    public static synchronized void setDefaultFile() {
        setFile(relPropertiesFilePathDefault);
    }

    private static void ensureLoaded() {
        if (properties == null) {
            synchronized (PropertiesHelpers.class) {
                if (properties == null) {
                    setDefaultFile();
                }
            }
        }
    }

    /**
     * Get value from system property, then environment variable, then the properties files.
     *
     * @param key the key
     * @return the value or null
     */
    public static String getValue(String key) {
        String override = getOverride(key);
        if (override != null) {
            return override;
        }
        return getFileValue(key);
    }

    /**
     * Get value with a default when the key is missing or empty.
     *
     * @param key          the key
     * @param defaultValue value returned when nothing is configured
     * @return the value
     */
    public static String getValue(String key, String defaultValue) {
        String value = getValue(key);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
    }

    /**
     * Get the value only from the properties files (ignore -D and environment variables).
     *
     * @param key the key
     * @return the value or null
     */
    public static String getFileValue(String key) {
        String keyValue = null;
        try {
            ensureLoaded();
            keyValue = properties.getProperty(key);
            return LanguageUtils.convertCharset_ISO_8859_1_To_UTF8(keyValue);
        } catch (Exception e) {
            LogUtils.warn("Can not read key '" + key + "' from properties: " + e.getMessage());
            return keyValue;
        }
    }

    /**
     * @param key the key
     * @return the -Dkey value, else the environment variable, else null (empty values are ignored)
     */
    public static String getOverride(String key) {
        String value = System.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(key);
        }
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    public static synchronized void setValue(String key, String keyValue) {
        ensureLoaded();
        String target = linkFile != null ? linkFile : SystemHelpers.getCurrentDir() + relPropertiesFilePathDefault;
        try (OutputStream out = new FileOutputStream(target)) {
            properties.setProperty(key, keyValue);
            properties.store(out, "Set value to properties file success.");
            LogUtils.info("Set value of key '" + key + "' to file: " + target);
        } catch (Exception e) {
            LogUtils.error("Can not write key '" + key + "' to file: " + target, e);
        }
    }

}
