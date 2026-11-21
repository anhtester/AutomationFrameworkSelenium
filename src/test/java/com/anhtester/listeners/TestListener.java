package com.anhtester.listeners;

import com.anhtester.annotations.FrameworkAnnotation;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.driver.TargetFactory;
import com.anhtester.enums.AuthorType;
import com.anhtester.enums.CategoryType;
import com.anhtester.helpers.*;
import com.anhtester.keywords.WebUI;
import com.anhtester.reports.AllureManager;
import com.anhtester.reports.ExtentReportManager;
import com.anhtester.reports.TelegramManager;
import com.anhtester.utils.BrowserInfoUtils;
import com.anhtester.utils.EmailSendUtils;
import com.anhtester.utils.LogUtils;
import com.anhtester.utils.ZipUtils;
import com.aventstack.extentreports.Status;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.testng.*;

import java.awt.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.anhtester.constants.FrameworkConstants.*;

/**
 * Main TestNG listener of the framework:
 * - logs (with the test name on every line + one log file per test),
 * - Extent report, screenshots, video,
 * - soft assertions (verified right after each test method),
 * - correct counters with retry and parallel execution,
 * - failure diagnostics (stack trace, URL, console logs, test log).
 */
public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener, IConfigurationListener {

   private static final AtomicInteger count_passedTCs = new AtomicInteger();
   private static final AtomicInteger count_skippedTCs = new AtomicInteger();
   private static final AtomicInteger count_failedTCs = new AtomicInteger();
   private static final AtomicInteger count_retriedTCs = new AtomicInteger();
   private static final AtomicBoolean TEST_LOG_FOLDER_CLEANED = new AtomicBoolean(false);

   private ScreenRecorderHelpers screenRecorder;

   public TestListener() {
      try {
         boolean enableRecord = YES.equalsIgnoreCase(VIDEO_RECORD);

         if (enableRecord && !GraphicsEnvironment.isHeadless()) {
            screenRecorder = new ScreenRecorderHelpers();
            LogUtils.info("Screen recorder initialized.");
         } else if (enableRecord) {
            LogUtils.warn("Skip screen recorder: VIDEO_RECORD=" + VIDEO_RECORD + " but the environment is headless.");
         }
      } catch (Exception e) {
         LogUtils.error("Failed to init ScreenRecorder: " + e.getMessage(), e);
      }
   }

   private boolean isRecording() {
      return screenRecorder != null;
   }

   public String getTestName(ITestResult result) {
      return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
   }

   public String getTestDescription(ITestResult result) {
      return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
   }

   /**
    * Name used in logs and for the per-test log file: SimpleClassName.methodName
    */
   private static String getLogName(ITestNGMethod method) {
      return method.getRealClass().getSimpleName() + "." + method.getMethodName();
   }

   private static long getDuration(ITestResult result) {
      return result.getEndMillis() - result.getStartMillis();
   }

   // ------------------------------------------------------------------------------------------
   // Method / configuration hooks: test name in logs + soft assertions
   // ------------------------------------------------------------------------------------------

   @Override
   public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
      if (method.isTestMethod()) {
         LogUtils.setTestContext(getLogName(method.getTestMethod()));
         // A new test must never inherit soft assert errors or diagnostics of a previous test on this thread
         WebUI.resetSoftAssert();
         DiagnosticHelpers.reset();
      }
   }

   @Override
   public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
      if (!method.isTestMethod()) {
         return;
      }
      // Verify soft assertions right after the test method: a soft failure marks THIS test as FAILED
      try {
         WebUI.stopSoftAssertAll();
      } catch (AssertionError softError) {
         if (testResult.getStatus() == ITestResult.SUCCESS) {
            testResult.setStatus(ITestResult.FAILURE);
            testResult.setThrowable(softError);
         } else {
            LogUtils.error("Soft assertion errors (test already failed): " + softError.getMessage());
         }
      }
   }

   @Override
   public void beforeConfiguration(ITestResult tr, ITestNGMethod tm) {
      // @BeforeMethod / @AfterMethod: write their logs into the log file of the related test
      if (tm != null) {
         LogUtils.setTestContext(getLogName(tm));
      }
   }

   @Override
   public void onConfigurationFailure(ITestResult tr) {
      LogUtils.error("Configuration method FAILED: " + tr.getMethod().getQualifiedName(), tr.getThrowable());
   }

   @Override
   public void onConfigurationSuccess(ITestResult tr, ITestNGMethod tm) {
      // After @AfterMethod the test is completely finished on this thread
      if (tm != null && tr.getMethod().isAfterMethodConfiguration()) {
         LogUtils.clearTestContext();
      }
   }

   // ------------------------------------------------------------------------------------------
   // Suite
   // ------------------------------------------------------------------------------------------

   @Override
   public void onStart(ISuite iSuite) {
      LogUtils.info("********** RUN STARTED **********");
      LogUtils.info("========= INSTALLING CONFIGURATION DATA =========");

      cleanTestLogFolder();
      PropertiesHelpers.loadAllFiles();
      AllureManager.setAllureEnvironmentInformation();
      ExtentReportManager.initReports();
      logRunConfiguration();
      LogUtils.info("========= INSTALLED CONFIGURATION DATA =========");
      LogUtils.info("=====> Starting Suite: " + iSuite.getName());
   }

   private static void cleanTestLogFolder() {
      // Only once per JVM: a run with several suites must not delete the logs of the previous suites
      if (!TEST_LOG_FOLDER_CLEANED.compareAndSet(false, true)) {
         return;
      }
      try {
         FileUtils.deleteDirectory(new File(LogUtils.TEST_LOG_DIR));
      } catch (Exception e) {
         // Files can be locked by Log4j when several suites run in the same JVM: old logs are kept
         LogUtils.debug("Can not clean folder " + LogUtils.TEST_LOG_DIR + ": " + e.getMessage());
      }
   }

   private static void logRunConfiguration() {
      LogUtils.info("Java: " + System.getProperty("java.version") + " | OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
      LogUtils.info(String.format("Browser: %s | Target: %s | Headless: %s | Remote: %s:%s",
              TargetFactory.resolveBrowser(null), TARGET, HEADLESS, REMOTE_URL, REMOTE_PORT));
      LogUtils.info(String.format("Wait explicit: %ss | Page load: %ss | Retry failed tests: %s | Click retry: %s | Driver create retry: %s",
              WAIT_EXPLICIT, WAIT_PAGE_LOADED, RETRY_TEST_FAIL, CLICK_RETRY, DRIVER_CREATE_RETRY));
      LogUtils.info(String.format("Screenshot passed/failed/all steps: %s/%s/%s | Video: %s | WebDriver event log: %s | Failure diagnostics: %s",
              SCREENSHOT_PASSED_TCS, SCREENSHOT_FAILED_TCS, SCREENSHOT_ALL_STEPS, VIDEO_RECORD, WEBDRIVER_EVENT_LOG, FAILURE_DIAGNOSTICS));
      LogUtils.info("Per-test log files: " + LogUtils.TEST_LOG_DIR);
   }

   @Override
   public void onFinish(ISuite iSuite) {
      LogUtils.clearTestContext();
      int passed = count_passedTCs.get();
      int failed = count_failedTCs.get();
      int skipped = count_skippedTCs.get();
      int total = passed + failed + skipped;

      LogUtils.info("********** RUN FINISHED **********");
      LogUtils.info("=====> End Suite: " + iSuite.getName());
      LogUtils.info(String.format("RESULT: Total=%d | Passed=%d | Failed=%d | Skipped=%d | Retried attempts=%d",
              total, passed, failed, skipped, count_retriedTCs.get()));

      //End Suite and execute Extents Report
      runSafely("Flush Extent report", ExtentReportManager::flushReports);
      //Zip Folder reports
      runSafely("Zip report folder", ZipUtils::zipReportFolder);
      //Send notification to Telegram
      runSafely("Send Telegram notification", TelegramManager::sendReportPath);
      //Send mail
      runSafely("Send email", () -> EmailSendUtils.sendEmail(total, passed, failed, skipped));

      //Write information in Allure Report
      runSafely("Write Allure environment", () -> AllureEnvironmentWriter.allureEnvironmentWriter(ImmutableMap.<String, String>builder()
              .put("Target Execution", FrameworkConstants.TARGET)
              .put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT))
              .put("Page Load Timeout", String.valueOf(FrameworkConstants.WAIT_PAGE_LOADED))
              .put("Headless Mode", FrameworkConstants.HEADLESS)
              .put("Local Browser", TargetFactory.resolveBrowser(null))
              .put("Remote URL", String.valueOf(FrameworkConstants.REMOTE_URL))
              .put("Remote Port", String.valueOf(FrameworkConstants.REMOTE_PORT))
              .put("TCs Total", String.valueOf(total))
              .put("TCs Passed", String.valueOf(passed))
              .put("TCs Skipped", String.valueOf(skipped))
              .put("TCs Failed", String.valueOf(failed))
              .build()));

      FileHelpers.copyFile("src/test/resources/config/allure/categories.json", "target/allure-results/categories.json");
      FileHelpers.copyFile("src/test/resources/config/allure/executor.json", "target/allure-results/executor.json");
   }

   /**
    * Reporting steps must not break each other: log the error and continue.
    */
   private static void runSafely(String stepName, Runnable step) {
      try {
         step.run();
      } catch (Exception e) {
         LogUtils.error("Step '" + stepName + "' failed: " + e.getMessage(), e);
      }
   }

   // ------------------------------------------------------------------------------------------
   // Tests
   // ------------------------------------------------------------------------------------------

   public AuthorType[] getAuthorType(ITestResult iTestResult) {
      FrameworkAnnotation annotation = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class);
      return annotation == null ? null : annotation.author();
   }

   public CategoryType[] getCategoryType(ITestResult iTestResult) {
      FrameworkAnnotation annotation = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class);
      return annotation == null ? null : annotation.category();
   }

   @Override
   public void onTestStart(ITestResult iTestResult) {
      LogUtils.setTestContext(getLogName(iTestResult.getMethod()));
      LogUtils.info("========== Test case: " + getTestName(iTestResult) + " is starting... ==========");

      ExtentReportManager.createTest(iTestResult.getName());
      ExtentReportManager.addAuthors(getAuthorType(iTestResult));
      ExtentReportManager.addCategories(getCategoryType(iTestResult));
      ExtentReportManager.addDevices();
      ExtentReportManager.info(BrowserInfoUtils.getOSInfo());

      if (isRecording()) {
         screenRecorder.startRecording(getTestName(iTestResult));
      }
   }

   @Override
   public void onTestSuccess(ITestResult iTestResult) {
      LogUtils.info("Test case: " + getTestName(iTestResult) + " is PASSED (" + getDuration(iTestResult) + " ms).");
      count_passedTCs.incrementAndGet();

      if (SCREENSHOT_PASSED_TCS.equals(YES)) {
         CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));
         ExtentReportManager.addScreenShot(Status.PASS, getTestName(iTestResult));
      }

      ExtentReportManager.logMessage(Status.PASS, "Test case: " + getTestName(iTestResult) + " is passed.");
      stopRecording();
   }

   @Override
   public void onTestFailure(ITestResult iTestResult) {
      Throwable error = iTestResult.getThrowable();
      LogUtils.error("FAILED !! Test case " + getTestName(iTestResult) + " is failed (" + getDuration(iTestResult) + " ms).", error);

      count_failedTCs.incrementAndGet();

      if (SCREENSHOT_FAILED_TCS.equals(YES)) {
         CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));
         ExtentReportManager.addScreenShot(Status.FAIL, getTestName(iTestResult));
      }

      if (error != null) {
         ExtentReportManager.logThrowable(Status.FAIL, error);
      } else {
         ExtentReportManager.logMessage(Status.FAIL, "Test case: " + getTestName(iTestResult) + " is failed.");
      }

      if (YES.equalsIgnoreCase(FAILURE_DIAGNOSTICS)) {
         DiagnosticHelpers.Diagnostics diagnostics = DiagnosticHelpers.collectOnce();
         if (diagnostics != null) {
            LogUtils.error("Browser state when failed:" + System.lineSeparator() + diagnostics.summary());
            ExtentReportManager.logCodeBlock(Status.INFO, "Failure info", diagnostics.summary());
            ExtentReportManager.logCodeBlock(Status.INFO, "Browser console logs", diagnostics.consoleLogs);
         }
         ExtentReportManager.logCodeBlock(Status.INFO, "Test log (last 200 lines)", LogUtils.readTestLog(LogUtils.getTestContext(), 200));
      }

      stopRecording();
   }

   @Override
   public void onTestSkipped(ITestResult iTestResult) {
      if (iTestResult.wasRetried()) {
         // This attempt failed and will be executed again: it is neither a final failure nor a real skip
         count_retriedTCs.incrementAndGet();
         LogUtils.warn("Test case: " + getTestName(iTestResult) + " failed and will be RETRIED.", iTestResult.getThrowable());
         stopRecording();
         // Keep only the last attempt in the Extent report (Allure shows the retries in its own tab)
         ExtentReportManager.removeCurrentTest();
         return;
      }

      LogUtils.warn("WARNING!! Test case: " + getTestName(iTestResult) + " is skipped.", iTestResult.getThrowable());
      count_skippedTCs.incrementAndGet();

      if (SCREENSHOT_SKIPPED_TCS.equals(YES) && DriverManager.getDriver() != null) {
         CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));
      }

      ExtentReportManager.logMessage(Status.SKIP, "Test case: " + getTestName(iTestResult) + " is skipped.");
      if (iTestResult.getThrowable() != null) {
         ExtentReportManager.logThrowable(Status.SKIP, iTestResult.getThrowable());
      }
      stopRecording();
   }

   private void stopRecording() {
      if (isRecording()) {
         try {
            WebUI.sleep(2); // keep the last state of the screen in the video
            screenRecorder.stopRecording(true);
         } catch (Exception e) {
            LogUtils.warn("Can not stop screen recording: " + e.getMessage(), e);
         }
      }
   }

   @Override
   public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
      ExtentReportManager.logMessage("Test failed but it is in defined success ratio: " + getTestName(iTestResult));
   }

}
