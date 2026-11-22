package com.anhtester.projects.cms.testcases;

import com.anhtester.projects.cms.BaseTestCMS;
import com.anhtester.constants.FrameworkConstants;
import com.anhtester.helpers.ExcelHelpers;
import com.anhtester.projects.cms.CommonPageCMS;
import com.anhtester.projects.cms.admin.pages.category.CategoryPage;
import com.anhtester.projects.cms.admin.pages.logins.LoginPageCMS;
import com.anhtester.utils.DataGenerateUtils;
import com.anhtester.constants.TestGroups;
import org.testng.annotations.Test;

public class CategoryTest extends BaseTestCMS {

    LoginPageCMS loginPageCMS;
    CategoryPage categoryPage;
    CommonPageCMS commonPageCMS;
    ExcelHelpers excelHelpers;

    public CategoryTest() {
        commonPageCMS = new CommonPageCMS();
        loginPageCMS = new LoginPageCMS();
        excelHelpers = new ExcelHelpers();
    }

    @Test(groups = TestGroups.REGRESSION)
    public void TC_AddCategory() {
        loginPageCMS.loginSuccessAdminPage();
        categoryPage = commonPageCMS.clickMenuProducts().clickMenuCategory();
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_CMS_DATA, "Category");
        // The demo site is shared: add a unique suffix so the search finds the category created by THIS run
        String categoryName = excelHelpers.getCellData("category_name", 1) + " " + DataGenerateUtils.randomString(6).toUpperCase();
        //category_name   order_number   meta_title  description
        categoryPage.clickAddNewButton()
                .inputDataCategory(
                        categoryName,
                        excelHelpers.getCellData("order_number", 1),
                        excelHelpers.getCellData("meta_title", 1),
                        excelHelpers.getCellData("description", 1)
                )
                .clickSaveButton()
                .checkCategoryDisplayed(categoryName);
    }

}
