package anhtester.com.projects.website.crm.pages.SignIn;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.utils.DecodeUtils;
import anhtester.com.utils.ObjectUtils;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import org.testng.Assert;

import java.util.Hashtable;
import java.util.Map;

public class SignInPage {

    private String pageUrl = "/signin";
    private String pageText = "";
    private String pageTitle = "CRM Project Manager | Anh Tester Demo";
    private String dashboardPageURL = "/dashboard";

    public SignInPage() {
    }

    public DashboardPage signIn(String email, String password) {
        WebUI.getToUrl(FrameworkConstants.BASE_URL);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        WebUI.sleep(1);
        WebUI.setText(ObjectUtils.getLocator("SigninPage.email"), email);
        WebUI.setText(ObjectUtils.getLocator("SigninPage.passwordInput"), password);
        WebUI.clickElement(ObjectUtils.getLocator("SigninPage.signInBtn"));
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

    public DashboardPage signInWithDataProviderHashtable(Hashtable<String, String> data) {
        WebUI.getToUrl(FrameworkConstants.BASE_URL);
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        WebUI.setText(ObjectUtils.getLocator("SigninPage.email"), data.get("EMAIL"));
        WebUI.setText(ObjectUtils.getLocator("SigninPage.passwordInput"), DecodeUtils.decrypt(data.get("PASSWORD")));
        WebUI.clickElement(ObjectUtils.getLocator("SigninPage.signInBtn"));
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(dashboardPageURL),"Sign in failed. Can not redirect to Dashboard page.");
        WebUI.waitForPageLoaded();

        return new DashboardPage();
    }

}
