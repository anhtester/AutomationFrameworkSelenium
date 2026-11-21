/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.projects.demo;

import com.anhtester.common.BaseTest;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.driver.DriverManager;
import com.anhtester.helpers.SystemHelpers;
import com.anhtester.keywords.WebUI;
import com.anhtester.utils.LocalStorageUtils;
import com.anhtester.utils.LogUtils;
import com.anhtester.utils.ObjectUtils;
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

public class TestHandle {

    private WebDriver driver;

    @BeforeMethod
    public void setupDriver() {
        driver = new BaseTest().createBrowser("chrome"); //Initialization method 1
        // new BaseTest().createDriver("chrome"); //Initialization method 2

        driver.manage().window().maximize();
    }

    @AfterMethod
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testDownloadFileWithJS() {
        // Start from an empty download folder (exports/Downloads) so the file name is not "ExportCSV (1).csv"
        WebUI.deleteAllFileInDownloadDirectory();
        WebUI.openWebsite("https://www.onlinedatagenerator.com/home/demo");
        WebUI.waitForPageLoaded();
        WebUI.clickElementWithJs(By.xpath("//button[normalize-space()='Export']"));
        WebUI.waitForPageLoaded();
        WebUI.waitForJQueryLoad();
        WebUI.sleep(2);
        Assert.assertTrue(WebUI.verifyFileDownloadedWithJS_Contains("ExportCSV"), "Download failed. File not match.");
    }

    @Test
    public void testDownloadFileWithJava() {
        // Start from an empty download folder (exports/Downloads) so the file name is not "ExportCSV (1).csv"
        WebUI.deleteAllFileInDownloadDirectory();
        LogUtils.info(WebUI.countFilesInDownloadDirectory());
        WebUI.openWebsite("https://www.onlinedatagenerator.com/home/demo");
        WebUI.waitForPageLoaded();
        WebUI.clickElementWithJs(By.xpath("//button[normalize-space()='Export']"));
        WebUI.waitForPageLoaded();
        WebUI.waitForJQueryLoad();
        WebUI.sleep(2);
        LogUtils.info(WebUI.countFilesInDownloadDirectory());
        //File name is ExportCSV.csv
        Assert.assertTrue(WebUI.verifyDownloadFileEqualsName("ExportCSV.csv", 5), "Download failed. File not found.");
    }

    @Test
    public void testConvertWebElementToBy() {
        WebUI.openWebsite(FrameworkConstants.URL_CMS_USER + "/users/login");
        WebUI.waitForPageLoaded();

        //WebElement
        WebElement emailElement = WebUI.getWebElement(By.xpath("//input[@id='email']"));

        //Convert WebElement to By
        By emailBy = ObjectUtils.getByFromWebElement(emailElement);
        LogUtils.info("Converted WebElement to By: " + emailBy);

        WebUI.clearAndFillText(emailBy, "customer@example.com");
        Assert.assertEquals(WebUI.getAttributeElement(emailBy, "value"), "customer@example.com");
    }

    @Test
    public void testLocalStorage() {
        WebUI.openWebsite(FrameworkConstants.URL_CMS_USER + "/users/login");
        WebUI.waitForPageLoaded();

        //Set key=value in Login page
        LocalStorageUtils.setItem("email", "customer@example.com");
        LocalStorageUtils.setItem("note", "Saved in Local Storage");

        //Get data from Local Storage was set above
        By inputEmail = By.xpath("//input[@id='email']");
        WebUI.clearAndFillText(inputEmail, LocalStorageUtils.getItem("email"));
        Assert.assertEquals(WebUI.getAttributeElement(inputEmail, "value"), "customer@example.com");

        //Local Storage is kept when the page is reloaded (same domain)
        WebUI.reloadPage();
        Assert.assertEquals(LocalStorageUtils.getItem("note"), "Saved in Local Storage");

        //=> You can get value by key everywhere on the same domain before closing the browser
    }

    @Test
    public void handleHTML5ValidationMessage() {
        WebUI.openWebsite("https://anhtester.com/login");
        WebUI.waitForPageLoaded();

        By button_Login = By.xpath("//button[@data-testid='login-submit']");
        By input_Email = By.xpath("//input[@data-testid='login-email-input']");
        WebUI.clickElement(button_Login);
        WebUI.sleep(2);

        WebUI.logConsole("Verify required field: " + WebUI.verifyHTML5RequiredField(input_Email));

        WebUI.sleep(2);

        WebUI.logConsole("Message from field: " + WebUI.getHTML5MessageField(input_Email));
        Assert.assertEquals("Please fill out this field.", WebUI.getHTML5MessageField(input_Email));

        WebUI.setText(input_Email, "abc@ ");
        WebUI.sleep(2);
        WebUI.logConsole("Verify valid value: " + WebUI.verifyHTML5ValidValueField(input_Email));
        WebUI.logConsole("Message from field: " + WebUI.getHTML5MessageField(input_Email));

        WebUI.sleep(1);
    }

