package com.anhtester.projects.crm.pages.Dashboard;

import static com.anhtester.keywords.WebUI.*;

import com.anhtester.projects.crm.pages.Clients.ClientPageCRM;
import com.anhtester.projects.crm.pages.CommonPageCRM;
import com.anhtester.projects.crm.pages.Projects.ProjectPageCRM;
import com.anhtester.projects.crm.pages.Tasks.TaskPage;
import org.openqa.selenium.By;

public class DashboardPageCRM extends CommonPageCRM {

    public DashboardPageCRM() {
        super();
    }

    public String pageText = "Dashboard";
    public String pageUrl = "/dashboard";

    public By menuDashboard = By.xpath("//span[normalize-space()='Dashboard']");
    public By menuClients = By.xpath("//span[normalize-space()='Clients']");
    public By menuProjects = By.xpath("//span[normalize-space()='Projects']");
    public By menuTasks = By.xpath("//span[normalize-space()='Tasks']");

    public DashboardPageCRM openDashboardPage() {
        clickElement(menuDashboard);
        return this;
    }

    public ClientPageCRM openClientPage() {
        clickElement(menuClients);
        return new ClientPageCRM();
    }

    public ProjectPageCRM openProjectPage() {
        clickElement(menuProjects);
        return new ProjectPageCRM();
    }

    public TaskPage openTaskPage() {
        clickElement(menuTasks);
        return new TaskPage();
    }
}
