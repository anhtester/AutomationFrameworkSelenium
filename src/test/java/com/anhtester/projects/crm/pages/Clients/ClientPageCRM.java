package com.anhtester.projects.crm.pages.Clients;

import com.anhtester.enums.FailureHandling;
import com.anhtester.keywords.WebUI;
import com.anhtester.projects.crm.models.ClientModel;
import com.anhtester.projects.crm.pages.CommonPageCRM;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.Hashtable;

public class ClientPageCRM extends CommonPageCRM {

    public ClientPageCRM() {
        super();
    }

    public String pageText = "Total clients";
    public String pageUrl = "/clients";

    public By buttonAddClient = By.xpath("//a[normalize-space()='Add client']");
    public By inputCompanyName = By.xpath("//input[@id='company_name']");
    public By selectOwner = By.xpath("//div[@id='s2id_created_by']");
    public By inputSearchOwner = By.xpath("//div[@id='select2-drop']//input");
    public By selectFirstItemOwner = By.xpath("(//div[contains(@id,'select2-result-label')])[1]");
    public By inputAddress = By.xpath("//textarea[@id='address']");
    public By inputCity = By.xpath("//input[@id='city']");
    public By inputState = By.xpath("//input[@id='state']");
    public By inputZip = By.xpath("//input[@id='zip']");
    public By inputCountry = By.xpath("//input[@id='country']");
    public By inputPhone = By.xpath("//input[@id='phone']");
    public By inputWebsite = By.xpath("//input[@id='website']");
    public By inputVat = By.xpath("//input[@id='vat_number']");
    public By inputClientGroups = By.xpath("(//label[normalize-space()='Client groups']/following-sibling::div//input)[1]");
    public By spanFirstItemClientGroups = By.xpath("//span[@class='select2-match']");
    public By buttonSaveOnDialog = By.xpath("//div[@id='ajaxModalContent']//button[normalize-space()='Save']");
    public By inputSearch = By.xpath("//div[@id='client-table_filter']//input");
    public By itemClientFirstRow = By.xpath("//table[@id='client-table']//tbody/tr[1]/td[2]/a");
    public By tabClientInfo = By.xpath("//a[normalize-space()='Client info']");
    public By ownerDetail = By.xpath("//div[@id='s2id_created_by']//a[@class='select2-choice']/span[1]");
    public By radioOrganization = By.xpath("//input[@id='type_organization']");
    public By labelOnClientPage = By.xpath("//span[normalize-space()='Total clients']");
    public By tabClient = By.xpath("//ul[@id='client-tabs']//li[2]");
    public By labelClientGroups = By.xpath("//li[@class='select2-search-choice']/div");
    public By inputApplication = By.xpath("//label[normalize-space()='Application']/following-sibling::div//input");


    public void openClientTabPage() {
        //If you want to continue running, select FailureHandling.CONTINUE_ON_FAILURE
        WebUI.verifyElementTextEquals(labelOnClientPage, pageText, FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.sleep(1);
        WebUI.clickElement(tabClient);
        WebUI.waitForPageLoaded();
        WebUI.waitForJQueryLoad();
    }

    public void addClient(Hashtable<String, String> data) {
        WebUI.clickElement(buttonAddClient);
        WebUI.setText(inputCompanyName, data.get(ClientModel.getCompanyName()));
        WebUI.clickElement(selectOwner);
        WebUI.setText(inputSearchOwner, data.get(ClientModel.getOwner()), Keys.ENTER);
        WebUI.setText(inputAddress, data.get(ClientModel.getAddress()));
        WebUI.setText(inputCity, data.get(ClientModel.getCity()));
        WebUI.setText(inputState, data.get(ClientModel.getState()));
        WebUI.setText(inputZip, data.get(ClientModel.getZip()));
        WebUI.setText(inputCountry, data.get(ClientModel.getCountry()));
        WebUI.setText(inputPhone, data.get(ClientModel.getPhone()));
        WebUI.setText(inputWebsite, data.get(ClientModel.getWebsite()));
        WebUI.setText(inputVat, data.get(ClientModel.getVat()));
        WebUI.setText(inputClientGroups, data.get(ClientModel.getClientGroup()), Keys.ENTER);
        //WebUI.setText(inputApplication, data.get(ClientModel.getApplication()));

        WebUI.clickElement(buttonSaveOnDialog);
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
        WebUI.setText(inputSearch, data.get(ClientModel.getCompanyName()));
        WebUI.waitForPageLoaded();
        WebUI.sleep(3);
        WebUI.checkContainsValueOnTableByColumn(2, data.get(ClientModel.getCompanyName()));
        checkClientDetail(data);
    }

    public void checkClientDetail(Hashtable<String, String> data) {
        WebUI.clickElement(itemClientFirstRow);
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);
        WebUI.clickElement(tabClientInfo);
        WebUI.waitForPageLoaded();
        WebUI.sleep(1);
        WebUI.verifyElementChecked(radioOrganization, "Type off Client is not Organization");
        WebUI.verifyElementAttributeValue(inputCompanyName, "value", data.get(ClientModel.getCompanyName()));
        WebUI.verifyElementTextEquals(ownerDetail, data.get(ClientModel.getOwner()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementTextEquals(inputAddress, data.get(ClientModel.getAddress()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementAttributeValue(inputCity, "value", data.get(ClientModel.getCity()));
        WebUI.verifyElementAttributeValue(inputState, "value", data.get(ClientModel.getState()));
        WebUI.verifyElementAttributeValue(inputZip, "value", data.get(ClientModel.getZip()));
        WebUI.verifyElementAttributeValue(inputCountry, "value", data.get(ClientModel.getCountry()));
        WebUI.verifyElementAttributeValue(inputPhone, "value", data.get(ClientModel.getPhone()));
        WebUI.verifyElementAttributeValue(inputWebsite, "value", data.get(ClientModel.getWebsite()));
        WebUI.verifyElementAttributeValue(inputVat, "value", data.get(ClientModel.getVat()));
        WebUI.verifyElementTextEquals(labelClientGroups, data.get(ClientModel.getClientGroup()), FailureHandling.CONTINUE_ON_FAILURE);

    }

    public void enterDataSearchClient(String value) {
        WebUI.sleep(2);
        WebUI.moveToElement(inputSearch);
        WebUI.clearText(inputSearch);
        WebUI.setText(inputSearch, value);
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
    }

}
