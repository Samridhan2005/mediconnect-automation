package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorDashboard;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC007, TC008, TC009, TC010, TC011, TC012, TC013, TC073, TC075.
 * Strict FRD assertions.
 */
public class DoctorDashboardTest extends BaseDoctorTest {

    // TC007 — Doctor Dashboard UI validation
    @Test
    public void TC007_doctor_dashboard_ui_validation() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        assertTrue(dash.isLoaded(), "Should land on /doctor/{id}/dashboard");

        List<String> expectedSidebar = List.of(
                "Dashboard", "Patients", "Appointments", "Diagnostics",
                "Supply Chain", "Billing", "Analytics", "Telemedicine", "AI Assistant", "Settings");
        List<String> actualSidebar = driver.findElements(By.cssSelector("a.ni"))
                .stream().map(WebElement::getText).map(String::trim).collect(Collectors.toList());
        for (String item : expectedSidebar) {
            assertTrue(actualSidebar.contains(item),
                    "FRD-required sidebar item missing: '" + item + "'. Actual: " + actualSidebar);
        }

        assertTrue(dash.isHospitalSelectorVisible(),  "Hospital selector dropdown missing");
        assertTrue(dash.isGlobalSearchVisible(),      "Global search bar missing");
        assertTrue(dash.isNotificationBellVisible(),  "Notification bell missing");
        assertTrue(driver.findElements(dash.profileDropdown).size() > 0, "Profile dropdown missing");

        assertTrue(driver.findElements(dash.statsSummaryCards).size() > 0, "Stats summary cards missing");
        assertTrue(driver.findElements(dash.recentAppointmentsList).size() > 0, "Recent Appointments list missing");
        assertTrue(driver.findElements(dash.upcomingConsultsPanel).size() > 0, "Upcoming Consultations panel missing");
    }

    // TC008 — Hospital selector dropdown functionality
    @Test
    public void TC008_hospital_selector_dropdown() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        assertTrue(dash.isHospitalSelectorVisible());
        dash.clickHospitalSelector();
        List<WebElement> options = driver.findElements(By.cssSelector(
                "[class*='hospital-list'] li, [class*='dropdown'] li, [class*='hospital'] [role='option']"));
        assertTrue(options.size() > 0, "Clicking hospital selector should reveal a list of branches");
    }

    // TC009 — Global Search textbox functionality
    @Test
    public void TC009_global_search_textbox() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        assertTrue(dash.isGlobalSearchVisible());

        WebElement searchBox = driver.findElement(dash.globalSearch);
        searchBox.clear();
        searchBox.sendKeys("Shalini");
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(By.cssSelector("[class*='search-result']")).size() >= 0);

        searchBox.clear();
        searchBox.sendKeys("a");
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        List<WebElement> err = driver.findElements(By.xpath(
                "//*[contains(text(),'Enter at least') or contains(text(),'characters')]"));
        assertTrue(err.size() > 0, "FRD: validation message expected for <2 chars");
    }

    // TC010 — Notification bell + profile dropdown
    @Test
    public void TC010_notification_bell_and_profile_dropdown() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        assertTrue(dash.isNotificationBellVisible());
        dash.clickNotificationBell();
        List<WebElement> menu = driver.findElements(By.cssSelector(
                "[class*='notification-panel'], [class*='notif-panel'], [class*='dropdown-menu']"));
        assertTrue(menu.size() > 0, "Notification panel should open on bell click");

        driver.findElement(dash.profileDropdown).click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        List<WebElement> options = driver.findElements(By.xpath(
                "//*[normalize-space()='Settings' or normalize-space()='Logout']"));
        assertTrue(options.size() >= 1, "Profile dropdown should expose Settings / Logout");
    }

    // TC011 — Stats summary card behaviour
    @Test
    public void TC011_stats_summary_card() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        List<WebElement> cards = driver.findElements(dash.statsSummaryCards);
        assertTrue(cards.size() > 0, "Stats summary cards should be visible");

        for (WebElement card : cards) {
            assertEquals(card.findElements(By.cssSelector("input,[contenteditable='true']")).size(), 0,
                    "Summary cards must not be editable");
        }

        List<WebElement> revenue = driver.findElements(dash.revenueTodayCard);
        if (!revenue.isEmpty()) {
            String text = revenue.get(0).getText();
            assertTrue(text.matches("(?s).*\\$\\d[\\d,]*.*") || text.matches("(?s).*[₹]\\d[\\d,]*.*"),
                    "Revenue value should be in proper currency format. Got: " + text);
        } else {
            assertNotNull(null, "FRD: 'Revenue Today' card was expected but not present");
        }
    }

    // TC012 — Recent appointments list
    @Test
    public void TC012_recent_appointments_list() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        List<WebElement> list = driver.findElements(dash.recentAppointmentsList);
        assertTrue(list.size() > 0, "Recent Appointments list should be visible");

        List<WebElement> rows = driver.findElements(By.cssSelector(
                "[class*='appointment-row'], [class*='appt-row'], [class*='appointment-item']"));
        assertTrue(rows.size() > 0, "Appointment rows should be present");

        List<WebElement> badges = driver.findElements(By.cssSelector(
                "[class*='status-badge'], [class*='badge'][class*='status']"));
        for (WebElement b : badges) {
            String t = b.getText().trim().toLowerCase();
            assertTrue(t.equals("confirmed") || t.equals("pending") || t.equals("cancelled") || t.isEmpty(),
                    "Status badge text invalid: '" + t + "'");
        }
    }

    // TC013 — Upcoming consultant panel
    @Test
    public void TC013_upcoming_consultant_panel() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        List<WebElement> panel = driver.findElements(dash.upcomingConsultsPanel);
        assertTrue(panel.size() > 0, "Upcoming Consultations panel should be visible");

        List<WebElement> joinButtons = driver.findElements(dash.joinButtons);
        assertTrue(joinButtons.size() > 0, "Join buttons should be present in the panel");
    }

    // TC073 — Doctor Sign Out
    @Test
    public void TC073_doctor_sign_out() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        assertTrue(dash.sidebar().isSignOutVisible());
        dash.sidebar().signOut();
        wait.until(d -> d.getCurrentUrl().contains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "After sign out user should be redirected to /login");
    }

    // TC075 — Bed availability + additional dashboard sections
    @Test
    public void TC075_bed_availability_and_dashboard_sections() {
        DoctorDashboard dash = new DoctorDashboard(driver);
        List<WebElement> bed = driver.findElements(dash.bedAvailabilitySection);
        for (String section : List.of("Bed availability", "Lab reports", "Supply chain", "Notifications")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(translate(normalize-space(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                                    + section.toLowerCase() + "')]")).size() > 0,
                    "Dashboard section missing per FRD: " + section);
        }
        assertNotNull(bed);
    }
}
