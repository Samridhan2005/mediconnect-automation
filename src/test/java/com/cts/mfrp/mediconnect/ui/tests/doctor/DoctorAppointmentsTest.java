package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAppointments;
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
 * Doctor Appointments page tests.
 * URL: /doctor/{id}/appointments
 *
 * DOM locators from browser inspection:
 *   div.appts-page              → page wrapper
 *   h1.page-title               → "Appointments"
 *   p.page-sub                  → subtitle with date
 *   button.btn-ghost            → "Calendar view"
 *   button.btn-primary          → "New Appointment"
 *   div.stats-row div.stat-card → 4 stat cards
 *   div.stat-label              → Today | Confirmed | Pending | Cancelled
 *   div.stat-val                → numeric value
 *   div.stat-sub                → sub-label text
 *   div.tab-bar button.tab      → Today | Upcoming | Past
 *   button.tab.active           → active tab
 *   div.toolbar div.search-wrap → search input
 *   select.tb-input             → All types / All status dropdowns
 *   input[type=date].tb-input   → date picker
 *   div.table-wrap table        → appointments table
 *   thead tr th                 → PATIENT|TIME|TYPE|REASON|STATUS|ACTIONS
 *   tbody tr.data-row           → appointment data rows
 *   div.pagination span         → "Showing X–X of X appointments"
 *   aside.side-panel            → detail panel
 *   span.panel-title            → "Appointment Details"
 *   button.panel-close          → close panel button
 */
