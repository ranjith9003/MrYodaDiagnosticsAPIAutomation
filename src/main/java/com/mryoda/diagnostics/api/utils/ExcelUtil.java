package com.mryoda.diagnostics.api.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel Utility for reading and writing Excel files
 */
public class ExcelUtil {
    
    private Workbook workbook;
    private Sheet sheet;
    private String filePath;
    
    /**
     * Constructor to initialize Excel file
     */
    public ExcelUtil(String filePath, String sheetName) {
        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            fis.close();
        } catch (Exception e) {
            LoggerUtil.error("Error initializing Excel file", e);
            throw new RuntimeException("Failed to initialize Excel file: " + filePath, e);
        }
    }
    
    /**
     * Get row count
     */
    public int getRowCount() {
        return sheet.getLastRowNum() + 1;
    }
    
    /**
     * Get cell count in a row
     */
    public int getCellCount(int rowNum) {
        Row row = sheet.getRow(rowNum);
        return row != null ? row.getLastCellNum() : 0;
    }
    
    /**
     * Get cell data as string
     */
    public String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) return "";
            
            Cell cell = row.getCell(colNum);
            if (cell == null) return "";
            
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        } catch (Exception e) {
            LoggerUtil.error("Error reading cell data", e);
            return "";
        }
    }
    
    /**
     * Get cell data by column name
     */
    public String getCellData(int rowNum, String columnName) {
        try {
            int colNum = getColumnNumber(columnName);
            return getCellData(rowNum, colNum);
        } catch (Exception e) {
            LoggerUtil.error("Error reading cell data by column name", e);
            return "";
        }
    }
    
    /**
     * Get column number by column name
     */
    private int getColumnNumber(String columnName) {
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getStringCellValue().trim().equalsIgnoreCase(columnName.trim())) {
                return i;
            }
        }
        throw new RuntimeException("Column '" + columnName + "' not found");
    }
    
    /**
     * Set cell data
     */
    public void setCellData(int rowNum, int colNum, String data) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }
            
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }
            
            cell.setCellValue(data);
            
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            
            LoggerUtil.info("Data written to cell [" + rowNum + "," + colNum + "]: " + data);
        } catch (Exception e) {
            LoggerUtil.error("Error writing cell data", e);
            throw new RuntimeException("Failed to write cell data", e);
        }
    }
    
    /**
     * Get all data as list of maps
     */
    public List<Map<String, String>> getAllData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try {
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();
            
            for (int i = 1; i < getRowCount(); i++) {
                Map<String, String> rowData = new HashMap<>();
                Row row = sheet.getRow(i);
                
                if (row != null) {
                    for (int j = 0; j < colCount; j++) {
                        Cell headerCell = headerRow.getCell(j);
                        Cell dataCell = row.getCell(j);
                        
                        String header = headerCell != null ? headerCell.getStringCellValue() : "";
                        String value = dataCell != null ? new DataFormatter().formatCellValue(dataCell) : "";
                        
                        rowData.put(header, value);
                    }
                    dataList.add(rowData);
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Error reading all data from Excel", e);
        }
        
        return dataList;
    }
    
    /**
     * Get test data based on test case name
     */
    public Map<String, String> getTestData(String testCaseName) {
        for (Map<String, String> data : getAllData()) {
            if (data.get("TestCase").equalsIgnoreCase(testCaseName)) {
                return data;
            }
        }
        return null;
    }
    
    /**
     * Close workbook
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            LoggerUtil.error("Error closing workbook", e);
        }
    }
}
