package com.anhtester.projects.cms.users.pages.order;

import com.anhtester.helpers.PropertiesHelpers;
import com.anhtester.keywords.WebUI;
import com.anhtester.projects.cms.CommonPageCMS;
import com.anhtester.projects.cms.users.pages.dashboard.DashboardPage;
import com.anhtester.projects.cms.users.pages.products.ProductInfoPageCMS;
import com.anhtester.utils.LogUtils;
import org.openqa.selenium.By;

public class OrderPage extends CommonPageCMS {

    private By buttonAddToCart = By.xpath("//button[@class='btn btn-soft-primary mr-2 add-to-cart fw-600']");
    private By popupAddToCartSucceeded = By.xpath("//h3[normalize-space()='Item added to your cart!']");
    private By closeSuccessAddToCartPopup = By.xpath("//span[@class = 'la-2x']");
    private By buttonBackToShopping = By.xpath("//button[normalize-space()='Back to shopping']");
    private By buttonPlus = By.xpath("//button[contains(@data-type,'plus')]");
    private By buttonCart = By.xpath("//i[@class='la la-shopping-cart la-2x opacity-80']");
    private By buttonViewCart = By.xpath("//a[normalize-space()='View cart']");
    private By buttonCheckout = By.xpath("//a[normalize-space()='Checkout']");
    private By buttonContinueToShipping = By.xpath("//button[normalize-space()='Continue to Shipping']");
    private By buttonAddNewAddress = By.xpath("//div[@class='border p-3 rounded mb-3 c-pointer text-center bg-white h-100 d-flex flex-column justify-content-center']");
    private By verifyCheckedAddress = By.xpath("(//input[@name = 'address_id' ])[2]");
    private By buttonProcessToCheckout = By.xpath("//a[normalize-space()='Proceed to Checkout']");
    private By buttonContinueToDeliveryInfo = By.xpath("//button[normalize-space()='Continue to Delivery Info']");
    private By buttonContinueToPayment = By.xpath("//button[normalize-space()='Continue to Payment']");
    private By textboxOrderInfo = By.xpath("//textarea[@placeholder='Type your text']");

    private By checkboxAgreeTermAndConditions = By.xpath("//span[@class='aiz-square-check']");
    private By buttonCompleteOrder = By.xpath("//button[normalize-space()='Complete Order']");
    private By messageOrderSuccess = By.xpath("//h1[normalize-space()='Thank You for Your Order!']");
    private By quantityProduct = By.xpath("//input[@name='quantity']");
    private By titleNewAddress = By.xpath("//div[@id='new-address-modal']//h5[@id='exampleModalLabel']");
    private By inputYourAddress = By.xpath("//textarea[@placeholder='Your Address']");
    private By selectCountry = By.xpath("//div[contains(text(),'Select your country')]");
    private By buttonSelectAddress = By.xpath("//span[@class='aiz-rounded-check flex-shrink-0 mt-1']");
    private By quantity = By.xpath("//input[@name='quantity']");
    private By paymentPage = By.xpath("//h3[normalize-space()='Any additional info?']");
    private By totalPrice = By.xpath("//tr[@class = 'cart-subtotal']//td[@class = 'text-right']//span");
    private By subTotal = By.xpath("//div[@id='cart_items']//span[normalize-space()='Subtotal']/following-sibling::span");
    private By labelOrderCode = By.xpath("//*[contains(text(), 'Order Code:')]/span");


