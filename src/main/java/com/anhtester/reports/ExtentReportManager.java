package com.anhtester.reports;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.enums.AuthorType;
import com.anhtester.enums.CategoryType;
import com.anhtester.utils.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
//import tech.grasshopper.reporter.ExtentPDFReporter;

import java.io.File;
import java.util.Objects;

public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static String link = "";

    public static synchronized void initReports() {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();

            if (FrameworkConstants.OVERRIDE_REPORTS.trim().equals(FrameworkConstants.NO)) {
                LogUtils.info("OVERRIDE EXTENT REPORTS = " + FrameworkConstants.OVERRIDE_REPORTS);
                link = FrameworkConstants.EXTENT_REPORT_FOLDER_PATH + File.separator + DateUtils.getCurrentDateTimeCustom("_") + "_" + FrameworkConstants.EXTENT_REPORT_FILE_NAME;
                LogUtils.info("Link Extent Report: " + link);
            } else {
                LogUtils.info("OVERRIDE EXTENT REPORTS = " + FrameworkConstants.OVERRIDE_REPORTS);
                link = FrameworkConstants.EXTENT_REPORT_FILE_PATH;
                LogUtils.info("Link Extent Report: " + link);
            }

//            ExtentPDFReporter pdf = new ExtentPDFReporter("reports/ExtentReports/PdfReport.pdf");
//            try {
//                pdf.loadJSONConfig(new File("src/test/resources/pdf-config.json"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            extentReports.attachReporter(pdf);

            ExtentSparkReporter spark = new ExtentSparkReporter(link);
            extentReports.attachReporter(spark);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle(FrameworkConstants.REPORT_TITLE);
            spark.config().setReportName(FrameworkConstants.REPORT_TITLE);
            extentReports.setSystemInfo("Framework Name", FrameworkConstants.REPORT_TITLE);
            extentReports.setSystemInfo("Author", FrameworkConstants.AUTHOR);

            LogUtils.info("Extent Reports is installed.");
        }
    }

    public static synchronized void flushReports() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentTestManager.unload();
        ReportUtils.openReports(link);
    }

    /**
     * @return the ExtentTest of the current thread, or null when no test is running
     */
    private static ExtentTest current() {
        return ExtentTestManager.getExtentTest();
    }

    public static synchronized void createTest(String testCaseName) {
        initReports();
        ExtentTestManager.setExtentTest(extentReports.createTest(IconUtils.getBrowserIcon() + " " + testCaseName));
    }

    public static synchronized void createTest(String testCaseName, String description) {
        initReports();
        ExtentTestManager.setExtentTest(extentReports.createTest(testCaseName, description));
    }

    public static synchronized void removeTest(String testCaseName) {
        if (extentReports != null) {
            extentReports.removeTest(testCaseName);
        }
    }

    /**
     * Remove the ExtentTest of the current thread from the report (used when a test is retried,
     * so only the last attempt is shown).
     */
    public static synchronized void removeCurrentTest() {
        ExtentTest test = current();
        if (extentReports != null && test != null) {
            extentReports.removeTest(test);
        }
        ExtentTestManager.unload();
    }

    /**
     * Adds the screenshot.
     *
     * @param message the message
     */
    public static void addScreenShot(String message) {
        addScreenShot(Status.INFO, message);
    }

    /**
     * Adds the screenshot.
     *
     * @param status  the status
     * @param message the message
     */
    public static void addScreenShot(Status status, String message) {
        ExtentTest test = current();
        WebDriver driver = DriverManager.getDriver();
        if (test == null || driver == null) {
            return;
        }
        try {
            String base64Image = "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            test.log(status, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
        } catch (Exception e) {
            LogUtils.warn("Can not add screenshot to Extent report: " + e.getMessage());
        }
    }

    synchronized public static void addAuthors(AuthorType[] authors) {
        ExtentTest test = current();
        if (test == null) return;
        if (authors == null) {
            test.assignAuthor("ANHTESTER");
        } else {
            for (AuthorType author : authors) {
                test.assignAuthor(author.toString());
            }
        }
    }

    synchronized public static void addCategories(CategoryType[] categories) {
        ExtentTest test = current();
        if (test == null) return;
        if (categories == null) {
            test.assignCategory("REGRESSION");
        } else {
            for (CategoryType category : categories) {
                test.assignCategory(category.toString());
            }
        }
    }

    synchronized public static void addDevices() {
        ExtentTest test = current();
        if (test == null) return;
        test.assignDevice(BrowserInfoUtils.getBrowserInfo());
    }

    public static void logMessage(String message) {
        logMessage(Status.INFO, message);
    }

    public static void logMessage(Status status, String message) {
        ExtentTest test = current();
        if (test != null) test.log(status, message);
    }

    public static void logMessage(Status status, Object message) {
        ExtentTest test = current();
        if (test == null) return;
        if (message instanceof Throwable) {
            test.log(status, (Throwable) message);
        } else {
            test.log(status, String.valueOf(message));
        }
    }

    /**
     * Log a throwable with its full stack trace.
     */
    public static void logThrowable(Status status, Throwable throwable) {
        ExtentTest test = current();
        if (test != null && throwable != null) test.log(status, throwable);
    }

    /**
     * Log a block of text (logs, page info...) as a code block.
     */
    public static void logCodeBlock(Status status, String title, String content) {
        ExtentTest test = current();
        if (test == null || content == null || content.isEmpty()) return;
        test.log(status, "<b>" + title + "</b>");
        test.log(status, MarkupHelper.createCodeBlock(content, CodeLanguage.XML));
    }

    public static void pass(String message) {
        ExtentTest test = current();
        if (test != null) test.pass(message);
    }

    public static void pass(Markup message) {
        ExtentTest test = current();
        if (test != null) test.pass(message);
    }

    public static void fail(String message) {
        ExtentTest test = current();
        if (test != null) test.fail(message);
    }

    public static void fail(Object message) {
        logMessage(Status.FAIL, message);
    }

    public static void fail(Markup message) {
        ExtentTest test = current();
        if (test != null) test.fail(message);
    }

    public static void skip(String message) {
        ExtentTest test = current();
        if (test != null) test.skip(message);
    }

    public static void skip(Markup message) {
        ExtentTest test = current();
        if (test != null) test.skip(message);
    }

    public static void info(Markup message) {
        ExtentTest test = current();
        if (test != null) test.info(message);
    }

    public static void info(String message) {
        ExtentTest test = current();
        if (test != null) test.info(message);
    }

    public static void warning(String message) {
        ExtentTest test = current();
        if (test != null) test.log(Status.WARNING, message);
    }

}
