package com.anhtester.projects.cms.testcases;

import com.anhtester.common.BaseTest;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.helpers.ExcelHelpers;
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
