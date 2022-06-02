/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.enums.FailureHandling;
import anhtester.com.helpers.CaptureHelpers;
import anhtester.com.helpers.Helpers;
import anhtester.com.report.AllureManager;
import anhtester.com.report.ExtentReportManager;
import anhtester.com.report.ExtentTestManager;
import com.aventstack.extentreports.Status;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v102.network.Network;
import org.openqa.selenium.devtools.v102.network.model.Headers;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static anhtester.com.constants.FrameworkConstants.*;
import static anhtester.com.constants.FrameworkConstants.BOLD_END;

/**
 * Keyword WebUI là class chung làm thư viện xử lý sẵn với nhiều hàm custom từ Selenium và Java.
 * Trả về là một Class chứa các hàm Static. Gọi lại dùng bằng cách lấy tên class chấm tên hàm (WebUI.method)
 */
public class WebUI {

    private static SoftAssert softAssert = new SoftAssert();

    public static void stopSoftAssertAll() {
        softAssert.assertAll();
    }

    /**
     * Login as Authentication on URL
     *
     * @param url
     * @param username
     * @param password
     */
    public static void getToUrlAuthentication(String url, String username, String password) {
        // Get the devtools from the running driver and create a session
        DevTools devTools = ((HasDevTools) DriverManager.getDriver()).getDevTools();
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
        getToUrl(url);
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
    }

