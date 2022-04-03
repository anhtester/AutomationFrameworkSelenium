package anhtester.com.projects.website.crm.testcases;

import anhtester.com.annotations.FrameworkAnnotation;
import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.enums.AuthorType;
import anhtester.com.enums.CategoryType;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.models.SignIn;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.Dashboard.DashboardPage;
import anhtester.com.projects.website.crm.pages.SignIn.SignInPage;
import anhtester.com.utils.DataProviderUtils;
import anhtester.com.utils.DecodeUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

@Epic("Regression Test CRM")
@Feature("SignInModel Test")
public class SignInModelTest extends BaseTest {

    SignInPage signInPage;
    DashboardPage dashboardPage;
    ClientPage clientPage;

    @Step("SignIn to CRM system")
    public void SignIn() {
        DriverManager.getDriver().get(FrameworkConstants.BASE_URL);
        signInPage = new SignInPage();
        dashboardPage = signInPage.signIn(PropertiesHelpers.getValue("emailAdmin"), DecodeUtils.decrypt(PropertiesHelpers.getValue("password")));
    }

//    @BeforeClass
//    public void beforeSteps() {
//        SignIn();
//    }

    @FrameworkAnnotation(author = {AuthorType.ANHTESTER, AuthorType.VOTHAIAN},
            category = {CategoryType.SANITY, CategoryType.REGRESSION})
    @Test(dataProvider = "getSignInDataSupplier", dataProviderClass = DataProviderUtils.class, description = "Sign In Model Test", priority = 1)
    @Step("SignInTestSupplier")
    public void SignInTestSupplier(SignIn data) {
        webUI.getToUrl(FrameworkConstants.BASE_URL);
        signInPage = new SignInPage();
        signInPage.signIn(data.getEmail(), data.getPassword());
    }

//    @Test(dataProvider = "LoginData", dataProviderClass = DataProviderFactory.class, description = "Sign In Model Test", priority = 1)
//    @Step("SignInModelTest")
//    public void SignInModelTest(Hashtable<String, String> data) {
//        DriverManager.getDriver().get(FrameworkConstants.BASE_URL);
//        signInPage = new SignInPage();
//        System.out.println(data);
//        signInPage.signInWithDataProvider(data);
//    }
//
//    @Test(dataProvider = "LoginDataReflection", dataProviderClass = DataProviderFactory.class, description = "Sign In Model Test Reflection", priority = 2)
//    @Step("SignInModelTestReflection")
//    public void SignInModelTestReflection(LoginModel data) {
//        DriverManager.getDriver().get(FrameworkConstants.BASE_URL);
//        signInPage = new SignInPage();
//        System.out.println(data);
//        signInPage.signIn(data.getEmail(), data.getPassword());
//    }

}
