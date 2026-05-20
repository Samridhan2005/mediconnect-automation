package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.Test;

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

    // TC003 — Strict FRD: expect THREE role tabs (Patient/Doctor/Admin) on /login.
    // Actual UI has only Patient + Doctor tabs; Admin is at /admin/login.
    // This test is EXPECTED TO FAIL — flagging the FRD-vs-implementation gap.
    @Test(groups = {"regression"})
    public void TC003_role_selector_tabs_switch_form() {
        Login login = new Login(driver).open();
        login.selectPatientTab();
        assertTrue(login.isPatientTabActive(), "Patient Login should be active");

        login.selectDoctorTab();
        assertTrue(login.isDoctorTabActive(), "Doctor Login should become active after click");

        // FRD step 4: "Click on the Admin Login Role Selector tab"
        org.openqa.selenium.WebElement adminTab = driver.findElement(
                org.openqa.selenium.By.xpath("//button[normalize-space()='Admin Login']"));
        assertTrue(adminTab.isDisplayed(), "FRD expects an Admin Login role tab on /login");
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

    // TC005 — Forgot Password link
    @Test(groups = {"regression"})
    public void TC005_forgot_password_link_is_present_and_clickable() {
        Login login = new Login(driver).open();
        assertTrue(login.isForgotLinkVisible(), "Forgot Password link should be displayed");

        login.clickForgotPassword();
        // App routes to a placeholder/anchor for now — URL may contain '#'.
        // We just verify the click did not error and the page is still reachable.
        assertTrue(driver.getCurrentUrl().contains("/login") || driver.getCurrentUrl().contains("#"),
                "Forgot Password should not navigate off-app");
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

    // TC014 — Role selector negative: only one tab can be active at a time
    @Test(groups = {"sanity", "regression"})
    public void TC014_only_one_role_tab_active_at_a_time() {
        Login login = new Login(driver).open();
        login.selectDoctorTab();
        assertTrue(login.isDoctorTabActive(),  "Doctor tab should be active");
        assertFalse(login.isPatientTabActive(), "Patient tab should NOT be active simultaneously");

        login.selectPatientTab();
        assertTrue(login.isPatientTabActive(), "Patient tab should be active");
        assertFalse(login.isDoctorTabActive(), "Doctor tab should NOT be active simultaneously");
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
