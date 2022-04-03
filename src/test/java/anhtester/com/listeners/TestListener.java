package anhtester.com.listeners;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.config.ConfigFactory;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.report.AllureManager;
import anhtester.com.report.ExtentReportManager;
import anhtester.com.utils.*;
import anhtester.com.helpers.CaptureHelpers;
import com.aventstack.extentreports.Status;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static anhtester.com.constants.FrameworkConstants.*;

public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener  {

    static int count_totalTCs;
    static int count_passedTCs;
    static int count_skippedTCs;
    static int count_failedTCs;

    public String getTestName(ITestResult result) {
        return result.getTestName() != null ? result.getTestName()
                : result.getMethod().getConstructorOrMethod().getName();
    }

    public String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        //System.out.println(method.getTestMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        //System.out.println(method.getTestMethod().getMethodName());
    }

    @Override
    public void onStart(ISuite iSuite) {
        Log.info("Start suite testing for " + iSuite.getName());
        iSuite.setAttribute("WebDriver", DriverManager.getDriver());
        //Gọi hàm startRecord video trong CaptureHelpers class
        CaptureHelpers.startRecord(iSuite.getName());
        ExtentReportManager.initReports();
        ExtentReportManager.createTest("Before Steps setup");
    }

    @Override
    public void onFinish(ISuite iSuite) {
        Log.info("End suite testing " + iSuite.getName());
        //Kết thúc và thực thi Extents Report
        ExtentReportManager.flushReports();
        ZipUtils.zip();
        EmailSendUtils.sendEmail(count_totalTCs, count_passedTCs, count_failedTCs, count_skippedTCs);
        //Gọi hàm stopRecord video trong CaptureHelpers class
        CaptureHelpers.stopRecord();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Log.info(getTestName(iTestResult) + " test is starting...");
        count_totalTCs = count_totalTCs + 1;

        ExtentReportManager.createTest(iTestResult.getName(), getTestDescription(iTestResult));
        ExtentReportManager.addAuthors(iTestResult.getMethod().getConstructorOrMethod().getMethod()
                .getAnnotation(FrameworkAnnotation.class).author());
        ExtentReportManager.addCategories(iTestResult.getMethod().getConstructorOrMethod().getMethod()
                .getAnnotation(FrameworkAnnotation.class).category());
        ExtentReportManager.addDevices();

        ExtentReportManager.info(BOLD_START + IconUtils.getOSIcon() + " "
                + BrowserOSInfoUtils.getOS_Browser_BrowserVersionInfo() + BOLD_END);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        Log.info(getTestName(iTestResult) + " test is passed.");
        count_passedTCs = count_passedTCs + 1;

        AllureManager.takeScreenshotToAttachOnAllureReport();
        Allure.addAttachment("Browser Info", AllureManager.addBrowserInformationOnAllureReport());
        //ExtentReports log operation for passed tests.
        ExtentReportManager.logMessage(Status.PASS, getTestDescription(iTestResult));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Log.error(getTestName(iTestResult) + " test is failed.");
        count_failedTCs = count_failedTCs + 1;

        CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));

        //Allure report screenshot file and log
        Log.info("Allure Screenshot for test case: " + getTestName(iTestResult));

        AllureManager.takeScreenshotToAttachOnAllureReport();
        //takeScreenshotToAttachOnAllureReport();
        //AllureManager.saveTextLog(getTestName(iTestResult) + " failed and screenshot taken!");

        //Extent report screenshot file and log
        ExtentReportManager.addScreenShot(Status.FAIL, getTestName(iTestResult));
        ExtentReportManager.logMessage(Status.FAIL, getTestDescription(iTestResult));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Log.warn(getTestName(iTestResult) + " test is skipped.");
        count_skippedTCs = count_skippedTCs + 1;

        //ExtentReportManager.logMessage(Status.SKIP, getTestName(iTestResult) + " test is skipped.");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Log.error("Test failed but it is in defined success ratio " + getTestName(iTestResult));
        ExtentReportManager.logMessage("Test failed but it is in defined success ratio " + getTestName(iTestResult));
    }
}
