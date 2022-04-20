package anhtester.com.projects.website.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;
import anhtester.com.models.SignIn;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.data.DataProviderManager;
import anhtester.com.utils.DecodeUtils;
import anhtester.com.utils.WebUI;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.util.Hashtable;
import java.util.Map;

@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class SignInTest extends BaseTest {

    SignInPage signInPage;

    public SignInTest(){
        signInPage = new SignInPage();
    }

    //Sử dụng với thư viện DataSupplier
    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 1, dataProvider = "getDataSignInSupplierFromExcel", dataProviderClass = DataProviderManager.class)
    @Step("SignInTestDataSupplierFromClassDefine")
    public void SignInTestDataSupplierFromClassDefine(SignIn data) {
        signInPage.signIn(data.getEmail(), DecodeUtils.decrypt(data.getPassword()));
    }


    //Sử dụng với thư viện DataProvider Hashtable
    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 2, dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    @Step("SignInTestDataProviderHashtable")
    public void SignInTestDataProviderHashtable(Hashtable<String, String> data) {
        signInPage.signInWithDataProviderHashtable(data);
    }


    @FrameworkAnnotation(author = {AuthorType.ANHTESTER}, category = {CategoryType.REGRESSION})
    @Test(priority = 3, dataProvider = "getDataSignInFromExampleData", dataProviderClass = DataProviderManager.class)
    @Step("SignInTestExampleData")
    public void SignInTestDataSupplierFromExample(String email, String password, String message) {
        signInPage.signIn(email, password);
    }

}
