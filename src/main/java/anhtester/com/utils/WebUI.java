/*
 * Copyright (c) 2022. Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.report.AllureManager;
import anhtester.com.report.ExtentReportManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static anhtester.com.constants.FrameworkConstants.*;
import static anhtester.com.constants.FrameworkConstants.BOLD_END;

/**
 * Class chung làm thư viện xử lý sẵn với nhiều hàm custom từ Selenium và Java.
 *  Trả về là một Class chứa các hàm Static. Gọi lại dùng bằng cách lấy tên class chấm tên hàm (WebUI.method)
 */
public class WebUI {

    public static JavascriptExecutor getJsExecutor() {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        return js;
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm một element
     *
     * @param by là đối tượng By
     * @return Trả về là một đối tượng WebElement
     */
    public static WebElement findWebElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm nhiều element
     *
     * @param by là đối tượng By
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
    public static void sleep(long second) {
        try {
            Thread.sleep(second * 1000);
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
     * @param by       truyền vào element dạng đối tượng By
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
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        sleep(1);
        //Dán link file vào ô upload
        findWebElement(by).sendKeys(filePath);
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

    public static boolean verifyPageLoaded(String pageUrl) {
        waitForPageLoaded();
        boolean res = false;

        if (DriverManager.getDriver().getCurrentUrl().trim().toLowerCase().contains(pageUrl.trim().toLowerCase())) {
            res = true;
        } else {
            res = false;
            Log.info("Page " + pageUrl + " NOT loaded.");
        }
        return res;
    }

    public static boolean verifyPageUrl(String pageUrl) {
        Log.info("Current URL: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl().contains(pageUrl);
    }

    //Handle dropdown select option

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     *
     * @param bys  element cùng loại (nhiều giá trị) dạng đối tượng By
     * @param text giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    public static boolean selectOptionOther(By bys, String text) {
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = DriverManager.getDriver().findElements(bys);

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

    public static void selectOptionByText(By by, String Text) {
        Select select = new Select(findWebElement(by));
        select.selectByVisibleText(Text);
    }

    public static void selectOptionByValue(By by, String Value) {
        Select select = new Select(findWebElement(by));
        select.selectByValue(Value);
    }

    public static void selectOptionByIndex(By by, int Index) {
        Select select = new Select(findWebElement(by));
        select.selectByIndex(Index);
    }

    public static void verifyOptionTotal(By element, int total) {
        Select select = new Select(findWebElement(element));
        Assert.assertEquals(total, select.getOptions().size());
    }

    public static boolean verifySelectedByText(By by, String Text) {
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected: " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText().equals(Text);
    }

    public static boolean verifySelectedByValue(By by, String optionValue) {
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected: " + select.getFirstSelectedOption().getAttribute("value"));
        return select.getFirstSelectedOption().getAttribute("value").equals(optionValue);
    }

    public static boolean verifySelectedByIndex(By by, int Index) {
        boolean res = false;
        Select select = new Select(findWebElement(by));
        int indexFirstOption = select.getOptions().indexOf(select.getFirstSelectedOption());
        Log.info("First Option Selected Index: " + indexFirstOption);
        Log.info("Expected Index: " + Index);
        if (indexFirstOption == Index) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    //Handle frame iframe

    public static void switchToFrameByIndex(int Index) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Index));
        //driver.switchTo().frame(Index);
    }

    public static void switchToFrameByIdOrName(String IdOrName) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
        //driver.switchTo().frame(IdOrName);
    }

    public static void switchToFrameByElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        DriverManager.getDriver().switchTo().frame(findWebElement(by));
    }

    public static void switchToMainWindow() {
        DriverManager.getDriver().switchTo().defaultContent();
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
            Log.info("Element not exists");
        }
        return res;
    }

