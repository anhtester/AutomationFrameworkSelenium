package anhtester.com.projects.cms.admin.pages.logins;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.keywords.WebUI;
import anhtester.com.projects.cms.CommonPageCMS;
import anhtester.com.projects.cms.users.pages.dashboard.DashboardPage;
import org.openqa.selenium.By;

import static anhtester.com.keywords.WebUI.*;

public class LoginPageCMS extends CommonPageCMS {

    private By closeAdvertisementPopup = By.xpath("//i[@class='la la-close fs-20']");
    private By buttonLogin = By.xpath("//a[normalize-space() = 'Login' and @class = 'text-reset d-inline-block opacity-60 py-2']");
    private By buttonCopyAdminAcc = By.xpath("//button[normalize-space()='Copy']");
    private By buttonSubmitLogin = By.xpath("//button[normalize-space()='Login']");
    private By titleLoginPage = By.xpath("//h1[normalize-space() = 'Login to your account.']");
    private By messageRequiredEmail = By.xpath("//strong[contains(text(),'The email field is required when phone is not present.')]");
    private By inputEmail = By.xpath("//input[@id='email']");
    private By inputPassword = By.xpath("//input[@id='password']");
    private By messageAccDoesNotExist = By.xpath("//span[@data-notify='message']");
    private By messageRequiredPassword = By.xpath("//input[contains(@class, 'is-invalid') and @id = 'password']");
    private By titleAnhTesterAdminPage = By.xpath("//img[@alt='Active eCommerce CMS']");

    public void clickCloseAdvertisementPopup() {
        clickElement(closeAdvertisementPopup);
    }

    public void openLoginPage() {
        openURL(FrameworkConstants.URL_CMS_USER);
        clickCloseAdvertisementPopup();
        clickElement(buttonLogin);
        verifyElementVisible(titleLoginPage, "Login page is NOT displayed");
    }

    public void verifyRedirectToAdminPage(){
        verifyElementVisible(avatarProfile, "Can not redirect to Admin page.");
    }

    public void loginFailWithNullEmail() {
        openLoginPage();
        clickElement(buttonSubmitLogin);
        verifyEquals(getTextElement(messageRequiredEmail).trim(), "The email field is required when phone is not present.", "");
    }

    public void loginFailWithEmailDoesNotExist(String email, String password) {
        openLoginPage();
        setText(inputEmail, email);
        setText(inputPassword, password);
        clickElement(buttonSubmitLogin);
        verifyElementVisible(messageAccDoesNotExist, "Email is incorrect but valid is NOT displayed.");
    }

    public void loginFailWithNullPassword(String email) {
        openLoginPage();
        setText(inputEmail, email);
        clickElement(buttonSubmitLogin);
        verifyElementVisible(messageRequiredPassword, "Password is NULL but valid is NOT displayed.");
    }

    public void loginFailWithIncorrectPassword(String email, String password) {
        openLoginPage();
        setText(inputEmail, email);
        clearText(inputPassword);
        setText(inputPassword, password);
        clickElement(buttonSubmitLogin);
        verifyElementVisible(messageAccDoesNotExist, "Password is failed but valid is NOT displayed.");
    }

    public void loginSuccessWithCustomerAccount(String email, String password) {
        openLoginPage();
        setText(inputEmail, email);
        clearText(inputPassword);
        setText(inputPassword, password);
        clickElement(buttonSubmitLogin);
        waitForElementVisible(DashboardPage.titleDashboard);
        verifyElementVisible(DashboardPage.titleDashboard, "Dashboard page is NOT displayed.");
    }

    public CommonPageCMS loginSuccessAdminPage(String email, String password) {
        openURL(FrameworkConstants.URL_CMS_ADMIN);
        setText(inputEmail, email);
        setText(inputPassword, password);
        clickElement(buttonSubmitLogin);
        waitForElementVisible(titleAnhTesterAdminPage);
        verifyElementVisible(titleAnhTesterAdminPage, "Admin page is NOT displayed.");
        return new CommonPageCMS();
    }

    public CommonPageCMS loginSuccessAdminPage() {
        openURL(FrameworkConstants.URL_CMS_ADMIN);
        setText(inputEmail, PropertiesHelpers.getValue("email"));
        setText(inputPassword, PropertiesHelpers.getValue("password"));
        clickElement(buttonSubmitLogin);
        waitForElementVisible(titleAnhTesterAdminPage);
        verifyElementVisible(titleAnhTesterAdminPage, "Admin page is NOT displayed.");
        return new CommonPageCMS();
    }
}


