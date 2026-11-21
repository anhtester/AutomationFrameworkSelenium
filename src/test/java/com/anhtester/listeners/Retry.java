package com.anhtester.listeners;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.utils.LogUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-run a failed test up to RETRY_TEST_FAIL times (config.properties or -DRETRY_TEST_FAIL=n).
 * TestNG creates one instance per test method, so the counter is per method.
 */
public class Retry implements IRetryAnalyzer {
    private int count = 0;
    private static final int maxTry = parseMaxTry();

    private static int parseMaxTry() {
        try {
            return Math.max(0, Integer.parseInt(FrameworkConstants.RETRY_TEST_FAIL.trim()));
        } catch (Exception e) {
            LogUtils.warn("Invalid RETRY_TEST_FAIL='" + FrameworkConstants.RETRY_TEST_FAIL + "', retry is disabled.");
            return 0;
        }
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (iTestResult.isSuccess() || count >= maxTry) {
            return false;
        }
        count++;
        Throwable cause = iTestResult.getThrowable();
        LogUtils.warn(String.format("RETRY %d/%d: test %s.%s failed because: %s",
                count, maxTry,
                iTestResult.getTestClass().getRealClass().getSimpleName(),
                iTestResult.getMethod().getMethodName(),
                cause == null ? "unknown" : cause.getClass().getSimpleName() + " - " + cause.getMessage()));
        return true; // TestNG marks this attempt as SKIPPED (wasRetried = true) and runs the test again
    }
}
