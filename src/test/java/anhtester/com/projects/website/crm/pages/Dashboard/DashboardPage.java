package anhtester.com.projects.website.crm.pages.Dashboard;

import anhtester.com.keyword.WebUI;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.CommonPage;
import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.projects.website.crm.pages.Tasks.TaskPage;
import org.openqa.selenium.By;

public class DashboardPage extends CommonPage {

    public DashboardPage() {
    }

    public String pageText = "Dashboard";
    public String pageUrl = "/dashboard";

    public By menuDashboard = By.xpath("//span[normalize-space()='Dashboard']");
    public By menuClients = By.xpath("//span[normalize-space()='Clients']");
    public By menuProjects = By.xpath("//span[normalize-space()='Projects']");
    public By menuTasks = By.xpath("//span[normalize-space()='Tasks']");

    public DashboardPage openDashboardPage() {
        WebUI.clickElement(menuDashboard);
        return this;
    }

    public ClientPage openClientPage() {
        WebUI.clickElement(menuClients);
        return new ClientPage();
    }

    public ProjectPage openProjectPage() {
        WebUI.clickElement(menuProjects);
        return new ProjectPage();
    }

    public TaskPage openTaskPage() {
        WebUI.clickElement(menuTasks);
        return new TaskPage();
    }
}
