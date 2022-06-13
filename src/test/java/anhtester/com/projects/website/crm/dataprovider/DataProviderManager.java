/*
 * Copyright (c) 2022. Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.dataprovider;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.Helpers;
import anhtester.com.projects.website.crm.models.ClientModel;
import anhtester.com.projects.website.crm.models.SignInModel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Hashtable;

public final class DataProviderManager {

    private DataProviderManager() {
    }

    @Test(dataProvider = "getSignInDataHashTable")
    public void testGetSignInData(Hashtable<String, String> data) {
        System.out.println("signInData.testCaseName = " + data.get(SignInModel.getTestCaseName()));
        System.out.println("signInData.username = " + data.get(SignInModel.getEmail()));
        System.out.println("signInData.password = " + data.get(SignInModel.getPassword()));
        System.out.println("signInData.expectedTitle = " + data.get(SignInModel.getExpectedTitle()));
        System.out.println("signInData.expectedError = " + data.get(SignInModel.getExpectedError()));

    }

    @Test(dataProvider = "getClientDataHashTable")
    public void testGetClientData(Hashtable<String, String> data) {
        System.out.println("clientData.TestCaseName = " + data.get(ClientModel.getTestCaseName()));
        System.out.println("clientData.CompanyName = " + data.get(ClientModel.getCompanyName()));
        System.out.println("clientData.OWNER = " + data.get(ClientModel.getOwner()));
        System.out.println("clientData.Address = " + data.get(ClientModel.getAddress()));
        System.out.println("clientData.CITY = " + data.get(ClientModel.getCity()));
        System.out.println("clientData.STATE = " + data.get(ClientModel.getState()));

    }

    @DataProvider(name = "getSignInDataHashTable")
    public static Object[][] getSignInData() {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        Object[][] data = excelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH, "SignIn", 1, 2);
        System.out.println("getSignInData: " + data);
        return data;
    }

    @DataProvider(name = "getSignInDataHashTable2")
    public static Object[][] getSignInData2() {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        Object[][] data = excelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH, "SignIn", 2, 3);
        System.out.println("getSignInData: " + data);
        return data;
    }

    @DataProvider(name = "getClientDataHashTable", parallel = true)
    public static Object[][] getClientData() {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        Object[][] data = excelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH, "Client", 1, 4);
        System.out.println("getClientData: " + data);
        return data;
    }

}
