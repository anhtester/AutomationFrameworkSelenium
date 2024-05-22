package com.anhtester.projects.crm.pages.SignIn;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.helpers.ExcelHelpers;

import static com.anhtester.keywords.WebUI.*;

import com.anhtester.projects.crm.pages.CommonPageCRM;
import com.anhtester.projects.crm.models.SignInModel;
import com.anhtester.projects.crm.pages.Dashboard.DashboardPageCRM;
import com.anhtester.utils.DecodeUtils;
import org.openqa.selenium.By;

import java.util.Hashtable;

public class SignInPageCRM extends CommonPageCRM {

    private String pageUrl = "/signin";
    private String pageTitle = "Sign in | RISE CRM | Anh Tester Demo";

    public By inputEmail = By.xpath("//input[@id='email']");
    public By inputPassword = By.xpath("//input[@id='password']");
    public By buttonSignIn = By.xpath("//button[normalize-space()='Sign in']");
    public By alertErrorMessage = By.xpath("//div[@role='alert']");
    public By linkForgotPassword = By.xpath("//a[normalize-space()='Forgot password?']");
    public By linkSignUp = By.xpath("//a[normalize-space()='Sign up']");
    public By labelEmailError = By.xpath("//span[@id='email-error']");
    public By labelPasswordError = By.xpath("//span[@id='password-error']");

    public SignInPageCRM() {
    }

    public DashboardPageCRM signInWithAdminRole() {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        openWebsite(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, excelHelpers.getCellData(1, SignInModel.getEmail()));
        setText(inputPassword, DecodeUtils.decrypt(excelHelpers.getCellData(1, SignInModel.getPassword())));
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPageCRM();
    }

    public DashboardPageCRM signInWithClientRole() {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        openWebsite(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, excelHelpers.getCellData(2, SignInModel.getEmail()));
        setText(inputPassword, DecodeUtils.decrypt(excelHelpers.getCellData(2, SignInModel.getPassword())));
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPageCRM();
    }

    public DashboardPageCRM signIn(String email, String password) {
        openWebsite(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, email);
        setText(inputPassword, password);
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPageCRM();
    }

    public DashboardPageCRM signIn(Hashtable<String, String> data) {
        openWebsite(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, data.get(SignInModel.getEmail()));
        setText(inputPassword, DecodeUtils.decrypt(data.get(SignInModel.getPassword())));
        clickElement(buttonSignIn);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");

        return new DashboardPageCRM();
    }

}