    @Test
    public void handleSetWindow() {
        WebUI.openWebsite("https://anhtester.com");
        WebUI.waitForPageLoaded();
        WebUI.setWindowSize(1000, 600);
        WebUI.sleep(2);
        WebUI.setWindowPosition(100, 100);
        WebUI.sleep(1);
    }

    @Test
    public void handleScreenshotElement() {
        WebUI.openWebsite("https://anhtester.com/login");
        WebUI.waitForPageLoaded();
        WebUI.screenshotElement(By.xpath("//form[.//button[@data-testid='login-submit']]"), "Login_Form");
    }

    @Test
    public void testUploadFileFormDialog() {
        WebUI.openWebsite("https://files.fm/");
        WebUI.waitForPageLoaded();

        By textOnPage = By.xpath("//div[@id='file_select_dragndrop_text']");
        By divFileUpload = By.xpath("//div[@id='uploadifive-file_upload']");
        By inputFileUpload = By.xpath("//div[@id='file_select_button']//input[@id='file_upload']");

        String filePath = SystemHelpers.getCurrentDir() + "src/test/resources/testdata/TxtFileData.txt";

        WebUI.uploadFileWithLocalForm(divFileUpload, filePath);

        WebUI.sleep(4);
    }

    //Phân trang và check data in table
    @Test
    public void checkDataTableWithPagination() {
        WebUI.openWebsite("https://datatables.net/");
        WebUI.waitForPageLoaded();

        By title_H1 = By.xpath("//h1[@class='page_title']");

        WebUI.scrollToElementAtTop(title_H1);

        By button_Next = By.xpath("//div[@id='myTable_wrapper']//button[contains(@class,'dt-paging-button') and contains(@class,'next')]");
        By label_Info_PageTotal = By.xpath("//div[@id='myTable_info']");
        String info = driver.findElement(label_Info_PageTotal).getText(); //Showing 1 to 10 of 57 staff members
        LogUtils.info("Chuỗi chứa số item: " + info);

        //I separate the above string with a space character and then get the 5th element starting from 0
        //To get the total number of Items
        ArrayList<String> arrayListString = new ArrayList<>();
        for (String s : info.split(" ")) {
            arrayListString.add(s);
        }

        //Showing 1 to 10 of 57 staff members => Get 57 number
        int itemTotal = Integer.parseInt(arrayListString.get(5));
        LogUtils.info("Tổng số item: " + itemTotal);

        int itemTotalOnePage = 10; //default as sample. Use your system to change accordingly
        //Or you can also get the default automatic number. There are many additional variations.
        LogUtils.info("Số item trên 1 trang: " + itemTotalOnePage);

        double pageTotal = (double) itemTotal / (double) itemTotalOnePage;

        DecimalFormat df = new DecimalFormat("#"); //Round the number to the decimal point
        //For example, 5.7 is rounded to 6 like that
        int pageTotalInt = Integer.parseInt(df.format(pageTotal));
        LogUtils.info("Tổng số trang: " + pageTotalInt);

        //This FOR runs to < pageTotalInt so it doesn't click the last time
        //For example, for 6 pages, it only clicks 5 times, right =))
        for (int i = 1; i <= pageTotalInt; i++) {
            WebUI.scrollToElementAtTop(title_H1);
            //Call the function Check data in table by column from keywords WebUI
            WebUI.checkContainsValueOnTableByColumn(1, "", "//div[@id='myTable_wrapper']//tbody/tr");
            WebUI.sleep(1);
            //Click Next
            if (i != pageTotalInt) {
                driver.findElement(button_Next).click();
            }
        }

        WebUI.scrollToElementAtTop(title_H1);
        WebUI.sleep(2);

    }

    @Test
    public void handleZoomInZoomOut() {
        WebUI.openWebsite("https://anhtester.com");
        WebUI.sleep(1);
        //driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL,Keys.ADD));
        //driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL,Keys.SUBTRACT));
        WebUI.getJsExecutor().executeScript("document.body.style.zoom = '80%';");
        WebUI.sleep(1);
    }

