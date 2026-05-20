package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC028, TC029 — Patient Medical Records page. */
public class PatientMedicalRecordsTest extends BasePatientTest {

    // TC028 — UI structure: header, left panel with search, at least one record
    @Test(groups = {"regression"})
    public void TC028_medical_records_ui() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Medical Records header should be visible");
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.leftPanel)).isDisplayed(),
                "Left list panel should be visible");
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.searchInput)).isDisplayed(),
                "Search input should be visible");

        int records = page.waitForRecordsLoaded();
        assertTrue(records >= 1, "At least one medical record should load for the seeded patient");
    }

    // TC029 — Search filters the records list
    @Test(groups = {"regression"})
    public void TC029_medical_records_search() {
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
    }

    // ============== Extended coverage — TC167..TC174 ==============
    // Data-agnostic tests that pass for both empty and populated patients.

    // TC167 — Page sub-label visible
    @Test(groups = {"regression"})
    public void TC167_sub_label_visible() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Your health history' should be visible");
    }

    // TC168 — Top-right shows blood group chip + notification bell + hamburger menu
    @Test(groups = {"regression"})
    public void TC168_top_right_chip_and_bell() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
        assertTrue(driver.findElements(page.hamburgerMenu).size() > 0,
                "Hamburger menu should be visible");
    }

    // TC169 — Two-panel layout: both left and right panels visible
    @Test(groups = {"regression"})
    public void TC169_two_panel_layout_visible() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.leftPanel).size() > 0,
                "Left panel (record list with search) should be visible");
        assertTrue(driver.findElements(page.rightPanel).size() > 0,
                "Right panel (record detail area) should be visible");
    }

    // TC170 — Left panel empty state shows 'No records found' when patient has no records
    @Test(groups = {"regression"})
    public void TC170_left_panel_empty_state_when_no_records() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        int recordCount = driver.findElements(page.recordItems).size();
        if (recordCount == 0) {
            assertTrue(driver.findElements(page.emptyRecordsLeftMsg).size() > 0,
                    "Empty-state message 'No records found' should be visible when the patient has no records");
        } else {
            System.out.println("[TC170] Patient has " + recordCount + " record(s); skipping empty-state assertion.");
        }
    }

    // TC171 — Right panel default state shows 'Select a record to view details' before any record is selected
    @Test(groups = {"regression"})
    public void TC171_right_panel_default_state() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.selectRecordRightMsg).size() > 0,
                "Right panel should show 'Select a record to view details' before any record is selected");
    }

    // TC172 — Search input is always visible regardless of whether records exist
    @Test(groups = {"regression"})
    public void TC172_search_input_always_visible() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.searchInput).size() > 0,
                "Search input 'Search records...' should always be visible in the left panel");
    }

    // TC173 — Sidebar profile shows Patient ID and Age
    @Test(groups = {"regression"})
    public void TC173_sidebar_profile_shows_patient_id_and_age() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.sidebarPatientId).size() > 0,
                "Sidebar should display Patient ID");
        assertTrue(driver.findElements(page.sidebarPatientAge).size() > 0,
                "Sidebar should display patient's age");
    }

    // TC174 — BUG-002 regression guard: 'null null' should NEVER appear
    @Test(groups = {"regression"})
    public void TC174_no_null_null_anywhere() {
        new PatientMedicalRecords(driver).open(loggedInUserId);
        int nullNullCount = driver.findElements(
                org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Sidebar / profile should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) — patient record has null firstName/lastName (BUG-002).");
    }
}
