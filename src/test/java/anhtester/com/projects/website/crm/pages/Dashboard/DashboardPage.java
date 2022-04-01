package anhtester.com.projects.website.crm.pages.Dashboard;

import anhtester.com.driver.DriverManager;
import anhtester.com.projects.website.crm.pages.CommonPage;
import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.utils.ObjectRepository;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends CommonPage {

    private ObjectRepository object;

    public DashboardPage()
    {
        object = new ObjectRepository("src/test/resources/objects.crm/locators.properties");
    }

    public String pageText = "Project Timeline";
    public String pageUrl = "/dashboard";

    public ClientPage openClientPage(){
        webUI.waitForPageLoaded();
        webUI.clickElement(object.getLocator("ProjectPage.clientMenu"));

        return new ClientPage();
    }

    public ProjectPage openProjectPage(){
        webUI.waitForPageLoaded();
        webUI.clickElement(object.getLocator("ProjectPage.projectMenu"));

        return new ProjectPage();
    }
}
