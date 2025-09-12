package com.examplemathi.aiml.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelReader {

    private final String excelPath = "save1.xlsx";

    // Read Excel and return all values as a list
    public List<String> getAllData() {
        List<String> dataList = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(excelPath)) {
            if (is == null) {
                System.out.println("Excel file not found!");
                return dataList;
            }

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    String value = "";
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            value = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        default:
                            value = "";
                    }
                    dataList.add(value.trim());
                }
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    // Check if a user input matches any value in Excel
    public boolean checkMatch(String userInput) {
        List<String> allData = getAllData();
        for (String value : allData) {
            if (userInput.trim().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    // For testing: print Excel contents
    public void readExcel() {
        List<String> allData = getAllData();
        System.out.println("Excel Data:");
        for (String val : allData) {
            System.out.println(val);
        }
    }
}
