package com.cts.mfrp.mediconnect.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


public class ExcelUtils {

    /**
     * Read one row identified by a key value in a named column.
     * Returns a Map<columnName, cellValueAsString> for the matched row.
     * Throws if no row matches.
     */
    public static Map<String, String> getRowByKey(String filePath, String sheetName,
                                                  String keyColumn, String keyValue) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Row header = sheet.getRow(0);
            if (header == null) throw new RuntimeException("Sheet has no header row: " + sheetName);

            int keyCol = -1;
            int totalCols = header.getLastCellNum();
            String[] colNames = new String[totalCols];
            for (int c = 0; c < totalCols; c++) {
                String name = getCellValueAsString(header.getCell(c));
                colNames[c] = name;
                if (name.equalsIgnoreCase(keyColumn)) keyCol = c;
            }
            if (keyCol < 0) throw new RuntimeException("Key column '" + keyColumn + "' not found in " + sheetName);

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                String cellVal = getCellValueAsString(row.getCell(keyCol));
                if (cellVal.equals(keyValue)) {
                    String uniqueValue = String.valueOf(System.nanoTime());
                    Map<String, String> result = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    for (int c = 0; c < totalCols; c++) {
                        String raw = getCellValueAsString(row.getCell(c));
                        result.put(colNames[c], substitutePlaceholders(raw, uniqueValue));
                    }
                    System.out.println("[TestData] " + sheetName + "[" + keyValue + "] columns: " + result.keySet());
                    return result;
                }
            }
            throw new RuntimeException("No row in " + sheetName + " where " + keyColumn + "='" + keyValue + "'");
        }
    }

    /**
     * Replace placeholders in a cell value with runtime-generated values.
     * Supported: ${UNIQUE} → a nanosecond timestamp, identical for every cell in the same row read.
     */
    private static String substitutePlaceholders(String value, String uniqueValue) {
        if (value == null || value.isEmpty()) return value;
        return value.replace("${UNIQUE}", uniqueValue);
    }

    /**
     * Read all values from a single column, useful as a TestNG @DataProvider source.
     * Returns Object[][] where each row is one cell value (no header).
     */
    public static Object[][] getColumnValues(String filePath, String sheetName,
                                             String columnName) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Row header = sheet.getRow(0);
            if (header == null) throw new RuntimeException("Sheet has no header row: " + sheetName);

            int targetCol = -1;
            for (int c = 0; c < header.getLastCellNum(); c++) {
                if (getCellValueAsString(header.getCell(c)).equalsIgnoreCase(columnName)) {
                    targetCol = c;
                    break;
                }
            }
            if (targetCol < 0) throw new RuntimeException("Column '" + columnName + "' not found in " + sheetName);

            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                String v = getCellValueAsString(row.getCell(targetCol));
                if (v != null && !v.isEmpty()) {
                    rows.add(new Object[]{v});
                }
            }
            return rows.toArray(new Object[0][]);
        }
    }

    /**
     * Read all rows (except header) from the given sheet.
     * @param filePath  absolute or relative path to .xlsx file
     * @param sheetName name of the sheet to read
     * @return 2D Object array for TestNG @DataProvider
     */
    public static Object[][] getTestData(String filePath, String sheetName) throws IOException {
        FileInputStream fis   = new FileInputStream(filePath);
        Workbook        wb    = new XSSFWorkbook(fis);
        Sheet           sheet = wb.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + sheetName);
        }

        int totalRows = sheet.getLastRowNum();        // excludes header row 0
        int totalCols = sheet.getRow(0).getLastCellNum();

        Object[][] data = new Object[totalRows][totalCols];

        for (int r = 1; r <= totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            for (int c = 0; c < totalCols; c++) {
                Cell cell = row.getCell(c);
                data[r - 1][c] = getCellValueAsString(cell);
            }
        }

        wb.close();
        fis.close();
        return data;
    }

    /** Convert any cell type to String */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    return cell.getLocalDateTimeCellValue().toString();
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default:      return "";
        }
    }

    /**
     * Get a single cell value.
     * @param filePath  path to xlsx
     * @param sheetName sheet name
     * @param row       0-based row index
     * @param col       0-based column index
     */
    public static String getCellData(String filePath, String sheetName, int row, int col)
            throws IOException {
        FileInputStream fis   = new FileInputStream(filePath);
        Workbook        wb    = new XSSFWorkbook(fis);
        Sheet           sheet = wb.getSheet(sheetName);
        String          value = getCellValueAsString(sheet.getRow(row).getCell(col));
        wb.close(); fis.close();
        return value;
    }
}