package anhtester.com.projects.website.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.utils.DecodeUtils;
import anhtester.com.utils.WebUI;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.*;

@Epic("Regression Test CRM")
@Feature("ClientModel Test")
public class ClientModelTest extends BaseTest {

    SignInPage signInPage;
    DashboardPage dashboardPage;
    ClientPage clientPage;

    @Step("SignIn to CRM system")
    public void SignIn() {
        webUI.getToUrl(FrameworkConstants.BASE_URL);
        webUI.waitForPageLoaded();
        signInPage = new SignInPage();
        System.out.println("Email: " + ExcelHelpers.getCellData(4, "Email"));
        dashboardPage = signInPage.signIn(ExcelHelpers.getCellData(4, "Email"), DecodeUtils.decrypt(ExcelHelpers.getCellData(4, "Password")));
    }

    @BeforeMethod
    public void beforeSteps() {
        SignIn();
    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 1, description = "Add new Client")
    @Step("Add new Client")
    public void testAddClient() {
        webUI.waitForPageLoaded();
        clientPage = dashboardPage.openClientPage();
        webUI.waitForPageLoaded();
        clientPage.openClientTabPage();
        clientPage.addClient();
    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.AUTOMATION},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 2, description = "Search Client")
    @Step("Search Client")
    public void testSearchClient() {
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

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SMOKE, CategoryType.REGRESSION})
    @Test(priority = 3, description = "Test Invalid Page Title")
    @Step("Test Invalid Page Title")
    public void testInvalidPageTitle() {
        webUI.waitForPageLoaded();
        Assert.assertEquals(webUI.getPageTitle(), "AnhTester");
    }

}
