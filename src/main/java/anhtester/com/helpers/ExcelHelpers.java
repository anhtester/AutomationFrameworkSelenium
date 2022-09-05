/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.helpers;

import anhtester.com.exceptions.InvalidPathForExcelException;
import anhtester.com.utils.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ExcelHelpers {

    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook workbook;
    private Sheet sheet;
    private Cell cell;
    private Row row;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public ExcelHelpers() {
        PropertiesHelpers.loadAllFiles();
    }

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
            if (sheetName.isEmpty()) {
                try {
                    Log.info("The Sheet Name is empty.");
                    throw new InvalidPathForExcelException("The Sheet Name is empty.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            //sh = wb.getSheetAt(0); //0 - index of 1st sheet
            if (sheet == null) {
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
            sheet.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

            Log.info("Set Excel file " + excelPath + " and selected Sheet: " + sheetName);

        } catch (Exception e) {
            e.getMessage();
            Log.error(e.getMessage());
        }
    }

    //Phương thức này nhận số hàng làm tham số và trả về dữ liệu của hàng đó.
    public Row getRowData(int rowNum) {
        row = sheet.getRow(rowNum);
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
            if (sheetName.isEmpty()) {
                try {
                    Log.info("The Sheet Name is empty.");
                    throw new InvalidPathForExcelException("The Sheet Name is empty.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                try {
                    Log.info("Sheet name not found.");
                    throw new InvalidPathForExcelException("Sheet name not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int noOfRows = sheet.getPhysicalNumberOfRows();
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
            e.getMessage();
            Log.error(e.getMessage());
        }
        return data;
    }

    public Object[][] getTableArray(String filePath, String sheetName, int iTestCaseRow) throws Exception {

        String[][] tabArray = null;

        try {
            FileInputStream ExcelFile = new FileInputStream(filePath);

            // Access the required test data sheet
            workbook = new XSSFWorkbook(ExcelFile);
            sheet = workbook.getSheet(sheetName);

            int startCol = 1;
            int ci = 0, cj = 0;

            int totalRows = 1;
            int totalCols = 2;

            tabArray = new String[totalRows][totalCols];

            for (int j = startCol; j <= totalCols; j++, cj++) {
                tabArray[ci][cj] = getCellData(iTestCaseRow, j);
                System.out.println(tabArray[ci][cj]);
            }

        } catch (IOException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        return tabArray;
    }

    public Object[][] getDataHashTable(String excelPath, String sheetName, int startRow, int endRow) {
        Log.info("Excel Path: " + excelPath);
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
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            int rows = getRows();
            int columns = getColumns();

            Log.info("Row: " + rows + " - Column: " + columns);
            Log.info("StartRow: " + startRow + " - EndRow: " + endRow);

            data = new Object[(endRow - startRow) + 1][1];
            Hashtable<String, String> table = null;
            for (int rowNums = startRow; rowNums <= endRow; rowNums++) {
                table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));
                }
                data[rowNums - startRow][0] = table;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.error(e.getMessage());
        }

        return data;

    }

    public String getTestCaseName(String testCaseName) {
        String value = testCaseName;
        int position = value.indexOf("@");
        value = value.substring(0, position);
        position = value.lastIndexOf(".");

        value = value.substring(position + 1);
        return value;
    }

    public int getRowContains(String sTestCaseName, int colNum) {
        int i;
        int rowCount = getRows();
        for (i = 0; i < rowCount; i++) {
            if (getCellData(i, colNum).equalsIgnoreCase(sTestCaseName)) {
                break;
            }
        }
        return i;
    }

    public int getRows() {
        try {
            return sheet.getLastRowNum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    public int getColumns() {
        try {
            row = sheet.getRow(0);
            return row.getLastCellNum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    // Get cell data
    public String getCellData(int rowNum, int colNum) {
        try {
            cell = sheet.getRow(rowNum).getCell(colNum);
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

    // Write data to excel sheet
    public void setCellData(String text, int rowNumber, int colNumber) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(colNumber);

            if (cell == null) {
                cell = row.createCell(colNumber);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            }
            if (text == "fail" || text == "passed") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
            Log.error(e.getMessage());
        }
    }

    public void setCellData(String text, int rowNumber, String columnName) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(columns.get(columnName));

            if (cell == null) {
                cell = row.createCell(columns.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            }
            if (text == "fail" || text == "passed") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }

            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
            Log.error(e.getMessage());
        }
    }


}
