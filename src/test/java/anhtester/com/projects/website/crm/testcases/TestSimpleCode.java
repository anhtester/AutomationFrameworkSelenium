/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.projects.website.crm.testcases;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.helpers.*;
import anhtester.com.helpers.PropertiesHelpers;
import anhtester.com.utils.*;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSimpleCode {

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
        WebUI.logConsole(LanguageUtils.removeAccent("Võ Thái An"));
    }

    @Test
    public void testMakeSlug() {
        WebUI.logConsole(Helpers.makeSlug("Anh Tester Automation Testing"));
    }

    @Test
    public void testReadFileJSON() {
        WebUI.logConsole(JsonUtils.get("url"));
        WebUI.logConsole(JsonUtils.get("browser"));
        WebUI.logConsole(JsonUtils.get("button"));
    }

    @Test
    public void testGetCurrentDirectory() {
        WebUI.logConsole(Helpers.getCurrentDir());
    }

    @Test
    public void testGetAndSetPropertiesFile() {
        PropertiesHelpers.loadAllFiles();

        WebUI.logConsole(PropertiesHelpers.getValue("browser"));
        WebUI.logConsole(PropertiesHelpers.getValue("buttonTag"));
        WebUI.logConsole(PropertiesHelpers.getValue("buttonDangNhap"));
    }

    @Test
    public void testSplitString() {
        String s1 = "Automation, Testing, Selenium, Java";

        for (String arr : Helpers.splitString(s1, ", ")) {
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
        Helpers.CreateFolder("src/test/resources/TestCreateNewFolder");
    }

    @Test
    public void testPropertiesFile() {
        PropertiesHelpers.loadAllFiles();
        //  Handle Properties file
        WebUI.logConsole(PropertiesHelpers.getValue("browser"));
        WebUI.logConsole(PropertiesHelpers.getValue("base_url"));
        WebUI.logConsole(PropertiesHelpers.getValue("author"));
        WebUI.logConsole(PropertiesHelpers.getValue("projectName"));
//        PropertiesHelpers.setFile("src/test/resources/config/datatest.properties");
//        PropertiesHelpers.setValue("base_url", "https://anhtetser.com");
    }

    @Test
    public void testGetCurrentDateTime() {
        WebUI.logConsole(DateUtils.getCurrentDateTime());
        //Log.info(Helpers.CurrentDateTime());
    }

    @Test
    public void testReadAndWriteTxtFile() {
        PropertiesHelpers.loadAllFiles();
        //Read all data
        TxtFileHelpers.readTxtFile(PropertiesHelpers.getValue("txtFilePath"));
        //Read data by line number
        WebUI.logConsole(TxtFileHelpers.readLineTxtFile(PropertiesHelpers.getValue("txtFilePath"), 0));
    }

    @Test
    public void testExcelFile1() {
        PropertiesHelpers.loadAllFiles();
        WebUI.logConsole(Helpers.getCurrentDir() + PropertiesHelpers.getValue("excelClients"));
        //  Handle Excel file
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(Helpers.getCurrentDir() + PropertiesHelpers.getValue("excelClients"), "SignInModel");
        WebUI.logConsole(excelHelpers.getCellData(1, "EMAIL"));
        WebUI.logConsole(excelHelpers.getCellData(1, "PASSWORD"));
        excelHelpers.setCellData("pass", 1, "EXPECTED_TITLE");
    }

    @Test()
    public void testExcelFile2() throws Exception {
        PropertiesHelpers.loadAllFiles();
        ExcelHelpers excelHelpers = new ExcelHelpers();
        WebUI.logConsole(excelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH, "SignInModel", 1, 2));
        //WebUI.logConsole(ExcelHelpers.getDataReflection(Helpers.getCurrentDir() + "src/test/resources/testdatafile/ClientsDataExcel.xlsx", "SignInModel", 1, 2));

    }

    @Test
    public void connectDBMySQL() throws SQLException, ClassNotFoundException {
        //Này connect DB mẫu Free. Các bạn dùng thằng khác thì đổi thông tin connect mẫu bên dưới là được.
//        https://www.phpmyadmin.co/
//        Host: sql6.freesqldatabase.com
//        Database name: sql6464696
//        Database user: sql6464696
//        Database password: LIAGIkgd44
//        Port number: 3306

        Connection connection = DatabaseHelpers.getMySQLConnection("sql6.freesqldatabase.com", "sql6464696", "sql6464696", "LIAGIkgd44");

        // Tạo đối tượng Statement.
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM `company`";

        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        ResultSet rs = statement.executeQuery(sql);

        WebUI.logConsole(rs);

        // Duyệt trên kết quả trả về.
        while (rs.next()) {// Di chuyển con trỏ xuống bản ghi kế tiếp.
            int Id = rs.getInt(1);
            String COMPANY_ID = rs.getString("COMPANY_ID");
            String COMPANY_NAME = rs.getString("COMPANY_NAME");
            String COMPANY_CITY = rs.getString("COMPANY_CITY");
            WebUI.logConsole("--------------------");
            WebUI.logConsole("COMPANY_ID:" + COMPANY_ID);
            WebUI.logConsole("COMPANY_NAME:" + COMPANY_NAME);
            WebUI.logConsole("COMPANY_CITY:" + COMPANY_CITY);
        }

        // Đóng kết nối
        connection.close();
    }

}
