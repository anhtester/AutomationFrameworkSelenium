package anhtester.com.projects.website.crm.pages.Clients;

import anhtester.com.enums.FailureHandling;
import anhtester.com.projects.website.crm.models.ClientModel;
import anhtester.com.utils.ObjectUtils;
import anhtester.com.utils.WebUI;

import java.util.Hashtable;

public class ClientPage {

    public ClientPage() {
    }

    public String pageText = "Total clients";
    public String pageUrl = "/clients";

    public void openClientTabPage() {
        //Muốn chạy tiếp thì chọn FailureHandling.CONTINUE_ON_FAILURE
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("labelOnClientPage"), pageText, FailureHandling.CONTINUE_ON_FAILURE);
        //WebUI.verifyPageUrl(pageUrl);
        WebUI.sleep(1);
        WebUI.clickElement(ObjectUtils.getObject("tabClient"));
    }

    public void addClient(Hashtable<String, String> data) {
        WebUI.clickElement(ObjectUtils.getObject("buttonAddClient"));
        WebUI.setText(ObjectUtils.getObject("inputCompanyName"), data.get(ClientModel.getCompanyName()));
        WebUI.clickElement(ObjectUtils.getObject("selectOwner"));
        WebUI.setText(ObjectUtils.getObject("inputSearchOwner"), data.get(ClientModel.getOwner()));
        WebUI.clickElement(ObjectUtils.getObject("selectFirstItemOwner"));
        WebUI.setText(ObjectUtils.getObject("inputAddress"), data.get(ClientModel.getAddress()));
        WebUI.setText(ObjectUtils.getObject("inputCity"), data.get(ClientModel.getCity()));
        WebUI.setText(ObjectUtils.getObject("inputState"), data.get(ClientModel.getState()));
        WebUI.setText(ObjectUtils.getObject("inputZip"), data.get(ClientModel.getZip()));
        WebUI.setText(ObjectUtils.getObject("inputCountry"), data.get(ClientModel.getCountry()));
        WebUI.setText(ObjectUtils.getObject("inputPhone"), data.get(ClientModel.getPhone()));
        WebUI.setText(ObjectUtils.getObject("inputWebsite"), data.get(ClientModel.getWebsite()));
        WebUI.setText(ObjectUtils.getObject("inputVat"), data.get(ClientModel.getVat()));
        WebUI.setText(ObjectUtils.getObject("inputClientGroups"), data.get(ClientModel.getClientGroup()));
        WebUI.clickElement(ObjectUtils.getObject("spanFirstItemClientGroups"));
        WebUI.clickElement(ObjectUtils.getObject("buttonSaveOnDialog"));

        WebUI.setText(ObjectUtils.getObject("inputSearch"), data.get(ClientModel.getCompanyName()));
        WebUI.waitForPageLoaded();
        WebUI.sleep(3);
        WebUI.checkContainsSearchTableByColumn(2, data.get(ClientModel.getCompanyName()));
        checkClientDetail(data);
    }

    public void checkClientDetail(Hashtable<String, String> data) {
        WebUI.clickElement(ObjectUtils.getObject("itemClientFirstRow"));
        WebUI.clickElement(ObjectUtils.getObject("tabClientInfo"));
        WebUI.waitForPageLoaded();
        WebUI.verifyElementChecked(ObjectUtils.getObject("radioOrganization"), "Type off Client is not Organization");
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputCompanyName"), "value", data.get(ClientModel.getCompanyName()));
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("ownerDetail"), data.get(ClientModel.getOwner()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("inputAddress"), data.get(ClientModel.getAddress()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputCity"), "value", data.get(ClientModel.getCity()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputState"), "value", data.get(ClientModel.getState()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputZip"), "value", data.get(ClientModel.getZip()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputCountry"), "value", data.get(ClientModel.getCountry()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputPhone"), "value", data.get(ClientModel.getPhone()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputWebsite"), "value", data.get(ClientModel.getWebsite()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputVat"), "value", data.get(ClientModel.getVat()));
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("labelClientGroups"), data.get(ClientModel.getClientGroup()), FailureHandling.CONTINUE_ON_FAILURE);

    }

    public void enterDataSearchClient(String value) {
        WebUI.sleep(2);
        WebUI.moveToElement(ObjectUtils.getObject("inputSearch"));
        WebUI.clearText(ObjectUtils.getObject("inputSearch"));
        WebUI.setText(ObjectUtils.getObject("inputSearch"), value);
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
    }

}
