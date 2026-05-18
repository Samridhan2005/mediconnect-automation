package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC016 — Admin login positive flow, plus extended coverage for /admin/login.
 *
 * Batches:
 *   A) UI elements visible          — TC017, TC018, TC019
 *   B) Negative validation          — TC020, TC021, TC022, TC023, TC024
 *   C) Link navigation              — TC025, TC026
 *
 * NOTE: This class extends UiBaseTest (not BaseAdminTest) because the tests
 * need to land on /admin/login directly, without being auto-logged-in first.
 */
public class AdminLoginTest extends UiBaseTest {

    // ============== TC016 — positive login ==============
    @Test
    public void TC016_admin_login_positive() {
        new AdminLogin(driver).open()
                .enterEmail(ConfigReader.get("admin.email"))
                .enterPassword(ConfigReader.get("admin.password"))
                .submit();

        wait.until(d -> d.getCurrentUrl().matches(".*/admin/\\d+/.*"));
        assertTrue(driver.getCurrentUrl().matches(".*/admin/\\d+/.*"),
                "After valid login admin should land on /admin/{id}/...");
    }


    @Test
    public void TC017_admin_login_page_ui_elements_visible() {
        AdminLogin login = new AdminLogin(driver).open();

        assertEquals(login.getHeading(), "Admin Sign In", "Heading should be 'Admin Sign In'");
        assertTrue(login.isSubtitleVisible(),
                "Subtitle 'Access restricted to authorised personnel only.' should be visible");
        assertTrue(login.isAdminPanelLabelVisible(),
                "'ADMIN CONTROL PANEL' label should be visible on the left panel");
        assertTrue(login.areFeatureBulletsVisible(),
                "All three feature bullets should be visible on the left panel");
        assertTrue(login.isEmailFieldVisible(),     "Email field should be visible");
        assertTrue(login.isLoginButtonVisible(),    "'Sign In to Admin Panel' button should be visible");
        assertTrue(login.isBackHomeLinkVisible(),   "'Back to Home' link should be visible");
        assertTrue(login.isRegisterLinkVisible(),   "'Create admin account' link should be visible");
        assertEquals(login.getEmailFieldType(), "email",
                "Email input should be of type 'email'");
    }

    // TC018 — Password is masked by default
    @Test
    public void TC018_admin_login_password_field_is_masked_by_default() {
        AdminLogin login = new AdminLogin(driver).open();
        login.enterPassword("Anything@123");
        assertTrue(login.isPasswordMasked(),
                "Password field should be of type 'password' (masked) by default");
    }

    // TC019 — Eye toggle flips password visibility
    @Test
    public void TC019_admin_login_password_visibility_toggle_works() {
        AdminLogin login = new AdminLogin(driver).open();
        login.enterPassword("Anything@123");
        assertTrue(login.isPasswordMasked(), "Should start masked");

        login.togglePasswordVisibility();
        assertFalse(login.isPasswordMasked(), "After toggling, password should be revealed (type=text)");

        login.togglePasswordVisibility();
        assertTrue(login.isPasswordMasked(), "After toggling again, password should be masked again");
    }

    // ============== Batch B — Negative validation ==============

    // TC020 — Empty submit shows an error / stays on page
    @Test
    public void TC020_admin_login_empty_fields_show_error_or_blocked() {
        AdminLogin login = new AdminLogin(driver).open();
        login.submit();
        // Either an in-app error appears, OR browser-native required-field blocks submission.
        // Both outcomes are acceptable — what's NOT acceptable is silently leaving /admin/login.
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "After empty submit user should remain on /admin/login");
    }

    // TC021 — Invalid email format is rejected (browser-native or app-level)
    @Test
    public void TC021_admin_login_invalid_email_format_rejected() {
        AdminLogin login = new AdminLogin(driver).open();
        login.enterEmail("not-an-email");
        login.enterPassword("Whatever@123");
        login.submit();
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "After invalid-email submit user should remain on /admin/login");
    }

    // TC022 — Wrong email with correct password → error, stay on page
    @Test
    public void TC022_admin_login_wrong_email_shows_error() {
        AdminLogin login = new AdminLogin(driver).open();
        login.loginAs("wrong.admin@mediconnect.com", ConfigReader.get("admin.password"));
        assertTrue(login.isErrorDisplayed(),
                "Error alert should appear for unknown admin email");
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "User should remain on /admin/login after wrong email");
    }

    // TC023 — Correct email with wrong password → error, stay on page
    @Test
    public void TC023_admin_login_wrong_password_shows_error() {
        AdminLogin login = new AdminLogin(driver).open();
        login.loginAs(ConfigReader.get("admin.email"), "WrongPass@999");
        assertTrue(login.isErrorDisplayed(),
                "Error alert should appear for incorrect admin password");
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "User should remain on /admin/login after wrong password");
    }

    // TC024 — Both wrong → error, stay on page
    @Test
    public void TC024_admin_login_both_credentials_wrong_shows_error() {
        AdminLogin login = new AdminLogin(driver).open();
        login.loginAs("nobody@mediconnect.com", "TotallyWrong@1");
        assertTrue(login.isErrorDisplayed(),
                "Error alert should appear when both credentials are wrong");
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "User should remain on /admin/login after fully invalid creds");
    }

    // ============== Batch C — Link navigation ==============

    // TC025 — "Back to Home" returns user to landing page
    @Test
    public void TC025_back_to_home_link_navigates_to_home() {
        AdminLogin login = new AdminLogin(driver).open();
        login.clickBackHome();

        wait.until(d -> !d.getCurrentUrl().contains(AdminLogin.PATH));
        assertFalse(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "URL should no longer contain '/admin/login' after clicking 'Back to Home'");
        assertTrue(driver.getCurrentUrl().startsWith(ConfigReader.get("ui.baseUrl")),
                "Browser should be back on the MediConnect landing page");
    }

    // TC026 — "Create admin account" link navigates to /admin/register
    @Test
    public void TC026_create_admin_account_link_navigates_to_register() {
        AdminLogin login = new AdminLogin(driver).open();
        login.clickRegister();

        wait.until(d -> d.getCurrentUrl().contains("/admin/register"));
        assertTrue(driver.getCurrentUrl().contains("/admin/register"),
                "Clicking 'Create admin account' should navigate to /admin/register");
    }
}
