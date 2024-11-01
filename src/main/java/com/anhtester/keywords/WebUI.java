/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.keywords;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.enums.FailureHandling;
import com.anhtester.helpers.SystemHelpers;
import com.anhtester.report.AllureManager;
import com.anhtester.report.ExtentReportManager;
import com.anhtester.report.ExtentTestManager;
import com.anhtester.utils.BrowserInfoUtils;
import com.anhtester.utils.DateUtils;
import com.anhtester.utils.LogUtils;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.Headers;
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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.*;

/**
 * Keyword WebUI is a generic class that is a preprocessed library with many custom functions from Selenium and Java.
 * Returns is a Class containing Static functions. Callback is used by taking the class name and dotting the function name (WebUI.method)
 */
public class WebUI {

    /**
     * The SoftAssert object is created
     */
    private static SoftAssert softAssert = new SoftAssert();

    /**
     * Stop the Soft Assert of TestNG
     */
    public static void stopSoftAssertAll() {
        softAssert.assertAll();
    }

    /**
     * Smart Waits contains waitForPageLoaded and sleep functions
     */
    public static void smartWait() {
        if (FrameworkConstants.ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        if (FrameworkConstants.WAIT_SLEEP_STEP > 0) {
            sleep(FrameworkConstants.WAIT_SLEEP_STEP);
        }
    }

    /**
     * Take entire-page screenshot and add to Extent report and Allure report
     *
     * @param screenName Screenshot name
     */
    public static void addScreenshotToReport(String screenName) {
        if (FrameworkConstants.SCREENSHOT_ALL_STEPS.equals(FrameworkConstants.YES)) {
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.addScreenShot(SystemHelpers.makeSlug(screenName));
            }
            //CaptureHelpers.captureScreenshot(DriverManager.getDriver(), Helpers.makeSlug(screenshotName));
            AllureManager.takeScreenshotStep();
        }
    }

    /**
     * Take a screenshot of a specific web element. The captured image will be saved in '.png' format.
     *
     * @param screenName Screenshot name
     */
    public static void takeElementScreenshot(By by, String screenName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        try {
            String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXPORT_CAPTURE_PATH;
            File file = new File(path);
            if (!file.exists()) {
                LogUtils.info("No Folder: " + path);
                file.mkdir();
                LogUtils.info("Folder created: " + file);
            }

            File source = getWebElement(by).getScreenshotAs(OutputType.FILE);
            // result.getName() gets the name of the test case and assigns it to the screenshot file name
            FileUtils.copyFile(source, new File(path + "/" + screenName + "_" + dateFormat.format(new Date()) + ".png"));
            LogUtils.info("Screenshot taken: " + screenName);
            LogUtils.info("Screenshot taken current URL: " + getCurrentUrl());
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }

    /**
     * Take entire-page screenshot, including overflow parts. The captured image will be saved in '.png' format.
     * This method simulates a scroll action to take a number of shots and merge them together to make a full-page screenshot.
     *
     * @param screenName Screenshot name
     */
    public static void takeFullPageScreenshot(String screenName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        try {
            String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXPORT_CAPTURE_PATH;
            File file = new File(path);
            if (!file.exists()) {
                LogUtils.info("No Folder: " + path);
                file.mkdir();
                LogUtils.info("Folder created: " + file);
            }

            LogUtils.info("Driver for Screenshot: " + DriverManager.getDriver());
            // Create reference of TakesScreenshot
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            // Call the capture screenshot function - getScreenshotAs
            File source = ts.getScreenshotAs(OutputType.FILE);
            // result.getName() gets the name of the test case and assigns it to the screenshot file name
            FileUtils.copyFile(source, new File(path + "/" + screenName + "_" + dateFormat.format(new Date()) + ".png"));
            LogUtils.info("Screenshot taken: " + screenName);
            LogUtils.info("Screenshot taken current URL: " + getCurrentUrl());
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }


    /**
     * Get the Download Directory path
     *
     * @return the download directory path
     */
    public static String getPathDownloadDirectory() {
        String path = "";
        String machine_name = System.getProperty("user.home");
        path = machine_name + File.separator + "Downloads";
        return path;
    }

    /**
     * Count files in Download Directory
     *
     * @return files total in download directory
     */
    public static int countFilesInDownloadDirectory() {
        String pathFolderDownload = getPathDownloadDirectory();
        File file = new File(pathFolderDownload);
        int i = 0;
        for (File listOfFiles : file.listFiles()) {
            if (listOfFiles.isFile()) {
                i++;
            }
        }
        return i;
    }

    /**
     * Verify files in the Download Directory contain the specified file (CONTAIN)
     *
     * @param fileName the specified file
     * @return true if file is contain in download directory, else is false
     */
    public static boolean verifyFileContainsInDownloadDirectory(String fileName) {
        boolean flag = false;
        try {
            String pathFolderDownload = getPathDownloadDirectory();
            File dir = new File(pathFolderDownload);
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                flag = false;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(fileName)) {
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            e.getMessage();
            return flag;
        }
    }

    /**
     * Verify files in the Download Directory contain the specified file (EQUALS)
     *
     * @param fileName the specified file
     * @return true if file is contain in download directory, else is false
     */
    public static boolean verifyFileEqualsInDownloadDirectory(String fileName) {
        boolean flag = false;
        try {
            String pathFolderDownload = getPathDownloadDirectory();
            File dir = new File(pathFolderDownload);
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                flag = false;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(fileName)) {
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            e.getMessage();
            return flag;
        }
    }

    /**
     * Verify the file is downloaded (CONTAIN)
     *
     * @param fileName       the specified file
     * @param timeoutSeconds System will wait at most timeout (seconds) to return result
     * @return true if file is downloaded, else is false
     */
    public static boolean verifyDownloadFileContainsName(String fileName, int timeoutSeconds) {
        int i = 0;
        while (i < timeoutSeconds) {
            boolean exist = verifyFileContainsInDownloadDirectory(fileName);
            if (exist) {
                i = timeoutSeconds;
                return true;
            }
            sleep(1);
            i++;
        }
        return false;
    }

    /**
     * Verify the file is downloaded (EQUALS)
     *
     * @param fileName       the specified file
     * @param timeoutSeconds System will wait at most timeout (seconds) to return result
     * @return true if file is downloaded, else is false
     */
    public static boolean verifyDownloadFileEqualsName(String fileName, int timeoutSeconds) {
        int i = 0;
        while (i < timeoutSeconds) {
            boolean exist = verifyFileEqualsInDownloadDirectory(fileName);
            if (exist) {
                i = timeoutSeconds;
                return true;
            }
            sleep(1);
            i++;
        }
        return false;
    }

    /**
     * Delete all files in Download Directory
     */
    public static void deleteAllFileInDownloadDirectory() {
        try {
            String pathFolderDownload = getPathDownloadDirectory();
            File file = new File(pathFolderDownload);
            File[] listOfFiles = file.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    new File(listOfFiles[i].toString()).delete();
                }
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }

    /**
     * Delete all files in Download Directory
     *
     * @param pathDirectory the Download Directory path
     */
    public static void deleteAllFileInDirectory(String pathDirectory) {
        try {
            File file = new File(pathDirectory);
            File[] listOfFiles = file.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    new File(listOfFiles[i].toString()).delete();
                }
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }

    /**
     * Verify the file is downloaded with JavascriptExecutor (EQUALS)
     *
     * @param fileName the specified file
     * @return true if file is downloaded, else is false
     */
    @Step("Verify File Downloaded With JS [Equals]: {0}")
    public static boolean verifyFileDownloadedWithJS_Equals(String fileName) {
        openWebsite("chrome://downloads");
        sleep(3);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        String element = (String) js.executeScript("return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#show').getAttribute('title')");
        File file = new File(element);
        LogUtils.info(element);
        LogUtils.info(file.getName());
        if (file.exists() && file.getName().trim().equals(fileName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verify the file is downloaded with JavascriptExecutor (CONTAINS)
     *
     * @param fileName the specified file
     * @return true if file is downloaded, else is false
     */
    @Step("Verify File Downloaded With JS [Contains]: {0}")
    public static boolean verifyFileDownloadedWithJS_Contains(String fileName) {
        openWebsite("chrome://downloads");
        sleep(3);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        String element = (String) js.executeScript("return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#show').getAttribute('title')");
        File file = new File(element);
        LogUtils.info(element);
        LogUtils.info(file.getName());
        if (file.exists() && file.getName().trim().contains(fileName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Login as Authentication on URL
     *
     * @param url
     * @param username
     * @param password
     */
    @Step("Get to URL {0} with authentication")
    public static void getToUrlAuthentication(String url, String username, String password) {
        // Get the devtools from the running drivers and create a session
        DevTools devTools = ((HasDevTools) DriverManager.getDriver()).getDevTools();
        devTools.createSession();

        // Enable the Network domain of devtools
        devTools.send(Network.enable(Optional.of(100000), Optional.of(100000), Optional.of(100000)));
        String auth = username + ":" + password;

        // Encoding the username and password using Base64 (java.util)
        String encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());

        // Pass the network header -> Authorization : Basic <encoded String>
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodeToString);
        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));

        LogUtils.info("getToUrlAuthentication with URL: " + url);
        LogUtils.info("getToUrlAuthentication with Username: " + username);
        LogUtils.info("getToUrlAuthentication with Password: " + password);
        // Load the application url
        openWebsite(url);
        sleep(3);
    }

    //Handle HTML5 validation message and valid value

    /**
     * Verify HTML5 message of element required field
     *
     * @param by is an element of type By
     * @return true/false corresponds to required
     */
    @Step("Verify HTML5 message of element {0} required field")
    public static boolean verifyHTML5RequiredField(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        boolean verifyRequired = (Boolean) js.executeScript("return arguments[0].required;", getWebElement(by));
        return verifyRequired;
    }

    /**
     * Verify the HTML5 message of the element has a value of Valid
     *
     * @param by is an element of type By
     * @return true/false corresponds to Valid
     */
    @Step("Verify HTML5 message of element {0} valid")
    public static boolean verifyHTML5ValidValueField(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        boolean verifyValid = (Boolean) js.executeScript("return arguments[0].validity.valid;", getWebElement(by));
        return verifyValid;
    }

    /**
     * Get HTML5 message of element
     *
     * @param by is an element of type By
     * @return the Text string value of the notification (String)
     */
    @Step("Get HTML5 message of element {0}")
    public static String getHTML5MessageField(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        String message = (String) js.executeScript("return arguments[0].validationMessage;", getWebElement(by));
        return message;
    }

    /**
     * Set window sizes.
     *
     * @param widthPixel  is Width with Pixel
     * @param heightPixel is Height with Pixel
     */
    public static void setWindowSize(int widthPixel, int heightPixel) {
        DriverManager.getDriver().manage().window().setSize(new Dimension(widthPixel, heightPixel));
    }

    /**
     * Move the window to the selected position X, Y from the top left corner 0
     *
     * @param X (int) - horizontal
     * @param Y (int) - vertical
     */
    public static void setWindowPosition(int X, int Y) {
        DriverManager.getDriver().manage().window().setPosition(new Point(X, Y));
    }

    /**
     * Maximize window
     */
    public static void maximizeWindow() {
        DriverManager.getDriver().manage().window().maximize();
    }

    /**
     * Minimize window
     */
    public static void minimizeWindow() {
        DriverManager.getDriver().manage().window().minimize();
    }

    /**
     * Take a screenshot at the element location. Do not capture the entire screen.
     *
     * @param by          is an element of type By
     * @param elementName to name the .png image file
     */
    public static void screenshotElement(By by, String elementName) {
        File scrFile = getWebElement(by).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("./" + elementName + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Print the current page in the browser.
     * Note: Only works in headless mode
     *
     * @param endPage is the total number of pages to print. Adjective 1.
     * @return is content of page form PDF file
     */
    public static String printPage(int endPage) {
        PrintOptions printOptions = new PrintOptions();
        //From page 1 to end page
        printOptions.setPageRanges("1-" + endPage);

        Pdf pdf = ((PrintsPage) DriverManager.getDriver()).print(printOptions);
        return pdf.getContent();
    }

    /**
     * Get the JavascriptExecutor object created
     *
     * @return JavascriptExecutor
     */
    public static JavascriptExecutor getJsExecutor() {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        return js;
    }

    /**
     * Convert the By object to the WebElement
     *
     * @param by is an element of type By
     * @return Returns a WebElement object
     */
    public static WebElement getWebElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    /**
     * Find multiple elements with the locator By object
     *
     * @param by is an element of type By
     * @return Returns a List of WebElement objects
     */
    public static List<WebElement> getWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }

    /**
     * Print out the message in the Console log
     *
     * @param object passes any object
     */
    public static void logConsole(@Nullable Object object) {
        System.out.println(object);
    }

    /**
     * Forced wait with unit of Seconds
     *
     * @param second is a positive integer corresponding to the number of Seconds
     */
    public static void sleep(double second) {
        try {
            Thread.sleep((long) (second * 1000));
        } catch (InterruptedException e) {
            LogUtils.error(e.getMessage());
        }
    }

    /**
     * Allow browser popup notifications on the website
     *
     * @return the value set Allow - belongs to the ChromeOptions object
     */
    @Step("Allow Notifications")
    public static ChromeOptions notificationsAllow() {
        // Create a Map to store options
        Map<String, Object> prefs = new HashMap<String, Object>();

        // Add keys and values to Map as follows to disable browser notifications
        // Pass argument 1 to ALLOW and 2 to BLOCK
        prefs.put("profile.default_content_setting_values.notifications", 1);

        // Create a ChromeOptions session
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        // Use the setExperimentalOption function to execute the value through the above prefs object
        options.setExperimentalOption("prefs", prefs);

        //Returns the set value of the ChromeOptions object
        return options;
    }

    /**
     * Block browser popup notifications on the website
     *
     * @return the value of the setup Block - belongs to the ChromeOptions object
     */
    @Step("Blocked Notifications")
    public static ChromeOptions notificationsBlock() {
        // Create a Map to store options
        Map<String, Object> prefs = new HashMap<String, Object>();

        // Add keys and values to Map as follows to disable browser notifications
        // Pass argument 1 to ALLOW and 2 to BLOCK
        prefs.put("profile.default_content_setting_values.notifications", 2);

        // Create a ChromeOptions session
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        // Use the setExperimentalOption function to execute the value through the above prefs object
        options.setExperimentalOption("prefs", prefs);

        //Returns the set value of the ChromeOptions object
        return options;
    }

    /**
     * Uploading files with a click shows a form to select local files on your computer
     *
     * @param by       is an element of type By
     * @param filePath the absolute path to the file on your computer
     */
    @Step("Upload File with open Local Form")
    public static void uploadFileWithLocalForm(By by, String filePath) {
        smartWait();

        Actions action = new Actions(DriverManager.getDriver());
        //Click to open form upload
        action.moveToElement(getWebElement(by)).click().perform();
        sleep(2);

        // Create Robot class
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            LogUtils.error(e.getMessage());
        }

        // Copy File path to Clipboard
        StringSelection str = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);

        //Check OS for Windows
        if (BrowserInfoUtils.isWindows()) {
            // Press Control+V to paste
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            // Release the Control V
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
            robot.delay(1000);
            // Press Enter
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
        //Check OS for MAC
        if (BrowserInfoUtils.isMac()) {
            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(1000);

            //Open goto MAC
            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_G);
            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_G);

            //Paste the clipboard value
            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_V);
            robot.delay(1000);

            //Press Enter key to close the Goto MAC and Upload on MAC
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }

        LogUtils.info("Upload File with Local Form: " + filePath);
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Upload File with Local Form: " + filePath);
        }
        AllureManager.saveTextLog("Upload File with Local Form: " + filePath);

    }

