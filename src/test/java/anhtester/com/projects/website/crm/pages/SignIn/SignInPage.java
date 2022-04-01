package anhtester.com.projects.website.crm.pages.SignIn;

import anhtester.com.driver.DriverManager;
import anhtester.com.projects.website.crm.pages.CommonPage;
import anhtester.com.utils.ObjectRepository;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Hashtable;

public class SignInPage extends CommonPage {

    private ObjectRepository object;

    private String pageUrl = "/signin";
    private String pageText = "";
    private String pageTitle = "CRM Project Manager | Anh Tester Demo";

    //Sign In element
//    private By emailInput = By.xpath("//input[@id='email']");
//    private By passwordInput = By.xpath("//input[@id='password']");
//    private By signInBtn = By.xpath("//button[normalize-space()='Sign in']");

    public SignInPage() {
        object = new ObjectRepository("src/test/resources/objects.crm/locators.properties");
    }

    public DashboardPage signIn(String email, String password) {
        webUI.waitForPageLoaded();
        Assert.assertTrue(webUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(webUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        webUI.setText(object.getLocator("SigninPage.email"), email);
        webUI.setText(object.getLocator("SigninPage.passwordInput"), password);
        webUI.clickElement(object.getLocator("SigninPage.signInBtn"));

        return new DashboardPage();
    }

    public DashboardPage signInWithDataProvider(Hashtable<String, String> data) {
        webUI.waitForPageLoaded();
        Assert.assertTrue(webUI.verifyPageUrl(pageUrl), "The url of Sign in page not match.");
        Assert.assertTrue(webUI.verifyPageTitle(pageTitle), "Tiêu đề trang sign in chưa đúng");
        webUI.setText(object.getLocator("SigninPage.email"), data.get("email"));
        webUI.setText(object.getLocator("SigninPage.passwordInput"), data.get("password"));
        webUI.clickElement(object.getLocator("SigninPage.signInBtn"));

        return new DashboardPage();
    }

}
