package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC044, TC045 — Doctor Medical Records page. */
public class DoctorMedicalRecordsTest extends BaseDoctorTest {

    // TC044 — Medical Records UI + patient search
    @Test
    public void TC044_doctor_medical_records_ui_search() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header 'Medical Records' not found");
        assertTrue(driver.findElements(page.searchInput).size() > 0, "Patient search bar should be visible");
        assertTrue(driver.findElements(page.newRecordBtn).size() > 0, "+ New Record button should be visible");
    }

    // TC045 — New Medical Record modal form fields
    @Test
    public void TC045_doctor_create_new_record() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1 — + New Record button is in top-right, visible without patient selection
        wait.until(ExpectedConditions.elementToBeClickable(page.newRecordBtn));
        assertTrue(driver.findElements(page.newRecordBtn).size() > 0, "+ New Record button should be visible");

        // Step 2 — Click + New Record, modal should open
        driver.findElement(page.newRecordBtn).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(page.modalTitle));
        assertTrue(driver.findElements(page.modalTitle).size() > 0, "Modal title 'New Medical Record' should appear");

        // Steps 3-5 — Verify actual form fields visible in modal
        for (String label : List.of("Patient", "Record Date", "Diagnosis", "Treatment", "Prescription", "Clinical Notes")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "New Record form label missing: " + label);
        }

        // Save button should be present
        assertTrue(driver.findElements(page.saveRecordBtn).size() > 0, "'Create Record' button should be visible");
    }
}
