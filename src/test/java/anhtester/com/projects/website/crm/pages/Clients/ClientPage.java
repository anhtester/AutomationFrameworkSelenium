package anhtester.com.projects.website.crm.pages.Clients;

import anhtester.com.models.Client;
import anhtester.com.utils.WebUI;
import org.openqa.selenium.By;

public class ClientPage {

    public ClientPage() {
    }

    public String pageText = "Client";
    public String pageUrl = "/clients";

    //Client Element
    private By clientTab = By.xpath("//ul[@id='client-tabs']//li[2]");
    private By addClientBtn = By.xpath("//a[normalize-space()='Add client']");
    private By companyNameInput = By.xpath("//input[@id='company_name']");
    private By ownerSelect = By.xpath("//div[@id='s2id_created_by']");
    private By ownerSearchInput = By.xpath("//div[@id='select2-drop']//input");
    private By ownerFirstItemSelect = By.xpath("(//div[contains(@id,'select2-result-label')])[1]");
    private By addressInput = By.xpath("//textarea[@id='address']");
    private By cityInput = By.xpath("//input[@id='city']");
    private By stateInput = By.xpath("//input[@id='state']");
    private By zipInput = By.xpath("//input[@id='zip']");
    private By countryInput = By.xpath("//input[@id='country']");
    private By phoneInput = By.xpath("//input[@id='phone']");
    public By saveDialogBtn = By.xpath("//div[@id='ajaxModalContent']//button[normalize-space()='Save']");
    public By searchInput = By.xpath("//input[@placeholder='Search']");


    public void openClientTabPage() {
        WebUI.sleep(1);
        WebUI.clickElement(clientTab);
    }

    public void addClient(Client clientData) {
        WebUI.clickElement(addClientBtn);
        WebUI.setText(companyNameInput, clientData.getCompanyName());
        WebUI.clickElement(ownerSelect);
        WebUI.setText(ownerSearchInput, clientData.getOwner());
        WebUI.clickElement(ownerFirstItemSelect);
        WebUI.setText(addressInput, clientData.getAddress());
        WebUI.setText(cityInput, clientData.getCity());
        WebUI.setText(stateInput, clientData.getState());
        WebUI.setText(zipInput, clientData.getZip());
        WebUI.setText(countryInput, clientData.getCountry());
        WebUI.setText(phoneInput, clientData.getPhone());
        WebUI.clickElement(saveDialogBtn);
        WebUI.setText(searchInput, clientData.getCompanyName());
        WebUI.checkContainsSearchTableByColumn(2, clientData.getCompanyName());
    }

    public void enterDataSearchClient(String value) {
        WebUI.moveToElement(searchInput);
        WebUI.clearText(searchInput);
        WebUI.setText(searchInput, value);
    }

}
