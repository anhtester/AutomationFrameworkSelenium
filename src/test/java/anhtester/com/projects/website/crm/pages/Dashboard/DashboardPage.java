package anhtester.com.projects.website.crm.pages.Dashboard;

import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.utils.ObjectUtils;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;

public class DashboardPage {

    public DashboardPage() {
    }

    public String pageText = "Project Timeline";
    public String pageUrl = "/dashboard";

    public ClientPage openClientPage() {
        WebUI.clickElement(ObjectUtils.getObject("menuClients"));

        return new ClientPage();
    }

    public ProjectPage openProjectPage() {
        WebUI.clickElement(ObjectUtils.getObject("menuProjects"));

        return new ProjectPage();
    }
}
