package com.anhtester.projects.cms;

import com.anhtester.common.BaseTest;
import com.anhtester.projects.cms.admin.pages.logins.LoginPageCMS;
import com.anhtester.projects.cms.admin.pages.products.AddProductPage;
import com.anhtester.projects.cms.users.pages.order.OrderPage;
import com.anhtester.projects.cms.users.pages.products.ProductInfoPageCMS;
import com.anhtester.projects.cms.users.pages.profiles.ProfilePage;

/**
 * Base class of the CMS test cases: browser lifecycle from {@link BaseTest} + access to the CMS pages.
 * BaseTest itself stays generic (no dependency on a project's pages).
 */
public class BaseTestCMS extends BaseTest {

    private final CommonPageCMS pages = new CommonPageCMS();

    public LoginPageCMS getLoginPageCMS() {
        return pages.getLoginPageCMS();
    }

    public ProfilePage getProfilePage() {
        return pages.getProfilePage();
    }

    public OrderPage getOrderPage() {
        return pages.getOrderPage();
    }

    public AddProductPage getAddProductPage() {
        return pages.getAddProductPage();
    }

    public ProductInfoPageCMS getProductInfoPage() {
        return pages.getProductInfoPage();
    }
}
