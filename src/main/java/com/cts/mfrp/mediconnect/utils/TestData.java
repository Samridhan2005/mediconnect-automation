package com.cts.mfrp.mediconnect.utils;

import java.io.IOException;
import java.util.Map;

public final class TestData {

    private static final String FILE = "src/test/resources/testdata/TestData.xlsx";

    private TestData() {}

    public static Map<String, String> login(String testId) {
        return read("Logins", testId);
    }

    public static Map<String, String> patientRegister(String testId) {
        return read("PatientRegister", testId);
    }

    public static Object[][] patientRegisterIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "PatientRegister", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PatientRegister testIds", e);
        }
    }

    private static Map<String, String> read(String sheet, String testId) {
        try {
            return ExcelUtils.getRowByKey(FILE, sheet, "testId", testId);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to read sheet '" + sheet + "' for testId='" + testId + "'", e);
        }
    }
}