    /**
     * Upload files by dragging the link directly into the input box
     *
     * @param by       passes an element of object type By
     * @param filePath absolute path to the file
     */
    @Step("Upload File with SendKeys")
    public static void uploadFileWithSendKeys(By by, String filePath) {
        smartWait();

        waitForElementVisible(by).sendKeys(filePath);

        LogUtils.info("Upload File with SendKeys");
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Upload File with SendKeys");
        }
        AllureManager.saveTextLog("Upload File with SendKeys");

    }

    /**
     * Get current URL from current drivers
     *
     * @return the current URL as String
     */
    @Step("Get Current URL")
    public static String getCurrentUrl() {
        smartWait();
        LogUtils.info("Get Current URL: " + DriverManager.getDriver().getCurrentUrl());
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Get Current URL: " + DriverManager.getDriver().getCurrentUrl());
        }
        AllureManager.saveTextLog("Get Current URL: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl();
    }

    /**
     * Get current web page's title
     *
     * @return the current URL as String
     */
    @Step("Get Page Title")
    public static String getPageTitle() {
        smartWait();
        String title = DriverManager.getDriver().getTitle();
        LogUtils.info("Get Page Title: " + DriverManager.getDriver().getTitle());
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Get Page Title: " + DriverManager.getDriver().getTitle());
        }
        AllureManager.saveTextLog("Get Page Title: " + DriverManager.getDriver().getTitle());
        return title;
    }

    /**
     * Verify the web page's title equals with the specified title
     *
     * @param pageTitle The title of the web page that needs verifying
     * @return the current URL as String
     */
    @Step("Verify Page Title equals {0}")
    public static boolean VerifyPageTitle(String pageTitle) {
        smartWait();
        return getPageTitle().equals(pageTitle);
    }


    /**
     * Verify if the given text presents anywhere in the page source.
     *
     * @param text
     * @return true/false
     */
    @Step("Verify Page sources Contains Text {0}")
    public static boolean verifyPageContainsText(String text) {
        smartWait();
        return DriverManager.getDriver().getPageSource().contains(text);
    }

    /**
     * Verify if the given web element is checked.
     *
     * @param by Represent a web element as the By object
     * @return true if the element is checked, otherwise false.
     */
    @Step("Verify Element Checked {0}")
    public static boolean verifyElementChecked(By by) {
        smartWait();

        boolean checked = getWebElement(by).isSelected();
        if (checked) {
            return true;
        } else {
            Assert.assertTrue(false, "❌ The element NOT checked.");
            return false;
        }
    }

    /**
     * Verify if the given web element is checked.
     *
     * @param by      Represent a web element as the By object
     * @param message the custom message if false
     * @return true if the element is checked, otherwise false.
     */
    @Step("Verify Element Checked {0}")
    public static boolean verifyElementChecked(By by, String message) {
        smartWait();
        waitForElementVisible(by);

        boolean checked = getWebElement(by).isSelected();

        if (checked) {
            return true;
        } else {
            Assert.assertTrue(false, message);
            return false;
        }
    }

    //Handle dropdown

    /**
     * Select value in dropdown dynamic (not pure Select Option)
     *
     * @param objectListItem is the locator of the list item as a By object
     * @param text           the value to select as Text of the item
     * @return click to select a specified item with Text value
     */
    @Step("Select Option Dynamic by Text {0}")
    public static boolean selectOptionDynamic(By objectListItem, String text) {
        smartWait();
        //For dynamic dropdowns (div, li, span,...not select options)
        try {
            List<WebElement> elements = getWebElements(objectListItem);

            for (WebElement element : elements) {
                LogUtils.info(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    element.click();
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
        }
        return false;
    }

    /**
     * Verify All Options contains the specified text (select option)
     *
     * @param by   Represent a web element as the By object
     * @param text the specified text
     * @return true if all option contains the specified text
     */
    @Step("Verify Option Dynamic Exist by Text {0}")
    public static boolean verifyOptionDynamicExist(By by, String text) {
        smartWait();

        try {
            List<WebElement> elements = getWebElements(by);

            for (WebElement element : elements) {
                LogUtils.info(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
        }
        return false;
    }

    /**
     * Get total number of options the given web element has. (select option)
     *
     * @param objectListItem Represent a web element as the By object
     * @return total number of options
     */
    @Step("Get total of Option Dynamic with list element {0}")
    public static int getOptionDynamicTotal(By objectListItem) {
        smartWait();

        LogUtils.info("Get total of Option Dynamic with list element. " + objectListItem);
        try {
            List<WebElement> elements = getWebElements(objectListItem);
            return elements.size();
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
        }
        return 0;
    }

    /**
     * Select the options with the given label (displayed text).
     *
     * @param by   Represent a web element as the By object
     * @param text the specified text of option
     */
    @Step("Select Option by Text {0}")
    public static void selectOptionByText(By by, String text) {
        smartWait();
        Select select = new Select(getWebElement(by));
        select.selectByVisibleText(text);
        LogUtils.info("Select Option " + by + "by text " + text);
    }

    /**
     * Select the options with the given value.
     *
     * @param by    Represent a web element as the By object
     * @param value the specified value of option
     */
    @Step("Select Option by Value {0}")
    public static void selectOptionByValue(By by, String value) {
        smartWait();

        Select select = new Select(getWebElement(by));
        select.selectByValue(value);
        LogUtils.info("Select Option " + by + "by value " + value);
    }

    /**
     * Select the options with the given index.
     *
     * @param by    Represent a web element as the By object
     * @param index the specified index of option
     */
    @Step("Select Option by Index {0}")
    public static void selectOptionByIndex(By by, int index) {
        smartWait();

        Select select = new Select(getWebElement(by));
        select.selectByIndex(index);
        LogUtils.info("Select Option " + by + "by index " + index);
    }

    /**
     * Verify the number of options equals the specified total
     *
     * @param by    Represent a web element as the By object
     * @param total the specified options total
     */
    @Step("Verify Option Total equals {0}")
    public static void verifyOptionTotal(By by, int total) {
        smartWait();

        Select select = new Select(getWebElement(by));
        LogUtils.info("Verify Option Total equals: " + total);
        Assert.assertEquals(total, select.getOptions().size());
    }

    /**
     * Verify if the options at the given text are selected.
     *
     * @param by   Represent a web element as the By object
     * @param text the specified options text
     * @return true if options given selected, else is false
     */
    @Step("Verify Selected Option by Text {0}")
    public static boolean verifySelectedByText(By by, String text) {
        smartWait();

        Select select = new Select(getWebElement(by));
        LogUtils.info("Verify Option Selected by text: " + select.getFirstSelectedOption().getText());

        if (select.getFirstSelectedOption().getText().equals(text)) {
            return true;
        } else {
            Assert.assertEquals(select.getFirstSelectedOption().getText(), text, "The option NOT selected. " + by);
            return false;
        }
    }

    /**
     * Verify if the options at the given value are selected.
     *
     * @param by    Represent a web element as the By object
     * @param value the specified options value
     * @return true if options given selected, else is false
     */
    @Step("Verify Selected Option by Value {0}")
    public static boolean verifySelectedByValue(By by, String value) {
        smartWait();

        Select select = new Select(getWebElement(by));
        LogUtils.info("Verify Option Selected by value: " + select.getFirstSelectedOption().getAttribute("value"));
        if (select.getFirstSelectedOption().getAttribute("value").equals(value)) {
            return true;
        } else {
            Assert.assertEquals(select.getFirstSelectedOption().getAttribute("value"), value, "The option NOT selected. " + by);
            return false;
        }
    }

    /**
     * Verify if the options at the given index are selected.
     *
     * @param by    Represent a web element as the By object
     * @param index the specified options index
     * @return true if options given selected, else is false
     */
    @Step("Verify Selected Option by Index {0}")
    public static boolean verifySelectedByIndex(By by, int index) {
        smartWait();

        Select select = new Select(getWebElement(by));
        int indexFirstOption = select.getOptions().indexOf(select.getFirstSelectedOption());
        LogUtils.info("The First Option selected by index: " + indexFirstOption);
        LogUtils.info("Expected index: " + index);
        if (indexFirstOption == index) {
            return true;
        } else {
            Assert.assertTrue(false, "The option NOT selected. " + by);
            return false;
        }
    }

    /**
     * Switch to iframe by index of iframe tag
     *
     * @param index index of iframe tag
     */
    @Step("Switch to Frame by Index: {0}")
    public static void switchToFrameByIndex(int index) {
        smartWait();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
        //DriverManager.getDriver().switchTo().frame(Index);
        LogUtils.info("Switch to Frame by Index. " + index);
    }

    /**
     * Switch to iframe by ID or Name of iframe tag
     *
     * @param IdOrName ID or Name of iframe tag
     */
    @Step("Switch to Frame by ID or Name: {0}")
    public static void switchToFrameByIdOrName(String IdOrName) {
        smartWait();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
        LogUtils.info("Switch to Frame by ID or Name. " + IdOrName);
    }

    /**
     * Switch to iframe by Element is this iframe tag
     *
     * @param by iframe tag
     */
    @Step("Switch to Frame by Element {0}")
    public static void switchToFrameByElement(By by) {
        smartWait();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        LogUtils.info("Switch to Frame by Element. " + by);
    }

    /**
     * Switch to Default Content
     */
    @Step("Switch to Default Content")
    public static void switchToDefaultContent() {
        smartWait();

        DriverManager.getDriver().switchTo().defaultContent();
        LogUtils.info("Switch to Default Content");
    }

    /**
     * Switch to iframe by position of iframe tag
     *
     * @param position index of iframe tag
     */
    @Step("Switch to Window or Tab by Position: {0}")
    public static void switchToWindowOrTabByPosition(int position) {
        smartWait();

        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[position].toString());
        LogUtils.info("Switch to Window or Tab by Position: " + position);
    }

    /**
     * Switch to popup window by title
     *
     * @param title title of popup window
     */
    @Step("Switch to Window or Tab by Title: {0}")
    public static void switchToWindowOrTabByTitle(String title) {
        smartWait();

        //Store the ID of the original window
        String originalWindow = DriverManager.getDriver().getWindowHandle();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        //Wait for the new window or tab
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        //Loop through until we find a new window handle
        for (String windowHandle : DriverManager.getDriver().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                DriverManager.getDriver().switchTo().window(windowHandle);
                if (DriverManager.getDriver().getTitle().equals(title)) {
                    break;
                }
            }
        }

    }

    /**
     * Switch to popup window by URL
     *
     * @param url url of popup window
     */
    @Step("Switch to Window or Tab by Url: {0}")
    public static void switchToWindowOrTabByUrl(String url) {
        smartWait();
        //Store the ID of the original window
        String originalWindow = DriverManager.getDriver().getWindowHandle();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        //Wait for the new window or tab
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        //Loop through until we find a new window handle
        for (String windowHandle : DriverManager.getDriver().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                DriverManager.getDriver().switchTo().window(windowHandle);
                if (DriverManager.getDriver().getCurrentUrl().equals(url)) {
                    break;
                }
            }
        }

    }

    /**
     * Close current Window
     */
    @Step("Close current Window")
    public static void closeCurrentWindow() {
        LogUtils.info("Close current Window: " + getCurrentUrl());
        DriverManager.getDriver().close();
        LogUtils.info("Closed current Window");
    }


    /**
     * Get the total number of popup windows the given web page.
     *
     * @param number the specified number
     * @return true/false
     */
    @Step("Verify total of Windows or Tab")
    public static boolean verifyTotalOfWindowsOrTab(int number) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT)).until(ExpectedConditions.numberOfWindowsToBe(number));
    }

    /**
     * Open new Tab
     */
    @Step("Open new Tab")
    public static void openAndSwitchNewTab() {
        smartWait();
        // Opens a new tab and switches to new tab
        DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
        LogUtils.info("Open new Tab");
    }

    /**
     * Open new Window
     */
    @Step("Open new Window")
    public static void openAndSwitchNewWindow() {
        smartWait();
        // Opens a new window and switches to new window
        DriverManager.getDriver().switchTo().newWindow(WindowType.WINDOW);
        LogUtils.info("Open new Window");
    }

    /**
     * Switch to Main Window
     */
    @Step("Switch to Main Window")
    public static void switchToMainWindow() {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[0].toString());
        LogUtils.info("Switch to Main Window." + DriverManager.getDriver());
    }

    /**
     * Switch to Main Window by ID
     *
     * @param originalWindow ID of main window
     */
    @Step("Switch to Main Window by ID {0}")
    public static void switchToMainWindow(String originalWindow) {
        DriverManager.getDriver().switchTo().window(originalWindow);
        LogUtils.info("Switch to Main Window." + originalWindow);
    }

    /**
     * Switch to Last Window
     */
    @Step("Switch to Last Window")
    public static void switchToLastWindow() {
        Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
        WebDriver newDriver = DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[windowHandles.size() - 1].toString());
        LogUtils.info("Switch to Last Window." + newDriver.getCurrentUrl());
    }

    /**
     * Click Accept on Alert
     */
    @Step("Click Accept on Alert")
    public static void acceptAlert() {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().accept();
        LogUtils.info("Click Accept on Alert.");
    }

    /**
     * Click Dismiss on Alert
     */
    @Step("Click Dismiss on Alert")
    public static void dismissAlert() {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().dismiss();
        LogUtils.info("Click Dismiss on Alert.");
    }

    /**
     * Get text on Alert
     */
    @Step("Get text on Alert")
    public static String getTextAlert() {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
        LogUtils.info("Get text ion alert: " + DriverManager.getDriver().switchTo().alert().getText());
        return DriverManager.getDriver().switchTo().alert().getText();
    }

    /**
     * Set text on Alert
     */
    @Step("Set text on Alert {0}")
    public static void setTextAlert(String text) {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
        DriverManager.getDriver().switchTo().alert().sendKeys(text);
        LogUtils.info("Set " + text + " on Alert.");
    }

    /**
     * Verify if alert does present
     *
     * @param timeOut Timeout waiting for alert to present.(in seconds)
     * @return true/false
     */
    @Step("Verify Alert present with timeout {0}")
    public static boolean verifyAlertPresent(int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            LogUtils.error("Alert NOT Present.");
            Assert.fail("Alert NOT Present.");
            return false;
        }
    }

    /**
     * Get list text of specified elements
     *
     * @param by Represent a web element as the By object
     * @return Text list of specified elements
     */
    @Step("Get List Element {0}")
    public static List<String> getListElementsText(By by) {
        smartWait();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        List<WebElement> listElement = getWebElements(by);
        List<String> listText = new ArrayList<>();

        for (WebElement e : listElement) {
            listText.add(e.getText());
        }

        return listText;
    }

    /**
     * Verify if a web element is present (findElements.size > 0).
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Verify element exists {0}")
    public static boolean checkElementExists(By by) {
        smartWait();
        boolean result;

        List<WebElement> elementList = getWebElements(by);
        if (elementList.size() > 0) {
            LogUtils.info("✅ Element " + by + " existing.");
            result = true;
        } else {
            LogUtils.error("❌ Element " + by + " NOT exists.");
            result = false;
        }
        return result;
    }


    /**
     * Verify if two object are equal.
     *
     * @param actual The object one
     * @param expected The object two
     * @return true/false
     */
    @Step("Verify Equals: {0} ---AND--- {1}")
    public static boolean verifyEquals(Object actual, Object expected) {
        boolean result = actual.equals(expected);
        if (result) {
            LogUtils.info("Verify Equals: " + actual + " = " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.pass("Verify Equals: " + actual + " = " + expected);
            }
            AllureManager.saveTextLog("Verify Equals: " + actual + " = " + expected);
        } else {
            LogUtils.error("Verify Equals: " + actual + " != " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.fail("Verify Equals: " + actual + " != " + expected);
            }
            AllureManager.saveTextLog("❌ Verify Equals: " + actual + " != " + expected);
            Assert.assertEquals(actual, expected, "❌ " + actual + " != " + expected);
        }
        return result;
    }

    /**
     * Verify if two object are equal.
     *
     * @param actual  The object one
     * @param expected  The object two
     * @param message The custom message if false
     * @return true/false
     */
    @Step("Verify Equals: {0} ---AND--- {1}")
    public static boolean verifyEquals(Object actual, Object expected, String message) {
        boolean result = actual.equals(expected);
        if (result) {
            LogUtils.info("Verify Equals: " + actual + " = " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.pass("Verify Equals: " + actual + " = " + expected);
            }
            AllureManager.saveTextLog("Verify Equals: " + actual + " = " + expected);
        } else {
            LogUtils.error("Verify Equals: " + actual + " != " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.fail("Verify Equals: " + actual + " != " + expected);
            }
            AllureManager.saveTextLog("❌ Verify Equals: " + actual + " != " + expected);
            Assert.assertEquals(actual, expected, "❌ " + message);
        }
        return result;
    }

    /**
     * Verify if the first object contains the second object.
     *
     * @param actual The first object
     * @param expected The second object
     * @return true/false
     */
    @Step("Verify Contains: {0} AND {1}")
    public static boolean verifyContains(String actual, String expected) {
        boolean result = actual.contains(expected);
        if (result) {
            LogUtils.info("Verify Equals: " + actual + " CONTAINS " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.pass("Verify Contains: " + actual + " CONTAINS " + expected);
            }
            AllureManager.saveTextLog("Verify Contains: " + actual + "CONTAINS" + expected);
        } else {
            LogUtils.error("❌ Verify Contains: " + actual + " NOT CONTAINS " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.fail("Verify Contains: " + actual + " NOT CONTAINS " + expected);
            }
            AllureManager.saveTextLog("Verify Contains: " + actual + " NOT CONTAINS " + expected);

            Assert.assertEquals(actual, expected, "❌ " + actual + " NOT CONTAINS " + expected);
        }
        return result;
    }

    /**
     * Verify if the first object contains the second object.
     *
     * @param actual  The first object
     * @param expected  The second object
     * @param message The custom message if false
     * @return true/false
     */
    @Step("Verify Contains: {0} AND {1}")
    public static boolean verifyContains(String actual, String expected, String message) {
        boolean result = actual.contains(expected);
        if (result) {
            LogUtils.info("Verify Contains: " + actual + " CONTAINS " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.pass("Verify Contains: " + actual + " CONTAINS " + expected);
            }
            AllureManager.saveTextLog("Verify Contains: " + actual + "CONTAINS" + expected);
        } else {
            LogUtils.error("❌ Verify Contains: " + actual + " NOT CONTAINS " + expected);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.fail("Verify Contains: " + actual + " NOT CONTAINS " + expected);
            }
            AllureManager.saveTextLog("Verify Contains: " + actual + " NOT CONTAINS " + expected);

            Assert.assertEquals(actual, expected, message);
        }
        return result;
    }

    /**
     * Verify the condition is true.
     *
     * @return true/false
     */
    @Step("Verify TRUE: {0}")
    public static boolean verifyTrue(boolean condition) {
        if (condition) {
            LogUtils.info("Verify TRUE: " + condition);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.pass("Verify TRUE: " + condition);
            }
        } else {
            LogUtils.error("❌ Verify TRUE: " + condition);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.fail("Verify TRUE: " + condition);
            }
            Assert.assertTrue(condition, "The condition is FALSE.");
        }
        return condition;
    }

    /**
     * Verify the condition is true.
     *
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify TRUE: {0}")
    public static boolean verifyTrue(boolean condition, String message) {
        if (condition) {
            LogUtils.info("Verify TRUE: " + condition);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.pass("Verify TRUE: " + condition);
            }
        } else {
            LogUtils.error("❌ Verify TRUE: " + condition);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.fail("Verify TRUE: " + condition);
            }
            Assert.assertTrue(condition, message);
        }
        return condition;
    }


    /**
     * Verify text of an element. (equals)
     *
     * @param by   Represent a web element as the By object
     * @param text Text of the element to verify.
     * @return true if the element has the desired text, otherwise false.
     */
    public static boolean verifyElementText(By by, String text) {
        smartWait();
        waitForElementVisible(by);

        return getTextElement(by).trim().equals(text.trim());
    }

    /**
     * Verify text of an element. (equals)
     *
     * @param by          Represent a web element as the By object
     * @param text        Text of the element to verify.
     * @param flowControl Specify failure handling schema (STOP_ON_FAILURE, CONTINUE_ON_FAILURE, OPTIONAL) to determine whether the execution should be allowed to continue or stop
     * @return true if the element has the desired text, otherwise false.
     */
    @Step("Verify text of an element [Equals]")
    public static boolean verifyElementTextEquals(By by, String text, FailureHandling flowControl) {
        smartWait();

        waitForElementVisible(by);

        boolean result = getTextElement(by).trim().equals(text.trim());

        if (result) {
            LogUtils.info("Verify text of an element [Equals]: " + result);
        } else {
            LogUtils.error("❌ Verify text of an element [Equals]: " + result);
        }

        if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
            Assert.assertEquals(getTextElement(by).trim(), text.trim(), "❌ The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");
        }
        if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
            softAssert.assertEquals(getTextElement(by).trim(), text.trim(), "❌ The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");
            if (!result) {
                ExtentReportManager.fail("The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");
            }
        }
        if (flowControl.equals(FailureHandling.OPTIONAL)) {
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.warning("Verify text of an element [Equals] - " + result);
                ExtentReportManager.warning("The actual text is '" + getTextElement(by).trim() + "' not equals expected text '" + text.trim() + "'");
            }
            AllureManager.saveTextLog("Verify text of an element [Equals] - " + result + ". The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");
        }

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

        return getTextElement(by).trim().equals(text.trim());
    }

    /**
     * Verify text of an element. (equals)
     *
     * @param by   Represent a web element as the By object
     * @param text Text of the element to verify.
     * @return true if the element has the desired text, otherwise false.
     */
    @Step("Verify text of an element [Equals]")
    public static boolean verifyElementTextEquals(By by, String text) {
        smartWait();
        waitForElementVisible(by);

        boolean result = getTextElement(by).trim().equals(text.trim());

        if (result) {
            LogUtils.info("Verify text of an element [Equals]: " + result);
        } else {
            LogUtils.error("❌ Verify text of an element [Equals]: " + result);
        }

        Assert.assertEquals(getTextElement(by).trim(), text.trim(), "❌ The actual text is '" + getTextElement(by).trim() + "' NOT equals '" + text.trim() + "'");

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.warning("Verify text of an element [Equals] : " + result);
            ExtentReportManager.warning("The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");
        }
        AllureManager.saveTextLog("Verify text of an element [Equals] : " + result + ". The actual text is '" + getTextElement(by).trim() + "' NOT equals '" + text.trim() + "'");

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

        return result;
    }

    /**
     * Verify text of an element. (contains)
     *
     * @param by          Represent a web element as the By object
     * @param text        Text of the element to verify.
     * @param flowControl Specify failure handling schema (STOP_ON_FAILURE, CONTINUE_ON_FAILURE, OPTIONAL) to determine whether the execution should be allowed to continue or stop
     * @return true if the element has the desired text, otherwise false.
     */
    @Step("Verify text of an element [Contains]")
    public static boolean verifyElementTextContains(By by, String text, FailureHandling flowControl) {
        smartWait();
        waitForElementVisible(by);

        boolean result = getTextElement(by).trim().contains(text.trim());

        if (result) {
            LogUtils.info("Verify text of an element [Contains]: " + result);
        } else {
            LogUtils.error("❌ Verify text of an element [Contains]: " + result);
        }

        if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
            Assert.assertTrue(result, "❌ The actual text is " + getTextElement(by).trim() + " NOT contains " + text.trim());
        }
        if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
            softAssert.assertTrue(result, "❌ The actual text is " + getTextElement(by).trim() + " NOT contains " + text.trim());
        }
        if (flowControl.equals(FailureHandling.OPTIONAL)) {
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.warning("Verify text of an element [Contains] : " + result);
            }
            AllureManager.saveTextLog("Verify text of an element [Contains] : " + result);
        }

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

        return getTextElement(by).trim().contains(text.trim());
    }

    /**
     * Verify text of an element. (contains)
     *
     * @param by   Represent a web element as the By object
     * @param text Text of the element to verify.
     * @return true if the element has the desired text, otherwise false.
     */
    @Step("Verify text {1} of element [Contains] {0}")
    public static boolean verifyElementTextContains(By by, String text) {
        smartWait();
        waitForElementVisible(by);

        boolean result = getTextElement(by).trim().contains(text.trim());

        if (result) {
            LogUtils.info("Verify text of an element [Contains]: " + result);
        } else {
            LogUtils.error("❌ Verify text of an element [Contains]: " + result);
        }

        Assert.assertTrue(result, "The actual text is " + getTextElement(by).trim() + " NOT contains " + text.trim());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Verify text of an element [Contains] : " + result);
        }
        AllureManager.saveTextLog("Verify text of an element [Contains] : " + result);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

        return result;
    }

    /**
     * Verify if the given element is clickable.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Verify element Clickable {0}")
    public static boolean verifyElementClickable(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(by));
            LogUtils.info("Verify element clickable " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element clickable " + by);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error("❌ Element not clickable " + by);
            Assert.fail("❌ Element not clickable " + by);
            return false;
        }
    }

    /**
     * Verify if the given element is clickable. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Verify element Clickable {0} with timeout {1} second")
    public static boolean verifyElementClickable(By by, int timeout) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(by));
            LogUtils.info("Verify element clickable " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element clickable " + by);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error("❌ Element not clickable " + by);
            Assert.fail("❌ Element not clickable " + by);
            return false;
        }
    }

    /**
     * Verify if the given element is clickable. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element Clickable {0}")
    public static boolean verifyElementClickable(By by, int timeout, String message) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(by));
            LogUtils.info("Verify element clickable " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element clickable " + by);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error("❌ " + message);
            Assert.fail("❌ " + message);
            return false;
        }
    }

    /**
     * Verify if the given web element does present on DOM.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Verify element present {0}")
    public static boolean verifyElementPresent(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.info("Verify element present " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element present " + by);
            }
            addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
            return true;
        } catch (Exception e) {
            LogUtils.info("❌ The element does NOT present. " + by);
            Assert.fail("❌ The element does NOT present. " + by);
            return false;
        }
    }

    /**
     * Verify if the given web element does present on DOM. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Verify element present {0} with timeout {1} second")
    public static boolean verifyElementPresent(By by, int timeout) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.info("Verify element present " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element present " + by);
            }
            addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
            return true;
        } catch (Exception e) {
            LogUtils.info("❌ The element does NOT present. " + by);
            Assert.fail("❌ The element does NOT present. " + by);
            return false;
        }
    }

    /**
     * Verify if the given web element does present on DOM.
     *
     * @param by      Represent a web element as the By object
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element present {0}")
    public static boolean verifyElementPresent(By by, String message) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.info("Verify element present " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element present " + by);
            }
            addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
            return true;
        } catch (Exception e) {
            if (message.isEmpty() || message.isBlank()) {
                LogUtils.error("❌ The element does NOT present. " + by);
                Assert.fail("❌ The element does NOT present. " + by);
            } else {
                LogUtils.error(message);
                Assert.fail(message);
            }

            return false;
        }
    }

    /**
     * Verify if the given web element does present on DOM. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element present {0} with timeout {1} second")
    public static boolean verifyElementPresent(By by, int timeout, String message) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.info("Verify element present " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element present " + by);
            }
            addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
            return true;
        } catch (Exception e) {
            if (message.isEmpty() || message.isBlank()) {
                LogUtils.error("❌ The element does NOT present. " + by);
                Assert.fail("❌ The element does NOT present. " + by);
            } else {
                LogUtils.error(message);
                Assert.fail(message);
            }

            return false;
        }
    }

    /**
     * Verify if the given web element does NOT present on the DOM.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Verify element NOT present {0}")
    public static boolean verifyElementNotPresent(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.error("❌ Element is present " + by);
            Assert.fail("❌ The element presents. " + by);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Verify if the given web element does NOT present on the DOM. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Verify element NOT present {0} with timeout {1} second")
    public static boolean verifyElementNotPresent(By by, int timeout) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.error("❌ Element is present " + by);
            Assert.fail("❌ The element presents. " + by);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Verify if the given web element does NOT present on the DOM.
     *
     * @param by      Represent a web element as the By object
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element NOT present {0}")
    public static boolean verifyElementNotPresent(By by, String message) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            if (message.isEmpty() || message.isBlank()) {
                LogUtils.error("❌ The element presents. " + by);
                Assert.fail("❌ The element presents. " + by);
            } else {
                LogUtils.error("❌ " + message + by);
                Assert.fail("❌ " + message + by);
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Verify if the given web element does NOT present on the DOM. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element NOT present {0} with timeout {1} second")
    public static boolean verifyElementNotPresent(By by, int timeout, String message) {
        smartWait();
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            if (message.isEmpty() || message.isBlank()) {
                LogUtils.error("❌ The element presents. " + by);
                Assert.fail("❌ The element presents. " + by);
            } else {
                LogUtils.error("❌ " + message + by);
                Assert.fail("❌ " + message + by);
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Verify element is visible. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Check element visible {0}")
    public static boolean isElementVisible(By by, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify if the given web element is visible.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Verify element visible {0}")
    public static boolean verifyElementVisible(By by) {
        smartWait();
        LogUtils.info("Verify element visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            LogUtils.info("❌ The element is NOT visible. " + by);
            Assert.fail("❌ The element is NOT visible. " + by);
            return false;
        }
    }

    /**
     * Verify if the given web element is visible. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Verify element visible {0} with timeout {1} second")
    public static boolean verifyElementVisible(By by, int timeout) {
        smartWait();
        LogUtils.info("Verify element visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            LogUtils.error("❌ The element is NOT visible. " + by);
            Assert.fail("❌ The element is NOT visible. " + by);
            return false;
        }
    }

    /**
     * Verify if the given web element is visible.
     *
     * @param by      Represent a web element as the By object
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element visible {0}")
    public static boolean verifyElementVisible(By by, String message) {
        smartWait();
        LogUtils.info("Verify element visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            if (message.isEmpty()) {
                LogUtils.error("The element is not visible. " + by);
                Assert.fail("The element is NOT visible. " + by);
            } else {
                LogUtils.error(message + by);
                Assert.fail(message + by);
            }
            return false;
        }
    }

    /**
     * Verify if the given web element is visible. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element visible {0} with timeout {1} second")
    public static boolean verifyElementVisible(By by, int timeout, String message) {
        smartWait();
        LogUtils.info("Verify element visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            if (message.isEmpty()) {
                LogUtils.error("The element is not visible. " + by);
                Assert.fail("The element is NOT visible. " + by);
            } else {
                LogUtils.error(message + by);
                Assert.fail(message + by);
            }
            return false;
        }
    }

    /**
     * Verify if the given web element is NOT visible.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Verify element NOT visible {0}")
    public static boolean verifyElementNotVisible(By by) {
        smartWait();
        LogUtils.info("Verify element NOT visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            LogUtils.error("FAILED. The element is visible " + by);
            Assert.fail("FAILED. The element is visible " + by);
            return false;
        }
    }

    /**
     * Verify if the given web element is NOT visible. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Verify element NOT visible {0} with timeout {1} second")
    public static boolean verifyElementNotVisible(By by, int timeout) {
        smartWait();
        LogUtils.info("Verify element NOT visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            LogUtils.error("FAILED. The element is visible " + by);
            Assert.fail("FAILED. The element is visible " + by);
            return false;
        }
    }

    /**
     * Verify if the given web element is NOT visible.
     *
     * @param by      Represent a web element as the By object
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element NOT visible {0}")
    public static boolean verifyElementNotVisible(By by, String message) {
        smartWait();
        LogUtils.info("Verify element NOT visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            if (message.isEmpty()) {
                LogUtils.error("FAILED. The element is visible " + by);
                Assert.fail("FAILED. The element is visible " + by);
            } else {
                LogUtils.error(message + " " + by);
                Assert.fail(message + " " + by);
            }
            return false;
        }
    }

    /**
     * Verify if the given web element is NOT visible. (in seconds)
     *
     * @param by      Represent a web element as the By object
     * @param timeout System will wait at most timeout (seconds) to return result
     * @param message the custom message if false
     * @return true/false
     */
    @Step("Verify element NOT visible {0} with timeout {1} second")
    public static boolean verifyElementNotVisible(By by, int timeout, String message) {
        smartWait();
        LogUtils.info("Verify element NOT visible " + by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            if (message.isEmpty()) {
                LogUtils.error("FAILED. The element is visible " + by);
                Assert.fail("FAILED. The element is visible " + by);
            } else {
                LogUtils.error(message + " " + by);
                Assert.fail(message + " " + by);
            }
            return false;
        }
    }


    /**
     * Scroll an element into the visible area of the browser window. (at TOP)
     *
     * @param by Represent a web element as the By object
     */
    @Step("Scroll to element {0}")
    public static void scrollToElementAtTop(By by) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", getWebElement(by));
        LogUtils.info("Scroll to element " + by);
    }

    /**
     * Scroll an element into the visible area of the browser window. (at BOTTOM)
     *
     * @param by Represent a web element as the By object
     */
    @Step("Scroll to element {0}")
    public static void scrollToElementAtBottom(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(false);", getWebElement(by));
        LogUtils.info("Scroll to element " + by);
    }

    /**
     * Scroll an element into the visible area of the browser window. (at TOP)
     *
     * @param webElement Represent a web element as the By object
     */
    @Step("Scroll to element {0}")
    public static void scrollToElementAtTop(WebElement webElement) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", webElement);
        LogUtils.info("Scroll to element " + webElement);
    }

    /**
     * Scroll an element into the visible area of the browser window. (at BOTTOM)
     *
     * @param webElement Represent a web element as the By object
     */
    @Step("Scroll to element {0}")
    public static void scrollToElementAtBottom(WebElement webElement) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(false);", webElement);
        LogUtils.info("Scroll to element " + webElement);
    }

    /**
     * Scroll to an offset location
     *
     * @param X x offset
     * @param Y y offset
     */
    @Step("Scroll to position X={0}, Y={1}")
    public static void scrollToPosition(int X, int Y) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
        LogUtils.info("Scroll to position X = " + X + " ; Y = " + Y);
    }

    /**
     * Simulate users hovering a mouse over the given element.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Hover on element {0}")
    public static boolean hoverOnElement(By by) {
        smartWait();

        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(by)).perform();
            LogUtils.info("Hover on element " + by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Simulate users hovering a mouse over the given element.
     *
     * @param by Represent a web element as the By object
     * @return true/false
     */
    @Step("Mouse hover on element {0}")
    public static boolean mouseHover(By by) {
        smartWait();

        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(by)).perform();
            LogUtils.info("Mouse hover on element " + by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Drag and drop an element onto another element.
     *
     * @param fromElement represent the drag-able element
     * @param toElement   represent the drop-able element
     * @return true/false
     */
    @Step("Drag from element {0} to element {1}")
    public static boolean dragAndDrop(By fromElement, By toElement) {
        smartWait();

        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.dragAndDrop(getWebElement(fromElement), getWebElement(toElement)).perform();
            //action.clickAndHold(getWebElement(fromElement)).moveToElement(getWebElement(toElement)).release(getWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    /**
     * Drag and drop an element onto another element. (HTML5)
     *
     * @param fromElement represent the drag-able element
     * @param toElement   represent the drop-able element
     * @return true/false
     */
    @Step("Drag HTML5 from element {0} to element {1}")
    public static boolean dragAndDropHTML5(By fromElement, By toElement) {
        smartWait();

        try {
            Robot robot = new Robot();
            robot.mouseMove(0, 0);

            int X1 = getWebElement(fromElement).getLocation().getX() + (getWebElement(fromElement).getSize().getWidth() / 2);
            int Y1 = getWebElement(fromElement).getLocation().getY() + (getWebElement(fromElement).getSize().getHeight() / 2);
            System.out.println(X1 + " , " + Y1);

            int X2 = getWebElement(toElement).getLocation().getX() + (getWebElement(toElement).getSize().getWidth() / 2);
            int Y2 = getWebElement(toElement).getLocation().getY() + (getWebElement(toElement).getSize().getHeight() / 2);
            System.out.println(X2 + " , " + Y2);

            //This place takes the current coordinates plus 120px which is the browser header (1920x1080 current window)
            //Header: chrome is being controlled by automated test software
            sleep(1);
            robot.mouseMove(X1, Y1 + 120);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            sleep(1);
            robot.mouseMove(X2, Y2 + 120);
            sleep(1);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    /**
     * Drag an object and drop it to an offset location.
     *
     * @param fromElement represent the drag-able element
     * @param X           x offset
     * @param Y           y offset
     * @return true/false
     */
    @Step("Drag from element {0} to X={1}, Y={2}")
    public static boolean dragAndDropToOffset(By fromElement, int X, int Y) {
        smartWait();

        try {
            Robot robot = new Robot();
            robot.mouseMove(0, 0);
            int X1 = getWebElement(fromElement).getLocation().getX() + (getWebElement(fromElement).getSize().getWidth() / 2);
            int Y1 = getWebElement(fromElement).getLocation().getY() + (getWebElement(fromElement).getSize().getHeight() / 2);
            System.out.println(X1 + " , " + Y1);
            sleep(1);

            //This place takes the current coordinates plus 120px which is the browser header (1920x1080 current window)
            //Header: chrome is being controlled by automated test software
            robot.mouseMove(X1, Y1 + 120);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            sleep(1);
            robot.mouseMove(X, Y + 120);
            sleep(1);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    /**
     * Move to the given element.
     *
     * @param toElement Represent a web element as the By object
     * @return true/false
     */
    @Step("Move to element {0}")
    public static boolean moveToElement(By toElement) {
        smartWait();

        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(toElement)).release(getWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    /**
     * Move to an offset location.
     *
     * @param X x offset
     * @param Y y offset
     * @return true/false
     */
    @Step("Move to offset X={0}, Y={1}")
    public static boolean moveToOffset(int X, int Y) {
        smartWait();

        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveByOffset(X, Y).build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    /**
     * Reload the current web page.
     */
    @Step("Reload page")
    public static void reloadPage() {
        smartWait();

        DriverManager.getDriver().navigate().refresh();
        waitForPageLoaded();
        LogUtils.info("Reloaded page " + DriverManager.getDriver().getCurrentUrl());
    }


    /**
     * Fills the border color of the specified element.
     *
     * @param by passes the element object in the form By
     * @return Colors red borders for Elements on the website
     */
    @Step("Highlight on element")
    public static WebElement highLightElement(By by) {
        smartWait();

        // draw a border around the found element
        if (DriverManager.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid fuchsia'", waitForElementVisible(by));
            //sleep(1);
            LogUtils.info("Highlight element " + by);
        }
        return getWebElement(by);
    }

    /**
     * Navigate to the specified URL.
     *
     * @param URL the specified url
     */
    @Step("Open website with URL {0}")
    public static void openWebsite(String URL) {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);

        DriverManager.getDriver().get(URL);
        waitForPageLoaded();

        LogUtils.info("Open website with URL: " + URL);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Open website with URL: " + URL);
        }
        AllureManager.saveTextLog("Open website with URL: " + URL);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Navigate to the specified web page.
     *
     * @param URL the specified url
     */
    @Step("Navigate to URL {0}")
    public static void navigateToUrl(String URL) {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
        DriverManager.getDriver().navigate().to(URL);
        waitForPageLoaded();

        LogUtils.info("\uD83C\uDF10 Navigate to URL: " + URL);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Navigate to URL: " + URL);
        }

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Set the value of an input field
     *
     * @param by    an element of object type By
     * @param value the value to fill in the text box
     */
    @Step("Set text on text box")
    public static void setText(By by, String value) {
        smartWait();
        waitForElementVisible(by).sendKeys(value);
        LogUtils.info("Set text " + value + " on " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Set text " + value + " on " + by);
        }
        AllureManager.saveTextLog("Set text " + value + " on " + by);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Set the value of an input field and press the key on the keyboard
     *
     * @param by    an element of object type By
     * @param value the value to fill in the text box
     * @param keys  key on the keyboard to press
     */
    @Step("Set text on text box and press key")
    public static void setText(By by, String value, Keys keys) {
        smartWait();
        waitForElementVisible(by).sendKeys(value, keys);
        LogUtils.info("Set text " + value + " on " + by + " and press key " + keys.name());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Set text " + value + " on " + by + " and press key " + keys.name());
        }
        AllureManager.saveTextLog("Set text " + value + " on " + by + " and press key " + keys.name());

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key.
     *
     * @param by   an element of object type By
     * @param keys key on the keyboard to press
     */
    @Step("Set text on text box and press key")
    public static void sendKeys(By by, Keys keys) {
        smartWait();
        waitForElementVisible(by).sendKeys(keys);
        LogUtils.info("Press key " + keys.name() + " on element " + by);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Press key " + keys.name() + " on element " + by);
        }
        AllureManager.saveTextLog("Press key " + keys.name() + " on element " + by);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
    }

    /**
     * Simulates keystroke events at the current position, as though you typed the value key-by-key.
     *
     * @param keys key on the keyboard to press
     */
    @Step("Press key on keyboard")
    public static void sendKeys(Keys keys) {
        smartWait();
        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(keys);
        LogUtils.info("Press key " + keys.name() + " on keyboard");

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Press key " + keys.name() + " on keyboard");
        }
        AllureManager.saveTextLog("Press key " + keys.name() + " on keyboard");

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
    }

    /**
     * Clear all text of the element.
     *
     * @param by an element of object type By
     */
    @Step("Clear text in text box")
    public static void clearText(By by) {
        smartWait();
        waitForElementVisible(by).clear();
        LogUtils.info("Clear text in textbox " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Clear text in textbox " + by);
        }
        AllureManager.saveTextLog("Clear text in textbox");
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Clear all text of the element with press Ctrl A > Delete
     *
     * @param by an element of object type By
     */
    @Step("Clear text in text box with Ctrl A")
    public static void clearTextCtrlA(By by) {
        smartWait();
        waitForElementVisible(by);
        Actions actions = new Actions(DriverManager.getDriver());
        sleep(1);
        actions.click(getWebElement(by)).build().perform();
        //actions.moveToElement(getWebElement(by)).click().build();
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build().perform();
        sleep(1);
        actions.sendKeys(Keys.DELETE).build().perform();

        LogUtils.info("Clear text in textbox " + by.toString());
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Clear text in textbox " + by);
        }
        AllureManager.saveTextLog("Clear text in textbox");
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Clear all text of the element then set the text on that element.
     *
     * @param by    an element of object type By
     * @param value the value to fill in the text box
     */
    @Step("Clear and Fill text on text box")
    public static void clearAndFillText(By by, String value) {
        smartWait();
        waitForElementVisible(by);
        getWebElement(by).clear();
        getWebElement(by).sendKeys(value);
        LogUtils.info("Clear and Fill " + value + " on " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Clear and Fill " + value + " on " + by);
        }
        AllureManager.saveTextLog("Clear and Fill " + value + " on " + by);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Click on the specified element.
     *
     * @param by an element of object type By
     */
    @Step("Click on the element {0}")
    public static void clickElement(By by) {
        smartWait();
        waitForElementClickable(by).click();
        LogUtils.info("Clicked on the element " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Clicked on the element " + by);
        }
        AllureManager.saveTextLog("Clicked on the element " + by);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Click on element with timeout
     *
     * @param by an element of object type By
     */
    @Step("Click on the element {0} with timeout {1}s")
    public static void clickElement(By by, int timeout) {
        smartWait();
        waitForElementClickable(by, timeout).click();
        LogUtils.info("Clicked on the element " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Clicked on the element " + by);
        }
        AllureManager.saveTextLog("Clicked on the element " + by);

        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Click on Elements on the web with Javascript (click implicitly without fear of being hidden)
     *
     * @param by an element of object type By
     */
    @Step("Click on the element by Javascript {0}")
    public static void clickElementWithJs(By by) {
        smartWait();
        waitForElementPresent(by);
        //Scroll to element với Javascript Executor
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(false);", getWebElement(by));
        //Click with JS
        js.executeScript("arguments[0].click();", getWebElement(by));

        LogUtils.info("Click on element with JS: " + by);
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Click on element with JS: " + by);
        }
        AllureManager.saveTextLog("Click on element with JS: " + by);
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Click on the link on website with text
     *
     * @param linkText is the visible text of a link
     */
    @Step("Click on the link text {0}")
    public static void clickLinkText(String linkText) {
        smartWait();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
        elementWaited.click();

        LogUtils.info("Click on link text " + linkText);
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Click on link text " + linkText);
        }
        AllureManager.saveTextLog("Click on link text " + linkText);
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
    }

    /**
     * Right-click on the Element object on the web
     *
     * @param by an element of object type By
     */
    @Step("Right click on element {0}")
    public static void rightClickElement(By by) {
        smartWait();
        Actions action = new Actions(DriverManager.getDriver());
        action.contextClick(waitForElementVisible(by)).build().perform();
        LogUtils.info("Right click on element " + by);
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Right click on element " + by);
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Get text of an element
     *
     * @param by an element of object type By
     * @return text of a element
     */
    @Step("Get text of element {0}")
    public static String getTextElement(By by) {
        smartWait();
        AllureManager.saveTextLog("==> The Text is: " + waitForElementVisible(by).getText());
        return waitForElementVisible(by).getText().trim();
    }

    /**
     * Get the value from the element's attribute
     *
     * @param by            an element of object type By
     * @param attributeName attribute name
     * @return element's attribute value
     */
    @Step("Get attribute {1} of element {0}")
    public static String getAttributeElement(By by, String attributeName) {
        smartWait();
        return waitForElementVisible(by).getAttribute(attributeName);
    }

    /**
     * Get CSS value of an element
     *
     * @param by      Represent a web element as the By object
     * @param cssName is CSS attribute name
     * @return value of CSS attribute
     */
    @Step("Get CSS value {1} of element {0}")
    public static String getCssValueElement(By by, String cssName) {
        smartWait();
        return waitForElementVisible(by).getCssValue(cssName);
    }

    /**
     * Get size of specified element
     *
     * @param by Represent a web element as the By object
     * @return Dimension
     */
    @Step("Get Size of element {0}")
    public static Dimension getSizeElement(By by) {
        smartWait();
        return waitForElementVisible(by).getSize();
    }

    /**
     * Get location of specified element
     *
     * @param by Represent a web element as the By object
     * @return Point
     */
    @Step("Get Location of element {0}")
    public static Point getLocationElement(By by) {
        smartWait();
        return waitForElementVisible(by).getLocation();
    }

    /**
     * Get tag name (HTML tag) of specified element
     *
     * @param by Represent a web element as the By object
     * @return Tag name as String
     */
    @Step("Get Tag Name of element {0}")
    public static String getTagNameElement(By by) {
        smartWait();
        return waitForElementVisible(by).getTagName();
    }

    /**
     * Check the value of each column of the table when searching according to EQUAL conditions (equals)
     *
     * @param column column position
     * @param value  value to compare
     */
    @Step("Check data by EQUALS type after searching on the Table by Column.")
    public static void checkEqualsValueOnTableByColumn(int column, String value) {
        smartWait();
        sleep(1);
        List<WebElement> totalRows = getWebElements(By.xpath("//tbody/tr"));
        LogUtils.info("Number of results for keywords (" + value + "): " + totalRows.size());

        if (totalRows.size() < 1) {
            LogUtils.info("Not found value: " + value);
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                boolean res = false;
                WebElement title = waitForElementVisible(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
                res = title.getText().toUpperCase().equals(value.toUpperCase());
                LogUtils.info("Row " + i + ": " + res + " - " + title.getText());
                Assert.assertTrue(res, "Row " + i + " (" + title.getText() + ")" + " equals no value: " + value);
            }
        }
    }

    /**
     * Check the value of each column of the table when searching according to the CONTAINS condition (contains)
     *
     * @param column column position
     * @param value  value to compare
     */
    @Step("Check data by CONTAINS type after searching on the Table by Column.")
    public static void checkContainsValueOnTableByColumn(int column, String value) {
        smartWait();
        sleep(1);
        List<WebElement> totalRows = getWebElements(By.xpath("//tbody/tr"));
        LogUtils.info("Number of results for keywords (" + value + "): " + totalRows.size());

        if (totalRows.size() < 1) {
            LogUtils.info("Not found value: " + value);
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                boolean res = false;
                WebElement title = waitForElementVisible(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
                res = title.getText().toUpperCase().contains(value.toUpperCase());
                LogUtils.info("Row " + i + ": " + res + " - " + title.getText());
                Assert.assertTrue(res, "Row " + i + " (" + title.getText() + ")" + " contains no value: " + value);
            }
        }
    }

    /**
     * Check the value of each column of the table when searching according to the CONTAINS condition with custom xpath
     *
     * @param column           column position
     * @param value            value to compare
     * @param xpathToTRtagname xpath value up to TR tag
     */
    @Step("Check data by CONTAINS type after searching on the Table by Column.")
    public static void checkContainsValueOnTableByColumn(int column, String value, String xpathToTRtagname) {
        smartWait();

        //xpathToTRtagname is locator from table to "tr" tagname of data section: //tbody/tr, //div[@id='example_wrapper']//tbody/tr, ...
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath(xpathToTRtagname));
        sleep(1);
        LogUtils.info("Number of results for keywords (" + value + "): " + totalRows.size());

        if (totalRows.size() < 1) {
            LogUtils.info("Not found value: " + value);
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                boolean res = false;
                WebElement title = waitForElementVisible(By.xpath(xpathToTRtagname + "[" + i + "]/td[" + column + "]"));
                res = title.getText().toUpperCase().contains(value.toUpperCase());
                LogUtils.info("Row " + i + ": " + res + " - " + title.getText());
                Assert.assertTrue(res, "Row " + i + " (" + title.getText() + ")" + " contains no value " + value);
            }
        }
    }

//    /**
//     * Get the value of a column from the table
//     *
//     * @param column column position
//     * @return array of values of a column
//     */
//    public static ArrayList getValueTableByColumn(int column) {
//        smartWait();
//
//        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
//        sleep(1);
//        LogUtils.info("Number of results for column (" + column + "): " + totalRows.size()); //Không thích ghi log thì xóa nhen
//
//        ArrayList arrayList = new ArrayList<String>();
//
//        if (totalRows.size() < 1) {
//            LogUtils.info("Not found value !!");
//        } else {
//            for (int i = 1; i <= totalRows.size(); i++) {
//                boolean res = false;
//                WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
//                arrayList.add(title.getText());
//                LogUtils.info("Row " + i + ":" + title.getText()); //Không thích ghi log thì xóa nhen
//            }
//        }
//
//        return arrayList;
//    }

    /**
     * Get the value of a column from the table
     *
     * @param column column position
     * @return array of values of a column
     */
    public static ArrayList<String> getValueTableByColumn(int column) {
        smartWait();

        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        sleep(1);
        LogUtils.info("Number of results for column (" + column + "): " + totalRows.size());

        ArrayList<String> arrayList = new ArrayList<>(); // Khai báo rõ ràng kiểu dữ liệu là String

        if (totalRows.size() < 1) {
            LogUtils.info("Not found value !!");
        } else {
            for (int i = 1; i <= totalRows.size(); i++) {
                WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
                arrayList.add(title.getText());
                LogUtils.info("Row " + i + ":" + title.getText());
            }
        }

        return arrayList;
    }





    //Wait Element

    /**
     * Wait until the given web element is visible within the timeout.
     *
     * @param by      an element of object type By
     * @param timeOut maximum timeout as second
     * @return a WebElement object ready to be visible
     */
    public static WebElement waitForElementVisible(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut - 10));

            boolean check = isElementVisible(by, 10);
            if (check) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else {
                scrollToElementAtTop(by);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            }
        } catch (Throwable error) {
            LogUtils.error("❌ Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("❌ Timeout waiting for the element Visible. " + by.toString());
        }
        return null;
    }

    /**
     * Wait until the given web element is visible.
     *
     * @param by an element of object type By
     * @return a WebElement object ready to be visible
     */
    public static WebElement waitForElementVisible(By by) {
        waitForElementPresent(by);

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT - 10));
            boolean check = isElementVisible(by, 10);
            if (check) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else {
                scrollToElementAtBottom(by);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            }
        } catch (Throwable error) {
            LogUtils.error("❌ Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("❌ Timeout waiting for the element Visible. " + by.toString());
        }
        return null;
    }

    /**
     * Wait for the given element to be clickable within the given time (in seconds).
     *
     * @param by      an element of object type By
     * @param timeOut maximum timeout as seconds
     * @return a WebElement object ready to CLICK
     */
    public static WebElement waitForElementClickable(By by, long timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Throwable error) {
            LogUtils.error("❌ Timeout waiting for the element ready to click. " + by.toString());
            Assert.fail("❌ Timeout waiting for the element ready to click. " + by.toString());
        }
        return null;
    }

    /**
     * Wait for the given element to be clickable.
     *
     * @param by an element of object type By
     * @return a WebElement object ready to CLICK
     */
    public static WebElement waitForElementClickable(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Throwable error) {
            LogUtils.error("❌ Timeout waiting for the element ready to click. " + by.toString());
            Assert.fail("❌ Timeout waiting for the element ready to click. " + by.toString());
        }
        return null;
    }

    /**
     * Wait for the given element to present within the given time (in seconds).
     *
     * @param by      an element of object type By
     * @param timeOut maximum timeout as seconds
     * @return an existing WebElement object
     */
    public static WebElement waitForElementPresent(By by, long timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("❌ Timeout waiting for the element to exist. " + by.toString());
            Assert.fail("❌ Timeout waiting for the element to exist. " + by.toString());
        }

        return null;
    }

    /**
     * Wait for the given element to present
     *
     * @param by an element of object type By
     * @return an existing WebElement object
     */
    public static WebElement waitForElementPresent(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("❌ Element not exist. (waitForElementPresent) " + by.toString());
            Assert.fail("❌ Element not exist. (waitForElementPresent) " + by.toString());
        }
        return null;
    }

    /**
     * Wait for an alert to present.
     */
    public static boolean waitForAlertPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            LogUtils.error("❌ Alert NOT present.");
            Assert.fail("❌ Alert NOT present.");
            return false;
        }
    }

    /**
     * Wait for an alert to present.
     *
     * @param timeOut Timeout waiting for an alert to present.
     */
    public static boolean waitForAlertPresent(int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            LogUtils.error("Alert NOT present.");
            Assert.fail("Alert NOT present.");
            return false;
        }
    }

    /**
     * Wait until the given web element has an attribute with the specified name.
     *
     * @param by            an element of object type By
     * @param attributeName The name of the attribute to wait for.
     * @return true/false
     */
    public static boolean waitForElementHasAttribute(By by, String attributeName) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(by), attributeName));
        } catch (Throwable error) {
            LogUtils.error("Timeout for element " + by.toString() + " to exist attribute: " + attributeName);
            Assert.fail("Timeout for element " + by.toString() + " to exist attribute: " + attributeName);
        }
        return false;
    }

    /**
     * Verify if the web element has an attribute with the specified name and value.
     *
     * @param by             an element of object type By
     * @param attributeName  The name of the attribute to wait for.
     * @param attributeValue The value of attribute
     * @return true/false
     */
    @Step("Verify element {0} with attribute {1} has value is {2}")
    public static boolean verifyElementAttributeValue(By by, String attributeName, String attributeValue) {
        smartWait();

        waitForElementVisible(by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.attributeToBe(by, attributeName, attributeValue));
            return true;
        } catch (Throwable error) {
            LogUtils.error("Element: " + by.toString() + ". Not found Attribute value: " + attributeValue + " with attribute name: " + attributeName + ". Actual get Attribute value is: " + getAttributeElement(by, attributeName));
            Assert.fail("Element: " + by.toString() + ". Not found Attribute value: " + attributeValue + " with attribute name: " + attributeName + ". Actual get Attribute value is: " + getAttributeElement(by, attributeName));
            return false;
        }
    }

    /**
     * Verify if the web element has an attribute with the specified name.
     *
     * @param by            Represent a web element.
     * @param attributeName The name of the attribute to wait for.
     * @param timeOut       System will wait at most timeout (seconds) to return result
     * @return true/false
     */
    @Step("Verify element {0} has attribute {1} with timeout {2} second")
    public static boolean verifyElementHasAttribute(By by, String attributeName, int timeOut) {
        smartWait();
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(by), attributeName));
            return true;
        } catch (Throwable error) {
            LogUtils.error("Not found Attribute " + attributeName + " of element " + by.toString());
            Assert.fail("Not found Attribute " + attributeName + " of element " + by.toString());
            return false;
        }
    }


    /**
     * Wait for a page to load with the default time from the config
     */
    public static void waitForPageLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_PAGE_LOADED));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            LogUtils.info("Javascript in NOT Ready!");
            //Wait for Javascript to load
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                LogUtils.error("Timeout waiting for page load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
                Assert.fail("Timeout waiting for page load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
            }
        }
    }

    /**
     * Wait for a page to load within the given time (in seconds)
     */
    public static void waitForPageLoaded(int timeOut) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            LogUtils.info("Javascript in NOT Ready!");
            //Wait for Javascript to load
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                LogUtils.error("Timeout waiting for page load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
                Assert.fail("Timeout waiting for page load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
            }
        }
    }

    /**
     * Wait for JQuery to finish loading with default time from config
     */
    public static void waitForJQueryLoad() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_PAGE_LOADED), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        //Wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            assert driver != null;
            return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
        };

        //Get JQuery is Ready
        boolean jqueryReady = (Boolean) js.executeScript("return jQuery.active==0");

        //Wait JQuery until it is Ready!
        if (!jqueryReady) {
            LogUtils.info("JQuery is NOT Ready!");
            try {
                //Wait for jQuery to load
                wait.until(jQueryLoad);
            } catch (Throwable error) {
                LogUtils.error("Timeout waiting for JQuery load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
                Assert.fail("Timeout waiting for JQuery load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
            }
        }
    }

    /**
     * Wait for Angular to finish loading with default time from config
     */
    public static void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_PAGE_LOADED), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> {
            assert driver != null;
            return Boolean.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());
        };

        //Get Angular is Ready
        boolean angularReady = Boolean.parseBoolean(js.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if (!angularReady) {
            LogUtils.warn("Angular is NOT Ready!");
            //Wait for Angular to load
            try {
                //Wait for jQuery to load
                wait.until(angularLoad);
            } catch (Throwable error) {
                LogUtils.error("Timeout waiting for Angular load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
                Assert.fail("Timeout waiting for Angular load. (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
            }
        }

    }

}