/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.dataprovider.DataProviderManager;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;
import anhtester.com.keyword.WebUI;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.util.Hashtable;

@Epic("Regression Test CRM")
@Feature("Clients Test")
public class ClientTest extends BaseTest {

    public ClientTest() {
        signInPage = new SignInPage();
    }

    @FrameworkAnnotation(author = {AuthorType.AnhTester, AuthorType.Robert}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, dataProvider = "getClientDataHashTable", dataProviderClass = DataProviderManager.class)
    @Step("Add new Client")
    public void testAddClient(Hashtable<String, String> data) {
        dashboardPage = signInPage.signInWithAdminRole();
        clientPage = dashboardPage.openClientPage();
        clientPage.openClientTabPage();
        clientPage.addClient(data);
    }

    @FrameworkAnnotation(author = {AuthorType.James}, category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 2)
    @Step("Search Client")
    public void testSearchClient() {
        dashboardPage = signInPage.signInWithAdminRole();
        clientPage = dashboardPage.openClientPage();
        clientPage.openClientTabPage();
        // Search the first
        clientPage.enterDataSearchClient("Schamberger Inc");
        WebUI.checkContainsValueOnTableByColumn(2, "Schamberger Inc");
        // Search the second
        clientPage.enterDataSearchClient("Kassulke LLC");
        WebUI.checkContainsValueOnTableByColumn(2, "Kassulke LLC");

    }

}