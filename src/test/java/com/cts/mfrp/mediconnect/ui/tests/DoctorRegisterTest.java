package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.DoctorRegister;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Doctor registration tests for /register (after switching to the Doctor tab).
 */
public class DoctorRegisterTest extends UiBaseTest {

    // TD001 — Successful doctor registration logs the user in and lands on the doctor dashboard.
    // Verification: URL matches /doctor/{id}/... AND the doctor's name is visible on the dashboard.
    @Test(groups = {"regression"})
    public void TD001_doctor_registration_lands_on_doctor_dashboard() {
        long unique = System.currentTimeMillis();
        String firstName = "Auto";
        String lastName  = "Doctor" + unique;
        String email     = "auto.doctor." + unique + "@test.com";   // matches "Test Hospital" domain (best guess)

        new DoctorRegister(driver).open()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterPhone("9876543210")
                .selectSpecialization("Cardiology")
                .selectHospital("Test Hospital")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1")
                .acceptTerms()
                .submit();

        // Role check via URL — doctor portal lives under /doctor/{id}/...
        wait.until(d -> d.getCurrentUrl().matches(".*/doctor/\\d+/.*"));
        assertTrue(driver.getCurrentUrl().matches(".*/doctor/\\d+/.*"),
                "After registration, user should land on /doctor/{id}/... (role = Doctor). URL was: "
                        + driver.getCurrentUrl());

        // Name check — first name should appear on the dashboard
        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'" + firstName + "')]")).size() > 0,
                "Doctor's first name '" + firstName + "' should be visible on the dashboard after registration");

        // Doctor-specific sidebar link — Supply Chain is unique to doctor portal per FRD §3.1
        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'Supply Chain') or contains(normalize-space(),'Diagnostics')]")).size() > 0,
                "Doctor dashboard sidebar should display doctor-specific links (Supply Chain / Diagnostics)");
    }
}
