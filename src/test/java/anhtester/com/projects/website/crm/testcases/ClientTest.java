/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.models.Client;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
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

    public ClientTest() {
        signInPage = new SignInPage();
    }


    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 1, description = "Add new Client", dataProvider = "getDataClientSupplierFromExcel",
            dataProviderClass = DataProviderManager.class)
    @Step("Add new Client")
    public void testAddClient(Client clientData) {
        dashboardPage = signInPage.signIn(ExcelHelpers.getCellData(1, "EMAIL"), DecodeUtils.decrypt(ExcelHelpers.getCellData(1, "PASSWORD")));
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
        dashboardPage = signInPage.signIn(ExcelHelpers.getCellData(1, "EMAIL"), DecodeUtils.decrypt(ExcelHelpers.getCellData(1, "PASSWORD")));
        clientPage = dashboardPage.openClientPage();
        clientPage.openClientTabPage();
        // Search the first
        clientPage.enterDataSearchClient("Anh Tester Com 055555555");
        WebUI.checkContainsSearchTableByColumn(2, "Anh Tester Com 055555555");
        // Search the second
        clientPage.enterDataSearchClient("Phamiliar Tech");
        WebUI.checkContainsSearchTableByColumn(2, "Phamiliar Tech");

    }

    @Test(priority = 3, description = "Test Invalid Page Title")
    @Step("Test Invalid Page Title")
    public void testInvalidPageTitle() {
        dashboardPage = signInPage.signIn(ExcelHelpers.getCellData(1, "EMAIL"), DecodeUtils.decrypt(ExcelHelpers.getCellData(1, "PASSWORD")));

        Assert.assertEquals(WebUI.getPageTitle(), "AnhTester");

    }

}