package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class LoginTest extends UiBaseTest {

    // TC001 — URL validation
    @Test(groups = {"smoke", "sanity", "regression"})
    public void url_loads_mediconnect() {
        // baseUrl is opened by UiBaseTest.@BeforeMethod
        assertEquals(driver.getTitle(), "MediConnect", "Tab title should be 'MediConnect'");
        assertTrue(driver.getCurrentUrl().startsWith(ConfigReader.get("ui.baseUrl")),
                "Browser should be on the MediConnect site");
    }

    @Test(groups = {"smoke", "sanity", "regression"})
    public void login_page_shows_all_expected_elements() {
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
    public void login_role_selector_tabs() {
        Login login = new Login(driver).open();

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

    @Test(groups = {"regression"})
    public void doctor_login_fields_and_validation() {
        Map<String, String> data = TestData.login("TC004_doctor_valid");
        String email    = data.get("email");
        String password = data.get("password");

        Login login = new Login(driver).open();
        login.selectDoctorTab();
        assertTrue(login.isDoctorTabActive());

        login.enterEmail(email);
        assertTrue(login.getEmailField().getAttribute("value").contains("@"),
                "Email value should contain '@'");

        login.enterPassword(password);
        assertEquals(login.getPasswordField().getAttribute("value").length(), password.length(),
                "Password length should match the typed value");
        assertTrue(login.isPasswordMasked(), "Password field should be of type 'password' (masked)");
    }

    // Merged TC005 + TC005a + TC005b + TC005c + TC005d
    @Test(groups = {"regression"})
    public void forgot_password_flow() {
        Login login = new Login(driver).open();
        assertTrue(login.isForgotLinkVisible(), "Forgot Password link should be displayed");

        login.clickForgotPassword();

        System.out.println(driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("reset")
                        || driver.getCurrentUrl().contains("forgot")
                        || driver.getCurrentUrl().contains("recover"),
                "Forgot Password page is not shown");


    }

    // TC006 — Login button: empty + invalid email behaviour
    @Test(groups = {"regression"})
    public void login_button_validation_for_empty_and_invalid_inputs() {
        Login login = new Login(driver).open();

        // Empty submit -> "Please fill in all fields."
        login.submit();
        assertTrue(login.isErrorDisplayed(), "Error alert should appear when fields are empty");
        assertEquals(login.getErrorMessage(), "Please fill in all fields.");

        // Invalid format - browser should refuse to submit
        Map<String, String> invalid = TestData.login("TC006_invalid_format");
        login.enterEmail(invalid.get("email"));
        login.enterPassword(invalid.get("password"));
        login.submit();
        // The page should still be on /login (form did not submit / was rejected)
        assertTrue(driver.getCurrentUrl().contains(Login.PATH),
                "After invalid submit user should remain on /login");
    }

    @DataProvider(name = "doctorLogins")
    public Object[][] doctorLogins() {
        return TestData.doctorLoginIds();
    }

    @Test(groups = {"regression"}, dataProvider = "doctorLogins")
    public void doctor_login_with_valid_creds_lands_on_dashboard(String testId) {
        Map<String, String> data = TestData.doctorLogin(testId);

        new Login(driver).open()
                .selectDoctorTab()
                .enterEmail(data.get("email"))
                .enterPassword(data.get("password"))
                .submit();

        wait.until(d -> d.getCurrentUrl().matches(".*/doctor/\\d+/.*"));
        assertTrue(driver.getCurrentUrl().matches(".*/doctor/\\d+/.*"),
                "Doctor login should land on /doctor/{id}/... — got: " + driver.getCurrentUrl());

        assertTrue(driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'Supply Chain') or contains(normalize-space(),'Diagnostics')]")).size() > 0,
                "Doctor sidebar should be visible after login");
    }

    // TC072 — Invalid credentials show server-side error
    @Test(groups = {"sanity", "regression"})
    public void invalid_credentials_show_error() {
        Map<String, String> data = TestData.login("TC072_wrong_creds");

        Login login = new Login(driver).open();
        login.loginAs(data.get("email"), data.get("password"));
        assertTrue(login.isErrorDisplayed(), "Error alert should be shown for invalid creds");
        assertEquals(login.getErrorMessage(), "Invalid email or password");
        assertTrue(driver.getCurrentUrl().contains(Login.PATH),
                "User should remain on the login page after failed login");
    }
}
