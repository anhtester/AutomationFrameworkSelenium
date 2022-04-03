package anhtester.com.utils;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.report.ExtentReportManager;
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
import java.time.Duration;
import java.util.*;
import java.util.List;

import static anhtester.com.constants.FrameworkConstants.*;
import static anhtester.com.constants.FrameworkConstants.BOLD_END;

public class WebUI {

    private WebDriver driver;

    private Actions action;
    private Robot robot;
    private Select select;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public WebUI() {
        driver = DriverManager.getDriver();
        logConsole(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        js = (JavascriptExecutor) driver;
        action = new Actions(driver);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public JavascriptExecutor getJsExecutor() {
        js = (JavascriptExecutor) driver;
        return js;
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm một element
     *
     * @param by là đối tượng By
     * @return Trả về là một đối tượng WebElement
     */
    public WebElement findWebElement(By by) {
        return driver.findElement(by);
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm nhiều element
     *
     * @param by là đối tượng By
     * @return Trả về là Danh sách đối tượng WebElement
     */
    public List<WebElement> findWebElements(By by) {
        return driver.findElements(by);
    }

    /**
     * In ra câu message trong Console log
     *
     * @param object truyền vào object bất kỳ
     */
    public void logConsole(@Nullable Object object) {
        System.out.println(object);
    }

    /**
     * Chờ đợi ép buộc với đơn vị là Giây
     *
     * @param second là số nguyên dương tương ứng số Giây
     */
    public void sleep(int second) {
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
    public ChromeOptions notificationsAllow() {
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
    public ChromeOptions notificationsBlock() {
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
    public void uploadFileForm(By by, String filePath) {
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
    public void uploadFileSendkeys(By by, String filePath) {
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        sleep(1);
        //Dán link file vào ô upload
        findWebElement(by).sendKeys(filePath);
    }

    public String getCurrentUrl() {
        waitForPageLoaded();
        Log.info("Current Page Url: " + driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        waitForPageLoaded();
        String title = driver.getTitle();
        Log.info("Current Page Title: " + driver.getTitle());
        return title;
    }

    public boolean verifyPageTitle(String pageTitle) {
        return getPageTitle().equals(pageTitle);
    }

    public boolean verifyPageContains(String text) {
        return driver.getPageSource().contains(text);
    }

    public boolean verifyPageLoaded(String pageUrl) {
        waitForPageLoaded();
        boolean res = false;

        if (driver.getCurrentUrl().trim().toLowerCase().contains(pageUrl.trim().toLowerCase())) {
            res = true;
        } else {
            res = false;
            Log.info("Page " + pageUrl + " NOT loaded.");
        }
        return res;
    }

    public boolean verifyPageUrl(String pageUrl) {
        Log.info("Current URL: " + driver.getCurrentUrl());
        return driver.getCurrentUrl().contains(pageUrl);
    }

    //Handle dropdown select option

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     *
     * @param bys  element cùng loại (nhiều giá trị) dạng đối tượng By
     * @param text giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    public boolean selectOptionOther(By bys, String text) {
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = driver.findElements(bys);

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

    public void selectOptionByText(By by, String Text) {
        select = new Select(findWebElement(by));
        select.selectByVisibleText(Text);
    }

    public void selectOptionByValue(By by, String Value) {
        select = new Select(findWebElement(by));
        select.selectByValue(Value);
    }

    public void selectOptionByIndex(By by, int Index) {
        select = new Select(findWebElement(by));
        select.selectByIndex(Index);
    }

    public void verifyOptionTotal(By element, int total) {
        select = new Select(findWebElement(element));
        Assert.assertEquals(total, select.getOptions().size());
    }

    public boolean verifySelectedByText(By by, String Text) {
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected: " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText().equals(Text);
    }

    public boolean verifySelectedByValue(By by, String optionValue) {
        Select select = new Select(findWebElement(by));
        Log.info("Option Selected: " + select.getFirstSelectedOption().getAttribute("value"));
        return select.getFirstSelectedOption().getAttribute("value").equals(optionValue);
    }

    public boolean verifySelectedByIndex(By by, int Index) {
        Boolean res = false;
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

    public void switchToFrameByIndex(int Index) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Index));
        //driver.switchTo().frame(Index);
    }

    public void switchToFrameByIdOrName(String IdOrName) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
        //driver.switchTo().frame(IdOrName);
    }

    public void switchToFrameByElement(By by) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        driver.switchTo().frame(findWebElement(by));
    }

    public void switchToMainWindow() {
        driver.switchTo().defaultContent();
    }

    //Handle Alert
    public void alertAccept() {
        driver.switchTo().alert().accept();
    }

    public void alertDismiss() {
        driver.switchTo().alert().dismiss();
    }

    public void alertGetText() {
        driver.switchTo().alert().getText();
    }

    public void alertSetText(String text) {
        driver.switchTo().alert().sendKeys(text);
    }

    public boolean verifyAlertPresent(int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy Alert.");
            return false;
        }
    }

    //Handle Elements

    public List<String> getListElementsText(By by) {
        waitForPageLoaded();
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        List<WebElement> listElement = findWebElements(by);
        List<String> listText = new ArrayList<>();

        for (WebElement e : listElement) {
            listText.add(e.getText());
        }

        return listText;
    }

    public boolean verifyElementExists(By by) {
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

    public boolean verifyElementText(By by, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return getTextElement(by).equals(text);
    }

    public boolean verifyElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean verifyElementPresent(By by) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean verifyElementVisibility(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public void scrollToElement(By element) {
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(element));
    }

    public void scrollToPosition(int X, int Y) {
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
    }

    public boolean hoverElement(By by) {
        try {
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean MouseHover(By by) {
        try {
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dragAndDrop(By fromElement, By toElement) {
        try {
            waitForPageLoaded();
            action.dragAndDrop(findWebElement(fromElement), findWebElement(toElement)).perform();
            //action.clickAndHold(findWebElement(fromElement)).moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean dragAndDropElement(By fromElement, By toElement) {
        try {
            waitForPageLoaded();
            action.clickAndHold(findWebElement(fromElement)).moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean dragAndDropOffset(By fromElement, int X, int Y) {
        try {
            waitForPageLoaded();
            //Tính từ vị trí click chuột đầu tiên (clickAndHold)
            action.clickAndHold(findWebElement(fromElement)).moveByOffset(X, Y).release().build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean moveToElement(By toElement) {
        try {
            waitForPageLoaded();
            action.moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean moveToOffset(int X, int Y) {
        try {
            waitForPageLoaded();
            action.moveByOffset(X, Y).build().perform();
            return true;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return false;
        }
    }

    public boolean pressENTER() {
        try {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressESC() {
        try {
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressF11() {
        try {
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
    public WebElement highLightElement(By by) {
        // draw a border around the found element
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", findWebElement(by));
            sleep(1);
        }
        return findWebElement(by);
    }

    public void getToUrl(String URL) {
        DriverManager.getDriver().get(URL);
        ExtentReportManager.pass(ICON_Navigate_Right + " Open URL : " + BOLD_START + BASE_URL + BOLD_END);
    }

    public void navigateToUrl(String URL) {
        DriverManager.getDriver().get(URL);
        DriverManager.getDriver().navigate().to(URL);
        ExtentReportManager.pass(ICON_Navigate_Right + " Navigate to URL: " + BOLD_START + BASE_URL + BOLD_END);
    }

    /**
     * Điền giá trị vào ô Text
     *
     * @param by    element dạng đối tượng By
     * @param value giá trị cần điền vào ô text
     */
    public void setText(By by, String value) {
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.sendKeys(value);
        ExtentReportManager.pass(FrameworkConstants.BOLD_START + value + FrameworkConstants.BOLD_END + " value is successfully passed in textbox.");
    }

    /**
     * Xóa giá trị trong ô Text
     *
     * @param by element dạng đối tượng By
     */
    public void clearText(By by) {
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.clear();
        ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clear" + FrameworkConstants.BOLD_END + " value in textbox successfully.");
    }

    /**
     * Click chuột vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    public void clickElement(By by) {
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.click();
        ExtentReportManager.pass(FrameworkConstants.BOLD_START + "Clicked" + FrameworkConstants.BOLD_END + " on the object " + by.toString());
    }

    /**
     * Click chuột vào Element trên web với Javascript
     *
     * @param by element dạng đối tượng By
     */
    public void clickElementWithJs(By by) {
        //Đợi đến khi element đó tồn tại
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        //Scroll to element với Js
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        //click với js
        js.executeScript("arguments[0].click();", findWebElement(by));
    }

    public void clickLinkText(String linkText) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
        elementWaited.click();
    }

    /**
     * Click chuột phải vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
    public void rightClickElement(By by) {
        waitForPageLoaded();
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        action.contextClick().build().perform();
    }

    public String getTextElement(By by) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getText();
    }

    public String getAttributeElement(By by, String attributeValue) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getAttribute(attributeValue);
    }

    public String getCssValueElement(By by, String cssAttribute) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getCssValue(cssAttribute);
    }

    public Dimension getSizeElement(By by) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getSize();
    }

    public Point getLocationElement(By by) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getLocation();
    }

    public String getTagNameElement(By by) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getTagName();
    }

    //Handle Table
    public void checkContainsSearchTableByColumn(int column, String value) {
        List<WebElement> totalRows = driver.findElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Số kết quả cho từ khóa (" + value + "): " + totalRows.size());

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            js.executeScript("arguments[0].scrollIntoView(true);", title);
            res = title.getText().toUpperCase().contains(value.toUpperCase());
            Log.info("Dòng thứ " + i + ": " + res + " - " + title.getText());
            Assert.assertTrue(res, "Dòng thứ " + i + " (" + title.getText() + ")" + " không chứa giá trị " + value);
        }
    }

    public ArrayList getValueTableByColumn(int column) {
        List<WebElement> totalRows = driver.findElements(By.xpath("//tbody/tr"));
        sleep(1);
        Log.info("Số kết quả cho cột (" + column + "): " + totalRows.size());

        ArrayList arrayList = new ArrayList<String>();

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            arrayList.add(title.getText());
            Log.info("Dòng thứ " + i + ":" + title.getText()); //Không thích in coi chơi thì xóa nhen
        }

        return arrayList;
    }

    //    Wait Element
    public void waitForElementVisible(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element hiển thị.");
        }
    }

    public void waitForElementClickable(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.elementToBeClickable(findWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element sẵn sàng để Click.");
        }
    }

    public void waitForElementPresent(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Hết thời gian chờ Element tồn tại.");
        }
    }

    public boolean verifyElementAttributeValue(By by, String attribute, String value, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBe(by, attribute, value));
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy giá trị " + value + " của Attribute " + attribute);
            return false;
        }
    }

    public boolean verifyElementHasAttribute(By by, String attribute, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(findWebElement(by), attribute));
            return true;
        } catch (Throwable error) {
            Assert.fail("Không tìm thấy Attribute " + attribute + " của Element " + by);
            return false;
        }
    }

    // Wait Page loaded

    public void waitForPageLoaded() {
        // wait for jQuery to loaded
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        try {
            wait.until(jQueryLoad);
            wait.until(jsLoad);
            ExtentReportManager.info("Page loaded");
        } catch (Throwable error) {
            Assert.fail("Quá thời gian load trang.");
        }
    }

    //Wait for Angular Load
    protected void waitForAngularLoad() {
        final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return Boolean.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
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
