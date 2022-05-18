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
import java.util.ArrayList;

public class TestSimpleCode {

    @Test
    public void testRemoveAccent() {
        System.out.println(LanguageUtils.removeAccent("Võ Thái An"));
    }

    @Test
    public void testMakeSlug() {
        System.out.println(Helpers.makeSlug("Anh Tester Automation Testing"));
    }

    @Test
    public void testReadFileJSON() {
        System.out.println(JsonUtils.get("url"));
        System.out.println(JsonUtils.get("browser"));
        System.out.println(JsonUtils.get("button"));
    }

    @Test
    public void testGetCurrentDirectory() {
        System.out.println(Helpers.getCurrentDir());
    }

    @Test
    public void testGetAndSetPropertiesFile() {
        PropertiesHelpers.loadAllFiles();

        System.out.println(PropertiesHelpers.getValue("browser"));
        System.out.println(PropertiesHelpers.getValue("buttonTag"));
        System.out.println(PropertiesHelpers.getValue("buttonDangNhap"));
    }

    @Test
    public void testSplitString() {
        String s1 = "Automation, Testing, Selenium, Java";

        for (String arr : Helpers.splitString(s1, ", ")) {
            System.out.println(arr);
        }
    }

    @Test
    public void testEncryptDecryptData() {
        String pass = "123456";

        //Encrypt password
        System.out.println(DecodeUtils.encrypt(pass));
        //Decrypt password
        System.out.println(DecodeUtils.decrypt(DecodeUtils.encrypt(pass)));
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
        TxtFileHelpers.ReadTxtFile(PropertiesHelpers.getValue("txtFilePath"));
    }

    @Test
    public void testExcelFile1() {
        PropertiesHelpers.loadAllFiles();
        System.out.println(Helpers.getCurrentDir() + PropertiesHelpers.getValue("excelClients"));
        //  Handle Excel file
        ExcelHelpers.setExcelFile(Helpers.getCurrentDir() + PropertiesHelpers.getValue("excelClients"), "SignIn");
        System.out.println(ExcelHelpers.getCellData(1, "EMAIL"));
        System.out.println(ExcelHelpers.getCellData(1, "PASSWORD"));
        ExcelHelpers.setCellData("pass", 1, "EXPECTED_TITLE");
    }

    @Test()
    public void testExcelFile2() throws Exception {
        PropertiesHelpers.loadAllFiles();
        System.out.println(ExcelHelpers.getDataHashTable(Helpers.getCurrentDir() + FrameworkConstants.EXCEL_DATA_PATH_FULL, "SignIn", 1, 2));
        //System.out.println(ExcelHelpers.getDataReflection(Helpers.getCurrentDir() + "src/test/resources/testdatafile/ClientsDataExcel.xlsx", "SignIn", 1, 2));

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
            System.out.println("--------------------");
            System.out.println("COMPANY_ID:" + COMPANY_ID);
            System.out.println("COMPANY_NAME:" + COMPANY_NAME);
            System.out.println("COMPANY_CITY:" + COMPANY_CITY);
        }

        // Đóng kết nối
        connection.close();
    }

}