    @Test
    public void handleNotificationsBrowser() {
        WebDriver driver = new ChromeDriver(WebUI.notificationsBlock()); //
        driver.manage().window().maximize();
        WebUI.openWebsite("https://oto.com.vn/mua-ban-xe");
        WebUI.sleep(4);
        driver.quit();
    }

    @Test
    public void handleDragAndDrop() {
        WebUI.openWebsite("http://demo.guru99.com/test/drag_drop.html");
        By fromElement = By.xpath("//a[normalize-space()='BANK']");
        By toElement = By.xpath("(//div[@id='shoppingCart1']//div)[1]");

        //WebUI.switchToFrameByElement(toElement);
        //WebUI.scrollToElement(toElement);
        WebUI.dragAndDrop(fromElement, toElement);
        WebUI.sleep(1);
    }

    @Test
    public void handleDragAndDropHTML5() {
        WebUI.openWebsite("https://david-desmaisons.github.io/draggable-example/");
        WebUI.waitForPageLoaded();

        By fromElement = By.xpath("(//li[@class='list-group-item'])[1]");
        By toElement = By.xpath("(//li[@class='list-group-item'])[2]");

        WebUI.dragAndDropHTML5(fromElement, toElement);

        WebUI.sleep(2);
    }

    @Test
    public void handleDragAndDropOffset() {
        WebUI.openWebsite("https://david-desmaisons.github.io/draggable-example/");
        WebUI.waitForPageLoaded();

        By fromElement = By.xpath("(//li[@class='list-group-item'])[1]");

        WebUI.dragAndDropToOffset(fromElement, 330, 600);

        WebUI.sleep(2);
    }

    @Test
    public void handleHighLightElement() {
        // app.hrsale.com is down: use the login page of anhtester.com
        WebUI.openWebsite("https://anhtester.com/login");
        By button = By.xpath("//button[@data-testid='login-submit']");
        WebUI.highLightElement(button); //Color the border red for Elements on the website
        WebUI.verifyElementAttributeValue(button, "type", "submit");
        WebUI.waitForElementClickable(button, 5);
        WebUI.sleep(2);
    }

    @Test
    public void handleUploadFile() {
        WebUI.openWebsite("https://demoqa.com/upload-download");
        WebUI.waitForPageLoaded();

        final String path1 = SystemHelpers.getCurrentDir() + "src/test/resources/testdata/DOCX_File_01.docx";

        //Cách 1 sendKeys link từ source (works in headless and on Selenium Grid)
        WebUI.uploadFileWithSendKeys(By.xpath("//input[@id='uploadFile']"), path1);
        WebUI.verifyElementTextContains(By.xpath("//p[@id='uploadedFilePath']"), "DOCX_File_01.docx");
    }

    @Test
    public void handleUploadFileWithLocalForm() {
        // Cách 2 mở form local máy (AWT Robot): needs a visible browser, skipped in headless mode
        WebUI.requireVisibleDesktop("handleUploadFileWithLocalForm");
        WebUI.openWebsite("https://demoqa.com/upload-download");
        WebUI.waitForPageLoaded();

        final String path2 = SystemHelpers.getCurrentDir() + "src/test/resources/testdata/LoginCSV.csv";
        WebUI.uploadFileWithLocalForm(By.xpath("//input[@id='uploadFile']"), path2);
        WebUI.verifyElementTextContains(By.xpath("//p[@id='uploadedFilePath']"), "LoginCSV.csv");
    }

    @Test
    public void handleTable() {
        WebUI.openWebsite("https://colorlib.com/polygon/notika/data-table.html");
        System.out.println(WebUI.getValueTableByColumn(2));
    }

    @Test
    public void handlePrintPopup() throws AWTException {
        // AWT Robot presses real keys: needs a visible browser, skipped in headless mode
        WebUI.requireVisibleDesktop("handlePrintPopup");
        // saleserpdemo.bdtask-demo.com is down: open the print dialog of any page with window.print()
        WebUI.openWebsite("https://anhtester.com");
        WebUI.waitForPageLoaded();
        String originalUrl = WebUI.getCurrentUrl();

        // window.print() blocks the page: call it asynchronously so WebDriver is not blocked
        WebUI.getJsExecutor().executeScript("setTimeout(function () { window.print(); }, 500);");
        WebUI.sleep(2);

        Robot robotClass = new Robot();
        //Press ESC to cancel the print dialog
        robotClass.keyPress(KeyEvent.VK_ESCAPE);
        robotClass.keyRelease(KeyEvent.VK_ESCAPE);
        WebUI.sleep(1);

        Assert.assertEquals(WebUI.getCurrentUrl(), originalUrl, "The page changed after closing the print dialog");
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

}
