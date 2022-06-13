/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.helpers;

import java.awt.Color;
import java.io.*;
import java.util.*;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.exceptions.FrameworkException;
import anhtester.com.exceptions.InvalidPathForExcelException;
import anhtester.com.utils.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelpers {

    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public int rowNumber; //Row Number
    public int columnNumber; //Column Number

    //    Set Excel file
    public void setExcelFile(String excelPath, String sheetName) {
        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    Log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sheetName.isEmpty() || sheetName.equals("")) {
                try {
                    Log.info("The Sheet Name is empty.");
                    throw new InvalidPathForExcelException("The Sheet Name is empty.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet(sheetName);
            //sh = wb.getSheetAt(0); //0 - index of 1st sheet
            if (sh == null) {
//                sh = wb.createSheet(sheetName);
                try {
                    Log.info("Sheet name not found.");
                    throw new InvalidPathForExcelException("Sheet name not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            excelFilePath = excelPath;

            //adding all the column header names to the map 'columns'
            sh.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

            Log.info("Set Excel file " + excelPath + " and selected Sheet: " + sheetName);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Phương thức này nhận số hàng làm tham số và trả về dữ liệu của hàng đó.
    public Row getRowData(int rowNum) {
        row = sh.getRow(rowNum);
        return row;
    }

    public Object[][] getDataArray(String excelPath, String sheetName, int startCol, int totalCols) {

        Object[][] data = null;
        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    Log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sheetName.isEmpty() || sheetName.equals("")) {
                try {
                    Log.info("The Sheet Name is empty.");
                    throw new InvalidPathForExcelException("The Sheet Name is empty.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            wb = new XSSFWorkbook(fis);
            sh = wb.getSheet(sheetName);

            if (sh == null) {
                try {
                    Log.info("Sheet name not found.");
                    throw new InvalidPathForExcelException("Sheet name not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int noOfRows = sh.getPhysicalNumberOfRows();
            //int noOfCols = row.getLastCellNum();
            int noOfCols = totalCols + 1;

            System.out.println("Số Dòng: " + (noOfRows - 1));
            System.out.println("Số Cột: " + (noOfCols - startCol));

            data = new String[noOfRows - 1][noOfCols - startCol];
            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols - startCol; j++) {
                    data[i - 1][j] = getCellData(i, j + startCol);
                    System.out.println(data[i - 1][j]);
                }
            }
        } catch (Exception e) {
            System.out.println("The exception is: " + e.getMessage());
        }
        return data;
    }

    public Object[][] getTableArray(String filePath, String sheetName, int iTestCaseRow) throws Exception {

        String[][] tabArray = null;

        try {
            FileInputStream ExcelFile = new FileInputStream(filePath);

            // Access the required test data sheet
            wb = new XSSFWorkbook(ExcelFile);
            sh = wb.getSheet(sheetName);

            int startCol = 1;
            int ci = 0, cj = 0;

            int totalRows = 1;
            int totalCols = 2;

            tabArray = new String[totalRows][totalCols];

            for (int j = startCol; j <= totalCols; j++, cj++) {
                tabArray[ci][cj] = getCellData(iTestCaseRow, j);
                System.out.println(tabArray[ci][cj]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        return (tabArray);
    }

    public Object[][] getDataHashTable(String excelPath, String sheetName, int startRow, int endRow) {

        Object[][] data = null;
        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    Log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            wb = new XSSFWorkbook(fis);
            sh = wb.getSheet(sheetName);

            int rows = getRowCount();
            int columns = getColumnCount();
            //System.out.println("Row: " + rows + " - Column: " + columns);
            data = new Object[endRow - startRow][1];
            Hashtable<String, String> table = null;
            for (int rowNums = startRow; rowNums < endRow; rowNums++) {
                table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    // data[rowNums-2][colNum] = excel.getCellData(sheetName, colNum, rowNums);
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));
                    data[rowNums - startRow][0] = table;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getTestCaseName(String sTestCase) throws Exception {
        String value = sTestCase;
        try {
            int posi = value.indexOf("@");
            value = value.substring(0, posi);
            posi = value.lastIndexOf(".");

            value = value.substring(posi + 1);
            return value;

        } catch (Exception e) {
            throw (e);
        }
    }

    public int getRowContains(String sTestCaseName, int colNum) throws Exception {
        int i;
        try {
            int rowCount = getRowUsed();
            for (i = 0; i < rowCount; i++) {
                if (getCellData(i, colNum).equalsIgnoreCase(sTestCaseName)) {
                    break;
                }
            }
            return i;

        } catch (Exception e) {
            throw (e);
        }

    }

    public int getRowUsed() throws Exception {
        try {
            int RowCount = sh.getLastRowNum();
            return RowCount;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    // Get cell data
    public String getCellData(int rownum, int colnum) {
        try {
            cell = sh.getRow(rownum).getCell(colnum);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public String getCellData(int rowNum, String columnName) {
        return getCellData(rowNum, columns.get(columnName));
    }

    public int getRows() {
        return sh.getPhysicalNumberOfRows();
    }

    public int getRowCount() {
        int rowCount = sh.getLastRowNum() + 1;
        return rowCount;
    }

    public int getColumnCount() {
        row = sh.getRow(0);
        int colCount = row.getLastCellNum();
        return colCount;
    }

    // Write data to excel sheet
    public void setCellData(String text, int rowNumber, int colNumber) {
        try {
            row = sh.getRow(rowNumber);
            if (row == null) {
                row = sh.createRow(rowNumber);
            }
            cell = row.getCell(colNumber);

            if (cell == null) {
                cell = row.createCell(colNumber);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            if (text == "pass" || text == "passed" || text == "Pass" || text == "Passed") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            } else {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCellData(String text, int rowNum, String columnName) {
        try {
            row = sh.getRow(rowNum);
            if (row == null) {
                row = sh.createRow(rowNum);
            }
            cell = row.getCell(columns.get(columnName));

            if (cell == null) {
                cell = row.createCell(columns.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            if (text == "pass" || text == "passed" || text == "Pass" || text == "Passed") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            } else {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
