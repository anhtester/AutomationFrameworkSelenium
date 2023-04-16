/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.dataprovider.DataProviderManager;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;

import static anhtester.com.keywords.WebUI.*;

import anhtester.com.projects.crm.pages.Clients.ClientPageCRM;
import anhtester.com.projects.crm.pages.Dashboard.DashboardPageCRM;
import anhtester.com.projects.crm.pages.SignIn.SignInPageCRM;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.Hashtable;

@Epic("Regression Test CRM")
@Feature("Client Test")
public class ClientTest extends BaseTest {

    SignInPageCRM signInPageCRM;
    DashboardPageCRM dashboardPageCRM;
    ClientPageCRM clientPageCRM;

    public ClientTest() {
        signInPageCRM = new SignInPageCRM();
    }

    @FrameworkAnnotation(author = {AuthorType.AnhTester, AuthorType.Robert}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC05_testAddClient", dataProvider = "getClientDataHashTable", dataProviderClass = DataProviderManager.class)
    public void testAddClient(Hashtable<String, String> data) {
        dashboardPageCRM = signInPageCRM.signInWithAdminRole();
        clientPageCRM = dashboardPageCRM.openClientPage();
        clientPageCRM.openClientTabPage();
        clientPageCRM.addClient(data);
    }

    @FrameworkAnnotation(author = {AuthorType.James}, category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 2, description = "TC06_testSearchClient")
    public void testSearchClient() {
        dashboardPageCRM = signInPageCRM.signInWithAdminRole();
        clientPageCRM = dashboardPageCRM.openClientPage();
        clientPageCRM.openClientTabPage();
        // Search the first
        clientPageCRM.enterDataSearchClient("Schamberger Inc");
        checkContainsValueOnTableByColumn(2, "Schamberger Inc");
        // Search the second
        clientPageCRM.enterDataSearchClient("Kassulke LLC");
        checkContainsValueOnTableByColumn(2, "Kassulke LLC");

    }

}