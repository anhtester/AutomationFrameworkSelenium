/*
 * Copyright (c) 2022. Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.data;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.ExcelHelpers;
import anhtester.com.helpers.Helpers;
import anhtester.com.models.Client;
import anhtester.com.models.SignIn;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.TestDataReader;
import io.github.sskorol.data.XlsxReader;
import one.util.streamex.StreamEx;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static java.util.Arrays.asList;

public final class DataProviderManager {

    private DataProviderManager() {
    }

    @Test(dataProvider = "getDataSignInSupplierFromExcel")
    public void test2(SignIn signInData) {
        System.out.println("signInData.testCaseName = " + signInData.getTestCaseName());
        System.out.println("signInData.username = " + signInData.getEmail());
        System.out.println("signInData.password = " + signInData.getPassword());
        System.out.println("signInData.expectedTitle = " + signInData.getExpectedTitle());
        System.out.println("signInData.expectedError = " + signInData.getExpectedError());

    }

    @Test(dataProvider = "getDataClientSupplierFromExcel")
    public void testAddClient(Client clientData) {
        System.out.println("clientData.TestCaseName = " + clientData.getTestCaseName());
        System.out.println("clientData.CompanyName = " + clientData.getCompanyName());
        System.out.println("clientData.OWNER = " + clientData.getOwner());
        System.out.println("clientData.Address = " + clientData.getAddress());
        System.out.println("clientData.CITY = " + clientData.getCity());
        System.out.println("clientData.STATE = " + clientData.getState());

    }

    //@DataProvider --> Return type -> Object[][] or Object[]
    //@DataSupplier //--> It can read any file (CSV, xlsx, JSON, YAMLDataSupplierTest)
    //@DataSupplier(runInParallel = true)
    @DataSupplier(runInParallel = true, name = "getDataSignInSupplierFromExcel")
    public StreamEx<SignIn> getDataSignInSupplierFromExcel(Method method) {
        String methodName = method.getName().trim();
        System.out.println(methodName);
        return TestDataReader.use(XlsxReader.class)
                .withTarget(SignIn.class)
                //By default, it looks for files in src/test/resources directory
                .withSource(FrameworkConstants.EXCEL_DATA_PATH)
                .read();
        //.filter(testData -> testData.getTestCaseName().trim().equalsIgnoreCase(methodName));

    }

    @DataSupplier(runInParallel = true, name = "getDataClientSupplierFromExcel")
    public StreamEx<Client> getDataClientSupplierFromExcel(Method method) {
        String methodName = method.getName().trim();
        System.out.println(methodName);
        return TestDataReader.use(XlsxReader.class)
                .withTarget(Client.class)
                .withSource(FrameworkConstants.EXCEL_DATA_PATH)
                .read();
        //.filter(testData -> testData.getTestCaseName().trim().equalsIgnoreCase(methodName));
    }

    @DataSupplier(name = "getDataSignInFromExampleData", flatMap = true)
    public static StreamEx getDataSignInFromExampleData() {
        return StreamEx.of(
                asList("admin02@mailinator.com", "123456", "Invalid credentials"),
                asList("tld01@mailinator.com", "123456", "Invalid credentials")
        );
    }

    @DataProvider(name = "SignInDataHashTable")
    public Object[][] getSignInData() {
        Object[][] data = ExcelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH_FULL, "SignIn", 2, 4);
        System.out.println(data);
        return data;
    }

    @DataProvider(name = "ClientDataHashTable")
    public Object[][] getClientData() {
        Object[][] data = ExcelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH_FULL, "Client", 1, 2);
        System.out.println(data);
        return data;
    }

}
