/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.constants;

import com.anhtester.helpers.SystemHelpers;
import com.anhtester.helpers.PropertiesHelpers;
import com.anhtester.utils.ReportUtils;

import java.io.File;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    static {
        PropertiesHelpers.loadAllFiles();
    }

    public static final String PROJECT_PATH = SystemHelpers.getCurrentDir();
    public static final String JSON_DATA_FILE_PATH = PropertiesHelpers.getValue("JSON_DATA_FILE_PATH");
    public static final String EXCEL_CMS_LOGIN = PropertiesHelpers.getValue("EXCEL_CMS_LOGIN");
    public static final String EXCEL_CMS_DATA = PropertiesHelpers.getValue("EXCEL_CMS_DATA");
    public static final String EXCEL_CMS_PRODUCTS_USER = PropertiesHelpers.getValue("EXCEL_CMS_PRODUCTS_USER");

    /**
     * Browser from -DBROWSER / env BROWSER / config file (in that priority)
     */
    public static final String BROWSER = PropertiesHelpers.getValue("BROWSER");
    /**
     * Browser only from config.properties (used to let the suite parameter win over the file)
     */
    public static final String BROWSER_FROM_FILE = PropertiesHelpers.getFileValue("BROWSER");
    public static final String URL_CMS_ADMIN = PropertiesHelpers.getValue("URL_CMS_ADMIN");
    public static final String URL_CMS_USER = PropertiesHelpers.getValue("URL_CMS_USER");
    public static final String REMOTE_URL = PropertiesHelpers.getValue("REMOTE_URL");
    public static final String REMOTE_PORT = PropertiesHelpers.getValue("REMOTE_PORT");
    public static final String PROJECT_NAME = PropertiesHelpers.getValue("PROJECT_NAME");
    public static final String REPORT_TITLE = PropertiesHelpers.getValue("REPORT_TITLE");
    public static final String EXTENT_REPORT_NAME = PropertiesHelpers.getValue("EXTENT_REPORT_NAME");
    public static final String EXTENT_REPORT_FOLDER = PropertiesHelpers.getValue("EXTENT_REPORT_FOLDER");
    public static final String EXPORT_VIDEO_PATH = PropertiesHelpers.getValue("EXPORT_VIDEO_PATH");
    public static final String EXPORT_CAPTURE_PATH = PropertiesHelpers.getValue("EXPORT_CAPTURE_PATH");
    /**
     * Folder where the browser saves downloaded files (inside the project, never the user's Downloads folder)
     */
    public static final String EXPORT_DOWNLOAD_PATH = PropertiesHelpers.getValue("EXPORT_DOWNLOAD_PATH", "exports/Downloads");
    public static final String SEND_REPORT_TO_TELEGRAM = PropertiesHelpers.getValue("SEND_REPORT_TO_TELEGRAM");
    public static final String TELEGRAM_TOKEN = PropertiesHelpers.getValue("TELEGRAM_TOKEN");
    public static final String TELEGRAM_CHATID = PropertiesHelpers.getValue("TELEGRAM_CHATID");
    public static final String AUTHOR = PropertiesHelpers.getValue("AUTHOR");
    public static final String TARGET = PropertiesHelpers.getValue("TARGET", "local");
    public static final String HEADLESS = PropertiesHelpers.getValue("HEADLESS", "false");
    public static final String OVERRIDE_REPORTS = PropertiesHelpers.getValue("OVERRIDE_REPORTS");
    public static final String OPEN_REPORTS_AFTER_EXECUTION = PropertiesHelpers.getValue("OPEN_REPORTS_AFTER_EXECUTION");
    public static final String SEND_EMAIL_TO_USERS = PropertiesHelpers.getValue("SEND_EMAIL_TO_USERS");
    public static final String SCREENSHOT_PASSED_TCS = PropertiesHelpers.getValue("SCREENSHOT_PASSED_TCS", "no");
    public static final String SCREENSHOT_FAILED_TCS = PropertiesHelpers.getValue("SCREENSHOT_FAILED_TCS", "yes");
    public static final String SCREENSHOT_SKIPPED_TCS = PropertiesHelpers.getValue("SCREENSHOT_SKIPPED_TCS", "no");
    public static final String SCREENSHOT_ALL_STEPS = PropertiesHelpers.getValue("SCREENSHOT_ALL_STEPS", "no");
    public static final String ZIP_FOLDER = PropertiesHelpers.getValue("ZIP_FOLDER");
    public static final String ZIP_FOLDER_PATH = PropertiesHelpers.getValue("ZIP_FOLDER_PATH");
    public static final String ZIP_FOLDER_NAME = PropertiesHelpers.getValue("ZIP_FOLDER_NAME");
    public static final String VIDEO_RECORD = PropertiesHelpers.getValue("VIDEO_RECORD", "no");

    public static final String LOCATE = PropertiesHelpers.getValue("LOCATE");
    public static final String RETRY_TEST_FAIL = PropertiesHelpers.getValue("RETRY_TEST_FAIL", "0");

    public static final int WAIT_DEFAULT = getInt("WAIT_DEFAULT", 5);
    public static final int WAIT_IMPLICIT = getInt("WAIT_IMPLICIT", 10);
    public static final int WAIT_EXPLICIT = getInt("WAIT_EXPLICIT", 10);
    public static final int WAIT_PAGE_LOADED = getInt("WAIT_PAGE_LOADED", 40);
    public static final int WAIT_SLEEP_STEP = getInt("WAIT_SLEEP_STEP", 0);
    public static final String ACTIVE_PAGE_LOADED = PropertiesHelpers.getValue("ACTIVE_PAGE_LOADED", "true");

    /**
     * Number of extra attempts to create the browser when it fails (Grid busy, driver download error, ...)
     */
    public static final int DRIVER_CREATE_RETRY = getInt("DRIVER_CREATE_RETRY", 1);
    /**
     * Number of attempts of a click when the element is stale or covered by another element
     */
    public static final int CLICK_RETRY = getInt("CLICK_RETRY", 3);
    /**
     * yes: log every WebDriver action automatically (DriverEventListener) into the per-test log file
     */
    public static final String WEBDRIVER_EVENT_LOG = PropertiesHelpers.getValue("WEBDRIVER_EVENT_LOG", "yes");
    /**
     * yes: attach URL, page source, browser console logs and the test log to reports when a test fails
     */
    public static final String FAILURE_DIAGNOSTICS = PropertiesHelpers.getValue("FAILURE_DIAGNOSTICS", "yes");

    public static final String EXTENT_REPORT_FOLDER_PATH = PROJECT_PATH + EXTENT_REPORT_FOLDER;
    public static final String EXTENT_REPORT_FILE_NAME = EXTENT_REPORT_NAME + ".html";
    public static String EXTENT_REPORT_FILE_PATH = EXTENT_REPORT_FOLDER_PATH + File.separator + EXTENT_REPORT_FILE_NAME;

    //Zip file for Report folder
    public static final String ZIPPED_EXTENT_REPORTS_FOLDER = EXTENT_REPORT_FOLDER + ".zip";

    public static final String YES = "yes";
    public static final String NO = "no";

    public static final String BOLD_START = "<b>";
    public static final String BOLD_END = "</b>";

    /* ICONS - START */

    public static final String ICON_SMILEY_PASS = "<i class='fa fa-smile-o' style='font-size:24px'></i>";
    public static final String ICON_SMILEY_SKIP = "<i class=\"fas fa-frown-open\"></i>";
    public static final String ICON_SMILEY_FAIL = "<i class='fa fa-frown-o' style='font-size:24px'></i>";

    public static final String ICON_OS_WINDOWS = "<i class='fa fa-windows' ></i>";
    public static final String ICON_OS_MAC = "<i class='fa fa-apple' ></i>";
    public static final String ICON_OS_LINUX = "<i class='fa fa-linux' ></i>";

    public static final String ICON_BROWSER_OPERA = "<i class=\"fa fa-opera\" aria-hidden=\"true\"></i>";
    public static final String ICON_BROWSER_EDGE = "<i class=\"fa fa-edge\" aria-hidden=\"true\"></i>";
    public static final String ICON_BROWSER_CHROME = "<i class=\"fa fa-chrome\" aria-hidden=\"true\"></i>";
    public static final String ICON_BROWSER_FIREFOX = "<i class=\"fa fa-firefox\" aria-hidden=\"true\"></i>";
    public static final String ICON_BROWSER_SAFARI = "<i class=\"fa fa-safari\" aria-hidden=\"true\"></i>";

    public static final String ICON_Navigate_Right = "<i class='fa fa-arrow-circle-right' ></i>";
    public static final String ICON_LAPTOP = "<i class='fa fa-laptop' style='font-size:18px'></i>";
    public static final String ICON_BUG = "<i class='fa fa-bug' ></i>";
    /* style="text-align:center;" */

    public static final String ICON_SOCIAL_GITHUB_PAGE_URL = "https://anhtester.com/";
    public static final String ICON_SOCIAL_LINKEDIN_URL = "https://www.linkedin.com/in/anhtester/";
    public static final String ICON_SOCIAL_GITHUB_URL = "https://github.com/anhtester";
    public static final String ICON_SOCIAL_LINKEDIN = "<a href='" + ICON_SOCIAL_LINKEDIN_URL
            + "'><i class='fa fa-linkedin-square' style='font-size:24px'></i></a>";
    public static final String ICON_SOCIAL_GITHUB = "<a href='" + ICON_SOCIAL_GITHUB_URL
            + "'><i class='fa fa-github-square' style='font-size:24px'></i></a>";

    public static final String ICON_CAMERA = "<i class=\"fa fa-camera\" aria-hidden=\"true\"></i>";

    public static final String ICON_BROWSER_PREFIX = "<i class=\"fa fa-";
    public static final String ICON_BROWSER_SUFFIX = "\" aria-hidden=\"true\"></i>";
    /* ICONS - END */

    private static int getInt(String key, int defaultValue) {
        String value = PropertiesHelpers.getValue(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            com.anhtester.utils.LogUtils.warn("Invalid number for " + key + "='" + value + "', use default " + defaultValue);
            return defaultValue;
        }
    }

    public static String getExtentReportFilePath() {
        if (EXTENT_REPORT_FILE_PATH.isEmpty()) {
            EXTENT_REPORT_FILE_PATH = ReportUtils.createExtentReportPath();
        }
        return EXTENT_REPORT_FILE_PATH;
    }

}
