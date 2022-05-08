/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.enums.FailureHandling;
import anhtester.com.report.AllureManager;
import anhtester.com.report.ExtentReportManager;
import anhtester.com.report.ExtentTestManager;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static anhtester.com.constants.FrameworkConstants.*;
import static anhtester.com.constants.FrameworkConstants.BOLD_END;

/**
 * Class chung làm thư viện xử lý sẵn với nhiều hàm custom từ Selenium và Java.
 * Trả về là một Class chứa các hàm Static. Gọi lại dùng bằng cách lấy tên class chấm tên hàm (WebUI.method)
 */
public class WebUI {

    private static SoftAssert softAssert = new SoftAssert();

    public static void stopSoftAssertAll() {
        softAssert.assertAll();
    }

    /**
     * //Khôi phục cửa sổ và đặt kích thước cửa sổ.
     *
     * @param width  with Pixel
     * @param height with Pixel
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
        //Di chuyển cửa sổ lên trên cùng bên trái của màn hình chính
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
        waitForPageLoaded();
        Log.info("Current Page Url: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl();
    }

    public static String getPageTitle() {
        waitForPageLoaded();
        String title = DriverManager.getDriver().getTitle();
        Log.info("Current Page Title: " + DriverManager.getDriver().getTitle());
        return title;
    }

    public static boolean verifyPageTitle(String pageTitle) {
        return getPageTitle().equals(pageTitle);
    }

    public static boolean verifyPageContains(String text) {
        return DriverManager.getDriver().getPageSource().contains(text);
    }

    public static boolean verifyPageUrl(String pageUrl) {
        waitForPageLoaded();
        Log.info("Current URL: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl().contains(pageUrl.trim());
    }

    //Handle dropdown select option

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     *
     * @param by   là element cùng loại (nhiều giá trị) dạng đối tượng By
     * @param text giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    public static boolean selectOptionOther(By by, String text) {
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = DriverManager.getDriver().findElements(by);

            for (WebElement element : elements) {
                Log.info(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    element.click();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.info(e.getMessage());
        }
        return false;
    }

    public static void selectOptionByText(By by, String text) {
        Select select = new Select(findWebElement(by));
        select.selectByVisibleText(text);
    }

    public static void selectOptionByValue(By by, String value) {
        Select select = new Select(findWebElement(by));
        select.selectByValue(value);
    }

    public static void selectOptionByIndex(By by, int index) {
        Select select = new Select(findWebElement(by));
        select.selectByIndex(index);
    }

    public static void verifyOptionTotal(By element, int total) {
        Select select = new Select(findWebElement(element));
        Assert.assertEquals(total, select.getOptions().size());
    }

    public static boolean verifySelectedByText(By by, String text) {
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected by text: " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText().equals(text);
    }

    public static boolean verifySelectedByValue(By by, String optionValue) {
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected by value: " + select.getFirstSelectedOption().getAttribute("value"));
        return select.getFirstSelectedOption().getAttribute("value").equals(optionValue);
    }

    public static boolean verifySelectedByIndex(By by, int index) {
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
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
        //DriverManager.getDriver().switchTo().frame(Index);
    }

    public static void switchToFrameByIdOrName(String IdOrName) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
        //DriverManager.getDriver().switchTo().frame(IdOrName);
    }

    public static void switchToFrameByElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        //DriverManager.getDriver().switchTo().frame(findWebElement(by));
    }

    public static void switchToDefaultContent() {
        DriverManager.getDriver().switchTo().defaultContent();
    }


    public static void switchToWindowOrTab(int position) {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[position].toString());
    }

    public static void openNewTab() {
        // Opens a new tab and switches to new tab
        DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
    }

    public static void openNewWindow() {
        // Opens a new window and switches to new window
        DriverManager.getDriver().switchTo().newWindow(WindowType.WINDOW);
    }

    public static void switchToMainWindow() {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[0].toString());
    }

    public static void switchToMainWindow(String originalWindow) {
        DriverManager.getDriver().switchTo().window(originalWindow);
    }

    public static void switchToLastWindow() {
        Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[windowHandles.size() - 1].toString());
    }

    //Handle Alert
    public static void alertAccept() {
        DriverManager.getDriver().switchTo().alert().accept();
    }

    public static void alertDismiss() {
        DriverManager.getDriver().switchTo().alert().dismiss();
    }

    public static void alertGetText() {
        DriverManager.getDriver().switchTo().alert().getText();
    }

    public static void alertSetText(String text) {
        DriverManager.getDriver().switchTo().alert().sendKeys(text);
    }

    public static boolean verifyAlertPresent(int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy Alert.");
            return false;
        }
    }

    //Handle Elements

    public static List<String> getListElementsText(By by) {
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
        waitForPageLoaded();
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
        return getTextElement(by).trim().equals(text.trim());
    }

    public static boolean verifyElementTextEquals(By by, String text, FailureHandling flowControl) {
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
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(element));
    }

    public static void scrollToPosition(int X, int Y) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
    }

    public static boolean hoverElement(By by) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean mouseHover(By by) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void dragAndDropJS(WebElement from, WebElement to) {
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
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean pressF11() {
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
    public static WebElement highLightElement(By by) {
        // draw a border around the found element
        if (DriverManager.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid red'", waitForElementVisible(by));
            sleep(1.5);
        }
        return waitForElementVisible(by);
    }

    @Step("Open Website")
    public static void getToUrl(String URL) {
        DriverManager.getDriver().get(URL);
        waitForPageLoaded();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(ICON_Navigate_Right + " Open URL : " + BOLD_START + URL + BOLD_END);
        }
        AllureManager.saveTextLog("Open URL: " + URL);
    }

    public static void navigateToUrl(String URL) {
        DriverManager.getDriver().navigate().to(URL);
        waitForPageLoaded();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(ICON_Navigate_Right + " Navigate to URL: " + BOLD_START + URL + BOLD_END);
        }
        AllureManager.saveTextLog("Navigate to URL: " + URL);
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
    }

    /**
     * Click chuột vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    @Step("Clicked on the object")
    public static void clickElement(By by) {
        waitForElementVisible(by).click();

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clicked" + FrameworkConstants.BOLD_END + " on the object " + by.toString());
        }
        AllureManager.saveTextLog("Clicked on the object " + by.toString());
    }

    /**
     * Click chuột vào Element trên web với Javascript (click ngầm không sợ bị che)
     *
     * @param by element dạng đối tượng By
     */
    @Step("Clicked on the object by Javascript")
    public static void clickElementWithJs(By by) {
        //Đợi đến khi element đó tồn tại
        waitForElementPresent(by);
        //Scroll to element với Js
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        //click với js
        js.executeScript("arguments[0].click();", findWebElement(by));
    }