    /**
     * Get code text of QR Code image
     *
     * @param by là element dạng đối tượng By
     * @return text of QR Code
     */
    public static String getQRCodeFromImage(By by) {
        String qrCodeURL = WebUI.getAttributeElement(by, "src");
        //Create an object of URL Class
        URL url = null;
        try {
            url = new URL(qrCodeURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //Pass the URL class object to store the file as image
        BufferedImage bufferedimage = null;
        try {
            bufferedimage = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Process the image
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedimage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        //To Capture details of QR code
        Result result = null;
        try {
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (com.google.zxing.NotFoundException e) {
            throw new RuntimeException(e);
        }
        return result.getText();
    }

    //Handle HTML5 validation message and valid value

    /**
     * Kiểm tra field có bắt buộc nhập hay không
     *
     * @param by là element thuộc kiểu By
     * @return true/false tương ứng với required
     */
    public static Boolean verifyHTML5RequiredField(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        Boolean verifyRequired = (Boolean) js.executeScript("return arguments[0].required;", findWebElement(by));
        return verifyRequired;
    }

    /**
     * Kiểm tra giá trị trong field nhập có hợp lệ hay chưa
     *
     * @param by là element thuộc kiểu By
     * @return true/false tương ứng với Valid
     */
    public static Boolean verifyHTML5ValidValueField(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        Boolean verifyValid = (Boolean) js.executeScript("return arguments[0].validity.valid;", findWebElement(by));
        return verifyValid;
    }

    /**
     * Lấy ra câu thông báo từ HTML5 của field
     *
     * @param by là element thuộc kiểu By
     * @return giá trị chuỗi Text của câu thông báo (String)
     */
    public static String getHTML5MessageField(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        String message = (String) js.executeScript("return arguments[0].validationMessage;", findWebElement(by));
        return message;
    }


    /**
     * Khôi phục cửa sổ và đặt kích thước cửa sổ.
     *
     * @param widthPixel  is Width with Pixel
     * @param heightPixel is Height with Pixel
     */
    public static void setWindowSize(int widthPixel, int heightPixel) {
        DriverManager.getDriver().manage().window().setSize(new Dimension(widthPixel, heightPixel));
    }

    /**
     * Di chuyển cửa sổ đến vị trí đã chọn X, Y tính từ 0 gốc trái trên cùng
     *
     * @param X (int) - ngang
     * @param Y (int) - dọc
     */
    public static void setWindowPosition(int X, int Y) {
        DriverManager.getDriver().manage().window().setPosition(new Point(X, Y));
    }

    public static void maximizeWindow() {
        DriverManager.getDriver().manage().window().maximize();
    }

    public static void minimizeWindow() {
        DriverManager.getDriver().manage().window().minimize();
    }

    /**
     * Chụp ảnh màn hình tại vị trí element. Không chụp hết cả màn hình.
     *
     * @param by          là element thuộc kiểu By
     * @param elementName để đặt tên file ảnh .png
     */
    public static void screenshotElement(By by, String elementName) {
        File scrFile = findWebElement(by).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("./" + elementName + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In trang hiện tại trong trình duyệt.
     * Note: Chỉ hoạt động ở chế độ headless
     *
     * @param endPage là tổng số trang cần in ra. Tính từ 1.
     * @return is content of page form PDF file
     */
    public static String printPage(int endPage) {
        PrintOptions printOptions = new PrintOptions();
        //From page 1 to end page
        printOptions.setPageRanges("1-" + endPage);

        Pdf pdf = ((PrintsPage) DriverManager.getDriver()).print(printOptions);
        return pdf.getContent();
    }

    public static JavascriptExecutor getJsExecutor() {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        return js;
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm một element
     *
     * @param by là element thuộc kiểu By
     * @return Trả về là một đối tượng WebElement
     */
    public static WebElement findWebElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm nhiều element
     *
     * @param by là element thuộc kiểu By
     * @return Trả về là Danh sách đối tượng WebElement
     */
    public static List<WebElement> findWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }

    /**
     * In ra câu message trong Console log
     *
     * @param object truyền vào object bất kỳ
     */
    public static void logConsole(@Nullable Object object) {
        System.out.println(object);
    }

    /**
     * Chờ đợi ép buộc với đơn vị là Giây
     *
     * @param second là số nguyên dương tương ứng số Giây
     */
    public static void sleep(double second) {
        try {
            Thread.sleep((long) (second * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allow popup notifications của browser trên website
     *
     * @return giá trị đã setup Allow - thuộc đối tượng ChromeOptions
     */
    public static ChromeOptions notificationsAllow() {
        // Tạo Map để lưu trữ các tùy chọn
        Map<String, Object> prefs = new HashMap<String, Object>();

        // Thêm khóa và giá trị vào Map như sau để tắt thông báo của trình duyệt
        // Truyền đối số 1 để CHO PHÉP và 2 để CHẶN
        prefs.put("profile.default_content_setting_values.notifications", 1);

        // Tạo một phiên ChromeOptions
        ChromeOptions options = new ChromeOptions();

        // Dùng hàm setExperimentalOption thực thi giá trị thông qua đối tượng prefs trên
        options.setExperimentalOption("prefs", prefs);

        //Trả về giá trị đã setup thuộc đối tượng ChromeOptions
        return options;
    }

    /**
     * Block popup notifications của browser trên website
     *
     * @return giá trị đã setup Block - thuộc đối tượng ChromeOptions
     */
    public static ChromeOptions notificationsBlock() {
        // Tạo Map để lưu trữ các tùy chọn
        Map<String, Object> prefs = new HashMap<String, Object>();

        // Thêm khóa và giá trị vào Map như sau để tắt thông báo của trình duyệt
        // Truyền đối số 1 để CHO PHÉP và 2 để CHẶN
        prefs.put("profile.default_content_setting_values.notifications", 2);

        // Tạo một phiên ChromeOptions
        ChromeOptions options = new ChromeOptions();

        // Dùng hàm setExperimentalOption thực thi giá trị thông qua đối tượng prefs trên
        options.setExperimentalOption("prefs", prefs);

        //Trả về giá trị đã setup thuộc đối tượng ChromeOptions
        return options;
    }

    /**
     * Upload file kiểu click hiện form chọn file local trong máy tính của bạn
     *
     * @param by       là element thuộc kiểu By
     * @param filePath đường dẫn tuyệt đối đến file trên máy tính của bạn
     */
    public static void uploadFileForm(By by, String filePath) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Actions action = new Actions(DriverManager.getDriver());
        //Click để mở form upload
        action.moveToElement(findWebElement(by)).click().perform();
        sleep(3);

        // Khởi tạo Robot class
        Robot rb = null;
        try {
            rb = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Copy File path vào Clipboard
        StringSelection str = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);

        // Nhấn Control+V để dán
        rb.keyPress(KeyEvent.VK_CONTROL);
        rb.keyPress(KeyEvent.VK_V);

        // Xác nhận Control V trên
        rb.keyRelease(KeyEvent.VK_CONTROL);
        rb.keyRelease(KeyEvent.VK_V);
        sleep(2);
        // Nhấn Enter
        rb.keyPress(KeyEvent.VK_ENTER);
        rb.keyRelease(KeyEvent.VK_ENTER);
    }

    /**
     * Upload file kiểu kéo link trực tiếp vào ô input
     *
     * @param by       truyền vào element dạng đối tượng By
     * @param filePath đường dẫn tuyệt đối đến file
     */
    public static void uploadFileSendkeys(By by, String filePath) {
        //Dán link file vào ô upload
        waitForElementPresent(by).sendKeys(filePath);
    }

    public static String getCurrentUrl() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Log.info("Current Page Url: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl();
    }

    public static String getPageTitle() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        String title = DriverManager.getDriver().getTitle();
        Log.info("Current Page Title: " + DriverManager.getDriver().getTitle());
        return title;
    }

    public static boolean verifyPageTitle(String pageTitle) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        return getPageTitle().equals(pageTitle);
    }

    public static boolean verifyPageContainsText(String text) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        return DriverManager.getDriver().getPageSource().contains(text);
    }

    public static boolean verifyPageUrl(String pageUrl) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Log.info("Current URL: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl().contains(pageUrl.trim());
    }

    //Handle dropdown dynamic

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     *
     * @param objectListItem là locator của list item dạng đối tượng By
     * @param text           giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    public static boolean selectOptionDynamic(By objectListItem, String text) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = findWebElements(objectListItem);

            for (WebElement element : elements) {
                Log.info(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    element.click();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.info(e.getMessage());
            e.getMessage();
        }
        return false;
    }

    public static boolean verifyOptionDynamicExist(By objectListItem, String text) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        try {
            List<WebElement> elements = findWebElements(objectListItem);

            for (WebElement element : elements) {
                Log.info(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.info(e.getMessage());
            e.getMessage();
        }
        return false;
    }

    public static int getOptionDynamicTotal(By objectListItem) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        try {
            List<WebElement> elements = findWebElements(objectListItem);
            return elements.size();
        } catch (Exception e) {
            Log.info(e.getMessage());
            e.getMessage();
        }
        return 0;
    }

    //Dropdown tĩnh (Select Option)
    public static void selectOptionByText(By by, String text) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Select select = new Select(findWebElement(by));
        select.selectByVisibleText(text);
    }

    public static void selectOptionByValue(By by, String value) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Select select = new Select(findWebElement(by));
        select.selectByValue(value);
    }

    public static void selectOptionByIndex(By by, int index) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Select select = new Select(findWebElement(by));
        select.selectByIndex(index);
    }

    public static void verifyOptionTotal(By element, int total) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Select select = new Select(findWebElement(element));
        Assert.assertEquals(total, select.getOptions().size());
    }

    public static boolean verifySelectedByText(By by, String text) {
        sleep(WAIT_SLEEP_STEP);
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected by text: " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText().equals(text);
    }

    public static boolean verifySelectedByValue(By by, String optionValue) {
        sleep(WAIT_SLEEP_STEP);
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected by value: " + select.getFirstSelectedOption().getAttribute("value"));
        return select.getFirstSelectedOption().getAttribute("value").equals(optionValue);
    }

    public static boolean verifySelectedByIndex(By by, int index) {
        sleep(WAIT_SLEEP_STEP);

        boolean res = false;
        Select select = new Select(findWebElement(by));
        int indexFirstOption = select.getOptions().indexOf(select.getFirstSelectedOption());
        Log.info("The First Option selected by index: " + indexFirstOption);
        Log.info("Expected index: " + index);
        if (indexFirstOption == index) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    //Handle frame iframe

    public static void switchToFrameByIndex(int index) {
        sleep(WAIT_SLEEP_STEP);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
        //DriverManager.getDriver().switchTo().frame(Index);
    }

    public static void switchToFrameByIdOrName(String IdOrName) {
        sleep(WAIT_SLEEP_STEP);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
        //DriverManager.getDriver().switchTo().frame(IdOrName);
    }

    public static void switchToFrameByElement(By by) {
        sleep(WAIT_SLEEP_STEP);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        //DriverManager.getDriver().switchTo().frame(findWebElement(by));
    }

    public static void switchToDefaultContent() {
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().defaultContent();
    }


    public static void switchToWindowOrTab(int position) {
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[position].toString());
    }

    public static boolean verifyNumberOfWindowsOrTab(int number) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT)).until(ExpectedConditions.numberOfWindowsToBe(number));
    }

    public static void openNewTab() {
        sleep(WAIT_SLEEP_STEP);
        // Opens a new tab and switches to new tab
        DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
    }

    public static void openNewWindow() {
        sleep(WAIT_SLEEP_STEP);
        // Opens a new window and switches to new window
        DriverManager.getDriver().switchTo().newWindow(WindowType.WINDOW);
    }

    public static void switchToMainWindow() {
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[0].toString());
    }

    public static void switchToMainWindow(String originalWindow) {
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().window(originalWindow);
    }

    public static void switchToLastWindow() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[windowHandles.size() - 1].toString());
    }

    //Handle Alert
    public static void alertAccept() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().accept();
    }

    public static void alertDismiss() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().dismiss();
    }

    public static void alertGetText() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().getText();
    }

    public static void alertSetText(String text) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().sendKeys(text);
    }

    public static boolean verifyAlertPresent(int timeOut) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            Assert.fail("Not found Alert.");
            return false;
        }
    }

    //Handle Elements

    public static List<String> getListElementsText(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        List<WebElement> listElement = findWebElements(by);
        List<String> listText = new ArrayList<>();

        for (WebElement e : listElement) {
            listText.add(e.getText());
        }

        return listText;
    }

    public static boolean verifyElementExists(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        boolean res = false;

        List<WebElement> elementList = findWebElements(by);
        if (elementList.size() > 0) {
            res = true;
            Log.info("Element existing");
        } else {
            res = false;
            Log.warn("Element not exists");
        }
        return res;
    }

    public static boolean verifyElementText(By by, String text) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        return getTextElement(by).trim().equals(text.trim());
    }

    public static boolean verifyElementTextEquals(By by, String text, FailureHandling flowControl) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
            Assert.assertEquals(getTextElement(by).trim(), text.trim(), "The actual text is " + getTextElement(by).trim() + " not equals expected text " + text.trim());
        }
        if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
            softAssert.assertEquals(getTextElement(by).trim(), text.trim(), "The actual text is " + getTextElement(by).trim() + " not equals expected text " + text.trim());
        }
        return getTextElement(by).trim().equals(text.trim());
    }

    public static boolean verifyElementTextContains(By by, String text, FailureHandling flowControl) {
        if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
            Assert.assertTrue(getTextElement(by).trim().contains(text.trim()), "The actual text is " + getTextElement(by).trim() + " not contains expected text " + text.trim());
        }
        if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
            softAssert.assertTrue(getTextElement(by).trim().contains(text.trim()), "The actual text is " + getTextElement(by).trim() + " not contains expected text " + text.trim());
        }
        return getTextElement(by).trim().contains(text.trim());
    }

    public static boolean verifyElementToBeClickable(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            waitForElementClickable(by);
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean verifyElementPresent(By by) {
        try {
            waitForElementPresent(by);
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean verifyElementVisibility(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }


    public static void scrollToElement(By element) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(element));
    }

    public static void scrollToPosition(int X, int Y) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
    }

    public static boolean hoverElement(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean mouseHover(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void dragAndDropJS(WebElement from, WebElement to) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("function createEvent(typeOfEvent) {\n" + "var event =document.createEvent(\"CustomEvent\");\n"
                + "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n" + "data: {},\n"
                + "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
                + "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n" + "return event;\n"
                + "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
                + "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
                + "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
                + "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n"
                + "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
                + "var dragStartEvent =createEvent('dragstart');\n" + "dispatchEvent(element, dragStartEvent);\n"
                + "var dropEvent = createEvent('drop');\n"
                + "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
                + "var dragEndEvent = createEvent('dragend');\n"
                + "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
                + "var source = arguments[0];\n" + "var destination = arguments[1];\n"
                + "simulateHTML5DragAndDrop(source,destination);", from, to);
    }

    public static boolean dragAndDrop(By fromElement, By toElement) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.dragAndDrop(findWebElement(fromElement), findWebElement(toElement)).perform();
            //action.clickAndHold(findWebElement(fromElement)).moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean dragAndDropElement(By fromElement, By toElement) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.clickAndHold(findWebElement(fromElement)).moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean dragAndDropOffset(By fromElement, int X, int Y) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            //Tính từ vị trí click chuột đầu tiên (clickAndHold)
            action.clickAndHold(findWebElement(fromElement)).pause(1).moveByOffset(X, Y).release().build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean moveToElement(By toElement) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean moveToOffset(int X, int Y) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveByOffset(X, Y).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean pressENTER() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean pressESC() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Press F11 keyboard")
    public static boolean pressF11() {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_F11);
            robot.keyRelease(KeyEvent.VK_F11);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param by truyền vào đối tượng element dạng By
     * @return Tô màu viền đỏ cho Element trên website
     */
    @Step("High light on element")
    public static WebElement highLightElement(By by) {
        // draw a border around the found element
        if (DriverManager.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid red'", waitForElementVisible(by));
            sleep(1);
        }
        return waitForElementVisible(by);
    }

    /**
     * Open website with get URL
     *
     * @param URL
     */
    @Step("Open website with get URL")
    public static void getToUrl(String URL) {
        sleep(WAIT_SLEEP_STEP);
        DriverManager.getDriver().get(URL);
        waitForPageLoaded();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(BOLD_START + ICON_Navigate_Right + " Open URL : " + BOLD_END + URL);
        }
        AllureManager.saveTextLog("Open URL: " + URL);

        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("getToUrl_" + URL));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Open website with navigate to URL
     *
     * @param URL
     */
    @Step("Open website with navigate to URL")
    public static void navigateToUrl(String URL) {
        DriverManager.getDriver().navigate().to(URL);
        waitForPageLoaded();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(BOLD_START + ICON_Navigate_Right + " Navigate to URL: " + BOLD_END + URL);
        }
        AllureManager.saveTextLog("Navigate to URL: " + URL);

        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("navigateToUrl_" + URL));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Điền giá trị vào ô Text
     *
     * @param by    element dạng đối tượng By
     * @param value giá trị cần điền vào ô text
     */
    @Step("Set value in textbox")
    public static void setText(By by, String value) {
        waitForElementVisible(by).sendKeys(value);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(FrameworkConstants.BOLD_START + value + FrameworkConstants.BOLD_END + " value is successfully passed in textbox.");
        }
        AllureManager.saveTextLog(value + " value is successfully passed in textbox.");
        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("setText_" + value + "_" + by));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Xóa giá trị trong ô Text
     *
     * @param by element dạng đối tượng By
     */
    @Step("Clear value in textbox")
    public static void clearText(By by) {
        waitForElementVisible(by).clear();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clear" + FrameworkConstants.BOLD_END + " value in textbox successfully.");
        }
        AllureManager.saveTextLog("Clear value in textbox successfully.");
        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("clearText_" + by.toString()));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Click chuột vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    @Step("Click on the element")
    public static void clickElement(By by) {
        waitForElementVisible(by).click();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clicked" + FrameworkConstants.BOLD_END + " on the object " + by.toString());
        }
        AllureManager.saveTextLog("Clicked on the object " + by.toString());

        //Screenshot for this step if screenshot_all_steps = yes in config.properties file
        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("clickElement_" + by.toString()));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Click chuột vào Element trên web với Javascript (click ngầm không sợ bị che)
     *
     * @param by element dạng đối tượng By
     */
    @Step("Click on the object by Javascript")
    public static void clickElementWithJs(By by) {
        //Đợi đến khi element đó tồn tại
        waitForElementPresent(by);
        //Scroll to element với Js
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        //click với js
        js.executeScript("arguments[0].click();", findWebElement(by));

        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("clickElementWithJs_" + by));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Click on the link on website with text
     *
     * @param linkText is the visible text of a link
     */
    @Step("Click on the link text")
    public static void clickLinkText(String linkText) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
        elementWaited.click();

        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("clickLinkText_" + linkText));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Click chuột phải vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    @Step("Right click on element")
    public static void rightClickElement(By by) {
        Actions action = new Actions(DriverManager.getDriver());
        action.contextClick(waitForElementVisible(by)).build().perform();

        if (screenshot_all_steps.equals(YES)) {
            CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug("rightClickElement_" + by));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Get text of a element
     *
     * @param by element dạng đối tượng By
     * @return text of a element
     */
    @Step("Get text element")
    public static String getTextElement(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        AllureManager.saveTextLog("Get text object: " + by.toString());
        AllureManager.saveTextLog("===> The value is: " + waitForElementVisible(by).getText());
        return waitForElementVisible(by).getText().trim();
    }

    /**
     * Lấy giá trị từ thuộc tính của element
     *
     * @param by            element dạng đối tượng By
     * @param attributeName tên thuộc tính
     * @return giá trị thuộc tính của element
     */
    public static String getAttributeElement(By by, String attributeName) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        return waitForElementVisible(by).getAttribute(attributeName);
    }

    /**
     * Lấy giá trị CSS của một element
     *
     * @param by      element dạng đối tượng By
     * @param cssName tên thuộc tính CSS
     * @return giá trị của CSS
     */
    public static String getCssValueElement(By by, String cssName) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        return waitForElementVisible(by).getCssValue(cssName);
    }

    public static Dimension getSizeElement(By by) {
        return waitForElementVisible(by).getSize();
    }

    public static Point getLocationElement(By by) {
        return waitForElementVisible(by).getLocation();
    }

    public static String getTagNameElement(By by) {
        return waitForElementVisible(by).getTagName();
    }

    //Handle Table

    /**
     * Kiểm tra giá trị từng cột của table khi tìm kiếm theo điều kiện CHỨA
     *
     * @param column vị trí cột
     * @param value  giá trị cần so sánh
     */
    @Step("Check data by CONTAINS type after searching on the Table by Column.")
    public static void checkContainsSearchTableByColumn(int column, String value) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        List<WebElement> totalRows = findWebElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Number of results for keyword (" + value + "): " + totalRows.size());

        if (totalRows.size() < 1) {
            Log.info("Not found value: " + value);
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                boolean res = false;
                WebElement title = waitForElementPresent(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
                js.executeScript("arguments[0].scrollIntoView(true);", title);
                res = title.getText().toUpperCase().contains(value.toUpperCase());
                Log.info("Row " + i + ": " + res + " - " + title.getText());
                Assert.assertTrue(res, "Row " + i + " (" + title.getText() + ")" + " không chứa giá trị " + value);
            }
        }
    }

    /**
     * Kiểm tra giá trị từng cột của table khi tìm kiếm theo điều kiện CHỨA với xpath tuỳ chỉnh
     *
     * @param column           vị trí cột
     * @param value            giá trị cần so sánh
     * @param xpathToTRtagname giá trị xpath tính đến thẻ TR
     */
    @Step("Check data by CONTAINS type after searching on the Table by Column.")
    public static void checkContainsSearchTableByColumn(int column, String value, String xpathToTRtagname) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        //xpathToTRtagname is locator from table to "tr" tagname of data section: //tbody/tr, //div[@id='example_wrapper']//tbody/tr, ...
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath(xpathToTRtagname));
        sleep(1);
        Log.info("Number of results for keyword (" + value + "): " + totalRows.size());

        if (totalRows.size() < 1) {
            Log.info("Not found value: " + value);
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                boolean res = false;
                WebElement title = DriverManager.getDriver().findElement(By.xpath(xpathToTRtagname + "[" + i + "]/td[" + column + "]"));
                js.executeScript("arguments[0].scrollIntoView(true);", title);
                res = title.getText().toUpperCase().contains(value.toUpperCase());
                Log.info("Row " + i + ": " + res + " - " + title.getText());
                Assert.assertTrue(res, "Row " + i + " (" + title.getText() + ")" + " contains no value " + value);
            }
        }
    }

    /**
     * Lấy giá trị của một cột từ table
     *
     * @param column vị trí cột
     * @return mảng danh sách giá trị của một cột
     */
    public static ArrayList getValueTableByColumn(int column) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);

        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Number of results for column (" + column + "): " + totalRows.size()); //Không thích ghi log thì xóa nhen

        ArrayList arrayList = new ArrayList<String>();

        if (totalRows.size() < 1) {
            Log.info("Not found value !!");
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                boolean res = false;
                WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
                arrayList.add(title.getText());
                Log.info("Row " + i + ":" + title.getText()); //Không thích ghi log thì xóa nhen
            }
        }

        return arrayList;
    }

    //Wait Element

    /**
     * Chờ đợi element sẵn sàng hiển thị để thao tác theo thời gian tuỳ ý
     *
     * @param by      element dạng đối tượng By
     * @param timeOut thời gian chờ tối đa
     * @return một đối tượng WebElement đã sẵn sàng để thao tác
     */
    public static WebElement waitForElementVisible(By by, long timeOut) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element displays. " + by.toString());
            Log.error("Timeout waiting for the element displays. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng hiển thị để thao tác
     *
     * @param by element dạng đối tượng By
     * @return một đối tượng WebElement đã sẵn sàng để thao tác
     */
    public static WebElement waitForElementVisible(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        waitForElementPresent(by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element displays. " + by.toString());
            Log.error("Timeout waiting for the element displays. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng hiển thị để CLICK theo thời gian tuỳ ý
     *
     * @param by      element dạng đối tượng By
     * @param timeOut thời gian chờ tối đa
     * @return một đối tượng WebElement đã sẵn sàng để CLICK
     */
    public static WebElement waitForElementClickable(By by, long timeOut) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(findWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Timeout for element ready to click. " + by.toString());
            Log.error("Timeout for element ready to click. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng hiển thị để CLICK
     *
     * @param by element dạng đối tượng By
     * @return một đối tượng WebElement đã sẵn sàng để CLICK
     */
    public static WebElement waitForElementClickable(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(findWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Timeout for element ready to click. " + by.toString());
            Log.error("Timeout for element ready to click. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng tồn tại trong DOM theo thời gian tuỳ ý
     *
     * @param by      element dạng đối tượng By
     * @param timeOut thời gian chờ tối đa
     * @return một đối tượng WebElement đã tồn tại
     */
    public static WebElement waitForElementPresent(By by, long timeOut) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Timeout for element to exist. " + by.toString());
            Log.error("Timeout for element to exist. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng tồn tại trong DOM theo thời gian tuỳ ý
     *
     * @param by element dạng đối tượng By
     * @return một đối tượng WebElement đã tồn tại
     */
    public static WebElement waitForElementPresent(By by) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Element not exist. " + by.toString());
            Log.error("Element not exist. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi thuộc tính của một element tồn tại
     *
     * @param by        element dạng đối tượng By
     * @param attribute tên thuộc tính
     * @return true/false
     */
    public static boolean waitForElementHasAttribute(By by, String attribute) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(by), attribute));
        } catch (Throwable error) {
            Assert.fail("Timeout for element " + by.toString() + " to exist attribute: " + attribute);
            Log.error("Timeout for element " + by.toString() + " to exist attribute: " + attribute);
        }
        return false;
    }

    /**
     * Kiểm tra giá trị từ thuộc tính của một element có đúng hay không
     *
     * @param by        element dạng đối tượng By
     * @param attribute tên thuộc tính
     * @param value     giá trị
     * @return true/false
     */
    public static boolean verifyElementAttributeValue(By by, String attribute, String value) {
        if (ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(WAIT_SLEEP_STEP);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.attributeToBe(by, attribute, value));
            return true;
        } catch (Throwable error) {
            Assert.fail("Object: " + by.toString() + ". Not found value: " + value + " with attribute type: " + attribute + ". Actual get Attribute value is: " + getAttributeElement(by, attribute));
            return false;
        }
    }

    /**
     * Chờ đợi thuộc tính của một element tồn tại với thời gian tuỳ chỉnh
     *
     * @param by        element dạng đối tượng By
     * @param attribute tên thuộc tính
     * @return true/false
     * @timeOut thời gian chờ tối đa
     */
    public static boolean verifyElementHasAttribute(By by, String attribute, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(by), attribute));
            return true;
        } catch (Throwable error) {
            Assert.fail("Not found Attribute " + attribute + " of element " + by.toString());
            return false;
        }
    }

    // Wait Page loaded

    /**
     * Chờ đợi trang tải xong (Javascript) với thời gian mặc định từ config
     */
    public static void waitForPageLoaded() {

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
                .toString().equals("complete");

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_PAGE_LOADED), Duration.ofMillis(500));
            wait.until(jsLoad);
        } catch (Throwable error) {
            Assert.fail("Timeout for page load (Javascript). (" + WAIT_PAGE_LOADED + "s)");
        }
    }

    /**
     * Chờ đợi JQuery tải xong với thời gian mặc định từ config
     */
    public static void waitForJQueryLoaded() {

        // wait for jQuery to loaded
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        };

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_PAGE_LOADED), Duration.ofMillis(500));
            wait.until(jQueryLoad);
        } catch (Throwable error) {
            Assert.fail("Timeout for JQuery load. (" + WAIT_PAGE_LOADED + "s)");
        }
    }

    //Wait for Angular Load

    /**
     * Chờ đợi Angular tải xong với thời gian mặc định từ config
     */
    public static void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_PAGE_LOADED), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> {
            try {
                return Boolean.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());
            } catch (Exception e) {
                // no jQuery present
                return true;
            }
        };

        //Get Angular is Ready
        boolean angularReady = Boolean.valueOf(js.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if (!angularReady) {
            Log.warn("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            wait.until(angularLoad);
        } else {
            Log.info("ANGULAR is Ready!");
        }
    }

}
