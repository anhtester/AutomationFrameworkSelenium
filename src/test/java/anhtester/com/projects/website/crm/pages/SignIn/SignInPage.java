package anhtester.com.projects.website.crm.pages.SignIn;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.projects.website.crm.models.SignInModel;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.utils.DecodeUtils;
import anhtester.com.utils.ObjectUtils;
import anhtester.com.utils.WebUI;
import org.testng.Assert;

import java.util.Hashtable;

public class SignInPage {

    private String pageUrl = "/signin";
    private String pageText = "";
    private String pageTitle = "Sign in | CRM Project Manager | Anh Tester Demo";
    private String dashboardPageURL = "/dashboard";
    ExcelHelpers excelHelpers;

    public SignInPage() {
        excelHelpers = new ExcelHelpers();
    }

    public DashboardPage signInWithAdminRole() {
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        WebUI.getToUrl(FrameworkConstants.URL_CRM);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "The title of sign in page not match.");
        WebUI.setText(ObjectUtils.getObject("inputEmail"), excelHelpers.getCellData(1, SignInModel.getEmail()));
        WebUI.setText(ObjectUtils.getObject("inputPassword"), DecodeUtils.decrypt(excelHelpers.getCellData(1, SignInModel.getPassword())));
        WebUI.clickElement(ObjectUtils.getObject("buttonSignIn"));
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(dashboardPageURL), "Sign in failed. Can not redirect to Dashboard page.");
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

    public DashboardPage signInWithTeamLeaderRole() {
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        WebUI.getToUrl(FrameworkConstants.URL_CRM);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "The title of sign in page not match.");
        WebUI.setText(ObjectUtils.getObject("inputEmail"), excelHelpers.getCellData(2, SignInModel.getEmail()));
        WebUI.setText(ObjectUtils.getObject("inputPassword"), DecodeUtils.decrypt(excelHelpers.getCellData(2, SignInModel.getPassword())));
        WebUI.clickElement(ObjectUtils.getObject("buttonSignIn"));
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(dashboardPageURL), "Sign in failed. Can not redirect to Dashboard page.");
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

    public DashboardPage signInWithTeamMemberRole() {
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");
        WebUI.getToUrl(FrameworkConstants.URL_CRM);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "The title of sign in page not match.");
        WebUI.setText(ObjectUtils.getObject("inputEmail"), excelHelpers.getCellData(3, SignInModel.getEmail()));
        WebUI.setText(ObjectUtils.getObject("inputPassword"), DecodeUtils.decrypt(excelHelpers.getCellData(3, SignInModel.getPassword())));
        WebUI.clickElement(ObjectUtils.getObject("buttonSignIn"));
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(dashboardPageURL), "Sign in failed. Can not redirect to Dashboard page.");
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

    public DashboardPage signIn(String email, String password) {
        WebUI.getToUrl(FrameworkConstants.URL_CRM);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        WebUI.sleep(1);
        WebUI.setText(ObjectUtils.getObject("inputEmail"), email);
        WebUI.setText(ObjectUtils.getObject("inputPassword"), password);
        WebUI.clickElement(ObjectUtils.getObject("buttonSignIn"));
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

    public DashboardPage signIn(Hashtable<String, String> data) {
        WebUI.getToUrl(FrameworkConstants.URL_CRM);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "The title of sign in page not match.");
        WebUI.setText(ObjectUtils.getObject("inputEmail"), data.get(SignInModel.getEmail()));
        WebUI.setText(ObjectUtils.getObject("inputPassword"), DecodeUtils.decrypt(data.get(SignInModel.getPassword())));
        WebUI.clickElement(ObjectUtils.getObject("buttonSignIn"));
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(dashboardPageURL), "Sign in failed. Can not redirect to Dashboard page.");
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

}