    public static void clickLinkText(String linkText) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
        elementWaited.click();
    }

    /**
     * Click chuột phải vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    public static void rightClickElement(By by) {
        Actions action = new Actions(DriverManager.getDriver());
        action.contextClick(waitForElementVisible(by)).build().perform();
    }

    @Step("Get text object")
    public static String getTextElement(By by) {
        AllureManager.saveTextLog("Get text object: " + by.toString());
        AllureManager.saveTextLog("===> The value is: " + waitForElementVisible(by).getText());
        return waitForElementVisible(by).getText().trim();
    }

    public static String getAttributeElement(By by, String attributeValue) {
        return waitForElementVisible(by).getAttribute(attributeValue);
    }

    public static String getCssValueElement(By by, String cssAttribute) {
        return waitForElementVisible(by).getCssValue(cssAttribute);
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
    @Step("Check data by CONTAINS type after searching on the Table by Column.")
    public static void checkContainsSearchTableByColumn(int column, String value) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Số kết quả cho từ khóa (" + value + "): " + totalRows.size());

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            js.executeScript("arguments[0].scrollIntoView(true);", title);
            res = title.getText().toUpperCase().contains(value.toUpperCase());
            Log.info("Dòng thứ " + i + ": " + res + " - " + title.getText());
            Assert.assertTrue(res, "Dòng thứ " + i + " (" + title.getText() + ")" + " không chứa giá trị " + value);
        }
    }

    @Step("Check data by CONTAINS type after searching on the Table by Column.")
    public static void checkContainsSearchTableByColumn(int column, String value, String xpathToTRtagname) {
        //xpathToTRtagname is locator from table to "tr" tagname of data section: //tbody/tr, //div[@id='example_wrapper']//tbody/tr, ...
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath(xpathToTRtagname));
        sleep(1);
        Log.info("Số kết quả cho từ khóa (" + value + "): " + totalRows.size());

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = DriverManager.getDriver().findElement(By.xpath(xpathToTRtagname + "[" + i + "]/td[" + column + "]"));
            js.executeScript("arguments[0].scrollIntoView(true);", title);
            res = title.getText().toUpperCase().contains(value.toUpperCase());
            Log.info("Dòng thứ " + i + ": " + res + " - " + title.getText());
            Assert.assertTrue(res, "Dòng thứ " + i + " (" + title.getText() + ")" + " không chứa giá trị " + value);
        }
    }

    public static ArrayList getValueTableByColumn(int column) {
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Số kết quả cho cột (" + column + "): " + totalRows.size()); //Không thích ghi log thì xóa nhen

        ArrayList arrayList = new ArrayList<String>();

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            arrayList.add(title.getText());
            Log.info("Dòng thứ " + i + ":" + title.getText()); //Không thích ghi log thì xóa nhen
        }

        return arrayList;
    }

    //Wait Element
    public static WebElement waitForElementVisible(By by, long timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element hiển thị. " + by.toString());
            Log.error("Hết thời gian chờ Element hiển thị. " + by.toString());
        }
        return null;
    }

    public static WebElement waitForElementVisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element hiển thị. " + by.toString());
            Log.error("Hết thời gian chờ Element hiển thị. " + by.toString());
        }
        return null;
    }

    public static WebElement waitForElementClickable(By by, long timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(findWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element sẵn sàng để Click. " + by.toString());
            Log.error("Hết thời gian chờ Element sẵn sàng để Click. " + by.toString());
        }
        return null;
    }

    public static WebElement waitForElementClickable(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(findWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element sẵn sàng để Click. " + by.toString());
            Log.error("Hết thời gian chờ Element sẵn sàng để Click. " + by.toString());
        }
        return null;
    }

    public static WebElement waitForElementPresent(By by, long timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element tồn tại. " + by.toString());
            Log.error("Hết thời gian chờ Element tồn tại. " + by.toString());
        }
        return null;
    }

    public static WebElement waitForElementPresent(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element tồn tại. " + by.toString());
            Log.error("Hết thời gian chờ Element tồn tại. " + by.toString());
        }
        return null;
    }

    public static boolean waitForElementHasAttribute(By by, String attribute) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(by), attribute));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ element " + by.toString() + " tồn tại Attribute: " + attribute);
            Log.error("Hết thời gian chờ element " + by.toString() + " tồn tại Attribute: " + attribute);
        }
        return false;
    }

    public static boolean verifyElementAttributeValue(By by, String attribute, String value) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.attributeToBe(by, attribute, value));
            return true;
        } catch (Throwable error) {
            Assert.fail("Object: " + by.toString() + ". Not found value: " + value + " with attribute type: " + attribute + ". Actual get Attribute value is: " + getAttributeElement(by, attribute));
            return false;
        }
    }

    public static boolean verifyElementHasAttribute(By by, String attribute, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(by), attribute));
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy Attribute " + attribute + " của element " + by.toString());
            return false;
        }
    }

    // Wait Page loaded

    public static void waitForPageLoaded() {

        // wait for jQuery to loaded
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        };

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
                .toString().equals("complete");

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(jQueryLoad);
            wait.until(jsLoad);
//            ExtentReportManager.info("Page loaded");
//            AllureManager.saveTextLog("Page loaded");
        } catch (Throwable error) {
            Assert.fail("Quá thời gian tải trang. (" + WAIT_EXPLICIT + "s)");
        }
    }

    //Wait for Angular Load
    public static void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
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