    public void order(String noteForOrder) {
        WebUI.waitForPageLoaded();
        WebUI.setText(DashboardPage.inputSearchProduct, PropertiesHelpers.getValue("product_P01"));
        WebUI.waitForJQueryLoad();
        WebUI.sleep(3);
        WebUI.clickElement(By.xpath("//div[@id='search-content']//div[contains(text(),'" + PropertiesHelpers.getValue("product_P01") + "')]"));
        WebUI.waitForPageLoaded();
        String priceProduct01AsString = WebUI.getTextElement(ProductInfoPageCMS.productPrice).trim();
        WebUI.scrollToElementAtBottom(buttonAddToCart);
        WebUI.clickElement(buttonAddToCart);
        WebUI.waitForPageLoaded();
        WebUI.verifyElementVisible(popupAddToCartSucceeded, "Add to cart is failed. ");
        WebUI.clickElement(buttonBackToShopping);
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
        WebUI.setText(DashboardPage.inputSearchProduct, PropertiesHelpers.getValue("product_P02"));
        WebUI.waitForJQueryLoad();
        WebUI.sleep(3);
        WebUI.clickElement(By.xpath("//div[@id='search-content']//div[contains(text(),'" + PropertiesHelpers.getValue("product_P02") + "')]"));
        WebUI.waitForPageLoaded();
        String priceProduct02AsString = WebUI.getTextElement(ProductInfoPageCMS.productPrice).trim();
        WebUI.clickElement(buttonPlus);
        WebUI.sleep(1);
        WebUI.waitForPageLoaded();
        String quantities = WebUI.getAttributeElement(quantity, "value").trim();
        LogUtils.info("Quantity Product 02: " + quantities);
        WebUI.verifyEquals(WebUI.getAttributeElement(quantity, "value"), "2", "number of failed products");
        WebUI.scrollToElementAtBottom(buttonAddToCart);
        WebUI.clickElement(buttonAddToCart);
        WebUI.verifyElementVisible(popupAddToCartSucceeded, "Add to cart is failed");
        WebUI.sleep(2);
        WebUI.waitForPageLoaded();
        WebUI.clickElement(closeSuccessAddToCartPopup);
        WebUI.waitForPageLoaded();
        WebUI.clickElement(buttonCart);
        WebUI.sleep(2);
        WebUI.verifyElementVisible(buttonCheckout, "My product is NOT displayed");
        WebUI.clickElement(buttonCheckout);
        WebUI.clickElement(buttonSelectAddress);
        WebUI.clickElement(buttonContinueToDeliveryInfo);
        WebUI.scrollToElementAtBottom(buttonContinueToPayment);
        WebUI.clickElement(buttonContinueToPayment);
        WebUI.verifyElementVisible(paymentPage, "Step Payment is NOT displayed");
        WebUI.setText(textboxOrderInfo, noteForOrder);
        WebUI.scrollToElementAtBottom(checkboxAgreeTermAndConditions);
        WebUI.clickElement(checkboxAgreeTermAndConditions);
        WebUI.waitForPageLoaded();
        int priceProduct01AsInt = Integer.parseInt(priceProduct01AsString.replace("$", "").replace(",", "").split("\\.")[0]);
        int quantityProduct02 = Integer.parseInt(quantities);
        int priceProduct02AsInt = (Integer.parseInt(priceProduct02AsString.replace("$", "").replace(",", "").split("\\.")[0])) * quantityProduct02;
        int sumPrice = priceProduct01AsInt + priceProduct02AsInt;
        int subTotal = Integer.parseInt(WebUI.getTextElement(totalPrice).replace("$", "").replace(",", "").split("\\.")[0]);
        System.out.println("quantityProduct02: " + quantityProduct02);
        System.out.println("priceProduct01AsInt: " + priceProduct01AsInt);
        System.out.println("priceProduct02AsInt: " + priceProduct02AsInt);
        System.out.println("sumPrice: " + sumPrice);
        System.out.println("subTotal: " + subTotal);
        WebUI.sleep(2);
        WebUI.verifyEquals(sumPrice, subTotal, "The total price is failed");
        WebUI.clickElement(buttonCompleteOrder);
        WebUI.verifyElementVisible(messageOrderSuccess, "Order is failed");
        LogUtils.info(WebUI.getTextElement(labelOrderCode));
        WebUI.sleep(2);
    }

}
