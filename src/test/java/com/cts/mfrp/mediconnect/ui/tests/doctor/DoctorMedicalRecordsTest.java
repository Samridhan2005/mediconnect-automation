package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorMedicalRecords;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DoctorMedicalRecordsTest extends BaseDoctorTest {

    @Test(groups = {"regression"})
    public void TC044_doctor_medical_records_ui_search() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header 'Medical Records' not found");
        assertTrue(driver.findElements(page.searchInput).size() > 0, "Patient search bar should be visible");
        assertTrue(driver.findElements(page.newRecordBtn).size() > 0, "+ New Record button should be visible");
    }

    @Test(groups = {"regression"})
    public void TC045_doctor_create_new_record() {
        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(page.newRecordBtn));
        assertTrue(driver.findElements(page.newRecordBtn).size() > 0, "+ New Record button should be visible");

        driver.findElement(page.newRecordBtn).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(page.modalTitle));
        assertTrue(driver.findElements(page.modalTitle).size() > 0, "Modal title 'New Medical Record' should appear");

        for (String label : List.of("Patient", "Record Date", "Diagnosis", "Treatment", "Prescription", "Clinical Notes")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "New Record form label missing: " + label);
        }

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
        wait.until(d -> form.getAttribute("class").contains("ng-submitted"));

        assertTrue(driver.findElement(modal).isDisplayed(),
                "Submitting an empty form should NOT close the modal — required validation should block");
        assertTrue(form.getAttribute("class").contains("ng-invalid"),
                "Form should remain ng-invalid after empty submit. Got: " + form.getAttribute("class"));

        dialog.findElement(By.xpath(".//button[normalize-space()='Cancel']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modal));
    }

    @DataProvider(name = "newRecordData")
    public Object[][] newRecordData() {
        return TestData.newRecordIds();
    }

    @Test(groups = {"regression"}, dataProvider = "newRecordData")
    public void new_record_submit_creates_record(String testId) {
        Map<String, String> data = TestData.newRecord(testId);

        DoctorMedicalRecords page = new DoctorMedicalRecords(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.elementToBeClickable(page.newRecordBtn));
        driver.findElement(page.newRecordBtn).click();

        By modal = By.cssSelector("div.modal-card");
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(modal));

        assertEquals(dialog.findElement(By.cssSelector("h2.modal-title")).getText().trim(),
                "New Medical Record", "Modal title mismatch");

        assertTrue(dialog.findElement(By.cssSelector("div.autocomplete-wrap input")).isDisplayed(),
                "Patient autocomplete input not visible");
        assertTrue(dialog.findElement(By.cssSelector("input[type='date']")).isDisplayed(),
                "Record Date input not visible");

        WebElement patientInput = dialog.findElement(
                By.cssSelector("div.autocomplete-wrap input"));
        patientInput.click();
        patientInput.sendKeys(data.get("patientSearchQuery"));

        By suggestion = By.cssSelector("div.autocomplete-wrap li, "
                + "div.autocomplete-wrap [class*='suggest'], "
                + "div.autocomplete-wrap [class*='option'], "
                + "div.autocomplete-wrap [class*='item']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestion));
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Patient autocomplete did not show suggestions for query: '"
                    + data.get("patientSearchQuery") + "'");
        }
        List<WebElement> suggestions = driver.findElements(suggestion);
        assertFalse(suggestions.isEmpty(), "Patient autocomplete returned no suggestions");
        String chosenPatient = suggestions.get(0).getText().trim();
        suggestions.get(0).click();

        WebElement dateInput = dialog.findElement(
                By.cssSelector("input[type='date']"));
        dateInput.clear();
        dateInput.sendKeys(data.get("recordDate"));

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
        assertTrue(diagnosis.isDisplayed(), "Diagnosis input not visible");
        diagnosis.clear();
        diagnosis.sendKeys(data.get("diagnosis"));

        WebElement treatment = textInputs.stream()
                .filter(WebElement::isDisplayed)
                .filter(el -> {
                    String ph = el.getAttribute("placeholder");
                    return ph != null && ph.toLowerCase().contains("treatment");
                })
                .findFirst()
                .orElse(null);
        if (treatment != null && data.get("treatment") != null && !data.get("treatment").isBlank()) {
            treatment.clear();
            treatment.sendKeys(data.get("treatment"));
        }

        List<WebElement> textareas = dialog.findElements(By.tagName("textarea"));
        String prescription = data.get("prescription");
        if (!textareas.isEmpty() && prescription != null && !prescription.isBlank()) {
            textareas.get(0).clear();
            textareas.get(0).sendKeys(prescription);
        }
        String clinicalNotes = data.get("clinicalNotes");
        if (textareas.size() > 1 && clinicalNotes != null && !clinicalNotes.isBlank()) {
            textareas.get(1).clear();
            textareas.get(1).sendKeys(clinicalNotes);
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
