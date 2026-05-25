package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.PatientRegister;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * Patient registration tests for /register.
 * Patient role is selected by default — no slider interaction needed.
 */
public class PatientRegisterTest extends UiBaseTest {

    // TP001 — Successful patient registration logs the user in and lands on the patient dashboard.
    // Verification: URL matches /patient/{id}/... AND the patient's name is visible on the dashboard.
    @DataProvider(name = "patientRegistrations")
    public Object[][] patientRegistrations() {
        return TestData.patientRegisterIds();
    }

    @Test(groups = {"regression"}, dataProvider = "patientRegistrations")
    public void patient_registration_lands_on_patient_dashboard(String testId) {
        Map<String, String> data = TestData.patientRegister(testId);
        String firstName = data.get("firstName");
        String password  = data.get("password");

        new PatientRegister(driver).open()
                .enterFirstName(firstName)
                .enterLastName(data.get("lastName"))
                .enterEmail(data.get("email"))
                .enterPhone(data.get("phone"))
                .enterDateOfBirth(data.get("dateOfBirth"))
                .selectBloodGroup(data.get("bloodGroup"))
                .selectGender(data.get("gender"))
                .enterPassword(password)
                .enterConfirmPassword(password)
                .acceptTerms()
                .submit();

        // Role check via URL — patient portal lives under /patient/{id}/...
        wait.until(d -> d.getCurrentUrl().matches(".*/patient/\\d+/.*"));
        assertTrue(driver.getCurrentUrl().matches(".*/patient/\\d+/.*"),
                "After registration, user should land on /patient/{id}/... (role = Patient). URL was: "
                        + driver.getCurrentUrl());

        // Name check — first name should appear somewhere on the dashboard (greeting / sidebar / avatar block)
        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'" + firstName + "')]")).size() > 0,
                "Patient's first name '" + firstName + "' should be visible on the dashboard after registration");

        // Additional role check — the patient dashboard sidebar contains the 'Health Overview' link
        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'Health Overview')]")).size() > 0,
                "Patient dashboard sidebar should display the 'Health Overview' navigation link");
    }
}
