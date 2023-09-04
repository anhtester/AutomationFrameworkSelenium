package anhtester.com.projects.cms.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import org.testng.annotations.Test;

public class OrderTest extends BaseTest {

    @Test
    public void TC_OrderProduct() {
        ExcelHelpers excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginSuccessWithCustomerAccount(excel.getCellData(4, "email"), excel.getCellData(4, "password"));
        getOrderPage().order("Delivery as soon as possible");
    }

}
