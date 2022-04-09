package anhtester.com.projects.website.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.models.Client;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.report.ExtentReportManager;
import anhtester.com.data.DataProviderManager;
import anhtester.com.utils.DecodeUtils;
import anhtester.com.utils.WebUI;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.*;

@Epic("Regression Test CRM")
@Feature("Client Test")
public class ClientTest extends BaseTest {

    public SignInPage signInPage;
    public DashboardPage dashboardPage;
    public ClientPage clientPage;

    @BeforeMethod
    @Step("Sign in to the CRM system")
    public void SignIn() {
        ExtentReportManager.createTest("Sign in to the CRM system !!");
        WebUI.getToUrl(FrameworkConstants.BASE_URL);
        WebUI.waitForPageLoaded();
        signInPage = new SignInPage();
        dashboardPage = signInPage.signIn(ExcelHelpers.getCellData(1, "EMAIL"), DecodeUtils.decrypt(ExcelHelpers.getCellData(1, "PASSWORD")));
    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 1, description = "Add new Client", dataProvider = "getDataClientSupplierFromExcel",
            dataProviderClass = DataProviderManager.class)
    @Step("Add new Client")
    public void testAddClient(Client clientData) {
        //Client section
        clientPage = dashboardPage.openClientPage();
        clientPage.openClientTabPage();
        clientPage.addClient(clientData);

    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.AUTOMATION},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 2, description = "Search Client")
    @Step("Search Client")
    public void testSearchClient() {
        clientPage = dashboardPage.openClientPage();
        clientPage.openClientTabPage();
        // Search the first
        clientPage.enterDataSearchClient("Anh Tester Com 05");
        WebUI.checkContainsSearchTableByColumn(2, "Anh Tester Com 05");
        // Search the second
        clientPage.enterDataSearchClient("Phamiliar Tech");
        WebUI.checkContainsSearchTableByColumn(2, "Phamiliar Tech");

    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SMOKE, CategoryType.REGRESSION})
    @Test(priority = 3, description = "Test Invalid Page Title")
    @Step("Test Invalid Page Title")
    public void testInvalidPageTitle() {
        WebUI.waitForPageLoaded();
        Assert.assertEquals(WebUI.getPageTitle(), "AnhTester");

    }

}