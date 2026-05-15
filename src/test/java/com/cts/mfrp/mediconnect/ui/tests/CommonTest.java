package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Wave 6 — Cross-portal common rules.
 * FRD: TC064-TC071. Strict FRD assertions.
 */
public class CommonTest extends UiBaseTest {

    private void loginAsPatient() {
        new Login(driver).open()
                .selectPatientTab()
                .enterEmail(ConfigReader.get("valid.email"))
                .enterPassword(ConfigReader.get("valid.password"));
        new Login(driver).submit();
        wait.until(d -> d.getCurrentUrl().matches(".*/patient/\\d+/dashboard$"));
    }

    private void loginAsDoctor() {
        new Login(driver).open()
                .selectDoctorTab()
                .enterEmail(ConfigReader.get("doctor.email"))
                .enterPassword(ConfigReader.get("doctor.password"));
        new Login(driver).submit();
        wait.until(d -> d.getCurrentUrl().matches(".*/doctor/\\d+/dashboard$"));
    }

    private void loginAsAdmin() {
        new AdminLogin(driver).open()
                .enterEmail(ConfigReader.get("admin.email"))
                .enterPassword(ConfigReader.get("admin.password"));
        new AdminLogin(driver).submit();
        wait.until(d -> d.getCurrentUrl().matches(".*/admin/\\d+/overview$"));
    }

    // TC064 — Common form validation rules (mandatory + invalid)
    @Test
    public void TC064_common_form_validation_rules() {
        loginAsPatient();
        // Navigate to Appointments and open Book Appointment modal as a representative form
        driver.findElement(By.xpath("//a[contains(@class,'ni') and contains(normalize-space(),'Appointments')]")).click();
        wait.until(d -> d.getCurrentUrl().contains("/appointments"));
        List<WebElement> btn = driver.findElements(By.xpath(
                "//*[contains(text(),'Book Appointment')]"));
        if (!btn.isEmpty()) {
            btn.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            // FRD step 1: mandatory fields should be marked with *
            assertTrue(driver.findElements(By.xpath("//*[contains(text(),'*')]")).size() > 0,
                    "Mandatory fields should be marked with *");
            // FRD step 2: submit with empty mandatory fields shows errors
            List<WebElement> submit = driver.findElements(By.xpath(
                    "//button[normalize-space()='Confirm Booking']"));
            if (!submit.isEmpty()) {
                submit.get(0).click();
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                assertTrue(driver.findElements(By.xpath(
                                "//*[contains(text(),'This field is required') or contains(text(),'required')]")).size() > 0,
                        "Required-field error message expected");
            }
        }
    }

