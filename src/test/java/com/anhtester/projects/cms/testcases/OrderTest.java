package com.anhtester.projects.cms.testcases;

import com.anhtester.projects.cms.BaseTestCMS;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.helpers.ExcelHelpers;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import com.anhtester.constants.TestGroups;
import org.testng.annotations.Test;

@Epic("Regression Test CMS")
@Feature("Order Product Test")
public class OrderTest extends BaseTestCMS {

    @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.SINGLE_RUN})
    public void TC_OrderProduct() {
        ExcelHelpers excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginSuccessWithCustomerAccount(excel.getCellData(4, "email"), excel.getCellData(4, "password"));
        getOrderPage().emptyCart();
        getOrderPage().order("Delivery as soon as possible");
    }

}
