/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.projects.crm.testcases;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.helpers.*;
import com.anhtester.keywords.WebUI;
import com.anhtester.report.TelegramManager;
import com.anhtester.utils.*;
import org.testng.annotations.Test;

public class TestSimpleCode {

    @Test
    public void testReadDataFromJSON_01() {
        //JSONPath Online Evaluator - https://jsonpath.com/

        //Get book đầu tiên
        System.out.println(JsonUtils.getData("$.store.book[0]"));
        System.out.println(JsonUtils.getData("$['store']['book'][0]"));


        //Get category của Book đầu tiên
        System.out.println(JsonUtils.getData("$.store.book[0].category"));
        System.out.println(JsonUtils.getData("$['store']['book'][0].category"));

        //Get bicycle
        System.out.println(JsonUtils.getData("$.store.bicycle"));
        System.out.println(JsonUtils.getData("$['store']['bicycle']"));
    }

    @Test
    public void testReadDataFromJSON_02() {
        JsonUtils.setJsonFile("src/test/resources/datajson/tools.json");

        //Get name
        System.out.println(JsonUtils.getData("$.tool.jsonpath.creator.name"));

        //Get location
        System.out.println(JsonUtils.getData("$.tool.jsonpath.creator.name"));
    }

    @Test
    public void testReadDataFromJSON_03() {
        //Create JsonHelpers to run parallel
        JsonHelpers jsonHelpers = new JsonHelpers();

        //Set Json file
        jsonHelpers.setJsonFile("src/test/resources/datajson/book.json");

        //Get title of book
        System.out.println(jsonHelpers.getData("$.book[1].title"));

        //Get cheap
        System.out.println(jsonHelpers.getData("$.['price range'].cheap"));
    }


    @Test
    public void testDataFaker() {
        //DataFakerUtils.setLocate("vi");
        System.out.println(DataFakerUtils.getFaker().address().fullAddress());
        System.out.println(DataFakerUtils.getFaker().job().title());
    }

    @Test
    public void testZipFolder() {
        ZipUtils.zipFolder("reports/ExtentReports", "ExtentReports");
    }

    @Test
    public void testZipFile() {
        ZipUtils.zipFile("src/test/resources/pdf-config.json", "pdf-config");
    }

    @Test
    public void testUnZipFile() {
        //ZipUtils.unZip("pdf-config.zip", "target/pdf-config");
        ZipUtils.unZipFile("pdf-config.zip", "target/abc/pdf-config");
    }

    @Test
    public void testTelegramBotMessage() {
        TelegramManager.sendReportPath();
        //TelegramManager.sendFilePath("logs/applog.log");
    }

    @Test
    public void testGetOSInfo() {
        System.out.println(BrowserInfoUtils.getOSInfo());
        System.out.println(BrowserInfoUtils.isWindows());
        System.out.println(BrowserInfoUtils.isMac());
    }

    @Test
    public void testGetXpathDynamic() {
        String xpath1 = ObjectUtils.getXpathDynamic("//button[normalize-space()='%s']", "Login");
        WebUI.logConsole(xpath1);

        String xpath2 = ObjectUtils.getXpathDynamic("//button[normalize-space()='%s']//div[%d]//span[%d]", "Login", 2, 10);
        WebUI.logConsole(xpath2);

        PropertiesHelpers.loadAllFiles();
        String xpath3 = ObjectUtils.getXpathDynamic(ObjectUtils.getXpathValue("buttonDynamicXpath"), "Login");
        WebUI.logConsole(xpath3);
    }

    @Test
    public void testRemoveAccent() {
        WebUI.logConsole(LanguageUtils.removeAccent("Anh Tester"));
    }

    @Test
    public void testMakeSlug() {
        WebUI.logConsole(SystemHelpers.makeSlug("Anh Tester Automation Testing"));
    }

    @Test
    public void testReadFileJSON() {
        WebUI.logConsole(JsonUtils.get("url"));
        WebUI.logConsole(JsonUtils.get("BROWSER"));
        WebUI.logConsole(JsonUtils.get("button"));
    }

    @Test
    public void testGetCurrentDirectory() {
        WebUI.logConsole(SystemHelpers.getCurrentDir());
    }

