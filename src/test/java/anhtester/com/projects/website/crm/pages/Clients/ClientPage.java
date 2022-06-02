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
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("labelOnClientPage"), pageText, FailureHandling.STOP_ON_FAILURE);
        //WebUI.verifyPageUrl(pageUrl);
        WebUI.sleep(1);
        WebUI.clickElement(ObjectUtils.getObject("clientTab"));
    }

    public void addClient(Hashtable<String, String> data) {
        WebUI.clickElement(ObjectUtils.getObject("addClientBtn"));
        WebUI.setText(ObjectUtils.getObject("companyNameInput"), data.get(ClientModel.getCompanyName()));
        WebUI.clickElement(ObjectUtils.getObject("ownerSelect"));
        WebUI.setText(ObjectUtils.getObject("ownerSearchInput"), data.get(ClientModel.getOwner()));
        WebUI.clickElement(ObjectUtils.getObject("ownerFirstItemSelect"));
        WebUI.setText(ObjectUtils.getObject("addressInput"), data.get(ClientModel.getAddress()));
        WebUI.setText(ObjectUtils.getObject("cityInput"), data.get(ClientModel.getCity()));
        WebUI.setText(ObjectUtils.getObject("stateInput"), data.get(ClientModel.getState()));
        WebUI.setText(ObjectUtils.getObject("zipInput"), data.get(ClientModel.getZip()));
        WebUI.setText(ObjectUtils.getObject("countryInput"), data.get(ClientModel.getCountry()));
        WebUI.setText(ObjectUtils.getObject("phoneInput"), data.get(ClientModel.getPhone()));
        WebUI.clickElement(ObjectUtils.getObject("saveDialogBtn"));
        WebUI.setText(ObjectUtils.getObject("searchInput"), data.get(ClientModel.getCompanyName()));
        WebUI.checkContainsSearchTableByColumn(2, data.get(ClientModel.getCompanyName()));
        checkClientDetail(data);
    }

    public void checkClientDetail(Hashtable<String, String> data) {
        WebUI.clickElement(ObjectUtils.getObject("itemClientFirstRow"));
        WebUI.clickElement(ObjectUtils.getObject("tabClientInfo"));

        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("companyNameInput"), "value", data.get(ClientModel.getCompanyName()));
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("ownerDetail"), data.get(ClientModel.getOwner()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("addressInput"), data.get(ClientModel.getAddress()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("cityInput"), "value", data.get(ClientModel.getCity()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("stateInput"), "value", data.get(ClientModel.getState()));
        //Còn vài cái nữa là làm biếng code quá =))

    }

    public void enterDataSearchClient(String value) {
        WebUI.sleep(1);
        WebUI.moveToElement(ObjectUtils.getObject("searchInput"));
        WebUI.clearText(ObjectUtils.getObject("searchInput"));
        WebUI.setText(ObjectUtils.getObject("searchInput"), value);
    }

}
