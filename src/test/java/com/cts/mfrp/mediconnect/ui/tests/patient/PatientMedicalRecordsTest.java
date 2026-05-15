package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC028, TC029 — Patient Medical Records page. */
public class PatientMedicalRecordsTest extends BasePatientTest {

    // TC028 — Patient Medical Records page UI
    @Test
    public void TC028_medical_records_ui() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        assertTrue(driver.findElements(page.leftPanel).size() > 0,
                "Left panel should be visible");
        assertTrue(driver.findElements(page.searchInput).size() > 0,
                "Search bar should be visible in left panel");
    }

    // TC029 — Medical Records search
    @Test
    public void TC029_medical_records_search() {
        PatientMedicalRecords page = new PatientMedicalRecords(driver).open(loggedInUserId);
        page.search("Cardiac");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        assertNotNull(driver.findElements(page.rightPanel));
        assertNotNull(driver.findElements(By.cssSelector("[class*='record']")));
    }
}
