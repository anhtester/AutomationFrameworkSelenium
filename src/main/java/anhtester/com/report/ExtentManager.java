package anhtester.com.report;

import anhtester.com.helpers.PropertiesHelpers;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static final ExtentReports extentReports = new ExtentReports();

    public synchronized static ExtentReports getExtentReports() {
        ExtentSparkReporter reporter = new ExtentSparkReporter(PropertiesHelpers.getValue("extentReportPath"));
        reporter.config().setReportName(PropertiesHelpers.getValue("reportName"));
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Framework Name", PropertiesHelpers.getValue("reportName"));
        extentReports.setSystemInfo("Author", PropertiesHelpers.getValue("author"));
        return extentReports;
    }
}
