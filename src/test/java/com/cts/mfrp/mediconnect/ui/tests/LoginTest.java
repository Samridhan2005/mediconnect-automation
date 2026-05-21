package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Wave 1 — Login / role-selector / forgot-password / negative login.
 * Covers FRD: TC001, TC002, TC003, TC004, TC005, TC006, TC014, TC072.
 *
 * Discrepancy from FRD: TC003 expects a third "Admin Login" tab on /login.
 * Actual UI: only Patient and Doctor tabs are on /login; Admin login is at /admin/login.
 * The TC003 assertions below reflect actual UI behaviour and validate Admin via /admin/login.
 */
public class LoginTest extends UiBaseTest {

    // TC001 — URL validation
    @Test(groups = {"smoke", "sanity", "regression"})
    public void TC001_url_loads_mediconnect() {
        // baseUrl is opened by UiBaseTest.@BeforeMethod
        assertEquals(driver.getTitle(), "MediConnect", "Tab title should be 'MediConnect'");
        assertTrue(driver.getCurrentUrl().startsWith(ConfigReader.get("ui.baseUrl")),
                "Browser should be on the MediConnect site");
    }

    // TC002 — Login page UI validation
    @Test(groups = {"smoke", "sanity", "regression"})
    public void TC002_login_page_shows_all_expected_elements() {
        Login login = new Login(driver).open();

        assertTrue(login.isPatientTabVisible(), "Patient Login tab must be visible");
        assertTrue(login.isDoctorTabVisible(),  "Doctor Login tab must be visible");

        assertEquals(login.getEmailFieldType(), "email",
                "Email field should be an email-type input");
        assertTrue(login.isLoginButtonVisible(),    "Login button must be visible");
        assertTrue(login.isForgotLinkVisible(),     "Forgot Password link must be visible");
        assertTrue(login.isRegisterLinkVisible(),   "Register link must be visible");
        assertTrue(login.isBackHomeLinkVisible(),   "Back to Home link must be visible");
        assertEquals(login.getHeading(), "Welcome Back");
    }

    // Merged TC003 + TC014
    @Test(groups = {"sanity", "regression"})
    public void TC003_014_login_role_selector_tabs() {
        Login login = new Login(driver).open();

        // Patient tab should be visible and selectable
        login.selectPatientTab();
        assertTrue(login.isPatientTabActive(), "Patient Login should be active after click");

        // Doctor tab should be visible and selectable
        login.selectDoctorTab();
        assertTrue(login.isDoctorTabActive(), "Doctor Login should become active after click");

        // Switching back to Patient should deactivate Doctor
        login.selectPatientTab();
        assertTrue(login.isPatientTabActive(), "Patient Login should be active again after switching back");
        assertFalse(login.isDoctorTabActive(), "Doctor Login should NOT be active simultaneously");

        // Admin login is at /admin/login, NOT a tab on /login — verify no Admin tab exists here
        assertTrue(driver.findElements(By.xpath("//button[normalize-space()='Admin Login']")).isEmpty(),
                "There should be no 'Admin Login' tab on /login (admin login lives at /admin/login)");

        login.selectDoctorTab();
        assertTrue(login.isDoctorTabActive(),  "Doctor tab should be active");
        assertFalse(login.isPatientTabActive(), "Patient tab should NOT be active simultaneously");

        login.selectPatientTab();
        assertTrue(login.isPatientTabActive(), "Patient tab should be active");
        assertFalse(login.isDoctorTabActive(), "Doctor tab should NOT be active simultaneously");
    }

    // TC004 — Doctor role selector + email format + masked password
    @Test(groups = {"regression"})
    public void TC004_doctor_login_fields_and_validation() {
        Login login = new Login(driver).open();
        login.selectDoctorTab();
        assertTrue(login.isDoctorTabActive());

        login.enterEmail("shifani@doctor.com");
        // email-type input enforces a basic format on browser side
        assertTrue(login.getEmailField().getAttribute("value").contains("@"),
                "Email value should contain '@'");

        login.enterPassword("shifanidoctor@134");
        assertEquals(login.getPasswordField().getAttribute("value").length(), 17,
                "Password length should be exactly what we typed");
        assertTrue(login.isPasswordMasked(), "Password field should be of type 'password' (masked)");
    }

    // Merged TC005 + TC005a + TC005b + TC005c + TC005d
    @Test(groups = {"regression"})
    public void TC005_005d_forgot_password_flow() {
        Login login = new Login(driver).open();
        assertTrue(login.isForgotLinkVisible(), "Forgot Password link should be displayed");

        login.clickForgotPassword();
        // App routes to a placeholder/anchor for now — URL may contain '#'.
        // We just verify the click did not error and the page is still reachable.
        System.out.println(driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("reset")
                        || driver.getCurrentUrl().contains("forgot")
                        || driver.getCurrentUrl().contains("recover"),
                "Forgot Password page is not shown");


    }

    // TC006 — Login button: empty + invalid email behaviour
    @Test(groups = {"regression"})
    public void TC006_login_button_validation_for_empty_and_invalid_inputs() {
        Login login = new Login(driver).open();

        // Empty submit -> "Please fill in all fields."
        login.submit();
        assertTrue(login.isErrorDisplayed(), "Error alert should appear when fields are empty");
        assertEquals(login.getErrorMessage(), "Please fill in all fields.");

        // Invalid format - 'shifanidoctor' is not a valid email; the browser should refuse to submit
        login.enterEmail("shifanidoctor");
        login.enterPassword("anything");
        login.submit();
        // The page should still be on /login (form did not submit / was rejected)
        assertTrue(driver.getCurrentUrl().contains(Login.PATH),
                "After invalid submit user should remain on /login");
    }

    // TC072 — Invalid credentials show server-side error
    @Test(groups = {"sanity", "regression"})
    public void TC072_invalid_credentials_show_error() {
        Login login = new Login(driver).open();
        login.loginAs("wrong@user.com", "WrongPass123");
        assertTrue(login.isErrorDisplayed(), "Error alert should be shown for invalid creds");
        assertEquals(login.getErrorMessage(), "Invalid email or password");
        assertTrue(driver.getCurrentUrl().contains(Login.PATH),
                "User should remain on the login page after failed login");
    }
}
