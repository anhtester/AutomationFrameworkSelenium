package anhtester.com.common;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.listeners.TestListener;
import anhtester.com.driver.DriverManager;
import anhtester.com.driver.TargetFactory;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.report.AllureManager;
import anhtester.com.report.ExtentReportManager;
import anhtester.com.report.ExtentTestManager;
import anhtester.com.utils.DecodeUtils;
import anhtester.com.utils.WebUI;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.*;

@Listeners({TestListener.class})
public class BaseTest {

    @BeforeSuite
    public void beforeSuite() {
        AllureManager.setAllureEnvironmentInformation();
        PropertiesHelpers.loadAllFiles(); //Config and Locators
        ExcelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_PATH_FULL, "SignIn");
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void createDriver(@Optional("chrome") String browser) {
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);

        //ExtentReportManager.createTest("SETUP STEPS BEFORE !!!");

        //Dành cho bộ thiết kế test case khi có 1 bước gọi trước @Test và có dùng class WebUI
        //Cụ thể trong project của mình thì có hàm SignIn nên vì thế cần khởi tạo report trước mỗi hàm
        //Vì class WebUI gọi trước và có gọi lại từ class ExtentReport
        //     nên vì thế nếu không khởi tạo ExtentReport trước WebUI thì xảy ra null cho class ExtentReport
        //Và vì ExtentReport nó cần createTest là tên test case (@Test) nên hàm thường chúng ta đặt tên chính nó luôn là SignIn cũng được
        //An thì đặt là SETUP STEPS BEFORE !!!
        //Giờ nếu muốn bỏ cái bước này trong report thì các bạn buộc phải mang nội dung trong hàm SignIn vào trong mỗi @Test luôn
        //Xem ClientTest bạn sẽ thấy cái BeforeMethod là hàm SignIn

    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        DriverManager.quit();
    }

    public WebDriver createBrowser(@Optional("chrome") String browser) {
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);
        return DriverManager.getDriver();
    }

}
