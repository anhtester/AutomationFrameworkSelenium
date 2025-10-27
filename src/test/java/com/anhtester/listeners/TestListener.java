package com.anhtester.listeners;

import com.anhtester.annotations.FrameworkAnnotation;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.enums.AuthorType;
import com.anhtester.enums.Browser;
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
import org.testng.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.anhtester.constants.FrameworkConstants.*;

public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener {

   static int count_totalTCs;
   static int count_passedTCs;
   static int count_skippedTCs;
   static int count_failedTCs;

   private ScreenRecorderHelpers screenRecorder;

   public TestListener() {
      try {
         boolean enableRecord = VIDEO_RECORD != null && VIDEO_RECORD.toLowerCase().trim().equals(YES);

         if (enableRecord && !GraphicsEnvironment.isHeadless()) {
            screenRecorder = new ScreenRecorderHelpers();
            LogUtils.info("Screen recorder initialized.");
         } else {
            LogUtils.info("Skip screen recorder: VIDEO_RECORD=" + VIDEO_RECORD +
                    ", Headless=" + GraphicsEnvironment.isHeadless());
         }
      } catch (Exception e) {
         LogUtils.error("Failed to init ScreenRecorder: " + e.getMessage());
      }
   }

   public String getTestName(ITestResult result) {
      return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
   }

   public String getTestDescription(ITestResult result) {
      return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
   }

   @Override
   public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
      // Before every method in the Test Class
      //System.out.println(method.getTestMethod().getMethodName());
   }

   @Override
   public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
      // After every method in the Test Class
      //System.out.println(method.getTestMethod().getMethodName());
   }

   @Override
   public void onStart(ISuite iSuite) {
      LogUtils.info("********** RUN STARTED **********");
      LogUtils.info("========= INSTALLING CONFIGURATION DATA =========");
//        try {
//            FileUtils.deleteDirectory(new File("target/allure-results"));
//            System.out.println("Deleted Directory target/allure-results");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

      PropertiesHelpers.loadAllFiles();
      AllureManager.setAllureEnvironmentInformation();
      ExtentReportManager.initReports();
      LogUtils.info("========= INSTALLED CONFIGURATION DATA =========");
      LogUtils.info("=====> Starting Suite: " + iSuite.getName());
   }

   @Override
   public void onFinish(ISuite iSuite) {
      LogUtils.info("********** RUN FINISHED **********");
      LogUtils.info("=====> End Suite: " + iSuite.getName());
      //End Suite and execute Extents Report
      ExtentReportManager.flushReports();
      //Zip Folder reports
      ZipUtils.zipReportFolder();
      //Send notification to Telegram
      TelegramManager.sendReportPath();
      //Send mail
      EmailSendUtils.sendEmail(count_totalTCs, count_passedTCs, count_failedTCs, count_skippedTCs);

      //Write information in Allure Report
      AllureEnvironmentWriter.allureEnvironmentWriter(ImmutableMap.<String, String>builder().put("Target Execution", FrameworkConstants.TARGET).put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT)).put("Page Load Timeout", String.valueOf(FrameworkConstants.WAIT_PAGE_LOADED)).put("Headless Mode", FrameworkConstants.HEADLESS).put("Local Browser", String.valueOf(Browser.CHROME)).put("Remote URL", FrameworkConstants.REMOTE_URL).put("Remote Port", FrameworkConstants.REMOTE_PORT).put("TCs Total", String.valueOf(count_totalTCs)).put("TCs Passed", String.valueOf(count_passedTCs)).put("TCs Skipped", String.valueOf(count_skippedTCs)).put("TCs Failed", String.valueOf(count_failedTCs)).build());

      //FileHelpers.copyFile("src/test/resources/config/allure/environment.xml", "target/allure-results/environment.xml");
      FileHelpers.copyFile("src/test/resources/config/allure/categories.json", "target/allure-results/categories.json");
      FileHelpers.copyFile("src/test/resources/config/allure/executor.json", "target/allure-results/executor.json");

//        try {
//            // Generate Allure report
//            generateAllureReport();
//            // Expose the report using ngrok
//            exposeReportWithNgrok();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
   }

//    private void generateAllureReport() throws IOException, InterruptedException {
//        // Run the allure generate command
//        ProcessBuilder pb = new ProcessBuilder("allure.bat", "generate", "target/allure-results", "-o", "allure-report", "--clean");
//        pb.inheritIO(); // Outputs to console
//        Process process = pb.start();
//        process.waitFor();
//        System.out.println("Allure report generated successfully.");
//    }

