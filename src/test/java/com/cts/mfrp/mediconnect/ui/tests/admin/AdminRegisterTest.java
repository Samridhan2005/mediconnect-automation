package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Tests for the Admin registration page at /admin/register.
 * Reachable via the "Create admin account →" link from /admin/login.
 *
 * NOTE: Locators below are starter guesses — once we have the live DOM
 * we'll tighten them. For now this verifies the page loads and exposes
 * the basic fields you'd expect on a register form.
 */
public class AdminRegisterTest extends UiBaseTest {

    private static final String PATH = "/admin/register";

    private void openRegisterPage() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(d -> d.getCurrentUrl().contains(PATH));
    }

    // Merged admin_register_page_loads + admin_register_page_has_back_to_login_link
    @Test(groups = {"regression"})
    public void admin_register_page_loads_and_back_link() {
        openRegisterPage();
        assertTrue(driver.getCurrentUrl().contains(PATH),
                "Browser should be on /admin/register");
        assertTrue(driver.getTitle().toLowerCase().contains("mediconnect"),
                "Tab title should still be MediConnect");
        // Look for any link/button that takes the user back to login
        assertTrue(driver.findElements(By.xpath(
                        "//a[contains(translate(normalize-space(),'BACK TO LOGINSIGNIN','back to loginsignin'),'login') " +
                                "or contains(translate(normalize-space(),'BACK TO LOGINSIGNIN','back to loginsignin'),'sign in')]"
                )).size() > 0
                || driver.findElements(By.cssSelector("a.back-link")).size() > 0,
                "There should be a way to navigate back to the admin login from the register page");
    }

    // Merged admin_register_form_has_basic_fields + admin_register_form_has_submit_button
    @Test(groups = {"regression"})
    public void admin_register_form_fields_and_submit() {
        openRegisterPage();
        // Common register-form inputs: email + password at minimum
        assertTrue(driver.findElements(By.cssSelector("input[type='email']")).size() > 0,
                "Email input should be visible on register form");
        assertTrue(driver.findElements(By.cssSelector("input[type='password']")).size() > 0,
                "Password input should be visible on register form");
        assertTrue(driver.findElements(By.cssSelector("button[type='submit']")).size() > 0,
                "Submit button should be present on the register form");
    }
}
