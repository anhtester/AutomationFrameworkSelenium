package anhtester.com.projects.website.crm.pages.Dashboard;

import anhtester.com.keywords.WebUI;

import static anhtester.com.keywords.WebUI.*;

import anhtester.com.projects.website.crm.pages.Clients.ClientPage;
import anhtester.com.projects.website.crm.pages.CommonPage;
import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.projects.website.crm.pages.Tasks.TaskPage;
import org.openqa.selenium.By;

public class DashboardPage extends CommonPage {

    public DashboardPage() {
        super();
    }

    public String pageText = "Dashboard";
    public String pageUrl = "/dashboard";

    public By menuDashboard = By.xpath("//span[normalize-space()='Dashboard']");
    public By menuClients = By.xpath("//span[normalize-space()='Clients']");
    public By menuProjects = By.xpath("//span[normalize-space()='Projects']");
    public By menuTasks = By.xpath("//span[normalize-space()='Tasks']");

    public DashboardPage openDashboardPage() {
        clickElement(menuDashboard);
        return this;
    }

    public ClientPage openClientPage() {
        clickElement(menuClients);
        return new ClientPage();
    }

    public ProjectPage openProjectPage() {
        clickElement(menuProjects);
        return new ProjectPage();
    }

    public TaskPage openTaskPage() {
        clickElement(menuTasks);
        return new TaskPage();
    }
}
