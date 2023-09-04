/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.dataprovider.DataProviderManager;
import anhtester.com.projects.crm.pages.Dashboard.DashboardPageCRM;
import anhtester.com.projects.crm.pages.SignIn.SignInPageCRM;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.Hashtable;

import static anhtester.com.keywords.WebUI.*;

@Epic("Regression Test")
@Feature("Sign In Test")
public class SignInTest extends BaseTest {

    SignInPageCRM signInPageCRM;
    DashboardPageCRM dashboardPageCRM;

    public SignInTest() {
        signInPageCRM = new SignInPageCRM();
    }

    //Using library DataProvider with read Hashtable
    @Test(priority = 1, description = "TC01_signInWithDataProvider", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void signInWithDataProvider(Hashtable<String, String> data) {
        signInPageCRM.signIn(data);

    }

    @Test(priority = 2, description = "TC02_signInWithAdminRole")
    public void signInWithAdminRole() {
        dashboardPageCRM = signInPageCRM.signInWithAdminRole();
        verifyElementPresent(dashboardPageCRM.menuDashboard, 5, "The menu Dashboard does not exist.");

    }

    @Test(priority = 3, description = "TC03_signInWithClientRole")
    public void signInWithClientRole() {
        signInPageCRM.signInWithClientRole();
        verifyContains(getPageTitle(), "Dashboard | RISE - Ultimate Project Manager and CRM");

    }

    @Test(priority = 4, description = "TC04_loginPerfexCRM")
    public void loginPerfexCRM() {
        getURL("https://crm.anhtester.com/admin/authentication");
        clearAndFillText(By.xpath("//input[@id='email']"), "admin@example.com");
        clearAndFillText(By.xpath("//input[@id='password']"), "123456");
        clickElement(By.xpath("//button[normalize-space()='Login']"));
        verifyEquals(getPageTitle(), "Dashboard");

    }

}
