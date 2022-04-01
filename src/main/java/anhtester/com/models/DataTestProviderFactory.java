package anhtester.com.models;

import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.Helpers;
import org.testng.annotations.DataProvider;

public class DataTestProviderFactory {

    @DataProvider(name = "LoginData")
    public Object[][] getLoginData() {
        Object[][] data = ExcelHelpers.getDataHashTable(Helpers.getCurrentDir() + "src/test/resources/SignInDataExcel.xlsx", "Login", 2, 4);
        System.out.println(data);
        return data;
    }

    @DataProvider(name = "LoginDataReflection")
    public Object[][] getLoginDataReflection() {
        Object[][] data = ExcelHelpers.getDataReflection(Helpers.getCurrentDir() + "src/test/resources/SignInDataExcel.xlsx", "Login", 2, 4);
        System.out.println(data);
        return data;
    }

    @DataProvider(name = "ClientData")
    public Object[][] getClientData() {
        Object[][] data = ExcelHelpers.getDataHashTable(Helpers.getCurrentDir() + "src/test/resources/ClientsDataExcel.xlsx", "Login", 1, 2);
        System.out.println(data);
        return data;
    }
}
