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
}