    @Test
    public void testSplitString() {
        String s1 = "Automation, Testing, Selenium, Java";

        for (String arr : SystemHelpers.splitString(s1, ", ")) {
            WebUI.logConsole(arr);
        }
    }

    @Test
    public void testEncryptDecryptData() {
        String pass = "123456";
        //Encrypt password
        WebUI.logConsole(DecodeUtils.encrypt(pass));
        //Decrypt password
        WebUI.logConsole(DecodeUtils.decrypt(DecodeUtils.encrypt(pass)));
    }

    @Test
    public void testCreateFolder() {
        SystemHelpers.createFolder("src/test/resources/TestCreateNewFolder");
    }

    @Test
    public void testPropertiesFile() {
        PropertiesHelpers.loadAllFiles();
        //  Handle Properties file
        WebUI.logConsole(PropertiesHelpers.getValue("BROWSER"));
        WebUI.logConsole(PropertiesHelpers.getValue("URL_CRM"));
        WebUI.logConsole(PropertiesHelpers.getValue("AUTHOR"));
        WebUI.logConsole(FrameworkConstants.EXCEL_DATA_FILE_PATH);
        WebUI.logConsole(SystemHelpers.getCurrentDir() + PropertiesHelpers.getValue("EXCEL_DATA_FILE_PATH"));
//        PropertiesHelpers.setFile("src/test/resources/config/data.properties");
//        PropertiesHelpers.setValue("base_url", "https://anhtetser.com");
    }

    @Test
    public void testGetCurrentDateTime() {
        WebUI.logConsole(DateUtils.getCurrentDateTime());
    }

    @Test
    public void testReadAndWriteTxtFile() {
        PropertiesHelpers.loadAllFiles();
        //Read all data
        FileHelpers.readTxtFile(PropertiesHelpers.getValue("TXT_FILE_PATH"));
        //Read data by line number
        WebUI.logConsole(FileHelpers.readLineTxtFile(PropertiesHelpers.getValue("TXT_FILE_PATH"), 0));
    }

    @Test
    public void testExcelFile1() {
        PropertiesHelpers.loadAllFiles();
        WebUI.logConsole(SystemHelpers.getCurrentDir() + PropertiesHelpers.getValue("EXCEL_DATA_FILE_PATH"));
        //  Handle Excel file
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(SystemHelpers.getCurrentDir() + PropertiesHelpers.getValue("EXCEL_DATA_FILE_PATH"), "SignIn");
        WebUI.logConsole(excelHelpers.getCellData(1, "EMAIL"));
        WebUI.logConsole(excelHelpers.getCellData(1, "PASSWORD"));
        excelHelpers.setCellData("pass", 1, "EXPECTED_TITLE");
    }

    @Test()
    public void testExcelFile2() throws Exception {
        PropertiesHelpers.loadAllFiles();
        ExcelHelpers excelHelpers = new ExcelHelpers();
        WebUI.logConsole(excelHelpers.getDataHashTable(SystemHelpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn", 1, 2));
    }

//    @Test
//    public void connectDBMySQL() throws SQLException, ClassNotFoundException {
//
//        Connection connection = DatabaseHelpers.getMySQLConnection("sql6.freesqldatabase.com", "sql6464696", "sql6464696", "LIAGIkgd44");
//
//        // Create Statement object.
//        Statement statement = connection.createStatement();
//
//        String sql = "SELECT * FROM `company`";
//
//        // Execute the SQL statement that returns the ResultSet object.
//        ResultSet rs = statement.executeQuery(sql);
//
//        WebUI.logConsole(rs);
//
//        // Use while to Loop the returned results.
//        while (rs.next()) {// Move the cursor down to the next record.
//            int Id = rs.getInt(1);
//            String COMPANY_ID = rs.getString("COMPANY_ID");
//            String COMPANY_NAME = rs.getString("COMPANY_NAME");
//            String COMPANY_CITY = rs.getString("COMPANY_CITY");
//            WebUI.logConsole("--------------------");
//            WebUI.logConsole("COMPANY_ID:" + COMPANY_ID);
//            WebUI.logConsole("COMPANY_NAME:" + COMPANY_NAME);
//            WebUI.logConsole("COMPANY_CITY:" + COMPANY_CITY);
//        }
//
//        // Close connection
//        connection.close();
//    }

}
