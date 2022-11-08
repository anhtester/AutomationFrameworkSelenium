/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.helpers.Helpers;
import anhtester.com.keyword.WebUI;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.utils.LocalStorageUtils;
import anhtester.com.utils.Log;
import anhtester.com.utils.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

public class TestHandle {

    WebDriver driver;
    SignInPage signInPage;
    DashboardPage dashboardPage;
    ProjectPage projectPage;

    @BeforeMethod
    public void Setup() {
        driver = new BaseTest().createBrowser("chrome"); //Initialization method 1
        // new BaseTest().createDriver("chrome"); //Initialization method 2
        // driver = DriverManager.getDriver(); //Get WebDriver from global in ThreadLocal
    }

    @Test
    public void testDownloadFileWithJS() {
        WebUI.getURL("https://www.onlinedatagenerator.com/");
        WebUI.clickElementWithJs(By.xpath("//button[normalize-space()='Export']"));
        WebUI.sleep(3);
        Assert.assertTrue(WebUI.verifyFileDownloadedWithJS("ExportCSV (5).csv"), "Download failed. File not match.");
    }

    @Test
    public void testDownloadFileWithJava() {
        Log.info(WebUI.countFilesInDownloadDirectory());
        WebUI.getURL("https://www.onlinedatagenerator.com/");
        WebUI.clickElementWithJs(By.xpath("//button[normalize-space()='Export']"));
        WebUI.sleep(3);
        Log.info(WebUI.countFilesInDownloadDirectory());
        //File name is ExportCSV.csv
        Assert.assertTrue(WebUI.verifyDownloadFileEqualsNameCompletedWaitTimeout("ExportCSV.csv", 5), "Download failed. File not found.");
    }

    @Test
    public void testConvertWebElementToBy() {
        WebUI.getURL(FrameworkConstants.URL_CRM);
        SignInPage signInPage = new SignInPage();

        //WebElement
        WebElement emailElement = WebUI.getWebElement(signInPage.inputEmail);

        //Convert WebElement to By
        By emailBy = ObjectUtils.getByFromWebElement(emailElement);

        WebUI.setText(emailBy, "admin@mailinator.com");

        WebUI.setText(signInPage.inputPassword, "123456");
        WebUI.clickElement(signInPage.buttonSignIn);
        WebUI.waitForElementVisible(new DashboardPage().menuDashboard);
    }

    @Test
    public void testLocalStorage() {
        WebUI.getURL(FrameworkConstants.URL_CRM);
        WebUI.sleep(1);

        //Set key=value in Sign in page
        LocalStorageUtils.setItem("email", "admin@mailinator.com");
        LocalStorageUtils.setItem("password", "123456");

        WebUI.setText(ObjectUtils.getByLocatorFromConfig("inputEmail"), LocalStorageUtils.getItem("email"));
        WebUI.setText(ObjectUtils.getByLocatorFromConfig("inputPassword"), LocalStorageUtils.getItem("password"));
        WebUI.clickElement(ObjectUtils.getByLocatorFromConfig("buttonSignIn"));
        WebUI.waitForPageLoaded();

        //Get value in Project page
        WebUI.clickElement(ObjectUtils.getByLocatorFromConfig("menuProjects"));
        WebUI.logConsole(LocalStorageUtils.getItem("email"));
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);
        //Get value in ClientModel page
        WebUI.clickElement(ObjectUtils.getByLocatorFromConfig("menuClients"));
        WebUI.logConsole(LocalStorageUtils.getItem("password"));

