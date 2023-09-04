package anhtester.com.projects.cms.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void TC_LoginFailWithEmailNull() {
        getLoginPageCMS().loginFailWithEmailNull();
    }

    @Test(priority = 2)
    public void TC_LoginFailWithEmailDoesNotExist() {
        ExcelHelpers excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginFailWithEmailDoesNotExist(excel.getCellData(1, "email"), excel.getCellData(1, "password"));
    }

    @Test(priority = 3)
    public void TC_LoginFailWithNullPassword() {
        ExcelHelpers excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginFailWithNullPassword(excel.getCellData(2, "email"));
    }

    @Test(priority = 4)
    public void TC_LoginFailWithIncorrectPassword() {
        ExcelHelpers excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginFailWithIncorrectPassword(excel.getCellData(3, "email"), excel.getCellData(3, "password"));
    }

    @Test(priority = 5)
    public void TC_LoginSuccessWithCustomerAccount() {
        ExcelHelpers excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginSuccessWithCustomerAccount(excel.getCellData(4, "email"), excel.getCellData(4, "password"));
    }

//    @Test(priority = 6)
//    public void TC_LoginSuccessAdminPage() {
//        excel = new ExcelHelpers();
//        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
//        getLoginPageCMS().loginSuccessAdminPage(excel.getCellData(5, "email"), excel.getCellData(5, "password"));
//    }
}
