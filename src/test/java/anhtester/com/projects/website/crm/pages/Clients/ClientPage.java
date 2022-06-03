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
        WebUI.clickElement(ObjectUtils.getObject("buttonSaveOnDialog"));
        WebUI.setText(ObjectUtils.getObject("inputSearch"), data.get(ClientModel.getCompanyName()));
        WebUI.checkContainsSearchTableByColumn(2, data.get(ClientModel.getCompanyName()));
        checkClientDetail(data);
    }

    public void checkClientDetail(Hashtable<String, String> data) {
        WebUI.clickElement(ObjectUtils.getObject("itemClientFirstRow"));
        WebUI.clickElement(ObjectUtils.getObject("tabClientInfo"));

        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputCompanyName"), "value", data.get(ClientModel.getCompanyName()));
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("ownerDetail"), data.get(ClientModel.getOwner()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementTextEquals(ObjectUtils.getObject("inputAddress"), data.get(ClientModel.getAddress()), FailureHandling.CONTINUE_ON_FAILURE);
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputCity"), "value", data.get(ClientModel.getCity()));
        WebUI.verifyElementAttributeValue(ObjectUtils.getObject("inputState"), "value", data.get(ClientModel.getState()));
        //Còn vài cái nữa là làm biếng code quá =))

    }

    public void enterDataSearchClient(String value) {
        WebUI.sleep(1);
        WebUI.moveToElement(ObjectUtils.getObject("inputSearch"));
        WebUI.clearText(ObjectUtils.getObject("inputSearch"));
        WebUI.setText(ObjectUtils.getObject("inputSearch"), value);
    }

}
