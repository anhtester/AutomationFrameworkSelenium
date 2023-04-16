package anhtester.com.projects.cms.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private ExcelHelpers excel;

    @Test(priority = 1)
    public void loginFailWithNullEmail() {
        getLoginPageCMS().loginFailWithNullEmail();
    }

    @Test(priority = 2)
    public void loginFailWithEmailDoesNotExist() {
        excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginFailWithEmailDoesNotExist(excel.getCellData(1, "email"), excel.getCellData(1, "password"));
    }

    @Test(priority = 3)
    public void loginFailWithNullPassword() {
        excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginFailWithNullPassword(excel.getCellData(2, "email"));
    }

    @Test(priority = 4)
    public void loginFailWithIncorrectPassword() {
        excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginFailWithIncorrectPassword(excel.getCellData(3, "email"), excel.getCellData(3, "password"));
    }

    @Test(priority = 5)
    public void loginSuccessWithCustomerAccount() {
        excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginSuccessWithCustomerAccount(excel.getCellData(4, "email"), excel.getCellData(4, "password"));
    }

    @Test(priority = 6)
    public void loginSuccessAdminPage() {
        excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginSuccessAdminPage(excel.getCellData(5, "email"), excel.getCellData(5, "password"));
    }
}
