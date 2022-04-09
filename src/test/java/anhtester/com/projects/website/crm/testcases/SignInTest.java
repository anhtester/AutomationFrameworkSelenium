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

@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class SignInTest extends BaseTest {

    SignInPage signInPage;
    DashboardPage dashboardPage;
    ClientPage clientPage;

    //Sử dụng với thư viện DataSupplier
    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 1, dataProvider = "getDataSignInSupplierFromExcel", dataProviderClass = DataProviderManager.class)
    @Step("SignInTestDataSupplierFromClassDefine")
    public void SignInTestDataSupplierFromClassDefine(SignIn data) {
        WebUI.getToUrl(FrameworkConstants.BASE_URL);
        signInPage = new SignInPage();
        signInPage.signIn(data.getEmail(), DecodeUtils.decrypt(data.getPassword()));
    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(priority = 2, dataProvider = "getDataSignInFromExampleData", dataProviderClass = DataProviderManager.class)
    @Step("SignInTestExampleData")
    public void SignInTestDataSupplierFromExample(String email, String password, String message) {
        WebUI.getToUrl(FrameworkConstants.BASE_URL);
        signInPage = new SignInPage();
        signInPage.signIn(email, password);
    }

}
