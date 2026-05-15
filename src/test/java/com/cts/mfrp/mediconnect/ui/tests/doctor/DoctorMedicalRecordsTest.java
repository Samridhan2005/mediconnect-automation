package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC044, TC045 — Doctor Medical Records page. */
public class DoctorMedicalRecordsTest extends BaseDoctorTest {

    // TC044 — Medical Records UI + patient search
    @Test
    public void TC044_doctor_medical_records_ui_search() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        assertTrue(driver.findElements(page.searchInput).size() > 0,
                "Patient search bar should be visible");
        assertTrue(driver.findElements(page.newRecordBtn).size() > 0,
                "+ New Record button should be visible");
    }

    // TC045 — Create new record form
    @Test
    public void TC045_doctor_create_new_record() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        List<WebElement> patientRows = driver.findElements(page.patientRows);
        if (!patientRows.isEmpty()) {
            patientRows.get(0).click();
            try { Thread.sleep(700); } catch (InterruptedException ignored) {}
        }
        List<WebElement> btn = driver.findElements(page.newRecordBtn);
        if (!btn.isEmpty() && btn.get(0).isEnabled()) {
            btn.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            for (String label : List.of("Record Type", "Date", "Chief Complaint", "Diagnosis", "Doctor's Notes")) {
                assertTrue(driver.findElements(By.xpath(
                                "//*[contains(normalize-space(),\"" + label + "\")]")).size() > 0,
                        "New Record form label missing: " + label);
            }
        }
    }
}
