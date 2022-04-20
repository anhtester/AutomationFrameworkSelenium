/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import anhtester.com.exceptions.FrameworkException;
import anhtester.com.exceptions.InvalidPathForExtentReportFileException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static anhtester.com.constants.FrameworkConstants.*;

public final class ReportUtils {

    private ReportUtils() {
    }

    public static String createExtentReportPath() {
        if (override_reports.trim().equalsIgnoreCase(NO)) {
            /**
             * Report name ->
             *
             * Windows_10_Tue_Oct_05_02_30_46_IST_2021_AutomationReport.html
             * Mac_OS_X_Tue_Feb_22_17_09_05_IST_2022_AutomationReport.html
             */
            return EXTENT_REPORT_FOLDER_PATH + BrowserInfoUtils.getOSInfo() + "_" + DateUtils.getCurrentDate() + "_"
                    + EXTENT_REPORT_FILE_NAME;

        } else {
            return EXTENT_REPORT_FOLDER_PATH + "/" + EXTENT_REPORT_FILE_NAME;
        }
    }

    public static void openReports() {
        if (open_reports_after_execution.trim().equalsIgnoreCase(YES)) {
            try {
                Desktop.getDesktop().browse(new File(getExtentReportFilePath()).toURI());
            } catch (FileNotFoundException fileNotFoundException) {
                throw new InvalidPathForExtentReportFileException("Extent Report file you are trying to reach is not found", fileNotFoundException.fillInStackTrace());
            } catch (IOException ioException) {
                throw new FrameworkException("Extent Report file you are trying to reach got IOException while reading the file", ioException.fillInStackTrace());
            }
        }
    }
}

