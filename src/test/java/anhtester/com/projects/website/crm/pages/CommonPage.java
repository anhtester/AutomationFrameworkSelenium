package anhtester.com.projects.website.crm.pages;

import anhtester.com.driver.DriverManager;
import anhtester.com.utils.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonPage extends AbstractPageObject {

    public WebUI webUI;

    public CommonPage()
    {
        webUI = new WebUI();
    }

    public By headerDialogLabel = By.id("ajaxModalTitle");
    public By exitDialogBtn = By.xpath("//div[@class='modal-dialog modal-lg']//button[@aria-label='Close']");
    public By closeDialogBtn = By.xpath("//div[@id='ajaxModalContent']//button[normalize-space()='Close']");
    public By saveAndShowDialogBtn = By.xpath("//div[@id='ajaxModalContent']//button[normalize-space()='Save & show']");
    public By saveDialogBtn = By.xpath("//div[@id='ajaxModalContent']//button[normalize-space()='Save']");
    public By searchInput = By.xpath("//input[@placeholder='Search']");

}
