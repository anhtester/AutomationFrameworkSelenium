/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package anhtester.com.constants;

import anhtester.com.helpers.Helpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.utils.ReportUtils;

//final -> We do not want any class to extend this class
public final class FrameworkConstants {

    //private -> We do not want anyone to create the object of this class
    private FrameworkConstants() {
    }

    public static final String PROJECT_PATH = Helpers.getCurrentDir();
    public static final String EXCEL_DATA_PATH = PropertiesHelpers.getValue("excelDataFilePath");
    public static final String EXCEL_DATA_PATH_FULL = PropertiesHelpers.getValue("excelDataFilePathFull");

    public static final String BROWSER = PropertiesHelpers.getValue("browser");
    public static final String BASE_URL = PropertiesHelpers.getValue("base_url");
    public static final String REMOTE_URL = PropertiesHelpers.getValue("remote_url");
    public static final String REMOTE_PORT = PropertiesHelpers.getValue("remote_port");
    public static final String PROJECT_NAME = PropertiesHelpers.getValue("projectName");
    public static final String REPORT_TITLE = PropertiesHelpers.getValue("reportTitle");
    public static final String EXTENT_REPORT_NAME = PropertiesHelpers.getValue("extentReportName");
    public static final String EXTENT_REPORT_FOLDER = PropertiesHelpers.getValue("extentReportFolder");
    public static final String ExportVideoPath = PropertiesHelpers.getValue("exportVideoPath");
    public static final String ExportCapturePath = PropertiesHelpers.getValue("exportCapturePath");
    public static final String AUTHOR = PropertiesHelpers.getValue("author");
    public static final String TARGET = PropertiesHelpers.getValue("target");
    public static final String HEADLESS = PropertiesHelpers.getValue("headless");
    public static final String override_reports = PropertiesHelpers.getValue("override_reports");
    public static final String open_reports_after_execution = PropertiesHelpers.getValue("open_reports_after_execution");
    public static final String send_email_to_users = PropertiesHelpers.getValue("send_email_to_users");
    public static final String screenshot_passed_steps = PropertiesHelpers.getValue("screenshot_passed_steps");
    public static final String screenshot_failed_steps = PropertiesHelpers.getValue("screenshot_failed_steps");
    public static final String screenshot_skipped_steps = PropertiesHelpers.getValue("screenshot_skipped_steps");

    public static final int WAIT_DEFAULT = Integer.parseInt(PropertiesHelpers.getValue("WAIT_DEFAULT"));
    public static final int WAIT_IMPLICIT = Integer.parseInt(PropertiesHelpers.getValue("WAIT_IMPLICIT"));
    public static final int WAIT_EXPLICIT = Integer.parseInt(PropertiesHelpers.getValue("WAIT_EXPLICIT"));
    public static final int WAIT_PAGE_LOADED = Integer.parseInt(PropertiesHelpers.getValue("WAIT_PAGE_LOADED"));

    public static final String EXTENT_REPORT_FOLDER_PATH = PROJECT_PATH + EXTENT_REPORT_FOLDER;
    public static final String EXTENT_REPORT_FILE_NAME = EXTENT_REPORT_NAME + ".html";
    public static String EXTENT_REPORT_FILE_PATH = EXTENT_REPORT_FOLDER_PATH + "/" + EXTENT_REPORT_FILE_NAME;

    //Zip file for Report folder
    public static final String Zipped_ExtentReports_Folder_Name = EXTENT_REPORT_FOLDER + ".zip";

    public static final String TEST_DATA_XLSX_FILE = "src/test/resources/testdatafile/ClientsDataExcel.xlsx";

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

    public static String getExtentReportFilePath() {
        if (EXTENT_REPORT_FILE_PATH.isEmpty()) {
            EXTENT_REPORT_FILE_PATH = ReportUtils.createExtentReportPath();
        }
        return EXTENT_REPORT_FILE_PATH;
    }

}
