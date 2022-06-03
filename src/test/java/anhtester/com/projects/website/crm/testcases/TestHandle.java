/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.helpers.Helpers;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.utils.LocalStorageUtils;
import anhtester.com.utils.ObjectUtils;
import anhtester.com.utils.WebUI;
import anhtester.com.utils.Log;
import com.google.zxing.NotFoundException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

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
    public void testLocalStorage() {
        WebUI.getToUrl(FrameworkConstants.BASE_URL);
        WebUI.sleep(1);

        //Set key=value in Sign in page
        LocalStorageUtils.setItem("email", "admin02@mailinator.com");
        LocalStorageUtils.setItem("password", "123456");

        WebUI.setText(ObjectUtils.getObject("inputEmail"), LocalStorageUtils.getItem("email"));
        WebUI.setText(ObjectUtils.getObject("inputPassword"), LocalStorageUtils.getItem("password"));
        WebUI.clickElement(ObjectUtils.getObject("buttonSignIn"));
        WebUI.waitForPageLoaded();

        //Get value in Project page
        WebUI.clickElement(ObjectUtils.getObject("menuProjects"));
        WebUI.logConsole(LocalStorageUtils.getItem("email"));
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);
        //Get value in ClientModel page
        WebUI.clickElement(ObjectUtils.getObject("menuClients"));
        WebUI.logConsole(LocalStorageUtils.getItem("password"));

        //=> You can get value by key everywhere before closing the browser
    }

    @Test
    public void handleHTML5ValidationMessage() {
        WebUI.getToUrl("https://anhtester.com/login");
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
        WebUI.getToUrl("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.setWindowSize(1000, 600);
        WebUI.sleep(2);
        WebUI.setWindowPosition(100, 100);
        WebUI.sleep(1);
    }

    @Test
    public void handleScreenshotElement() {
        WebUI.getToUrl("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.screenshotElement(By.xpath("//div[@class='col-lg-5']//div[@class='row']//div[1]//div[1]"), "Website_Testing_Module");
    }

    @Test
    public void testUploadFileSendKeys() {
        WebUI.getToUrl("https://www.file.io/");
        WebUI.waitForPageLoaded();

        By inputFileUpload = By.xpath("//div[@class='actions']/input");

        String filePath = Helpers.getCurrentDir() + "src\\test\\resources\\testdatafile\\TxtFileData.txt";

        WebUI.uploadFileSendkeys(inputFileUpload, filePath);

        WebUI.sleep(4);
    }

    @Test
    public void testUploadFileFormDialog() {
        WebUI.getToUrl("https://files.fm/");
        WebUI.waitForPageLoaded();

        By textOnPage = By.xpath("//div[@id='file_select_dragndrop_text']");
        By divFileUpload = By.xpath("//div[@id='uploadifive-file_upload']");
        By inputFileUpload = By.xpath("//div[@id='file_select_button']//input[@id='file_upload']");

        String filePath = Helpers.getCurrentDir() + "src\\test\\resources\\testdatafile\\TxtFileData.txt";

        WebUI.uploadFileForm(divFileUpload, filePath);

        WebUI.sleep(4);
    }

    //Phân trang và check data in table
    @Test
    public void checkDataTableWithPagination() {
        WebUI.getToUrl("https://datatables.net/");
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

        int itemTotal = Integer.parseInt(arrayListString.get(5));
        System.out.println("Tổng số item: " + itemTotal);

        int itemTotalOnePage = 10; //mặc định như mẫu. Tuỳ vào hệ thống mình thay đổi theo
        System.out.println("Số item trên 1 trang: " + itemTotalOnePage);

        double pageTotal = (double) itemTotal / (double) itemTotalOnePage;

        DecimalFormat df = new DecimalFormat("#"); //Làm tròn số đến phần đơn vị của phần thập phân
        //Ví dụ 5.7 thì làm tròn 6 kiểu vậy
        int pageTotalInt = Integer.parseInt(df.format(pageTotal));
        System.out.println("Tổng số trang: " + df.format(pageTotalInt));

        //FOR này chạy tới < pageTotalInt để nó không click thêm lần cuối cùng
        //VD: 6 trang thì nó chỉ click 5 lần thôi chứ hả =))
        for (int i = 1; i < pageTotalInt; i++) {
            WebUI.scrollToElement(title_H1);
            //Gọi hàm Check data in table by column từ keyword WebUI
            WebUI.checkContainsSearchTableByColumn(1, "", "//div[@id='example_wrapper']//tbody/tr");
            WebUI.sleep(1);
            //Click Next
            driver.findElement(button_Next).click();
        }

        WebUI.scrollToElement(title_H1);
        WebUI.sleep(2);

    }


    @Test
    public void QRCode() throws NotFoundException, IOException {
        WebUI.getToUrl("http://qrcode.meetheed.com/qrcode_examples.php");
        WebUI.maximizeWindow();
        WebUI.waitForPageLoaded();
        WebUI.moveToElement(By.xpath("(//div[@class = 'topBox'])[1]/img"));
        WebUI.sleep(1);
        WebUI.logConsole(WebUI.getQRCodeFromImage(By.xpath("(//div[@class = 'topBox'])[1]/img")));
    }

    @Test
    public void handleZoomInZoomOut() {
        WebUI.getToUrl("https://anhtester.com");
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
        WebUI.getToUrl("https://oto.com.vn/mua-ban-xe");
        WebUI.sleep(4);
    }

    @Test
    public void handleDragAndDropJQuery() throws InterruptedException, IOException {
        try {
            String basePath = new File("").getAbsolutePath();

            DriverManager.getDriver().get("https://david-desmaisons.github.io/draggable-example/");
            Thread.sleep(1000);

            final String JQUERY_LOAD_SCRIPT = (basePath + "/src/main/resources/jquery_load_helper.js");
            final String DRAG_AND_DROP_SCRIPT = (basePath + "/src/main/resources/drag_and_drop_helper.js");
            String jQueryLoader = Helpers.readFile(JQUERY_LOAD_SCRIPT);
            String dragAndDropScriptLoader = Helpers.readFile(DRAG_AND_DROP_SCRIPT);

            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeAsyncScript(jQueryLoader);

            String source = "li:nth-child(1)";
            String target = "li:nth-child(2)";

            Thread.sleep(1000);

            String javaScript = dragAndDropScriptLoader + "window.jQuery('" + source + "').simulateDragDrop({ dropTarget: '" + target + "'});";

            ((JavascriptExecutor) DriverManager.getDriver()).executeScript(javaScript);

            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void handleDragAndDropJS() {
        DriverManager.getDriver().get("https://bestvpn.org/html5demos/drag/");
        WebUI.moveToElement(By.cssSelector("#five"));

        By from1 = By.cssSelector("#one");
        By to1 = By.cssSelector("#bin");
        By from2 = By.cssSelector("#two");
        WebUI.sleep(1);
        WebUI.dragAndDropJS(WebUI.findWebElement(from1), WebUI.findWebElement(to1));
        WebUI.sleep(1);
        WebUI.dragAndDropJS(WebUI.findWebElement(from2), WebUI.findWebElement(to1));
        WebUI.sleep(2);
    }

    @Test
    public void handleDragAndDrop() {
        WebUI.getToUrl("http://demo.guru99.com/test/drag_drop.html");
        By fromElement1 = By.xpath("//a[normalize-space()='BANK']");
        By toElement1 = By.xpath("(//div[@id='shoppingCart1']//div)[1]");

        By fromElement2 = By.xpath("(//li[@id='fourth'])[2]");
        By toElement2 = By.xpath("(//div[@id='shoppingCart4']//div)[1]");

        //WebUI.switchToFrameByElement(toElement);
        //WebUI.scrollToElement(toElement);
        WebUI.dragAndDrop(fromElement1, toElement1);
        WebUI.sleep(1);
        WebUI.dragAndDropElement(fromElement2, toElement2);
        WebUI.sleep(2);
    }

    @Test
    public void handleDragAndDropOffset() throws AWTException, InterruptedException {
        WebUI.getToUrl("https://david-desmaisons.github.io/draggable-example/");
        Thread.sleep(1000);

        By fromElement1 = By.xpath("(//li[@class='list-group-item'])[1]");
        By toElement1 = By.xpath("(//li[@class='list-group-item'])[2]");

        int X1 = driver.findElement(fromElement1).getLocation().getX();
        int Y1 = driver.findElement(fromElement1).getLocation().getY();
        System.out.println(X1 + " , " + Y1);

        int X2 = driver.findElement(toElement1).getLocation().getX();
        int Y2 = driver.findElement(toElement1).getLocation().getY();
        System.out.println(X2 + " , " + Y2);

        //Chổ này lấy theo toạ độ cụ thể. Chả biết sao nó lấy toạ độ Element chênh lệch vậy nữa =))
        Thread.sleep(1000);
        Robot robot = new Robot();
        robot.mouseMove(250, 570);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1000);
        robot.mouseMove(250, 610);

        Thread.sleep(1000);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(3000);
    }

    @Test
    public void handleHighLightElement() {
        WebUI.getToUrl("https://hrm.anhtester.com/");
        By button = By.xpath("//button[@type='submit']");
        WebUI.highLightElement(button); //Tô màu viền đỏ cho Element trên website
        WebUI.verifyElementAttributeValue(button, "type", "submit");
        WebUI.waitForElementClickable(button, 5);
        WebUI.sleep(2);
    }

    @Test
    public void handleUploadFile() {
        WebUI.getToUrl("https://demoqa.com/upload-download");
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);

        //Cách 1 sendKeys link từ source
        WebUI.uploadFileSendkeys(By.xpath("//input[@id='uploadFile']"), Helpers.getCurrentDir() + "src/test/resources/DOCX_File_01.docx");

        //Cách 2 mở form local máy nên file là trong ổ đĩa máy tính
        WebUI.uploadFileForm(By.xpath("//input[@id='uploadFile']"), "D:\\Document.csv");

        WebUI.sleep(3);
    }

    @Test
    public void handleTable1() {
        Log.info("handleTable1");
        WebUI.getToUrl("https://colorlib.com/polygon/notika/data-table.html");
        System.out.println(WebUI.getValueTableByColumn(2));
    }

    @Test
    public void handleTable2() {
        signInPage = new SignInPage();
        dashboardPage = signInPage.signIn("tld01@mailinator.com", "123456");
        projectPage = dashboardPage.openProjectPage();
        String dataSearchTitle = "Smart Home";
        String dataSearchClient = "AN check ClientModel 001";
        // Search cột 2 Title
        projectPage.searchByValue(dataSearchTitle);
        WebUI.checkContainsSearchTableByColumn(2, dataSearchTitle);
        // Search cột 3 ClientModel
        projectPage.searchByValue(dataSearchClient);
        WebUI.checkContainsSearchTableByColumn(3, dataSearchClient);
    }

    @Test
    public void handlePrintPopup() throws AWTException {
        WebUI.getToUrl("https://pos.anhtester.com/login");
        WebUI.waitForPageLoaded();
        String originalWindow = driver.getWindowHandle();

        WebUI.clickElement(By.xpath("//td[normalize-space()='user01@anhtester.com']"));
        WebUI.clickElement(By.xpath("//button[normalize-space()='Login']"));
        WebUI.waitForPageLoaded();
        WebUI.clickElement(By.xpath("//a[@role='button']"));
        WebUI.waitForPageLoaded();
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
        if (driver == null) {
            driver.quit();
        }
    }

}
