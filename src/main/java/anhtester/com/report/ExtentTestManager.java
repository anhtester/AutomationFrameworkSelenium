package anhtester.com.report;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Now, the scope is default - Outside this package, No one will be able to access this
    // We do not want any Automation Tester to come and use methods like addAuthors(), addTags()
    public static ExtentTest getExtentTest() {
        //System.out.println("ExtentTestManager class: " + extentTest.get());
        return extentTest.get();
    }

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void unload() {
        extentTest.remove();
    }

}