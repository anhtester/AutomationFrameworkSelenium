package anhtester.com.projects.website.crm.pages.SignIn;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.keywords.WebUI;

import static anhtester.com.keywords.WebUI.*;

import anhtester.com.projects.website.crm.models.SignInModel;
import anhtester.com.projects.website.crm.pages.CommonPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.utils.DecodeUtils;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.Hashtable;

public class SignInPage extends CommonPage {

    private String pageUrl = "/signin";
    private String pageTitle = "Sign in | RISE - Ultimate Project Manager and CRM";

    public By inputEmail = By.xpath("//input[@id='email']");
    public By inputPassword = By.xpath("//input[@id='password']");
    public By buttonSignIn = By.xpath("//button[normalize-space()='Sign in']");
    public By alertErrorMessage = By.xpath("//div[@role='alert']");
    public By linkForgotPassword = By.xpath("//a[normalize-space()='Forgot password?']");
    public By linkSignUp = By.xpath("//a[normalize-space()='Sign up']");
    public By labelEmailError = By.xpath("//span[@id='email-error']");
    public By labelPasswordError = By.xpath("//span[@id='password-error']");


    ExcelHelpers excelHelpers;

    public SignInPage() {
        super();
        excelHelpers = new ExcelHelpers();
    }

    public DashboardPage signInWithAdminRole() {
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        getURL(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, excelHelpers.getCellData(1, SignInModel.getEmail()));
        setText(inputPassword, DecodeUtils.decrypt(excelHelpers.getCellData(1, SignInModel.getPassword())));
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPage();
    }

    public DashboardPage signInWithClientRole() {
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        getURL(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, excelHelpers.getCellData(2, SignInModel.getEmail()));
        setText(inputPassword, DecodeUtils.decrypt(excelHelpers.getCellData(2, SignInModel.getPassword())));
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPage();
    }

    public DashboardPage signIn(String email, String password) {
        getURL(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, email);
        setText(inputPassword, password);
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPage();
    }

    public DashboardPage signIn(Hashtable<String, String> data) {
        getURL(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, data.get(SignInModel.getEmail()));
        setText(inputPassword, DecodeUtils.decrypt(data.get(SignInModel.getPassword())));
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPage();
    }

}
