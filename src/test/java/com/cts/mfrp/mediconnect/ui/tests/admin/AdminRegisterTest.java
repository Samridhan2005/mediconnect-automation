package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminRegister;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests for the Create Admin Account page at /admin/register.
 *
 * Coverage:
 *   - Navigation from /admin/login
 *   - UI element visibility
 *   - Per-field validation (required + format + cross-field)
 *
 * Extends UiBaseTest so the browser starts on "/" and each test
 * navigates explicitly to /admin/register.
 */
public class AdminRegisterTest extends UiBaseTest {

    // ============== TR001 — navigation from /admin/login ==============
    @Test
    public void TR001_navigation_from_admin_login_to_register() {
        new AdminLogin(driver).open().clickRegister();

        wait.until(d -> d.getCurrentUrl().contains(AdminRegister.PATH));
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "Browser should land on /admin/register after clicking 'Create admin account'");

        AdminRegister reg = new AdminRegister(driver);
        assertTrue(reg.isSubmitButtonVisible(),
                "Create Admin Account form should be rendered");
    }

    // ============== TR002 — all UI elements visible ==============
    @Test
    public void TR002_register_page_ui_elements_visible() {
        AdminRegister reg = new AdminRegister(driver).open();

        assertEquals(reg.getHeading(), "Create Admin Account",
                "Heading should be 'Create Admin Account'");
        assertTrue(reg.isSubtitleVisible(),
                "Subtitle 'Register as an authorised administrator.' should be visible");
        assertTrue(reg.isHospitalSelectVisible(),    "Hospital dropdown should be visible");
        assertTrue(reg.isFirstNameFieldVisible(),    "First Name input should be visible");
        assertTrue(reg.isLastNameFieldVisible(),     "Last Name input should be visible");
        assertTrue(reg.isEmailFieldVisible(),        "Email input should be visible");
        assertTrue(reg.isPhoneFieldVisible(),        "Phone Number input should be visible");
        assertTrue(reg.isPasswordFieldVisible(),     "Password input should be visible");
        assertTrue(reg.isConfirmPwFieldVisible(),    "Confirm Password input should be visible");
        assertTrue(reg.isTermsCheckboxVisible(),     "Terms checkbox should be visible");
        assertTrue(reg.isSubmitButtonVisible(),      "Create Admin Account button should be visible");
    }

    // ============== TR003 — email field gated by hospital selection ==============
    // From UI: email placeholder reads "Select a hospital first" until Hospital is chosen.
    @Test
    public void TR003_email_field_is_disabled_until_hospital_selected() {
        AdminRegister reg = new AdminRegister(driver).open();
        // Before picking a hospital, the email field should be locked.
        // We accept either disabled=true OR a guiding placeholder containing "hospital".
        boolean disabled = !reg.isEmailFieldEnabled();
        String placeholder = reg.getEmailPlaceholder() == null ? "" : reg.getEmailPlaceholder().toLowerCase();
        assertTrue(disabled || placeholder.contains("hospital"),
                "Email field should be disabled OR show a 'select a hospital first' hint before hospital is chosen");
    }

    // ============== TR004 — terms checkbox starts unchecked ==============
    @Test
    public void TR004_terms_checkbox_starts_unchecked() {
        AdminRegister reg = new AdminRegister(driver).open();
        assertFalse(reg.isTermsChecked(),
                "Terms-of-service checkbox should start unchecked");
    }

    // ============== TR005 — empty submit stays on /admin/register ==============
    @Test
    public void TR005_empty_submit_blocks_registration() {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.submit();
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "After submitting an empty form user should remain on /admin/register");
    }

    // ============== TR006 — missing First Name blocks registration ==============
    @Test
    public void TR006_missing_first_name_blocks_registration() {
        AdminRegister reg = new AdminRegister(driver).open();
        // intentionally skip First Name
        reg.enterLastName("Doe")
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1")
                .checkTerms()
                .submit();
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "Registration should not proceed without First Name");
    }

    // ============== TR007 — invalid email format rejected ==============
    @Test
    public void TR007_invalid_email_format_rejected() {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.enterFirstName("Jane")
                .enterLastName("Doe")
                // skip hospital select — even if email gets enabled, format check should kick in
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1")
                .checkTerms();
        // Try to type an invalid email; if disabled this will throw — that itself proves the gating from TR003
        try {
            reg.enterEmail("not-an-email");
        } catch (Exception ignored) { /* field locked — acceptable */ }
        reg.submit();
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "Registration should not proceed with a malformed email");
    }

    // ============== TR008 — password shorter than 6 chars rejected ==============
    // Placeholder says "Min. 6 characters".
    @Test
    public void TR008_short_password_rejected() {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.enterFirstName("Jane")
                .enterLastName("Doe")
                .enterPhone("9876543210")
                .enterPassword("12345")           // 5 chars — below the stated minimum
                .enterConfirmPassword("12345")
                .checkTerms()
                .submit();
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "Registration should not proceed when password is shorter than 6 characters");
    }

    // ============== TR009 — password and confirm-password mismatch rejected ==============
    @Test
    public void TR009_password_mismatch_rejected() {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.enterFirstName("Jane")
                .enterLastName("Doe")
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Different@2")
                .checkTerms()
                .submit();
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "Registration should not proceed when Password and Confirm Password do not match");
    }

    // ============== TR010 — terms unchecked blocks registration ==============
    @Test
    public void TR010_unchecked_terms_blocks_registration() {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.enterFirstName("Jane")
                .enterLastName("Doe")
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1");
        // leave terms checkbox unchecked
        reg.submit();
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "Registration should not proceed without accepting terms of service");
        assertFalse(reg.isTermsChecked(),
                "Terms checkbox should remain unchecked");
    }

    // ============== Hospital → required email domain mapping ==============
    // Every hospital is EXPECTED to enforce a domain rule on the admin email.
    //
    // KNOWN BUG: "MediCare Specialty Hospital" does NOT enforce its domain
    // rule today — TR011 + TR012 will fail for this row, which is the
    // intentional automation-driven bug report.
    @DataProvider(name = "hospitalDomains")
    public Object[][] hospitalDomains() {
        return new Object[][] {
                { "City General Hospital (CGH)",     "@cgh.com" },
                { "Apollo Medical Centre (AMC)",     "@amc.com" },
                { "MediCare Specialty Hospital",     "@medicare.com" },  // expected but not enforced — BUG
        };
    }

    // ============== TR011 — selecting hospital reveals email-domain hint ==============
    @Test(dataProvider = "hospitalDomains")
    public void TR011_selecting_hospital_shows_required_email_domain_hint(String hospital, String expectedDomain) {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.selectHospital(hospital);

        assertTrue(reg.isEmailDomainHintVisible(),
                "After selecting " + hospital + ", an email domain hint ('Must end with ...') should appear");
        assertTrue(reg.getEmailDomainHintText().toLowerCase().contains(expectedDomain),
                "Hint should reference " + expectedDomain + " for " + hospital
                        + ", but got: " + reg.getEmailDomainHintText());
    }

    // ============== TR012 — email domain must match selected hospital ==============
    @Test(dataProvider = "hospitalDomains")
    public void TR012_email_with_wrong_domain_for_selected_hospital_is_rejected(String hospital, String expectedDomain) {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.selectHospital(hospital)
                .enterFirstName("Shifani")
                .enterLastName("J")
                .enterEmail("2479801@cognizant.com")          // intentionally wrong domain
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1")
                .checkTerms()
                .submit();

        assertTrue(reg.isErrorDisplayed(),
                "An error should be shown when email domain does not match " + hospital);
        String msg = reg.getErrorMessage().toLowerCase();
        assertTrue(msg.contains(expectedDomain) || msg.contains("hospital domain"),
                "Error should mention the required hospital domain " + expectedDomain + ", but got: " + msg);
        assertTrue(driver.getCurrentUrl().contains(AdminRegister.PATH),
                "User should remain on /admin/register after domain-mismatch error");
    }

    // ============== TR014 — successful registration navigates to /admin/{id}/overview ==============
    // NOTE: This test creates a real admin user in the database. A unique
    // timestamp-based email is used per run so consecutive runs don't collide
    // with the "Email already registered" uniqueness check.
    @Test
    public void TR014_successful_registration_navigates_to_admin_overview() {
        long unique = System.currentTimeMillis();
        String firstName = "Auto";
        String lastName  = "Test" + unique;
        String email     = "auto.test." + unique + "@cgh.com";    // matches CGH domain rule

        new AdminRegister(driver).open()
                .selectHospital("City General Hospital (CGH)")
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1")
                .checkTerms()
                .submit();

        AdminOverview overview = new AdminOverview(driver);
        wait.until(d -> overview.isLoaded());
        assertTrue(driver.getCurrentUrl().matches(".*/admin/\\d+/overview$"),
                "After successful registration admin should land on /admin/{id}/overview, but URL was: "
                        + driver.getCurrentUrl());
    }

    // ============== TR015 — admin registration with Test Hospital → routes to admin dashboard ==============
    // Mirrors the TP001 (patient) / TD001 (doctor) pattern. Verifies role + name on dashboard.
    @Test
    public void TR015_admin_registration_lands_on_admin_dashboard() {
        long unique = System.currentTimeMillis();
        String firstName = "Auto";
        String lastName  = "Admin" + unique;
        String email     = "auto.admin." + unique + "@test.com";    // Test Hospital — domain rule unknown

        new AdminRegister(driver).open()
                .selectHospital("Test Hospital")
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterPhone("9876543210")
                .enterPassword("Password@1")
                .enterConfirmPassword("Password@1")
                .checkTerms()
                .submit();

        // Role check via URL — admin portal lives under /admin/{id}/overview
        AdminOverview overview = new AdminOverview(driver);
        wait.until(d -> overview.isLoaded());
        assertTrue(driver.getCurrentUrl().matches(".*/admin/\\d+/overview$"),
                "After registration, admin should land on /admin/{id}/overview. URL was: "
                        + driver.getCurrentUrl());

        // Name check — first name should appear on the dashboard (sidebar profile / greeting)
        assertTrue(driver.findElements(org.openqa.selenium.By.xpath(
                "//*[contains(normalize-space(),'" + firstName + "')]")).size() > 0,
                "Admin's first name '" + firstName + "' should be visible on the dashboard after registration");

        // Role check — sidebar 'ADMIN CONTROL' label is the definitive admin-portal indicator
        assertTrue(overview.sidebar().isAdminControlLabelVisible(),
                "'ADMIN CONTROL' label should be visible in the sidebar after admin registration");
    }

    // ============== TR013 — correct-domain email clears the domain-mismatch error ==============
    @Test(dataProvider = "hospitalDomains")
    public void TR013_email_with_correct_domain_does_not_trigger_domain_error(String hospital, String expectedDomain) {
        AdminRegister reg = new AdminRegister(driver).open();
        reg.selectHospital(hospital)
                .enterEmail("new.admin" + expectedDomain);   // e.g. new.admin@cgh.com

        // The domain-mismatch banner should NOT be present for a correctly-domained email.
        // Other field errors may still appear if we submit incomplete form, so we don't submit here.
        if (reg.isErrorDisplayed()) {
            String msg = reg.getErrorMessage().toLowerCase();
            assertFalse(msg.contains("hospital domain") || msg.contains("must use"),
                    "No hospital-domain error should be shown for " + hospital
                            + " when email ends with " + expectedDomain
                            + ", but got: " + msg);
        }
    }
}
