/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.dataprovider.DataProviderManager;
import anhtester.com.driver.DriverManager;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.keywords.WebUI;

import static anhtester.com.config.ConfigFactory.getConfigs;
import static anhtester.com.keywords.WebUI.*;

import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Hashtable;
import java.util.Properties;

@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class SignInTest extends BaseTest {

    public SignInTest() {
        signInPage = new SignInPage();
    }

    //Using library DataProvider with read Hashtable
    @Test(priority = 1, description = "TC01_signInTestDataProvider", dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void signInTestDataProvider(Hashtable<String, String> data) {
        signInPage.signIn(data);

    }

    @Test(priority = 2, description = "TC02_signInTestAdminRole")
    public void signInTestAdminRole() {
        signInPage.signInWithAdminRole();
        verifyElementPresent(getDashboardPage().menuDashboard, 5, "The menu Dashboard does not exist.");

    }

    @Test(priority = 3, description = "TC03_signInTestClientRole")
    public void signInTestClientRole() {
        signInPage.signInWithClientRole();
        verifyContains(getPageTitle(), "Dashboard | RISE - Ultimate Project Manager and CRM");

    }

    @Test(priority = 4, description = "TC04_testLoginCrmPerfex")
    public void testLoginCrmPerfex() {
        getURL("https://crm.anhtester.com/admin/authentication");
        clearAndFillText(By.xpath("//input[@id='email']"), "admin@example.com");
        clearAndFillText(By.xpath("//input[@id='password']"), "123456");
        clickElement(By.xpath("//button[normalize-space()='Login']"));
        verifyContains(getPageTitle(), "Dashboard");
    }

}
