package anhtester.com.projects.cms.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.PropertiesHelpers;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class ProductInfoTest extends BaseTest {
    public ExcelHelpers excel;
    public ExcelHelpers excel2;

    @Test
    public void TC_GetProductInfo() {
        excel = new ExcelHelpers();
        excel.setExcelFile(FrameworkConstants.EXCEL_CMS_PRODUCTS_USER, "ProductInfo");
        excel2 = new ExcelHelpers();
        excel2.setExcelFile(FrameworkConstants.EXCEL_CMS_LOGIN, "Login");
        getLoginPageCMS().loginSuccessWithCustomerAccount(excel2.getCellData(4, "email"), excel2.getCellData(4, "password"));
        ArrayList productInfo = getProductInfoPage().productInfo(PropertiesHelpers.getValue("product_P01"));
        int lastRow = excel.getRows();
        int newRow = lastRow + 1;
        excel.setCellData(String.valueOf(newRow), newRow, 0);
        for (int i = 0; i < productInfo.size(); i++) {
            if (String.valueOf(newRow) != null) {
                excel.setCellData((String) productInfo.get(i), newRow, i + 1);
            }
        }
    }
}
