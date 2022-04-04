package anhtester.com.projects.website.crm.pages.Dashboard;

import anhtester.com.projects.website.crm.pages.Projects.ProjectPage;
import anhtester.com.utils.ObjectRepository;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.Clients.ClientPage;

public class DashboardPage {

    public DashboardPage() {
    }

    public String pageText = "Project Timeline";
    public String pageUrl = "/dashboard";

    public ClientPage openClientPage() {
        WebUI.waitForPageLoaded();
        WebUI.clickElement(ObjectRepository.getLocator("ProjectPage.clientMenu"));

        return new ClientPage();
    }

    public ProjectPage openProjectPage() {
        WebUI.waitForPageLoaded();
        WebUI.clickElement(ObjectRepository.getLocator("ProjectPage.projectMenu"));

        return new ProjectPage();
    }
}
