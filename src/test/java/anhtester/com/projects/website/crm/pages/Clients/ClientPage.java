package anhtester.com.projects.website.crm.pages.Clients;

import anhtester.com.models.Client;
import anhtester.com.utils.ObjectRepository;
import anhtester.com.utils.WebUI;
import org.openqa.selenium.By;

public class ClientPage {

    public ClientPage() {
    }

    public String pageText = "Client";
    public String pageUrl = "/clients";

    public void openClientTabPage() {
        WebUI.sleep(1);
        WebUI.clickElement(ObjectRepository.getLocator("clientTab"));
    }

    public void addClient(Client clientData) {
        WebUI.clickElement(ObjectRepository.getLocator("addClientBtn"));
        WebUI.setText(ObjectRepository.getLocator("companyNameInput"), clientData.getCompanyName());
        WebUI.clickElement(ObjectRepository.getLocator("ownerSelect"));
        WebUI.setText(ObjectRepository.getLocator("ownerSearchInput"), clientData.getOwner());
        WebUI.clickElement(ObjectRepository.getLocator("ownerFirstItemSelect"));
        WebUI.setText(ObjectRepository.getLocator("addressInput"), clientData.getAddress());
        WebUI.setText(ObjectRepository.getLocator("cityInput"), clientData.getCity());
        WebUI.setText(ObjectRepository.getLocator("stateInput"), clientData.getState());
        WebUI.setText(ObjectRepository.getLocator("zipInput"), clientData.getZip());
        WebUI.setText(ObjectRepository.getLocator("countryInput"), clientData.getCountry());
        WebUI.setText(ObjectRepository.getLocator("phoneInput"), clientData.getPhone());
        WebUI.clickElement(ObjectRepository.getLocator("saveDialogBtn"));
        WebUI.setText(ObjectRepository.getLocator("searchInput"), clientData.getCompanyName());
        WebUI.checkContainsSearchTableByColumn(2, clientData.getCompanyName());
    }

    public void enterDataSearchClient(String value) {
        WebUI.moveToElement(ObjectRepository.getLocator("searchInput"));
        WebUI.clearText(ObjectRepository.getLocator("searchInput"));
        WebUI.setText(ObjectRepository.getLocator("searchInput"), value);
    }

}
