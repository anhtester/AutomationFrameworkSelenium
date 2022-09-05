package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.utils.WebUI;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class Okxe extends BaseTest {

    @Test
    public void dangtin() {
        WebUI.getURL("https://www.okxe.vn/");

        By buttonX = By.xpath("//button[@class='btn-image-dialog v-btn v-btn--icon theme--dark']");
        By buttonTaiKhoan = By.xpath("//span[contains(text(),'Tài khoản')]");
        By buttonDangNhap = By.xpath("//h3[contains(text(),'Đăng nhập')]");
        By inputUsername = By.xpath("//input[@id='login-input_user-name']");
        By inputPassword = By.xpath("//input[@id='login-input_password']");
        By buttonConfirm = By.xpath("//button[@id='login-button_confirm']");
        By buttonDangTin = By.xpath("//span[contains(text(),'Đăng tin')]");
        By imageUpload = By.xpath("//main[@class='v-content']//img[@alt='Upload']");
        By buttonDiTiep = By.xpath("//button[@id='register-product_next-page']");
        By labelSoKmDaDi = By.xpath("//div[contains(text(),'Số km đã đi')]");
        By buttonSelectKM = By.xpath("//button[@id='register-product_input-select-mileage']");
        By item20000 = By.xpath("//div[@id='register-product_mileage-20.000']");


        WebUI.clickElement(buttonX);
        WebUI.clickElement(buttonTaiKhoan);
        WebUI.clickElement(buttonDangNhap);
        WebUI.setText(inputUsername, "0932949905");
        WebUI.setText(inputPassword, "@Aa246357");
        WebUI.clickElement(buttonConfirm);
        WebUI.clickElement(buttonDangTin);
        WebUI.sleep(2);
        WebUI.uploadFileForm(imageUpload, "D:\\selenium_icon.png");
        WebUI.clickElement(buttonDiTiep);

        WebUI.waitForElementVisible(buttonSelectKM);
        WebUI.scrollToElement(buttonSelectKM);
        WebUI.clickElement(buttonSelectKM);
        WebUI.waitForElementVisible(item20000);
        WebUI.sleep(1);
        WebUI.clickElement(item20000);
        WebUI.scrollToElement(labelSoKmDaDi);
        WebUI.sleep(3);
    }
}
