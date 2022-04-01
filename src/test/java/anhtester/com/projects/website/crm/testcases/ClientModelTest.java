package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseWeb;
import anhtester.com.driver.DriverManager;
import anhtester.com.helpers.Helpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.annotations.*;

import static anhtester.com.config.ConfigurationManager.configuration;

@Epic("Regression Test CRM")
@Feature("ClientModel Test")
public class ClientModelTest extends BaseWeb {

    SignInPage signInPage;
    DashboardPage dashboardPage;
    ClientPage clientPage;

    @Step("Login to CRM system")
    public void SignIn() {
        DriverManager.getDriver().get(configuration().url());
        signInPage = new SignInPage();
        dashboardPage = signInPage.signIn(PropertiesHelpers.getValue("emailAdmin"), Helpers.decrypt(PropertiesHelpers.getValue("password")));
    }

    @BeforeMethod
    public void beforeSteps() {
        SignIn();
    }

    @Test(priority = 1, description = "Add new Client")
    @Step("Add new Client")
    public void AddClient() {
        webUI.waitForPageLoaded();
        clientPage = dashboardPage.openClientPage();
        webUI.waitForPageLoaded();
        clientPage.openClientTabPage();
        clientPage.addClient();
    }

    @Test(priority = 2, description = "Search Client")
    @Step("Search Client")
    public void SearchClient() {
        webUI.waitForPageLoaded();
        clientPage = dashboardPage.openClientPage();
        webUI.waitForPageLoaded();
        clientPage.openClientTabPage();
        // Search the first
        clientPage.enterDataSearchClient("Anh Tester Com 05");
        webUI.checkContainsSearchTableByColumn(2, "Anh Tester Com 05");
        // Search the second
        clientPage.enterDataSearchClient("Phamiliar Tech");
        webUI.checkContainsSearchTableByColumn(2, "Phamiliar Tech");
    }

}
