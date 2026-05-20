package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC028, TC029 — Patient Medical Records page. */
public class PatientMedicalRecordsTest extends BasePatientTest {

    // Merged TC028 + TC167 + TC168
    @Test(groups = {"regression"})
    public void TC028_167_168_patient_medical_records_ui_and_header() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Medical Records header should be visible");
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.leftPanel)).isDisplayed(),
                "Left list panel should be visible");
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.searchInput)).isDisplayed(),
                "Search input should be visible");

        int records = page.waitForRecordsLoaded();
        assertTrue(records >= 1, "At least one medical record should load for the seeded patient");

        // TC167 — Page sub-label visible
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Your health history' should be visible");

        // TC168 — Top-right shows blood group chip + notification bell + hamburger menu
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
        assertTrue(driver.findElements(page.hamburgerMenu).size() > 0,
                "Hamburger menu should be visible");
    }

    // Merged TC029 + TC172
    @Test(groups = {"regression"})
    public void TC029_172_patient_medical_records_search() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        int total = page.waitForRecordsLoaded();
        assertTrue(total >= 1, "Need records to test filtering");

        page.search("Hypertension");
        int afterMatch = wait.until(d -> {
            int n = d.findElements(page.recordItems).size();
            return n >= 1 ? n : null;
        });
        assertTrue(afterMatch >= 1, "Search for 'Hypertension' should return at least one record");

        page.search("zzzzzNoMatch_" + System.currentTimeMillis());
        boolean filtered = wait.until(d -> d.findElements(page.recordItems).size() < total);
        assertTrue(filtered, "Search for a non-existent term should reduce visible records below the initial count");

        // TC172 — Search input is always visible regardless of whether records exist
        assertTrue(driver.findElements(page.searchInput).size() > 0,
                "Search input 'Search records...' should always be visible in the left panel");
    }

    // Merged TC169 + TC170 + TC171
    @Test(groups = {"regression"})
    public void TC169_170_171_patient_medical_records_panels_and_empty_state() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.leftPanel).size() > 0,
                "Left panel (record list with search) should be visible");
        assertTrue(driver.findElements(page.rightPanel).size() > 0,
                "Right panel (record detail area) should be visible");

        // TC170 — Left panel empty state shows 'No records found' when patient has no records
        int recordCount = driver.findElements(page.recordItems).size();
        if (recordCount == 0) {
            assertTrue(driver.findElements(page.emptyRecordsLeftMsg).size() > 0,
                    "Empty-state message 'No records found' should be visible when the patient has no records");
        } else {
            System.out.println("[TC170] Patient has " + recordCount + " record(s); skipping empty-state assertion.");
        }

        // TC171 — Right panel default state shows 'Select a record to view details' before any record is selected
        assertTrue(driver.findElements(page.selectRecordRightMsg).size() > 0,
                "Right panel should show 'Select a record to view details' before any record is selected");
    }

    // Merged TC173 + TC174
    @Test(groups = {"regression"})
    public void TC173_174_patient_medical_records_sidebar_and_no_nulls() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.sidebarPatientId).size() > 0,
                "Sidebar should display Patient ID");
        assertTrue(driver.findElements(page.sidebarPatientAge).size() > 0,
                "Sidebar should display patient's age");

        // TC174 — BUG-002 regression guard: 'null null' should NEVER appear
        int nullNullCount = driver.findElements(
                org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Sidebar / profile should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) — patient record has null firstName/lastName (BUG-002).");
    }
}
