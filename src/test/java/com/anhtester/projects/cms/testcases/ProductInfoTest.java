package com.anhtester.projects.cms.testcases;

import com.anhtester.projects.cms.BaseTestCMS;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.helpers.ExcelHelpers;
import com.anhtester.helpers.PropertiesHelpers;
import com.anhtester.constants.TestGroups;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ProductInfoTest extends BaseTestCMS {

    @Test(groups = TestGroups.REGRESSION)
    public void TC_GetProductInfo() {
        ExcelHelpers excel = new ExcelHelpers();
        // Write the result into a copy in exports/ExportData: the template in src/test/resources stays unchanged
        excel.setExcelFile(prepareOutputFile(FrameworkConstants.EXCEL_CMS_PRODUCTS_USER).toString(), "ProductInfo");
        ExcelHelpers excel2 = new ExcelHelpers();
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

    /**
     * Copy the Excel template once into exports/ExportData and return the copy (results are appended there).
     */
    private static Path prepareOutputFile(String template) {
        Path output = Paths.get("exports", "ExportData", Paths.get(template).getFileName().toString());
        try {
            if (!Files.exists(output)) {
                Files.createDirectories(output.getParent());
                Files.copy(Paths.get(template), output);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Can not prepare the output Excel file " + output, e);
        }
        return output;
    }
}