//    private void exposeReportWithNgrok() throws IOException, InterruptedException {
//        // Step 1: Serve the allure-report folder with Python HTTP server (port 8000)
//        ProcessBuilder servePb = new ProcessBuilder("python", "-m", "http.server", "8000");
//        servePb.directory(new java.io.File(SystemHelpers.getCurrentDir() + "allure-report")); // Set working directory to allure-report
//        servePb.inheritIO();
//        Process serveProcess = servePb.start();
//
//        // Give the server a moment to start
//        Thread.sleep(5000);
//
//        // Step 2: Start ngrok to expose port 8000
//        ProcessBuilder ngrokPb = new ProcessBuilder("C:\\ngrok\\ngrok.exe", "http", "8000");
//        ngrokPb.inheritIO();
//        Process ngrokProcess = ngrokPb.start();
//
//        Thread.sleep(5000);
//
//        // Đọc và hiển thị đầu ra trong thread riêng
//        new Thread(() -> {
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(ngrokProcess.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                    if (line.contains("https://")) {
//                        String url = line.split("->")[0].trim().replace("Forwarding", "").trim();
//                        System.out.println("Public URL: " + url);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        // Đọc lỗi (nếu có)
//        new Thread(() -> {
//            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(ngrokProcess.getErrorStream()))) {
//                String line;
//                while ((line = errorReader.readLine()) != null) {
//                    System.err.println("ngrok Error: " + line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        Thread.sleep(5000);
//        System.out.println("ngrok is running. Check console for the public URL.");
//    }

   public AuthorType[] getAuthorType(ITestResult iTestResult) {
      if (iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class) == null) {
         return null;
      }
      AuthorType authorType[] = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class).author();
      return authorType;
   }

   public CategoryType[] getCategoryType(ITestResult iTestResult) {
      if (iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class) == null) {
         return null;
      }
      CategoryType categoryType[] = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class).category();
      return categoryType;
   }

   @Override
   public void onTestStart(ITestResult iTestResult) {
      LogUtils.info("Test case: " + getTestName(iTestResult) + " is starting...");
      count_totalTCs = count_totalTCs + 1;

      ExtentReportManager.createTest(iTestResult.getName());
      ExtentReportManager.addAuthors(getAuthorType(iTestResult));
      ExtentReportManager.addCategories(getCategoryType(iTestResult));
      ExtentReportManager.addDevices();
      ExtentReportManager.info(BrowserInfoUtils.getOSInfo());

      if (VIDEO_RECORD.toLowerCase().trim().equals(YES)) {
         screenRecorder.startRecording(getTestName(iTestResult));
      }

   }

   @Override
   public void onTestSuccess(ITestResult iTestResult) {
      LogUtils.info("Test case: " + getTestName(iTestResult) + " is passed.");
      count_passedTCs = count_passedTCs + 1;

      if (SCREENSHOT_PASSED_TCS.equals(YES)) {
         CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));
         ExtentReportManager.addScreenShot(Status.PASS, getTestName(iTestResult));
      }

      ExtentReportManager.logMessage(Status.PASS, "Test case: " + getTestName(iTestResult) + " is passed.");

      if (VIDEO_RECORD.trim().toLowerCase().equals(YES)) {
         WebUI.sleep(2);
         screenRecorder.stopRecording(true);
      }
   }

   @Override
   public void onTestFailure(ITestResult iTestResult) {
      LogUtils.error("FAILED !! Test case " + getTestName(iTestResult) + " is failed.");
      LogUtils.error(iTestResult.getThrowable());

      count_failedTCs = count_failedTCs + 1;

      if (SCREENSHOT_FAILED_TCS.equals(YES)) {
         CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));
         ExtentReportManager.addScreenShot(Status.FAIL, getTestName(iTestResult));
      }

      ExtentReportManager.logMessage(Status.FAIL, iTestResult.getThrowable().toString());

      if (VIDEO_RECORD.toLowerCase().trim().equals(YES)) {
         WebUI.sleep(2);
         screenRecorder.stopRecording(true);
      }
   }

   @Override
   public void onTestSkipped(ITestResult iTestResult) {
      LogUtils.warn("WARNING!! Test case: " + getTestName(iTestResult) + " is skipped.");
      count_skippedTCs = count_skippedTCs + 1;

      if (SCREENSHOT_SKIPPED_TCS.equals(YES)) {
         CaptureHelpers.captureScreenshot(DriverManager.getDriver(), getTestName(iTestResult));
      }

      ExtentReportManager.logMessage(Status.SKIP, "Test case: " + getTestName(iTestResult) + " is skipped.");

      if (VIDEO_RECORD.toLowerCase().trim().equals(YES)) {
         screenRecorder.stopRecording(true);
      }
   }

   @Override
   public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
      ExtentReportManager.logMessage("Test failed but it is in defined success ratio: " + getTestName(iTestResult));
   }

}
