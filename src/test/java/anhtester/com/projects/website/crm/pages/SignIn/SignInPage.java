package anhtester.com.projects.website.crm.pages.SignIn;

import anhtester.com.driver.DriverManager;
import anhtester.com.projects.website.crm.pages.CommonPage;
import anhtester.com.utils.ObjectRepository;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Hashtable;

public class SignInPage {

    private String pageUrl = "/signin";
    private String pageText = "";
    private String pageTitle = "CRM Project Manager | Anh Tester Demo";

    //Sign In element
//    private By emailInput = By.xpath("//input[@id='email']");
//    private By passwordInput = By.xpath("//input[@id='password']");
//    private By signInBtn = By.xpath("//button[normalize-space()='Sign in']");

    public SignInPage() {
    }

    public DashboardPage signIn(String email, String password) {
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        WebUI.setText(ObjectRepository.getLocator("SigninPage.email"), email);
        WebUI.setText(ObjectRepository.getLocator("SigninPage.passwordInput"), password);
        WebUI.clickElement(ObjectRepository.getLocator("SigninPage.signInBtn"));

        return new DashboardPage();
    }

    public DashboardPage signInWithDataProvider(Hashtable<String, String> data) {
        WebUI.waitForPageLoaded();
        Assert.assertTrue(WebUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(WebUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        WebUI.setText(ObjectRepository.getLocator("SigninPage.email"), data.get("email"));
        WebUI.setText(ObjectRepository.getLocator("SigninPage.passwordInput"), data.get("password"));
        WebUI.clickElement(ObjectRepository.getLocator("SigninPage.signInBtn"));

        return new DashboardPage();
    }

}
