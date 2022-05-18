package anhtester.com.projects.website.crm.pages.Clients;

import anhtester.com.enums.FailureHandling;
import anhtester.com.models.Client;
import anhtester.com.utils.ObjectUtils;
import anhtester.com.utils.WebUI;

public class ClientPage {

    public ClientPage() {
    }

    public String pageText = "Total clients";
    public String pageUrl = "/clients";

    public void openClientTabPage() {
        //Muốn chạy tiếp thì chọn FailureHandling.CONTINUE_ON_FAILURE
        WebUI.verifyElementTextEquals(ObjectUtils.getLocator("labelOnClientPage"), pageText, FailureHandling.STOP_ON_FAILURE);
        //WebUI.verifyPageUrl(pageUrl);
        WebUI.sleep(1);
        WebUI.clickElement(ObjectUtils.getLocator("clientTab"));
    }

    public void addClient(Client clientData) {
        WebUI.clickElement(ObjectUtils.getLocator("addClientBtn"));
        WebUI.setText(ObjectUtils.getLocator("companyNameInput"), clientData.getCompanyName());
        WebUI.clickElement(ObjectUtils.getLocator("ownerSelect"));
        WebUI.setText(ObjectUtils.getLocator("ownerSearchInput"), clientData.getOwner());
        WebUI.clickElement(ObjectUtils.getLocator("ownerFirstItemSelect"));
        WebUI.setText(ObjectUtils.getLocator("addressInput"), clientData.getAddress());
        WebUI.setText(ObjectUtils.getLocator("cityInput"), clientData.getCity());
        WebUI.setText(ObjectUtils.getLocator("stateInput"), clientData.getState());
        WebUI.setText(ObjectUtils.getLocator("zipInput"), clientData.getZip());
        WebUI.setText(ObjectUtils.getLocator("countryInput"), clientData.getCountry());
        WebUI.setText(ObjectUtils.getLocator("phoneInput"), clientData.getPhone());
        WebUI.clickElement(ObjectUtils.getLocator("saveDialogBtn"));
        WebUI.setText(ObjectUtils.getLocator("searchInput"), clientData.getCompanyName());
        WebUI.checkContainsSearchTableByColumn(2, clientData.getCompanyName());
        checkClientDetail(clientData);
    }

    public void checkClientDetail(Client clientData) {
        WebUI.clickElement(ObjectUtils.getLocator("itemClientFirstRow"));
        WebUI.clickElement(ObjectUtils.getLocator("tabClientInfo"));

        WebUI.verifyElementAttributeValue(ObjectUtils.getLocator("companyNameInput"), "value", clientData.getCompanyName());
        WebUI.verifyElementTextEquals(ObjectUtils.getLocator("ownerDetail"), clientData.getOwner(), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementTextEquals(ObjectUtils.getLocator("addressInput"), clientData.getAddress(), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementAttributeValue(ObjectUtils.getLocator("cityInput"), "value", clientData.getCity());
        WebUI.verifyElementAttributeValue(ObjectUtils.getLocator("stateInput"), "value", clientData.getState());
        //Còn vài cái nữa là làm biếng code quá =))

    }

    public void enterDataSearchClient(String value) {
        WebUI.sleep(1);
        WebUI.moveToElement(ObjectUtils.getLocator("searchInput"));
        WebUI.clearText(ObjectUtils.getLocator("searchInput"));
        WebUI.setText(ObjectUtils.getLocator("searchInput"), value);
    }

}
