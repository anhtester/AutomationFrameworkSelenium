package anhtester.com.common;

import anhtester.com.utils.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class CommonTest {

    private WebDriver driver;
    private WebUI webUI;

    public CommonTest(WebDriver driver) {
        this.driver = driver;
        webUI = new WebUI();
    }

    private String headerPinFromText = "Change Confirmation";

    //Commons Element
    private By headerPinForm = By.xpath("//h3[@class='header ng-binding']");
    private By pinInput = By.xpath("//input[@id='changeconfirm_modal_password']");
    private By reasonForChangeInput = By.xpath("//input[@id='changeconfirm_modal_reason']");
    private By cancelBtn = By.xpath("//div[@class='actions']/button[contains(text(),'Cancel')]");
    private By submitBtn = By.xpath("//button[@id='changeconfirm_modal_submit']");

    private By contentAlertMessage = By.cssSelector("div#pht_notification_box>div>p");
    private By closeAlertMessageBtn = By.className("notify-close-btn");

    public void validateAlertMessage(String message) {
        webUI.waitForPageLoaded();
        WebElement element = driver.findElement(contentAlertMessage);
        System.out.println("Alert message: " + element.getText());
        Assert.assertTrue(element.getText().equals(message), "The message does not match.");
        webUI.clickElement(closeAlertMessageBtn);
    }

}
