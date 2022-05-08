/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.driver.DriverManager;
import anhtester.com.helpers.Helpers;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.helpers.DatabaseHelpers;
import anhtester.com.utils.WebUI;
import anhtester.com.utils.Log;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.zxing.*;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v99.network.Network;
import org.openqa.selenium.devtools.v99.network.model.Headers;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.print.PrintOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class TestHandle {

    WebDriver driver;
    DatabaseHelpers databaseHelpers;
    SignInPage signInPage;
    DashboardPage dashboardPage;
    ProjectPage projectPage;

    @BeforeMethod
    public void Setup() {
        driver = new BaseTest().createBrowser("chrome"); //Cách khởi tạo thứ 1
        // new BaseTest().createDriver("chrome"); //Cách khởi tạo thứ 2
        // driver = DriverManager.getDriver(); //Get WebDriver global in ThreadLocal
    }

    @Test
    public void handleSetWindow() {
        driver.get("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.setWindowSize(1000, 600);
        WebUI.sleep(2);
        WebUI.setWindowPosition(100, 100);
        WebUI.sleep(1);
    }

    @Test
    public void handleScreenshotElement() {
        driver.get("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.screenshotElement(By.xpath("//div[@class='col-lg-5']//div[@class='row']//div[1]//div[1]"), "Website_Testing_Module");
    }

    @Test
    public void testUploadFileSendKeys() {
        driver.get("https://www.file.io/");
        WebUI.waitForPageLoaded();

        By inputFileUpload = By.xpath("//div[@class='actions']/input");

        String filePath = Helpers.getCurrentDir() + "src\\test\\resources\\testdatafile\\TxtFileData.txt";

        WebUI.uploadFileSendkeys(inputFileUpload, filePath);

        WebUI.sleep(4);
    }

    @Test
    public void testUploadFileFormDialog() {
        driver.get("https://files.fm/");
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
        driver.get("https://datatables.net/");
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
        driver.get("http://qrcode.meetheed.com/qrcode_examples.php");
        driver.manage().window().maximize();
        String qrCodeURL = driver.findElement(By.xpath("//img[@src='images/qr_code_con.png']")).getAttribute("src");
        //Create an object of URL Class
        URL url = new URL(qrCodeURL);
        //Pass the URL class object to store the file as image
        BufferedImage bufferedimage = ImageIO.read(url);
        // Process the image
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedimage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        //To Capture details of QR code
        Result result = new MultiFormatReader().decode(binaryBitmap);
        System.out.println(result.getText());
    }

    @Test
    public void handleZoomInZoomOut() {
        driver.get("https://anhtester.com");
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
        driver.get("https://oto.com.vn/mua-ban-xe");
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
        driver.get("http://demo.guru99.com/test/drag_drop.html");
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
        driver.get("https://david-desmaisons.github.io/draggable-example/");
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
        driver.get("https://hrm.anhtester.com/");
        By button = By.xpath("//button[@type='submit']");
        WebUI.highLightElement(button); //Tô màu viền đỏ cho Element trên website
        WebUI.verifyElementAttributeValue(button, "type", "submit");
        WebUI.waitForElementClickable(button, 5);
        WebUI.sleep(2);
    }

    @Test
    public void handleUploadFile() {
        driver.get("https://demoqa.com/upload-download");
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
        driver.get("https://colorlib.com/polygon/notika/data-table.html");
        WebUI.waitForPageLoaded();
        System.out.println(WebUI.getValueTableByColumn(2));
    }

    @Test
    public void handleTable2() {
        signInPage = new SignInPage();
        driver.get("https://crm.anhtester.com/signin");
        dashboardPage = signInPage.signIn("tld01@mailinator.com", "123456");
        projectPage = dashboardPage.openProjectPage();
        String dataSearch1 = "Project";
        String dataSearch2 = "Test";
        // Search cột 2 Title
        projectPage.searchByValue(dataSearch1);
        projectPage.checkContainsSearchTableByColumn(2, dataSearch1);
        // Search cột 3 Client
        projectPage.searchByValue(dataSearch2);
        projectPage.checkContainsSearchTableByColumn(3, dataSearch2);
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
//            driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
//        }

        //Or using for
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                //driver.switchTo().window(windowHandle);
                driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
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

        // Get the devtools from the running driver and create a session
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Enable the Network domain of devtools
        devTools.send(Network.enable(java.util.Optional.of(100000), java.util.Optional.of(100000), java.util.Optional.of(100000)));
        String auth = username + ":" + password;

        // Encoding the username and password using Base64 (java.util)
        String encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());

        // Pass the network header -> Authorization : Basic <encoded String>
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodeToString);
        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));

        // Load the application url
        driver.get("https://the-internet.herokuapp.com/basic_auth");
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
        String successFullyLoggedInText = driver.findElement(By.xpath("//p")).getText();
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
