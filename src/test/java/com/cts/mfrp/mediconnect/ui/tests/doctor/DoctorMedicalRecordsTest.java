package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/** FRD: TC044, TC045 — Doctor Medical Records page. */
public class DoctorMedicalRecordsTest extends BaseDoctorTest {

    // TC044 — Medical Records UI + patient search
    @Test(groups = {"regression"})
    public void TC044_doctor_medical_records_ui_search() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header 'Medical Records' not found");
        assertTrue(driver.findElements(page.searchInput).size() > 0, "Patient search bar should be visible");
        assertTrue(driver.findElements(page.newRecordBtn).size() > 0, "+ New Record button should be visible");
    }

    // TC045 — New Medical Record modal form fields
    @Test(groups = {"regression"})
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

    @Test(groups = {"regression"})
    public void new_record_empty_form_blocks_submit() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.elementToBeClickable(page.newRecordBtn));
        driver.findElement(page.newRecordBtn).click();

        By modal = By.cssSelector("div.modal-card");
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(modal));

        assertEquals(dialog.findElement(By.cssSelector("h2.modal-title")).getText().trim(),
                "New Medical Record", "Modal title mismatch");

        WebElement form = dialog.findElement(By.tagName("form"));
        assertTrue(form.getAttribute("class").contains("ng-invalid"),
                "Empty form should start as ng-invalid. Got: " + form.getAttribute("class"));

        for (String label : List.of("Patient", "Record Date", "Diagnosis")) {
            List<WebElement> reqMarkers = dialog.findElements(
                    By.xpath(".//label[contains(normalize-space(),'" + label + "')]"
                            + "//*[contains(@class,'req') or normalize-space()='*']"));
            assertFalse(reqMarkers.isEmpty(),
                    "Required-field marker (*) missing for: " + label);
        }

        WebElement submit = dialog.findElement(
                By.xpath(".//button[normalize-space()='Create Record']"));
        submit.click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        assertTrue(driver.findElement(modal).isDisplayed(),
                "Submitting an empty form should NOT close the modal — required validation should block");
        assertTrue(form.getAttribute("class").contains("ng-invalid"),
                "Form should remain ng-invalid after empty submit. Got: " + form.getAttribute("class"));

        dialog.findElement(By.xpath(".//button[normalize-space()='Cancel']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modal));
    }

    @Test(groups = {"regression"})
    public void new_record_submit_creates_record() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.elementToBeClickable(page.newRecordBtn));
        driver.findElement(page.newRecordBtn).click();

        By modal = By.cssSelector("div.modal-card");
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(modal));

        WebElement patientInput = dialog.findElement(
                By.cssSelector("div.autocomplete-wrap input"));
        patientInput.click();
        patientInput.sendKeys("a");

        By suggestion = By.cssSelector("div.autocomplete-wrap li, "
                + "div.autocomplete-wrap [class*='suggest'], "
                + "div.autocomplete-wrap [class*='option'], "
                + "div.autocomplete-wrap [class*='item']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestion));
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Patient autocomplete did not show suggestions after typing 'a'");
        }
        List<WebElement> suggestions = driver.findElements(suggestion);
        assertFalse(suggestions.isEmpty(), "Patient autocomplete returned no suggestions");
        String chosenPatient = suggestions.get(0).getText().trim();
        suggestions.get(0).click();

        WebElement dateInput = dialog.findElement(
                By.cssSelector("input[type='date']"));
        dateInput.clear();
        dateInput.sendKeys("06/15/2026");

        List<WebElement> textInputs = dialog.findElements(
                By.cssSelector("input[type='text'], input:not([type])"));
        WebElement diagnosis = textInputs.stream()
                .filter(WebElement::isDisplayed)
                .filter(el -> {
                    String ph = el.getAttribute("placeholder");
                    return ph != null && ph.toLowerCase().contains("diagnosis");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Diagnosis input not found by placeholder"));
        diagnosis.clear();
        diagnosis.sendKeys("Automated test diagnosis - routine check");

        WebElement treatment = textInputs.stream()
                .filter(WebElement::isDisplayed)
                .filter(el -> {
                    String ph = el.getAttribute("placeholder");
                    return ph != null && ph.toLowerCase().contains("treatment");
                })
                .findFirst()
                .orElse(null);
        if (treatment != null) {
            treatment.clear();
            treatment.sendKeys("Rest, hydration, follow-up in 7 days");
        }

        List<WebElement> textareas = dialog.findElements(By.tagName("textarea"));
        if (!textareas.isEmpty()) {
            textareas.get(0).clear();
            textareas.get(0).sendKeys("Paracetamol | 500mg | 1-0-1");
        }
        if (textareas.size() > 1) {
            textareas.get(1).clear();
            textareas.get(1).sendKeys("Created by Selenium automation suite");
        }

        WebElement form = dialog.findElement(By.tagName("form"));
        try {
            wait.until(d -> {
                String cls = form.getAttribute("class");
                return cls != null && cls.contains("ng-valid") && !cls.contains("ng-invalid");
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Form did not become ng-valid after filling required fields. "
                    + "Chosen patient: '" + chosenPatient + "'. "
                    + "Class: " + form.getAttribute("class"));
        }

        WebElement submit = dialog.findElement(
                By.xpath(".//button[normalize-space()='Create Record']"));
        assertTrue(submit.isEnabled(), "Create Record button disabled with valid form");
        submit.click();

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modal));
        } catch (org.openqa.selenium.TimeoutException e) {
            List<WebElement> errors = driver.findElements(
                    By.cssSelector("[class*='error'], [class*='alert'], [class*='toast']"));
            String errorMsg = errors.stream()
                    .filter(WebElement::isDisplayed)
                    .map(el -> el.getText().trim())
                    .filter(t -> !t.isEmpty())
                    .findFirst()
                    .orElse("(no visible error message)");
            org.testng.Assert.fail("Modal did not close after submit. "
                    + "Chosen patient: '" + chosenPatient + "'. "
                    + "Form class: " + form.getAttribute("class") + ". "
                    + "Error message: " + errorMsg);
        }
    }
}
