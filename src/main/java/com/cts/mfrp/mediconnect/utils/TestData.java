package com.cts.mfrp.mediconnect.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class TestData {

    private static final String FILE = "src/test/resources/testdata/TestData.xlsx";

    private TestData() {}

    public static Map<String, String> login(String testId) {
        return read("Logins", testId);
    }

    /**
     * Return Logins rows whose testId matches the substring filter.
     * If {@code expectation} is non-null, the row's "expectation" column must also match.
     * Output is shaped for TestNG @DataProvider: each Object[] is a single testId.
     */
    public static Object[][] loginIds(String testIdContains, String expectation) {
        try {
            List<Map<String, String>> all = ExcelUtils.getAllRows(FILE, "Logins");
            return all.stream()
                    .filter(rowMatches("testId", v -> v != null && v.contains(testIdContains)))
                    .filter(expectation == null
                            ? r -> true
                            : rowMatches("expectation", expectation::equalsIgnoreCase))
                    .map(row -> new Object[]{row.get("testId")})
                    .toArray(Object[][]::new);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Logins rows for filter testIdContains='"
                    + testIdContains + "' expectation='" + expectation + "'", e);
        }
    }

    private static Predicate<Map<String, String>> rowMatches(String column, Predicate<String> check) {
        return row -> check.test(row.get(column));
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

    public static Map<String, String> hospital(String testId) {
        return read("Hospitals", testId);
    }

    public static Object[][] hospitalIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "Hospitals", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Hospitals testIds", e);
        }
    }

    public static Map<String, String> appointment(String testId) {
        return read("Appointments", testId);
    }

    public static Object[][] appointmentIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "Appointments", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Appointments testIds", e);
        }
    }

    public static Map<String, String> newAppointment(String testId) {
        return read("NewAppointments", testId);
    }

    public static Object[][] newAppointmentIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "NewAppointments", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read NewAppointments testIds", e);
        }
    }

    public static Map<String, String> diagnosticsSearch(String testId) {
        return read("DiagnosticsSearch", testId);
    }

    public static Object[][] diagnosticsSearchIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "DiagnosticsSearch", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read DiagnosticsSearch testIds", e);
        }
    }

    public static Map<String, String> diagnosticsStatus(String testId) {
        return read("DiagnosticsStatus", testId);
    }

    public static Object[][] diagnosticsStatusIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "DiagnosticsStatus", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read DiagnosticsStatus testIds", e);
        }
    }

    public static Map<String, String> supplyOrder(String testId) {
        return read("SupplyOrders", testId);
    }

    public static Object[][] supplyOrderIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "SupplyOrders", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SupplyOrders testIds", e);
        }
    }

    public static Map<String, String> telemedicineSession(String testId) {
        return read("TelemedicineSessions", testId);
    }

    public static Object[][] telemedicineSessionIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "TelemedicineSessions", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read TelemedicineSessions testIds", e);
        }
    }

    public static Map<String, String> analyticsFilter(String testId) {
        return read("AnalyticsFilters", testId);
    }

    public static Object[][] analyticsFilterIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "AnalyticsFilters", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read AnalyticsFilters testIds", e);
        }
    }

    public static Map<String, String> patientBooking(String testId) {
        return read("PatientBookings", testId);
    }

    public static Object[][] patientBookingIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "PatientBookings", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PatientBookings testIds", e);
        }
    }

    public static Map<String, String> labReportAIQuestion(String testId) {
        return read("LabReportAIQuestions", testId);
    }

    public static Object[][] labReportAIQuestionIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "LabReportAIQuestions", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read LabReportAIQuestions testIds", e);
        }
    }

    public static Map<String, String> aiAssistantInteraction(String testId) {
        return read("AiAssistantInteractions", testId);
    }

    public static Object[][] aiAssistantInteractionIds() {
        try {
            return ExcelUtils.getColumnValues(FILE, "AiAssistantInteractions", "testId");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read AiAssistantInteractions testIds", e);
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
