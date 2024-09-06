package com.anhtester.projects.cms.admin.pages.products;

import com.anhtester.driver.DriverManager;
import com.anhtester.helpers.SystemHelpers;
import com.anhtester.keywords.WebUI;
import com.anhtester.projects.cms.CommonPageCMS;
import com.anhtester.utils.LogUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.Random;

public class AddProductPage extends CommonPageCMS {

    private static String nameProductVerify;
    private By menuProduct = By.xpath("//span[normalize-space()='Products']");
    private By submenuAddProduct = By.xpath("(//span[normalize-space()='Add New Product'])[1]");
    private By titleAddNewProduct = By.xpath("//h5[normalize-space()='Add New Product']");
    private By blockProductInf = By.xpath("//h5[normalize-space()='Product Information']");
    private By inputProductName = By.xpath("//input[@placeholder='Product Name']");
    private By selectCategory = By.xpath("//button[@data-id= 'category_id']");
    private By inputSearchCategory = By.xpath("//div[@class='dropdown-menu show']//input[@aria-label='Search']");
    private By inputUnit = By.xpath("//input[@placeholder='Unit (e.g. KG, Pc etc)']");
    private By inputWeight = By.xpath("//input[@name='weight']");
    private By inputTags = By.xpath("//span[@aria-placeholder='Type and hit enter to add a tag']");
    private By blockProductImages = By.xpath("//h5[normalize-space()='Product Images']");
    private By selectChooseGalleryImgs = By.xpath("//label[contains(text(),'Gallery Images')]/following-sibling::div//div[contains(text(),'Choose File')]");
    private By selectChooseThumbnailImgs = By.xpath("//label[contains(text(),'Thumbnail Image')]/following-sibling::div//div[contains(text(),'Choose File')]");
    private By uploadNewImageTab = By.xpath("//a[normalize-space()='Upload New']");
    private By buttonBrowseImages = By.xpath("//button[normalize-space()='Browse']");
    private By inputGalleryImages = By.xpath("//input[@class = 'uppy-Dashboard-input']");
    private By buttonAddFileImgs = By.xpath("//button[normalize-space()='Add Files']");
    private By selectFileTab = By.xpath("//a[normalize-space()='Select File']");
    private By selectGalleryImages = By.xpath("(//img[@class='img-fit'])[1]");
    private By inputSearchImg = By.xpath("//input[@placeholder='Search your files']");
    private By selectThumbnailImages = By.xpath("(//img[@class='img-fit'])[2]");
    private By blockProductPrice = By.xpath("//h5[normalize-space()='Product price + stock']");
    private By inputUnitPrice = By.xpath("//input[@placeholder='Unit price']");
    private By selectDate = By.xpath("//input[@placeholder='Select Date']");
    private By buttonSelectDiscountDate = By.xpath("//button[@class='applyBtn btn btn-sm btn-primary']");
    private By inputDiscount = By.xpath("//input[@placeholder='Discount']");
    private By selectUnitDiscount = By.xpath("(//div[@class='filter-option-inner-inner'][normalize-space()='Flat'])[1]");
    private By selectUnitDiscountPercent = By.xpath("//span[normalize-space()='Percent']");
    private By inputQuantity = By.xpath("//input[@placeholder='Quantity']");
    private By inputSKU = By.xpath("//input[@placeholder='SKU']");
    private By blockProductDescription = By.xpath("//h5[normalize-space()='Product Description']");
    private By inputMetaTitle = By.xpath("//input[@name='meta_title']");
    private By inputDescription = By.xpath("//div[@role='textbox']");
    private By selectChooseMetaImage = By.xpath("//label[normalize-space()='Meta Image']/following-sibling::div//div[contains(text(),'Choose File')]");
    private By buttonSavePublish = By.xpath("//button[normalize-space()='Save & Publish']");
    private By messageAddProductSuccess = By.xpath("//span[@data-notify='message']");
    private By allCategoriesTabUI = By.xpath("//a[normalize-space()='All categories']");
    private By unitUI = By.xpath("//span[@class='opacity-70']");
    private By descriptionUI = By.xpath("//div[@class = 'mw-100 overflow-auto text-left aiz-editor-data']//p");

    int randomNumber = new Random().nextInt(1000000);
    private By menuAllProducts = By.xpath("//span[normalize-space()='All products']");
    private By newProduct = By.xpath("(//span[@class='text-muted text-truncate-2'])[1]");
    private By inputSearchProduct = By.xpath("//input[@id='search']");
    private By buttonViewProduct = By.xpath("(//i[@class='las la-eye'])[1]");