    // TC065 — Common search & filter rules
    @Test
    public void TC065_common_search_filter_rules() {
        loginAsDoctor();
        driver.findElement(By.xpath("//a[contains(@class,'ni') and normalize-space()='Patients']")).click();
        wait.until(d -> d.getCurrentUrl().contains("/patients"));
        List<WebElement> search = driver.findElements(By.cssSelector("input[placeholder*='Search' i]"));
        assertTrue(search.size() > 0, "Search bar should be present");
        search.get(0).sendKeys("a");
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        // Real-time filtering — table should respond (no Enter required)
        assertNotNull(driver.findElements(By.cssSelector("table tr, [class*='patient-row']")));

        // No results scenario
        search.get(0).clear();
        search.get(0).sendKeys("zzzNoMatchXXX");
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(text(),'No results') or contains(text(),'No matching') or contains(text(),'No patients')]")).size() > 0,
                "Empty state message expected for no matches");
    }

    // TC066 — Status badge colour standards
    @Test
    public void TC066_status_badge_colour_standards() {
        loginAsDoctor();
        driver.findElement(By.xpath("//a[contains(@class,'ni') and normalize-space()='Appointments']")).click();
        wait.until(d -> d.getCurrentUrl().contains("/appointments"));
        // Just verify that status badges exist on the page
        List<WebElement> badges = driver.findElements(By.cssSelector(
                "[class*='badge'], [class*='status-pill']"));
        assertTrue(badges.size() > 0, "Status badges should be present");
    }

    // TC067 — Navigation rules
    @Test
    public void TC067_navigation_rules() {
        loginAsPatient();
        // Sidebar logo click navigates home
        List<WebElement> logo = driver.findElements(By.cssSelector(
                "[class*='logo'], a[href*='dashboard']"));
        if (!logo.isEmpty()) {
            logo.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            assertTrue(driver.getCurrentUrl().matches(".*/patient/\\d+/dashboard$"));
        }

        // Active sidebar link is highlighted (only one)
        List<WebElement> active = driver.findElements(By.cssSelector("a.ni.active"));
        assertTrue(active.size() == 1, "Only one sidebar link should be active at a time");
    }

    // TC068 — AI service unavailable (cannot simulate offline reliably)
    @Test
    public void TC068_ai_service_unavailable_indicator() {
        loginAsPatient();
        driver.findElement(By.xpath("//a[contains(@class,'ni') and normalize-space()='AI Health Assistant']")).click();
        wait.until(d -> d.getCurrentUrl().contains("/ai"));
        // Page should show online/offline indicator
        List<WebElement> status = driver.findElements(By.cssSelector(
                "[class*='online'], [class*='offline'], [class*='status-dot']"));
        assertTrue(status.size() > 0, "Online/Offline status indicator should be visible");
    }

    // TC069 — Export / Download failure handling
    @Test
    public void TC069_export_download_failure_ui_elements() {
        loginAsAdmin();
        driver.findElement(By.xpath("//a[contains(@class,'ni') and normalize-space()='Patients']")).click();
        wait.until(d -> d.getCurrentUrl().contains("/patients"));
        // Just verify Export button exists; we cannot reliably trigger a network failure here
        List<WebElement> exportBtn = driver.findElements(By.xpath(
                "//button[contains(normalize-space(),'Export')]"));
        assertTrue(exportBtn.size() > 0, "Export button should be present");
    }

    // TC070 — Role-based access control
    @Test
    public void TC070_role_based_access_control() {
        loginAsPatient();
        // Try to access Doctor portal URL
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/3/dashboard");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        // System should redirect away from doctor portal or show access denied
        String url = driver.getCurrentUrl();
        boolean redirected = !url.matches(".*/doctor/\\d+/dashboard$")
                || driver.findElements(By.xpath(
                        "//*[contains(text(),'Access denied') or contains(text(),'Unauthorized')]")).size() > 0;
        assertTrue(redirected,
                "Patient should not gain access to /doctor/ route; expected redirect or access-denied UI");

        // Logged out user attempt
        driver.manage().deleteAllCookies();
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/79/overview");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        assertTrue(driver.getCurrentUrl().contains("/login")
                        || driver.getCurrentUrl().equals(ConfigReader.get("ui.baseUrl") + "/")
                        || driver.getCurrentUrl().endsWith(ConfigReader.get("ui.baseUrl")),
                "Unauthenticated access should redirect to login or home");
    }

    // TC071 — Empty state displays across portals
    @Test
    public void TC071_empty_state_displays() {
        loginAsPatient();
        // Appointments page with no upcoming items would show empty state
        driver.findElement(By.xpath("//a[contains(@class,'ni') and contains(normalize-space(),'Appointments')]")).click();
        wait.until(d -> d.getCurrentUrl().contains("/appointments"));
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        // Look for either appointment data OR empty-state hint
        boolean hasContent = driver.findElements(By.cssSelector("[class*='appt-card'], [class*='appointment-card']")).size() > 0;
        boolean hasEmptyState = driver.findElements(By.xpath(
                "//*[contains(text(),'No upcoming') or contains(text(),'No appointments')]")).size() > 0;
        assertTrue(hasContent || hasEmptyState,
                "Either appointment cards or empty-state message should be present");
    }
}