        //=> You can get value by key everywhere before closing the browser
    }

    @Test
    public void handleHTML5ValidationMessage() {
        WebUI.getURL("https://anhtester.com/login");
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);

        By button_Login = By.id("login");
        By input_Email = By.xpath("//input[@placeholder='Email']");
        WebUI.clickElement(button_Login);
        WebUI.sleep(2);

        WebUI.logConsole("Verify required field: " + WebUI.verifyHTML5RequiredField(input_Email));

        WebUI.sleep(1);

        WebUI.logConsole("Message from field: " + WebUI.getHTML5MessageField(input_Email));
        Assert.assertEquals("Please fill out this field.", WebUI.getHTML5MessageField(input_Email));

        WebUI.setText(input_Email, "abc@ ");
        WebUI.sleep(1);
        WebUI.logConsole("Verify valid value: " + WebUI.verifyHTML5ValidValueField(input_Email));
        WebUI.logConsole("Message from field: " + WebUI.getHTML5MessageField(input_Email));

        WebUI.sleep(1);
    }

    @Test
    public void handleSetWindow() {
        WebUI.getURL("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.setWindowSize(1000, 600);
        WebUI.sleep(2);
        WebUI.setWindowPosition(100, 100);
        WebUI.sleep(1);
    }

    @Test
    public void handleScreenshotElement() {
        WebUI.getURL("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.screenshotElement(By.xpath("//div[@class='col-lg-5']//div[@class='row']//div[1]//div[1]"), "Website_Testing_Module");
    }

    @Test
    public void testUploadFileSendKeys() {
        WebUI.getURL("https://www.file.io/");
        WebUI.waitForPageLoaded();

        By inputFileUpload = By.xpath("//div[@class='actions']/input");

        String filePath = Helpers.getCurrentDir() + "src\\test\\resources\\testdata\\TxtFileData.txt";

        WebUI.uploadFileWithSendKeys(inputFileUpload, filePath);

        WebUI.sleep(4);
    }

    @Test
    public void testUploadFileFormDialog() {
        WebUI.getURL("https://files.fm/");
        WebUI.waitForPageLoaded();

        By textOnPage = By.xpath("//div[@id='file_select_dragndrop_text']");
        By divFileUpload = By.xpath("//div[@id='uploadifive-file_upload']");
        By inputFileUpload = By.xpath("//div[@id='file_select_button']//input[@id='file_upload']");

        String filePath = Helpers.getCurrentDir() + "src\\test\\resources\\testdata\\TxtFileData.txt";

        WebUI.uploadFileWithLocalForm(divFileUpload, filePath);

        WebUI.sleep(4);
    }

    //Phân trang và check data in table
    @Test
    public void checkDataTableWithPagination() {
        WebUI.getURL("https://datatables.net/");
        WebUI.waitForPageLoaded();

        By title_H1 = By.xpath("//div[@class='fw-hero']//h1");

        WebUI.scrollToElement(title_H1);

        By button_Next = By.xpath("//a[@id='example_next']");
        By label_Info_PageTotal = By.xpath("//div[@id='example_info']");
        String info = driver.findElement(label_Info_PageTotal).getText(); //Showing 1 to 10 of 57 entries
        System.out.println("Chuỗi chứa số item: " + info);

        //Mình tách cái chuỗi trên với ký tự khoảng trắng rồi lấy phần tử thứ 5 tính từ 0
        //Để bắt tổng số Item
        ArrayList<String> arrayListString = new ArrayList<>();
        for (String s : info.split(" ")) {
            arrayListString.add(s);
        }

        //Showing 1 to 10 of 57 entries => Lấy ra số 57 á :))
        int itemTotal = Integer.parseInt(arrayListString.get(5));
        System.out.println("Tổng số item: " + itemTotal);

        int itemTotalOnePage = 10; //mặc định như mẫu. Tuỳ vào hệ thống mình thay đổi theo
        //Hoặc lấy bằng auto số default luôn cũng được. Có gì biến tấu thêm hen.
        System.out.println("Số item trên 1 trang: " + itemTotalOnePage);

        double pageTotal = (double) itemTotal / (double) itemTotalOnePage;

        DecimalFormat df = new DecimalFormat("#"); //Làm tròn số đến phần đơn vị của phần thập phân
        //Ví dụ 5.7 thì làm tròn 6 kiểu vậy
        int pageTotalInt = Integer.parseInt(df.format(pageTotal));
        System.out.println("Tổng số trang: " + pageTotalInt);

        //FOR này chạy tới < pageTotalInt để nó không click thêm lần cuối cùng
        //VD: 6 trang thì nó chỉ click 5 lần thôi chứ hả =))
        for (int i = 1; i <= pageTotalInt; i++) {
            WebUI.scrollToElement(title_H1);
            //Gọi hàm Check data in table by column từ keyword WebUI
            WebUI.checkContainsValueOnTableByColumn(1, "", "//div[@id='example_wrapper']//tbody/tr");
            WebUI.sleep(1);
            //Click Next
            if (i != pageTotalInt) {
                driver.findElement(button_Next).click();
            }
        }

        WebUI.scrollToElement(title_H1);
        WebUI.sleep(2);

    }


    @Test
    public void QRCode() {
        WebUI.getURL("http://qrcode.meetheed.com/qrcode_examples.php");
        WebUI.maximizeWindow();
        WebUI.waitForPageLoaded();
        WebUI.moveToElement(By.xpath("(//div[@class = 'topBox'])[1]/img"));
        WebUI.sleep(1);
        WebUI.logConsole(WebUI.getQRCodeFromImage(By.xpath("(//div[@class = 'topBox'])[1]/img")));
    }

    @Test
    public void handleZoomInZoomOut() {
        WebUI.getURL("https://anhtester.com");
        WebUI.sleep(1);
        //driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL,Keys.ADD));
        //driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL,Keys.SUBTRACT));
        WebUI.getJsExecutor().executeScript("document.body.style.zoom = '80%';");
        WebUI.sleep(1);
    }

    @Test
    public void handleNotificationsBrowser() {
        driver = new ChromeDriver(WebUI.notificationsBlock()); //
        driver.manage().window().maximize();
        WebUI.getURL("https://oto.com.vn/mua-ban-xe");
        WebUI.sleep(4);
    }

    @Test
    public void handleDragAndDropJS() {
        DriverManager.getDriver().get("https://bestvpn.org/html5demos/drag/");
        WebUI.moveToElement(By.cssSelector("#five"));

        By from1 = By.cssSelector("#one");
        By to1 = By.cssSelector("#bin");
        By from2 = By.cssSelector("#two");
        WebUI.sleep(1);
        WebUI.dragAndDropJS(WebUI.getWebElement(from1), WebUI.getWebElement(to1));
        WebUI.sleep(1);
        WebUI.dragAndDropJS(WebUI.getWebElement(from2), WebUI.getWebElement(to1));
        WebUI.sleep(2);
    }

    @Test
    public void handleDragAndDrop() {
        WebUI.getURL("http://demo.guru99.com/test/drag_drop.html");
        By fromElement = By.xpath("//a[normalize-space()='BANK']");
        By toElement = By.xpath("(//div[@id='shoppingCart1']//div)[1]");

        //WebUI.switchToFrameByElement(toElement);
        //WebUI.scrollToElement(toElement);
        WebUI.dragAndDrop(fromElement, toElement);
        WebUI.sleep(1);
    }

    @Test
    public void handleDragAndDropHTML5() {
        WebUI.getURL("https://david-desmaisons.github.io/draggable-example/");
        WebUI.waitForPageLoaded();

        By fromElement = By.xpath("(//li[@class='list-group-item'])[1]");
        By toElement = By.xpath("(//li[@class='list-group-item'])[2]");

        WebUI.dragAndDropHTML5(fromElement, toElement);

        WebUI.sleep(2);
    }

    @Test
    public void handleDragAndDropOffset() {
        WebUI.getURL("https://david-desmaisons.github.io/draggable-example/");
        WebUI.waitForPageLoaded();

        By fromElement = By.xpath("(//li[@class='list-group-item'])[1]");

        WebUI.dragAndDropToOffset(fromElement, 330, 600);

        WebUI.sleep(2);
    }

    @Test
    public void handleHighLightElement() {
        WebUI.getURL("https://app.hrsale.com/");
        By button = By.xpath("//button[@type='submit']");
        WebUI.highLightElement(button); //Tô màu viền đỏ cho Element trên website
        WebUI.verifyElementAttributeValue(button, "type", "submit");
        WebUI.waitForElementClickable(button, 5);
        WebUI.sleep(2);
    }

    @Test
    public void handleUploadFile() {
        WebUI.getURL("https://demoqa.com/upload-download");
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);

        final String path1 = Helpers.getCurrentDir() + "src\\test\\resources\\testdata\\DOCX_File_01.docx";
        final String path2 = Helpers.getCurrentDir() + "src\\test\\resources\\testdata\\LoginCSV.csv";

        //Cách 1 sendKeys link từ source
        WebUI.uploadFileWithSendKeys(By.xpath("//input[@id='uploadFile']"), path1);
        WebUI.verifyElementTextContains(By.xpath("//p[@id='uploadedFilePath']"), "DOCX_File_01ABC.docx");
        WebUI.sleep(1);
        WebUI.reloadPage();
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);

        //Cách 2 mở form local máy nên file là trong ổ đĩa máy tính
        WebUI.uploadFileWithLocalForm(By.xpath("//input[@id='uploadFile']"), path2);
        WebUI.verifyElementTextContains(By.xpath("//p[@id='uploadedFilePath']"), "LoginCSV.csv");
        WebUI.sleep(3);
    }

    @Test
    public void handleTable() {
        WebUI.getURL("https://colorlib.com/polygon/notika/data-table.html");
        System.out.println(WebUI.getValueTableByColumn(2));
    }

    @Test
    public void handlePrintPopup() throws AWTException {
        WebUI.getURL("https://saleserpnew.bdtask.com/saleserp_v9.8_demo/login");
        WebUI.waitForPageLoaded();
        String originalWindow = driver.getWindowHandle();

        WebUI.setText(By.id("email"), "admin@gmail.com");
        WebUI.setText(By.id("password"), "123456");
        WebUI.clickElement(By.xpath("//button[normalize-space()='Login']"));
        WebUI.waitForPageLoaded();
        WebUI.clickElement(By.xpath("//span[normalize-space()='Sale']"));
        WebUI.clickElement(By.xpath("//a[normalize-space()='Manage Sale']"));
        WebUI.clickElement(By.xpath("//span[normalize-space()='Print']"));

        WebUI.sleep(1);

        Set<String> windowHandles = driver.getWindowHandles();
        WebUI.logConsole("Số cửa sổ hoặc tab: " + windowHandles.size());
//        if (!windowHandles.isEmpty() && windowHandles.size() > 1) {
//            //Chuyển sang tab thứ 2 (vị trí 1 tính từ vị trí 0)
//            WebUI.switchToWindowOrTab(1);
//        }

        //Or using for
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                WebUI.switchToWindowOrTab(1);
                break;
            }
        }

        WebUI.sleep(1);
        Robot robotClass = new Robot();
        //Nhấn Tab để chuyển sang nút Cancel
        robotClass.keyPress(KeyEvent.VK_TAB);
        robotClass.keyRelease(KeyEvent.VK_TAB);
        WebUI.sleep(2);
        //Nhấn ENTER để xác nhận Cancel
        robotClass.keyPress(KeyEvent.VK_ENTER);
        robotClass.keyRelease(KeyEvent.VK_ENTER);
        WebUI.sleep(1);
        //Chuyển về tab đầu (vị trí 0)
        WebUI.switchToMainWindow();
        //WebUI.switchToMainWindow(originalWindow);
        WebUI.sleep(1);
    }

    @Test
    public void handleAuthentication() {
        // Authentication username & password
        String username = "admin";
        String password = "admin";

        WebUI.getToUrlAuthentication("https://the-internet.herokuapp.com/basic_auth", username, password);

        String successFullyLoggedInText = DriverManager.getDriver().findElement(By.xpath("//p")).getText();
        Assert.assertEquals(successFullyLoggedInText, "Congratulations! You must have the proper credentials.");
    }

    @AfterMethod
    public void closeDriver() {
        DriverManager.quit();
        if (driver != null) {
            driver.quit();
        }
    }

}