    public void addProduct(String productName, String category, String unit, String weight, String tags, String unitPrice, String discountDate, String quantity, String description, String discount, String imgName) {
        productName = productName + " " + RandomStringUtils.randomAlphabetic(5);
        WebUI.waitForPageLoaded();
        WebUI.clickElement(menuProduct);
        WebUI.waitForPageLoaded();
        WebUI.clickElement(submenuAddProduct);
        WebUI.verifyElementVisible(titleAddNewProduct, "Title Add New Product is NOT displayed");
        WebUI.verifyElementVisible(blockProductInf, "Product Information block is not displayed");
        WebUI.setText(inputProductName, productName);
        WebUI.clickElement(selectCategory);
        WebUI.setText(inputSearchCategory, category, Keys.ENTER);
        WebUI.setText(inputUnit, unit);
        WebUI.setText(inputWeight, String.valueOf(weight));
        WebUI.setText(inputTags, tags);
        WebUI.verifyElementVisible(blockProductImages, "Product Images block is not displayed");
        WebUI.clickElement(selectChooseGalleryImgs);
        WebUI.clickElement(uploadNewImageTab);
        //Upload images Gallery with Form
        //WebUI.uploadFileWithLocalForm(buttonBrowseImages, SystemHelpers.getCurrentDir() + "src\\test\\resources\\testdataCMS\\" + imgName);
        //Upload images Gallery with sendKeys
        DriverManager.getDriver().findElement(inputGalleryImages).sendKeys(SystemHelpers.getCurrentDir() + "src\\test\\resources\\testdataCMS\\" + imgName);
        WebUI.clickElement(selectFileTab);
        LogUtils.info(imgName);
        LogUtils.info(SystemHelpers.splitString(imgName, "[.]"));
        String imageName = SystemHelpers.splitString(imgName, "[.]").get(0);
        //Search and select images
        WebUI.setText(inputSearchImg, imageName, Keys.ENTER);
        WebUI.waitForJQueryLoad();
        WebUI.sleep(2);
        WebUI.clickElementWithJs(selectGalleryImages);
        WebUI.clickElement(buttonAddFileImgs);
        WebUI.waitForPageLoaded();
        WebUI.clickElement(selectChooseThumbnailImgs);
        WebUI.setText(inputSearchImg, imageName, Keys.ENTER);
        WebUI.waitForJQueryLoad();
        WebUI.sleep(2);
        WebUI.clickElementWithJs(selectThumbnailImages);
        WebUI.clickElement(buttonAddFileImgs);
        WebUI.waitForPageLoaded();
        WebUI.verifyElementVisible(blockProductPrice, "Product price block is NOT displayed");
        WebUI.setText(inputUnitPrice, String.valueOf(unitPrice));
        WebUI.setText(selectDate, discountDate);
        WebUI.clickElement(buttonSelectDiscountDate);
        WebUI.setText(inputDiscount, String.valueOf(discount));
        WebUI.clickElement(selectUnitDiscount);
        WebUI.clickElement(selectUnitDiscountPercent);
        WebUI.setText(inputQuantity, String.valueOf(quantity));
        WebUI.setText(inputSKU, String.valueOf(randomNumber));
        WebUI.verifyElementVisible(blockProductDescription, "Product description is not displayed");
        WebUI.setText(inputMetaTitle, productName);
        WebUI.setText(inputDescription, description);
        WebUI.clickElement(selectChooseMetaImage);
        WebUI.setText(inputSearchImg, imageName, Keys.ENTER);
        WebUI.waitForJQueryLoad();
        WebUI.sleep(2);
        WebUI.clickElementWithJs(selectThumbnailImages);
        WebUI.clickElementWithJs(buttonAddFileImgs);
        //Click Save & Publish
        WebUI.waitForPageLoaded();
        WebUI.scrollToElementAtBottom(buttonSavePublish);
        WebUI.clickElement(buttonSavePublish);
        WebUI.verifyElementVisible(messageAddProductSuccess, "Add Product is failed");
        WebUI.clickElement(menuAllProducts);
        WebUI.waitForPageLoaded();
        WebUI.setText(inputSearchProduct, productName, Keys.ENTER);
        WebUI.waitForJQueryLoad();
        WebUI.sleep(2);
        WebUI.waitForElementVisible(newProduct);
        nameProductVerify = WebUI.getTextElement(newProduct);
    }

    public void verifyNewProduct(String category, String unit, Double unitPrice, String description) {
        WebUI.clickElement(buttonViewProduct);
        WebUI.waitForPageLoaded();
        WebUI.switchToWindowOrTabByTitle(nameProductVerify);
        getLoginPageCMS().clickCloseAdvertisementPopup();
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
        WebUI.verifyEquals(WebUI.getTextElement(By.xpath("//h1[normalize-space()='" + nameProductVerify + "']")).trim(), nameProductVerify, "Product name displayed wrong");
        WebUI.verifyEquals(WebUI.getTextElement(unitUI).trim(), "/" + unit, "Unit displayed wrong");
        WebUI.scrollToElementAtBottom(descriptionUI);
        WebUI.verifyEquals(WebUI.getTextElement(descriptionUI).trim(), description, "Description displayed wrong");
        WebUI.sleep(2);
        //Check Product in Category
        WebUI.clickElement(allCategoriesTabUI);
        WebUI.waitForPageLoaded();
        WebUI.clickElement(By.xpath("//a[contains(text(),'" + category + "')]"));
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
        WebUI.verifyElementVisible(By.xpath("(//a[normalize-space()='" + nameProductVerify + "'])"), "Product is NOT displayed in Category");
    }
}