public class DoctorAppointmentsTest extends BaseDoctorTest {

<<<<<<< HEAD
    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A01 — Page title "Appointments" visible
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A01_appointments_page_title() {
        new DoctorAppointments(driver).open(loggedInUserId);
=======
    // TC039 — Appointments page UI
    @Test(groups = {"sanity", "regression"})
    public void TC039_doctor_appointments_list_ui() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
>>>>>>> f6db4cd54a4fe28abf6baffa2fcc3643cf12044c

        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));

        assertEquals(driver.findElement(title).getText().trim(),
                "Appointments", "Page title mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A02 — Page subtitle visible and non-empty
    //          p.page-sub → "Wednesday, May 20, 2026 · 1 scheduled today"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A02_appointments_page_subtitle() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));

        String text = driver.findElement(sub).getText().trim();
        assertFalse(text.isEmpty(), "p.page-sub is empty");
        assertTrue(text.contains("scheduled today"),
                "Subtitle should contain 'scheduled today': '" + text + "'");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A03 — "Calendar view" button present and enabled
    //          button.btn-ghost → "Calendar view"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A03_calendar_view_button() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By calBtn = By.cssSelector("button.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(calBtn));

        WebElement btn = driver.findElement(calBtn);
        assertTrue(btn.isDisplayed(), "Calendar view button not visible");
        assertTrue(btn.getText().trim().contains("Calendar view"),
                "Button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Calendar view button is disabled");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A04 — "New Appointment" button present and enabled
    //          button.btn-primary → "New Appointment"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A04_new_appointment_button() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By newBtn = By.cssSelector("button.btn-primary");
        w().until(ExpectedConditions.visibilityOfElementLocated(newBtn));

        WebElement btn = driver.findElement(newBtn);
        assertTrue(btn.isDisplayed(), "New Appointment button not visible");
        assertTrue(btn.getText().trim().contains("New Appointment"),
                "Button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "New Appointment button is disabled");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A05 — All four stat card labels visible
    //          div.stat-label → Today | Confirmed | Pending | Cancelled
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A05_appointments_stat_card_labels() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of("Today", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(found.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A06 — Stat card values are non-empty
    //          div.stat-val → numeric text
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A06_appointments_stat_card_values() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A07 — Stat card sub-labels visible
    //          div.stat-sub → sub-text under each card
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A07_appointments_stat_card_sub_labels() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By statSub = By.cssSelector("div.stats-row div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No div.stat-sub found");

        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "div.stat-sub is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A08 — Tab bar has Today (active) | Upcoming | Past tabs
    //          div.tab-bar button.tab
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A08_appointments_tab_bar() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By tabBar = By.cssSelector("div.tab-bar");
        w().until(ExpectedConditions.visibilityOfElementLocated(tabBar));

        // Active tab must be "Today"
        By activeTab = By.cssSelector("div.tab-bar button.tab.active");
        w().until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        assertEquals(driver.findElement(activeTab).getText().trim(),
                "Today", "Active tab should be 'Today'");

        // All three tabs present
        List<String> tabTexts = driver.findElements(
                        By.cssSelector("div.tab-bar button.tab"))
                .stream().map(e -> e.getText().trim()).toList();

        for (String tab : List.of("Today", "Upcoming", "Past")) {
            assertTrue(tabTexts.contains(tab),
                    "Tab '" + tab + "' not found. Found: " + tabTexts);
        }
    }

<<<<<<< HEAD
    // ─────────────────────────────────────────────────────────────────────────
    // TC_A09 — Toolbar filters present
    //          search-wrap | 2× select.tb-input | input[type=date].tb-input
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A09_appointments_toolbar_filters() {
        new DoctorAppointments(driver).open(loggedInUserId);
=======
    // TC040 — Tabs + Calendar view
    @Test(groups = {"regression"})
    public void TC040_doctor_appointments_tabs_and_calendar() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
>>>>>>> f6db4cd54a4fe28abf6baffa2fcc3643cf12044c

        By toolbar = By.cssSelector("div.toolbar");
        w().until(ExpectedConditions.visibilityOfElementLocated(toolbar));

        // Search input
        By search = By.cssSelector("div.toolbar div.search-wrap");
        assertTrue(driver.findElements(search).size() > 0,
                "Search wrap not found in toolbar");

        // Two select dropdowns (All types + All status)
        List<WebElement> selects = driver.findElements(
                By.cssSelector("div.toolbar select.tb-input"));
        assertTrue(selects.size() >= 2,
                "Expected ≥2 select.tb-input, found: " + selects.size());

        // Date picker
        By datePicker = By.cssSelector("div.toolbar input[type='date'].tb-input");
        assertTrue(driver.findElements(datePicker).size() > 0,
                "Date picker input not found in toolbar");
    }

<<<<<<< HEAD
    // ─────────────────────────────────────────────────────────────────────────
    // TC_A10 — Appointments table column headers correct
    //          PATIENT | TIME | TYPE | REASON | STATUS | ACTIONS
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A10_appointments_table_columns() {
        new DoctorAppointments(driver).open(loggedInUserId);
=======
    // TC041 — New Appointment Modal field validation
    @Test(groups = {"regression"})
    public void TC041_doctor_new_appointment_modal() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        List<WebElement> btn = driver.findElements(page.newAppointmentBtn);
        assertTrue(btn.size() > 0, "+ New Appointment button should be visible");
        btn.get(0).click();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(page.modalTitle).size() > 0,
                "Modal title should be visible");
>>>>>>> f6db4cd54a4fe28abf6baffa2fcc3643cf12044c

        By thLocator = By.cssSelector("div.table-wrap table thead tr th");
        w().until(ExpectedConditions.visibilityOfElementLocated(thLocator));

        List<String> headers = driver.findElements(thLocator)
                .stream()
                .map(e -> e.getText().trim()
                        .replace("\n", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase())
                .toList();

        for (String col : List.of(
                "PATIENT", "TIME", "TYPE", "REASON", "STATUS", "ACTIONS")) {
            assertTrue(headers.stream().anyMatch(h -> h.equals(col)),
                    "Column '" + col + "' not found. Found: " + headers);
        }
    }

<<<<<<< HEAD
    // ─────────────────────────────────────────────────────────────────────────
    // TC_A11 — Appointments table has data rows (tr.data-row)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A11_appointments_table_has_rows() {
        new DoctorAppointments(driver).open(loggedInUserId);
=======
    // TC078 — Unknown patient search negative
    // TC078 — Unknown patient search negative
    // TC078 — Unknown patient search negative
    @Test(groups = {"regression"})
    public void TC078_new_appointment_patient_search_negative() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
>>>>>>> f6db4cd54a4fe28abf6baffa2fcc3643cf12044c

        By rows = By.cssSelector("div.table-wrap table tbody tr.data-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        assertTrue(driver.findElements(rows).size() > 0,
                "No tr.data-row found in appointments table");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A12 — Appointment row data validation
    //          Patient name | Time | Type | Status are non-empty
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A12_appointments_table_row_data() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By rows = By.cssSelector("div.table-wrap table tbody tr.data-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        List<WebElement> dataRows = driver.findElements(rows);
        int rowsToCheck = Math.min(3, dataRows.size());

        for (int i = 0; i < rowsToCheck; i++) {
            List<WebElement> cells = dataRows.get(i)
                    .findElements(By.cssSelector("td"));
            assertTrue(cells.size() >= 5,
                    "Row " + i + " should have ≥5 columns, found: " + cells.size());

            // col 0 — PATIENT
            assertFalse(cells.get(0).getText().trim().isEmpty(),
                    "Row " + i + " PATIENT is empty");

            // col 1 — TIME (e.g. "04:30 PM")
            String time = cells.get(1).getText().trim();
            assertFalse(time.isEmpty(), "Row " + i + " TIME is empty");
            assertTrue(time.matches("\\d{2}:\\d{2}\\s*(AM|PM)"),
                    "Row " + i + " TIME format unexpected: '" + time + "'");

            // col 2 — TYPE (In-person / Video)
            String type = cells.get(2).getText().trim();
            assertFalse(type.isEmpty(), "Row " + i + " TYPE is empty");
            assertTrue(List.of("In-person", "Video").stream()
                            .anyMatch(type::contains),
                    "Row " + i + " TYPE unexpected: '" + type + "'");

            // col 4 — STATUS (Pending / Confirmed / Cancelled)
            String status = cells.get(4).getText().trim();
            assertFalse(status.isEmpty(), "Row " + i + " STATUS is empty");
            assertTrue(List.of("Pending", "Confirmed", "Cancelled").stream()
                            .anyMatch(status::contains),
                    "Row " + i + " STATUS unexpected: '" + status + "'");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A13 — Pagination label visible
    //          div.pagination span → "Showing X–X of X appointments"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A13_appointments_pagination_label() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By pagination = By.cssSelector("div.pagination span");
        w().until(ExpectedConditions.visibilityOfElementLocated(pagination));

        String text = driver.findElement(pagination).getText().trim();
        assertFalse(text.isEmpty(), "Pagination span is empty");
        assertTrue(text.contains("appointments"),
                "Pagination label should contain 'appointments': '" + text + "'");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_A14 — Side panel "Appointment Details" opens on row click
    //          aside.side-panel → span.panel-title → "Appointment Details"
    //          button.panel-close → close button present
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_A14_appointments_detail_panel_opens() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By rows = By.cssSelector("div.table-wrap table tbody tr.data-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        // Click first row to open detail panel
        driver.findElements(rows).get(0).click();

        By panelTitle = By.cssSelector("aside.side-panel span.panel-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(panelTitle));

        assertEquals(driver.findElement(panelTitle).getText().trim(),
                "Appointment Details", "Panel title mismatch");

        // Close button must be present
        By closeBtn = By.cssSelector("aside.side-panel button.panel-close");
        assertTrue(driver.findElements(closeBtn).size() > 0,
                "button.panel-close not found in side panel");
    }
}
