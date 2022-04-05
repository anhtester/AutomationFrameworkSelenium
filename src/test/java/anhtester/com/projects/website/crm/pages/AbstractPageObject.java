package anhtester.com.projects.website.crm.pages;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import static org.openqa.selenium.support.PageFactory.initElements;

public class AbstractPageObject {
    //Này dùng cho PageFactory. Hiện tại chưa dùng vì mình design theo Page Object thuần
    protected AbstractPageObject() {
        initElements(new AjaxElementLocatorFactory(DriverManager.getDriver(), FrameworkConstants.WAIT_DEFAULT), this);
    }
}
