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

    public static Map<String, String> addPatient(String testId) {
        return read("AddPatient", testId);
    }

    public static Object[][] addPatientIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "AddPatient", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read AddPatient testIds", e);
        }
    }

    public static Map<String, String> newAppointment(String testId) {
        return read("NewAppointment", testId);
    }

    public static Object[][] newAppointmentIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "NewAppointment", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read NewAppointment testIds", e);
        }
    }

    public static Map<String, String> requestLabTest(String testId) {
        return read("RequestLabTest", testId);
    }

    public static Object[][] requestLabTestIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "RequestLabTest", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read RequestLabTest testIds", e);
        }
    }

    public static Map<String, String> newRecord(String testId) {
        return read("NewRecord", testId);
    }

    public static Object[][] newRecordIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "NewRecord", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read NewRecord testIds", e);
        }
    }

    public static Map<String, String> scheduleSession(String testId) {
        return read("ScheduleSession", testId);
    }

    public static Object[][] scheduleSessionIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "ScheduleSession", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read ScheduleSession testIds", e);
        }
    }

    public static Map<String, String> addItem(String testId) {
        return read("AddItem", testId);
    }

    public static Object[][] addItemIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "AddItem", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read AddItem testIds", e);
        }
    }

    public static Map<String, String> aiAssistant(String testId) {
        return read("AiAssistant", testId);
    }

    public static Object[][] aiAssistantIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "AiAssistant", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read AiAssistant testIds", e);
        }
    }

    public static Map<String, String> doctorRegister(String testId) {
        return read("DoctorRegister", testId);
    }

    public static Object[][] doctorRegisterIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "DoctorRegister", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read DoctorRegister testIds", e);
        }
    }

    public static Map<String, String> doctorLogin(String testId) {
        return read("DoctorLogin", testId);
    }

    public static Object[][] doctorLoginIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "DoctorLogin", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read DoctorLogin testIds", e);
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