    public static boolean verifyElementText(By by, String text) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return getTextElement(by).equals(text);
    }

    public static boolean verifyElementToBeClickable(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(by));
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public static boolean verifyElementPresent(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
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

    public static boolean MouseHover(By by) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
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
            action.clickAndHold(findWebElement(fromElement)).moveByOffset(X, Y).release().build().perform();
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
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid red'", findWebElement(by));
            sleep(1);
        }
        return findWebElement(by);
    }

    @Step("Open Website")
    public static void getToUrl(String URL) {
        DriverManager.getDriver().get(URL);

        logConsole(DriverManager.getDriver());
        ExtentReportManager.pass(ICON_Navigate_Right + " Open URL : " + BOLD_START + BASE_URL + BOLD_END);
        AllureManager.saveTextLog("Open URL: " + BASE_URL);
    }

    public static void navigateToUrl(String URL) {
        DriverManager.getDriver().get(URL);
        DriverManager.getDriver().navigate().to(URL);
        ExtentReportManager.pass(ICON_Navigate_Right + " Navigate to URL: " + BOLD_START + BASE_URL + BOLD_END);
        AllureManager.saveTextLog("Navigate to URL: " + BASE_URL);
    }

    /**
     * Điền giá trị vào ô Text
     *
     * @param by    element dạng đối tượng By
     * @param value giá trị cần điền vào ô text
     */
    @Step("Set value in textbox")
    public static void setText(By by, String value) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.sendKeys(value);
        ExtentReportManager.pass(FrameworkConstants.BOLD_START + value + FrameworkConstants.BOLD_END + " value is successfully passed in textbox.");
        AllureManager.saveTextLog(value + " value is successfully passed in textbox.");
    }

    /**
     * Xóa giá trị trong ô Text
     *
     * @param by element dạng đối tượng By
     */
    @Step("Clear value in textbox")
    public static void clearText(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.clear();
        ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clear" + FrameworkConstants.BOLD_END + " value in textbox successfully.");
        AllureManager.saveTextLog("Clear value in textbox successfully.");
    }

    /**
     * Click chuột vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    @Step("Clicked on the object")
    public static void clickElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.click();
        ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clicked" + FrameworkConstants.BOLD_END + " on the object " + by.toString());
        AllureManager.saveTextLog("Clicked on the object " + by.toString());
    }

    /**
     * Click chuột vào Element trên web với Javascript
     *
     * @param by element dạng đối tượng By
     */
    @Step("Clicked on the object by Javascript")
    public static void clickElementWithJs(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        //Đợi đến khi element đó tồn tại
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        //Scroll to element với Js
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
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        Actions action = new Actions(DriverManager.getDriver());
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        action.contextClick().build().perform();
    }

    @Step("Get text object")
    public static String getTextElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        AllureManager.saveTextLog("Get text object: " + String.valueOf(by));
        AllureManager.saveTextLog("===> The value is: " + elementWaited.getText());
        return elementWaited.getText();
    }

    public static String getAttributeElement(By by, String attributeValue) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getAttribute(attributeValue);
    }

    public static String getCssValueElement(By by, String cssAttribute) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getCssValue(cssAttribute);
    }

    public static Dimension getSizeElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getSize();
    }

    public static Point getLocationElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getLocation();
    }

    public static String getTagNameElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getTagName();
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

    public static ArrayList getValueTableByColumn(int column) {
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Số kết quả cho cột (" + column + "): " + totalRows.size());

        ArrayList arrayList = new ArrayList<String>();

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            arrayList.add(title.getText());
            Log.info("Dòng thứ " + i + ":" + title.getText()); //Không thích in coi chơi thì xóa nhen
        }

        return arrayList;
    }

    //Wait Element
    public static WebElement waitForElementVisible(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element " + by.toString());
            Log.error("Hết thời gian chờ Element " + by.toString());
        }
        return null;
    }

    public static void waitForElementClickable(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.elementToBeClickable(findWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element sẵn sàng để Click.");
        }
    }

    public static void waitForElementPresent(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element tồn tại.");
        }
    }

    public static boolean verifyElementAttributeValue(By by, String attribute, String value, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBe(by, attribute, value));
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy giá trị " + value + " của Attribute " + attribute);
            return false;
        }
    }

    public static boolean verifyElementHasAttribute(By by, String attribute, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(findWebElement(by), attribute));
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy Attribute " + attribute + " của Element " + by);
            return false;
        }
    }

    // Wait Page loaded

    @Step("Wait for page loaded")
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
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(jQueryLoad);
            wait.until(jsLoad);
//            ExtentReportManager.info("Page loaded");
//            AllureManager.saveTextLog("Page loaded");
        } catch (Throwable error) {
            Assert.fail("Quá thời gian load trang.");
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
            Log.info("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            wait.until(angularLoad);
        } else {
            Log.info("ANGULAR is Ready!");
        }
    }

}
