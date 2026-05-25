package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC016 — Admin login positive flow, plus extended coverage for /admin/login.
 *
 * Credential-using tests (TC016, TC022_024, TC027) are now data-driven from the
 * "Logins" sheet in TestData.xlsx. Rows are filtered by testId containing "_admin_"
 * and the row's "expectation" column.
 *
 * NOTE: This class extends UiBaseTest (not BaseAdminTest) because the tests
 * need to land on /admin/login directly, without being auto-logged-in first.
 */
public class AdminLoginTest extends UiBaseTest {

    // ---------- Data providers ----------

    @DataProvider(name = "adminValidLogins")
    public Object[][] adminValidLogins() {
        return TestData.loginIds("_admin_", "success");
    }

    @DataProvider(name = "adminWrongCredentials")
    public Object[][] adminWrongCredentials() {
        // All admin rows whose expectation is one of the wrong-credential variants.
        Object[][] wrongEmail    = TestData.loginIds("_admin_", "wrongEmail");
        Object[][] wrongPassword = TestData.loginIds("_admin_", "wrongPassword");
        Object[][] bothWrong     = TestData.loginIds("_admin_", "bothWrong");
        Object[][] merged = new Object[wrongEmail.length + wrongPassword.length + bothWrong.length][];
        int i = 0;
        for (Object[] r : wrongEmail)    merged[i++] = r;
        for (Object[] r : wrongPassword) merged[i++] = r;
        for (Object[] r : bothWrong)     merged[i++] = r;
        return merged;
    }

    // ============== TC016 — positive login (data-driven) ==============
    // Clicking 'Sign In to Admin Panel' must navigate to /admin/{userId}/overview
    // (the Admin System Overview dashboard).
    @Test(groups = {"smoke", "sanity", "regression"}, dataProvider = "adminValidLogins")
    public void TC016_admin_login_positive(String testId) {
        Map<String, String> data = TestData.login(testId);

        new AdminLogin(driver).open()
                .enterEmail(data.get("email"))
                .enterPassword(data.get("password"))
                .submit();

        AdminOverview overview = new AdminOverview(driver);
        wait.until(d -> overview.isLoaded());
        assertTrue(driver.getCurrentUrl().matches(".*/admin/\\d+/overview$"),
                "[" + testId + "] After valid login admin should land on /admin/{id}/overview, but URL was: "
                        + driver.getCurrentUrl());
        assertTrue(overview.sidebar().isAdminControlLabelVisible(),
                "[" + testId + "] 'ADMIN CONTROL' label should be visible in the sidebar after login");
    }

    // Merged TC017 + TC018 + TC019
    @Test(groups = {"regression"})
    public void TC017_019_admin_login_ui_and_password_masking() {
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

        login.enterPassword("Anything@123");
        assertTrue(login.isPasswordMasked(),
                "Password field should be of type 'password' (masked) by default");

        assertTrue(login.isPasswordMasked(), "Should start masked");

        login.togglePasswordVisibility();
        assertFalse(login.isPasswordMasked(), "After toggling, password should be revealed (type=text)");

        login.togglePasswordVisibility();
        assertTrue(login.isPasswordMasked(), "After toggling again, password should be masked again");
    }

    // ============== Batch B — Negative validation ==============

    // Merged TC020 + TC021
    @Test(groups = {"regression"})
    public void TC020_021_admin_login_empty_and_invalid_format() {
        AdminLogin login = new AdminLogin(driver).open();
        login.submit();
        // Either an in-app error appears, OR browser-native required-field blocks submission.
        // Both outcomes are acceptable — what's NOT acceptable is silently leaving /admin/login.
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "After empty submit user should remain on /admin/login");

        login.enterEmail("not-an-email");
        login.enterPassword("Whatever@123");
        login.submit();
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "After invalid-email submit user should remain on /admin/login");
    }

    // ============== TC022_024 — wrong credentials (data-driven) ==============
    // Runs once per admin row in the Logins sheet whose expectation is
    // wrongEmail / wrongPassword / bothWrong. Each scenario must show an error
    // and remain on /admin/login.
    @Test(groups = {"regression"}, dataProvider = "adminWrongCredentials")
    public void TC022_024_admin_login_wrong_credentials(String testId) {
        Map<String, String> data = TestData.login(testId);

        AdminLogin login = new AdminLogin(driver).open();
        login.loginAs(data.get("email"), data.get("password"));

        assertTrue(login.isErrorDisplayed(),
                "[" + testId + "] Error alert should appear for invalid admin credentials");
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "[" + testId + "] User should remain on /admin/login after bad credentials");
    }

    // ============== Batch C — Link navigation ==============

    // Merged TC025 + TC026
    @Test(groups = {"regression"})
    public void TC025_026_admin_login_back_home_and_register_links() {
        AdminLogin login = new AdminLogin(driver).open();
        login.clickBackHome();

        wait.until(d -> !d.getCurrentUrl().contains(AdminLogin.PATH));
        assertFalse(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "URL should no longer contain '/admin/login' after clicking 'Back to Home'");
        assertTrue(driver.getCurrentUrl().startsWith(ConfigReader.get("ui.baseUrl")),
                "Browser should be back on the MediConnect landing page");
        login.clickRegister();

        wait.until(d -> d.getCurrentUrl().contains("/admin/register"));
        assertTrue(driver.getCurrentUrl().contains("/admin/register"),
                "Clicking 'Create admin account' should navigate to /admin/register");
    }

    // ============== TC027 — Admin sign-out (data-driven) ==============
    // Mirrors TC073 (doctor) and TC074 (patient). After clicking Logout in the
    // sidebar, admin must be redirected away from /admin/{id}/... and land on
    // /admin/login (or the landing page). Runs once per valid admin row.
    @Test(groups = {"smoke", "regression"}, dataProvider = "adminValidLogins")
    public void TC027_admin_sign_out_redirects_to_login_or_landing(String testId) {
        Map<String, String> data = TestData.login(testId);

        // Log in first — this class extends UiBaseTest, no auto-login.
        new AdminLogin(driver).open()
                .enterEmail(data.get("email"))
                .enterPassword(data.get("password"))
                .submit();

        AdminOverview overview = new AdminOverview(driver);
        wait.until(d -> overview.isLoaded());
        assertTrue(driver.getCurrentUrl().matches(".*/admin/\\d+/overview$"),
                "[" + testId + "] Pre-condition: admin should be logged in on /admin/{id}/overview");

        // Click Logout in the sidebar
        overview.sidebar().signOut();

        // Wait for the URL to leave the authenticated admin area
        wait.until(d -> !d.getCurrentUrl().matches(".*/admin/\\d+/.*"));

        assertFalse(driver.getCurrentUrl().matches(".*/admin/\\d+/.*"),
                "[" + testId + "] After logout, user should no longer be on an authenticated admin page. URL was: "
                        + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("/login")
                        || driver.getCurrentUrl().endsWith("/")
                        || driver.getCurrentUrl().equals(ConfigReader.get("ui.baseUrl")),
                "[" + testId + "] After logout, user should land on /admin/login or the home page. URL was: "
                        + driver.getCurrentUrl());
    }
}
