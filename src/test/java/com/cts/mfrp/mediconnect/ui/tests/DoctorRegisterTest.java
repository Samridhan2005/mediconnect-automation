package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.DoctorRegister;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * Doctor registration tests for /register (after switching to the Doctor tab).
 */
public class DoctorRegisterTest extends UiBaseTest {

    @DataProvider(name = "doctorRegistrations")
    public Object[][] doctorRegistrations() {
        return TestData.doctorRegisterIds();
    }

    @Test(groups = {"regression"}, dataProvider = "doctorRegistrations")
    public void doctor_registration_lands_on_doctor_dashboard(String testId) {
        Map<String, String> data = TestData.doctorRegister(testId);
        String firstName = data.get("firstName");
        String password  = data.get("password");

        new DoctorRegister(driver).open()
                .enterFirstName(firstName)
                .enterLastName(data.get("lastName"))
                .enterEmail(data.get("email"))
                .enterPhone(data.get("phone"))
                .selectSpecialization(data.get("specialization"))
                .selectHospital(data.get("hospital"))
                .enterPassword(password)
                .enterConfirmPassword(password)
                .acceptTerms()
                .submit();

        wait.until(d -> d.getCurrentUrl().matches(".*/doctor/\\d+/.*"));
        assertTrue(driver.getCurrentUrl().matches(".*/doctor/\\d+/.*"),
                "After registration, user should land on /doctor/{id}/... (role = Doctor). URL was: "
                        + driver.getCurrentUrl());

        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'" + firstName + "')]")).size() > 0,
                "Doctor's first name '" + firstName + "' should be visible on the dashboard after registration");

        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'Supply Chain') or contains(normalize-space(),'Diagnostics')]")).size() > 0,
                "Doctor dashboard sidebar should display doctor-specific links (Supply Chain / Diagnostics)");
    }
}
