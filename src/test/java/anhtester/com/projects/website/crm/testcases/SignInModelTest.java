package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseWeb;
import anhtester.com.driver.DriverManager;
import anhtester.com.helpers.Helpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.models.DataTestProviderFactory;
import anhtester.com.models.LoginModel;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Hashtable;

import static anhtester.com.config.ConfigurationManager.configuration;

@Epic("Regression Test CRM")
@Feature("SignInModel Test")
public class SignInModelTest extends BaseWeb {

    SignInPage signInPage;
    DashboardPage dashboardPage;
    ClientPage clientPage;

    @Step("Login to CRM system")
    public void SignIn() {
        DriverManager.getDriver().get(configuration().url());
        signInPage = new SignInPage();
        dashboardPage = signInPage.signIn(PropertiesHelpers.getValue("emailAdmin"), Helpers.decrypt(PropertiesHelpers.getValue("password")));
    }

//    @BeforeClass
//    public void beforeSteps() {
//        SignIn();
//    }

    @Test(dataProvider = "LoginData", dataProviderClass = DataTestProviderFactory.class, description = "Sign In Model Test", priority = 1)
    @Step("SignInModelTest")
    public void SignInModelTest(Hashtable<String, String> data) {
        DriverManager.getDriver().get(configuration().url());
        signInPage = new SignInPage();
        System.out.println(data);
        signInPage.signInWithDataProvider(data);
    }

    @Test(dataProvider = "LoginDataReflection", dataProviderClass = DataTestProviderFactory.class, description = "Sign In Model Test", priority = 1)
    @Step("SignInModelTest")
    public void SignInModelTestReflection(LoginModel data) {
        DriverManager.getDriver().get(configuration().url());
        signInPage = new SignInPage();
        System.out.println(data);
        signInPage.signIn(data.getEmail(), data.getPassword());
    }

    @Test(dataProvider = "ClientData", dataProviderClass = DataTestProviderFactory.class, priority = 2, groups = {"regression"})
    public void addNewClient(Hashtable<String, String> data) {
        webUI.waitForPageLoaded();
        clientPage = dashboardPage.openClientPage();
        webUI.waitForPageLoaded();
        clientPage.openClientTabPage();
        // Search lần 1
        clientPage.enterDataSearchClient("Anh Tester Com 05");
        webUI.checkContainsSearchTableByColumn(2, "Anh Tester Com 05");
        // Search lần 2
        clientPage.enterDataSearchClient("Phamiliar Tech");
        webUI.checkContainsSearchTableByColumn(2, "Phamiliar Tech");
    }

}
