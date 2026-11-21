package com.anhtester.listeners;

import com.anhtester.driver.DriverManager;
import com.anhtester.helpers.DiagnosticHelpers;
import com.anhtester.reports.AllureManager;
import com.anhtester.utils.LogUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

import static com.anhtester.constants.FrameworkConstants.*;

/**
 * Allure lifecycle listener (registered in META-INF/services):
 * attach screenshot, browser diagnostics and the test log before the Allure test result is closed.
 */
public class AllureListener implements TestLifecycleListener {

    @Override
    public void beforeTestStop(TestResult result) {
        Status status = result.getStatus();
        boolean failed = status == Status.FAILED || status == Status.BROKEN;

        if (SCREENSHOT_PASSED_TCS.equals(YES) && status == Status.PASSED) {
            attachScreenshot(result.getName() + "_Passed_Screenshot");
        }
        if (SCREENSHOT_FAILED_TCS.equals(YES) && failed) {
            attachScreenshot(result.getName() + "_Failed_Screenshot");
        }
        if (failed && YES.equalsIgnoreCase(FAILURE_DIAGNOSTICS)) {
            AllureManager.attachFailureDiagnostics(DiagnosticHelpers.collectOnce(), LogUtils.readTestLog(LogUtils.getTestContext(), 1000));
        }
    }

    private static void attachScreenshot(String name) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return;
        }
        try {
            Allure.addAttachment(name, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        } catch (Exception e) {
            LogUtils.warn("Can not attach screenshot to Allure: " + e.getMessage());
        }
    }

}
