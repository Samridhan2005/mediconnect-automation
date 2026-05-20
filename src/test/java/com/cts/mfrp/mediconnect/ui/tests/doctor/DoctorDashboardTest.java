package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorDashboard;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC007–TC013, TC073, TC075 — Doctor Dashboard.
 * URL: /doctor/{id}/dashboard
 */
public class DoctorDashboardTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // TC007_010 — Dashboard header: page title, sidebar links, hospital selector, notification bell
    // Merged TC007 + TC008 + TC009 + TC010
    @Test(groups = {"sanity", "regression"})
    public void TC007_010_doctor_dashboard_header_and_nav() {
        new DoctorDashboard(driver);

        // TC007 — page title
        By titleLocator = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(titleLocator));
        WebElement title = driver.findElement(titleLocator);
        assertTrue(title.isDisplayed(), "h1.page-title not visible");
        assertEquals(title.getText().trim(), "Dashboard", "Page title mismatch");

        // TC008 — sidebar nav links
        By navLinks = By.cssSelector("aside.sb nav.sb-nav a.ni");
        w().until(ExpectedConditions.visibilityOfElementLocated(navLinks));
        List<String> found = driver.findElements(navLinks)
                .stream().map(e -> e.getText().trim()).toList();
        for (String expected : List.of(
                "Dashboard", "Patients", "Appointments", "Lab Reports",
                "Medical Records", "Telemedicine", "Analytics",
                "Supply Chain", "AI Assistant")) {
            assertTrue(found.stream().anyMatch(t -> t.contains(expected)),
                    "Sidebar link '" + expected + "' not found. Found: " + found);
        }

        // TC009 — hospital selector
        By hospitalBtn = By.cssSelector("button.hospital-btn");
        w().until(ExpectedConditions.visibilityOfElementLocated(hospitalBtn));
        WebElement hbtn = driver.findElement(hospitalBtn);
        assertTrue(hbtn.isDisplayed(), "button.hospital-btn not visible");
        assertFalse(hbtn.getText().trim().isEmpty(),
                "Hospital selector button text is empty");

        // TC010 — notification bell + badge
        By notifBtn = By.cssSelector("button.notif-btn");
        w().until(ExpectedConditions.visibilityOfElementLocated(notifBtn));
        assertTrue(driver.findElement(notifBtn).isDisplayed(),
                "button.notif-btn not visible");
        By badge = By.cssSelector("span.notif-badge");
        w().until(ExpectedConditions.visibilityOfElementLocated(badge));
        String badgeText = driver.findElement(badge).getText().trim();
        assertFalse(badgeText.isEmpty(), "Notification badge is empty");
        assertTrue(badgeText.matches("\\d+"),
                "Notification badge not numeric: '" + badgeText + "'");
    }

    // TC011 — Stat cards: labels and values
    @Test(groups = {"regression"})
    public void TC011_doctor_dashboard_stat_cards() {
        new DoctorDashboard(driver);

        By statCard = By.cssSelector("div.stat-cards div.stat-card");
        w().until(ExpectedConditions.visibilityOfElementLocated(statCard));

        By statLabel = By.cssSelector("span.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> labels = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Total patients", "Today's appointments",
                "Pending lab reports", "Unread notifications")) {
            assertTrue(labels.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + labels);
        }

        By statValue = By.cssSelector("span.stat-value");
        w().until(ExpectedConditions.visibilityOfElementLocated(statValue));

        List<WebElement> values = driver.findElements(statValue);
        assertFalse(values.isEmpty(), "No span.stat-value elements found");
        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "span.stat-value is blank");
        }
    }

    // TC012_012a — Today's appointments section + row data validation
    // Merged TC012 + TC012a
    @Test(groups = {"regression"})
    public void TC012_012a_doctor_dashboard_todays_appointments() {
        new DoctorDashboard(driver);

        // TC012 — section visible
        By cardTitles = By.cssSelector("span.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));
        boolean headingFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Today's appointments"));
        assertTrue(headingFound, "'Today's appointments' card-title not found");

        By apptList = By.cssSelector("div.appt-list");
        w().until(ExpectedConditions.presenceOfElementLocated(apptList));
        assertTrue(driver.findElements(apptList).size() > 0,
                "div.appt-list not found");

        List<WebElement> rows = driver.findElements(
                By.cssSelector("div.appt-list div.appt-row"));
        List<WebElement> empty = driver.findElements(
                By.cssSelector("div.appt-list div.empty-state"));
        assertTrue(rows.size() > 0 || empty.size() > 0,
                "appt-list must have rows or empty-state");

        // TC012a — row data validation if rows exist
        if (!rows.isEmpty()) {
            WebElement first = rows.get(0);
            String name = first.findElement(By.cssSelector("span.appt-name")).getText().trim();
            assertFalse(name.isEmpty(), "appt-name is empty");
            String meta = first.findElement(By.cssSelector("span.appt-meta")).getText().trim();
            assertFalse(meta.isEmpty(), "appt-meta is empty");
            List<WebElement> badges = first.findElements(By.cssSelector("div.appt-badges span"));
            assertFalse(badges.isEmpty(), "No badges found in appt-row");
        }
    }

    // TC013_073 — Upcoming consultations + Sign Out button
    // Merged TC013 + TC073
    @Test(groups = {"regression"})
    public void TC013_073_doctor_dashboard_consultations_and_signout() {
        new DoctorDashboard(driver);

        // TC013 — upcoming consultations
        By cardTitles = By.cssSelector("span.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));
        boolean headingFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Upcoming consultations"));
        assertTrue(headingFound, "'Upcoming consultations' card-title not found");

        By consultList = By.cssSelector("div.consult-list");
        w().until(ExpectedConditions.presenceOfElementLocated(consultList));

        List<WebElement> rows = driver.findElements(
                By.cssSelector("div.consult-list div.consult-row"));
        List<WebElement> empty = driver.findElements(
                By.cssSelector("div.consult-list div.empty-state"));
        assertTrue(rows.size() > 0 || empty.size() > 0,
                "consult-list must have rows or empty-state");

        if (!rows.isEmpty()) {
            WebElement first = rows.get(0);
            String name = first.findElement(
                    By.cssSelector("span.consult-name")).getText().trim();
            assertFalse(name.isEmpty(), "consult-name is empty");
            String meta = first.findElement(
                    By.cssSelector("span.consult-meta")).getText().trim();
            assertFalse(meta.isEmpty(), "consult-meta is empty");
            WebElement joinBtn = first.findElement(By.cssSelector("button.join-btn"));
            assertTrue(joinBtn.isDisplayed(), "button.join-btn not visible");
            assertEquals(joinBtn.getText().trim(), "Join", "Join button text mismatch");
            assertTrue(joinBtn.isEnabled(), "button.join-btn is disabled");
        }

        // TC073 — Sign Out button
        By signOut = By.cssSelector("button.sb-logout");
        w().until(ExpectedConditions.visibilityOfElementLocated(signOut));
        WebElement btn = driver.findElement(signOut);
        assertTrue(btn.isDisplayed(), "button.sb-logout not visible");
        assertTrue(btn.getText().trim().contains("Sign Out"),
                "Sign Out button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Sign Out button is disabled");
    }

    // TC075_075a_075b — Bed availability + Lab reports + Supply chain alerts sections
    // Merged TC075 + TC075a + TC075b
    @Test(groups = {"regression"})
    public void TC075_075a_075b_doctor_dashboard_side_sections() {
        new DoctorDashboard(driver);

        By cardTitles = By.cssSelector("span.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        // TC075 — Bed availability
        boolean bedHeading = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Bed availability"));
        assertTrue(bedHeading, "'Bed availability' card-title not found");

        By bedRows = By.cssSelector("div.bed-list div.bed-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(bedRows, 0));
        List<WebElement> beds = driver.findElements(bedRows);
        assertTrue(beds.size() > 0, "No bed rows found in div.bed-list");
        for (WebElement row : beds) {
            String badge = row.findElement(
                    By.cssSelector("span.bed-badge")).getText().trim();
            assertFalse(badge.isEmpty(), "span.bed-badge is empty in a bed-row");
            assertTrue(badge.contains("free"),
                    "bed-badge should contain 'free': '" + badge + "'");
        }

        // TC075a — Lab reports section
        boolean labHeading = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Lab reports"));
        assertTrue(labHeading, "'Lab reports' card-title not found");

        By labList = By.cssSelector("div.lab-list");
        w().until(ExpectedConditions.presenceOfElementLocated(labList));
        List<WebElement> emptyState = driver.findElements(
                By.cssSelector("div.lab-list div.empty-state"));
        List<WebElement> items = driver.findElements(
                By.cssSelector("div.lab-list div.lab-row"));
        assertTrue(emptyState.size() > 0 || items.size() > 0,
                "lab-list must have items or empty-state");
        if (!emptyState.isEmpty()) {
            assertEquals(emptyState.get(0).getText().trim(),
                    "No lab reports", "Empty state text mismatch");
        }

        // TC075b — Supply chain alerts
        boolean supplyHeading = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Supply chain alerts"));
        assertTrue(supplyHeading, "'Supply chain alerts' card-title not found");

        By invRows = By.cssSelector("div.inv-list div.inv-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(invRows, 0));
        List<WebElement> rows = driver.findElements(invRows);
        assertTrue(rows.size() > 0, "No inv-rows found in div.inv-list");
        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            String name = row.findElement(By.cssSelector("span.inv-name")).getText().trim();
            assertFalse(name.isEmpty(), "inv-name empty in row " + i);
            String cat = row.findElement(By.cssSelector("span.inv-cat")).getText().trim();
            assertFalse(cat.isEmpty(), "inv-cat empty in row " + i);
            String qty = row.findElement(By.cssSelector("span.inv-qty")).getText().trim();
            assertFalse(qty.isEmpty(), "inv-qty empty in row " + i);
            assertTrue(qty.contains("left"),
                    "inv-qty should contain 'left': '" + qty + "'");
        }
    }
}
