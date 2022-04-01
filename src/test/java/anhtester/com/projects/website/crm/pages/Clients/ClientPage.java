package anhtester.com.projects.website.crm.pages.Clients;

import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.projects.website.crm.pages.CommonPage;
import org.openqa.selenium.By;

public class ClientPage extends CommonPage {

    public ClientPage() {
    }

    public String pageText = "ClientModel";
    public String pageUrl = "/clients";

    //ClientModel Element
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


    public void openClientTabPage() {
        webUI.sleep(1);
        webUI.clickElement(clientTab);
    }

    public void addClient() {
        ExcelHelpers.setExcelFile(PropertiesHelpers.getValue("excelClients"), "AddClient");

        webUI.clickElement(addClientBtn);
        webUI.setText(companyNameInput, ExcelHelpers.getCellData(1, "company_name"));
        webUI.clickElement(ownerSelect);
        webUI.setText(ownerSearchInput, ExcelHelpers.getCellData(1, "owner"));
        webUI.clickElement(ownerFirstItemSelect);
        webUI.setText(addressInput, ExcelHelpers.getCellData(1, "address"));
        webUI.setText(cityInput, ExcelHelpers.getCellData(1, "city"));
        webUI.setText(stateInput, ExcelHelpers.getCellData(1, "state"));
        webUI.setText(zipInput, ExcelHelpers.getCellData(1, "zip"));
        webUI.setText(countryInput, ExcelHelpers.getCellData(1, "country"));
        webUI.setText(phoneInput, ExcelHelpers.getCellData(1, "phone"));
//        validateuiHelpers.clickElement(closeDialogBtn);
        webUI.clickElement(saveDialogBtn);
        webUI.setText(searchInput, ExcelHelpers.getCellData(1, "company_name"));
        webUI.checkContainsSearchTableByColumn(2, ExcelHelpers.getCellData(1, "company_name"));
    }

    public void enterDataSearchClient(String value) {
        webUI.moveToElement(searchInput);
        webUI.clearText(searchInput);
        webUI.setText(searchInput, value);
    }

}
