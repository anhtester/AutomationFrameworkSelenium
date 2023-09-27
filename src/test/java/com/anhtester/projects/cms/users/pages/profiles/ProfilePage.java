package com.anhtester.projects.cms.users.pages.profiles;

import com.anhtester.projects.cms.CommonPageCMS;
import com.anhtester.keywords.WebUI;
import com.anhtester.utils.DataGenerateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class ProfilePage extends CommonPageCMS {

    private By menuManageProfile = By.xpath("//div[@class='d-flex align-items-start']//span[@class='aiz-side-nav-text' and normalize-space()='Manage Profile']");
    private By titleManageProfile = By.xpath("//h1[normalize-space()='Manage Profile']");
    private By inputName = By.xpath("//input[@placeholder='Your name']");
    private By inputPhone = By.xpath("//input[@placeholder='Your Phone']");
    private By selectAvatar = By.xpath("//div[@class='form-control file-amount']");
    private By inputPassword = By.xpath("//input[@placeholder='New Password']");
    private By inputConfirmPassword = By.xpath("//input[@placeholder='Confirm Password']");
    private By buttonUpdateProfile = By.xpath("//button[normalize-space()='Update Profile']");

    private By messageNotPermitted = By.xpath("//span[@data-notify='message' and normalize-space() = 'Sorry! the action is not permitted in demo']");
    private By titleChangeEmail = By.xpath("//h5[normalize-space()='Change your email']");
    private By inputEmail = By.xpath("//input[@placeholder='Your Email']");
    private By buttonVerifyEmail = By.xpath("//button[@class='btn btn-outline-secondary new-email-verification']");
    private By buttonUpdateEmail = By.xpath("//button[normalize-space()='Update Email']");
    private By messageUpdateEmailSuccess = By.xpath("//span[@data-notify='message']");
    private By titleAddress = By.xpath("//h5[normalize-space()='Address']");
    private By buttonAddNewAddress = By.xpath("//div[@class='border p-3 rounded mb-3 c-pointer text-center bg-light']");
    private By titlePopupNewAddress = By.xpath("//div[@id='new-address-modal']//h5[@id='exampleModalLabel']");
    private By inputYourAddress = By.xpath("//textarea[@placeholder='Your Address']");
    private By selectCountry = By.xpath("//button[@title='Select your country']");
    private By inputSearchCountry = By.xpath("//div[@class='dropdown-menu show']//input[@aria-label='Search']");
    private By selectState = By.xpath("//div[contains(text(),'Select State')]");
    private By inputSearchState = By.xpath("//div[@class='dropdown-menu show']//input[@aria-label='Search']");
    private By selectCity = By.xpath("//div[contains(text(),'Select City')]");
    private By inputSearchCity = By.xpath("//div[@class='dropdown-menu show']//input[@aria-label='Search']");
    private By inputPostalCode = By.xpath("//input[@placeholder='Your Postal Code']");
    private By inputPhoneAddress = By.xpath("//input[@placeholder='+880']");
    private By buttonSaveNewAddress = By.xpath("//button[normalize-space()='Save']");


    public void updateProfile() {
        WebUI.waitForPageLoaded();
        WebUI.clickElement(menuManageProfile);
        WebUI.waitForPageLoaded();
        WebUI.verifyElementVisible(titleManageProfile, "Manage Profile page is NOT displayed");
        WebUI.setText(inputName, DataGenerateUtils.randomFullName());
        WebUI.setText(inputPhone, DataGenerateUtils.randomPhoneNumber());
        WebUI.setText(inputPassword, "123456");
        WebUI.setText(inputConfirmPassword, "123456");
        WebUI.clickElement(buttonUpdateProfile);
        WebUI.verifyElementVisible(messageNotPermitted, "The message not allowed in the demo is not displayed");
    }

    public void updateEmail() {
        WebUI.waitForPageLoaded();
        WebUI.clickElement(menuManageProfile);
        WebUI.scrollToElementAtTop(titleChangeEmail);
        WebUI.verifyElementVisible(titleChangeEmail, "Title Change your email is NOT displayed");
        WebUI.setText(inputEmail, "cashierngan001@gmail.com");
        WebUI.clickElement(buttonUpdateEmail);
        WebUI.verifyElementVisible(messageUpdateEmailSuccess, "Update email is failed");
    }

    public void addNewAddress() {
        WebUI.waitForPageLoaded();
        WebUI.clickElement(menuManageProfile);
        WebUI.scrollToElementAtBottom(titleAddress);
        WebUI.verifyElementVisible(titleAddress, "Change address block is NOT displayed");
        WebUI.clickElement(buttonAddNewAddress);
        WebUI.verifyElementVisible(titlePopupNewAddress, "Popup New Address is NOT displayed");
        WebUI.setText(inputYourAddress, "Phuong 6");
        WebUI.clickElement(selectCountry);
        WebUI.setText(inputSearchCountry, "Vietnam", Keys.ENTER);
        WebUI.clickElement(selectState);
        WebUI.setText(inputSearchState, "Dong Thap", Keys.ENTER);
        WebUI.clickElement(selectCity);
        WebUI.setText(inputSearchCity, "Cao Lanh", Keys.ENTER);
        WebUI.setText(inputPostalCode, "87010");
        WebUI.setText(inputPhoneAddress, "0123456789");
        WebUI.clickElement(buttonSaveNewAddress);
    }

}
