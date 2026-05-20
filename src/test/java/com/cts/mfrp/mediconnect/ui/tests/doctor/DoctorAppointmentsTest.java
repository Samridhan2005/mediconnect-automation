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

public class DoctorAppointmentsTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // TC039_A01_A04 — Page header, title, subtitle, calendar view + new appointment buttons
    // Merged TC039 + TC_A01 + TC_A02 + TC_A03 + TC_A04
    @Test(groups = {"sanity", "regression"})
    public void TC039_A01_A04_doctor_appointments_header_and_buttons() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC039 / TC_A01 — page title
        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));
        assertEquals(driver.findElement(title).getText().trim(),
                "Appointments", "Page title mismatch");

        // TC_A02 — subtitle non-empty + contains "scheduled today"
        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));
        String subText = driver.findElement(sub).getText().trim();
        assertFalse(subText.isEmpty(), "p.page-sub is empty");
        assertTrue(subText.contains("scheduled today"),
                "Subtitle should contain 'scheduled today': '" + subText + "'");

        // TC_A03 — Calendar view button
        By calBtn = By.cssSelector("button.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(calBtn));
        WebElement cal = driver.findElement(calBtn);
        assertTrue(cal.isDisplayed(), "Calendar view button not visible");
        assertTrue(cal.getText().trim().contains("Calendar view"),
                "Calendar view button text mismatch: '" + cal.getText() + "'");
        assertTrue(cal.isEnabled(), "Calendar view button is disabled");

        // TC_A04 — New Appointment button
        By newBtn = By.cssSelector("button.btn-primary");
        w().until(ExpectedConditions.visibilityOfElementLocated(newBtn));
        WebElement nb = driver.findElement(newBtn);
        assertTrue(nb.isDisplayed(), "New Appointment button not visible");
        assertTrue(nb.getText().trim().contains("New Appointment"),
                "New Appointment button text mismatch: '" + nb.getText() + "'");
        assertTrue(nb.isEnabled(), "New Appointment button is disabled");
    }

    // TC040_A05_A08 — Tab bar (Today active / Upcoming / Past) + stat cards
    // Merged TC040 + TC_A05 + TC_A06 + TC_A07 + TC_A08
    @Test(groups = {"regression"})
    public void TC040_A05_A08_doctor_appointments_tabs_and_stat_cards() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC040 / TC_A08 — tab bar active = Today, all 3 tabs present
        By tabBar = By.cssSelector("div.tab-bar");
        w().until(ExpectedConditions.visibilityOfElementLocated(tabBar));
        By activeTab = By.cssSelector("div.tab-bar button.tab.active");
        w().until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        assertEquals(driver.findElement(activeTab).getText().trim(),
                "Today", "Active tab should be 'Today'");

        List<String> tabTexts = driver.findElements(
                        By.cssSelector("div.tab-bar button.tab"))
                .stream().map(e -> e.getText().trim()).toList();
        for (String tab : List.of("Today", "Upcoming", "Past")) {
            assertTrue(tabTexts.contains(tab),
                    "Tab '" + tab + "' not found. Found: " + tabTexts);
        }

        // TC_A05 — stat card labels
        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));
        List<String> foundLabels = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();
        for (String expected : List.of("Today", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(foundLabels.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + foundLabels);
        }

        // TC_A06 — stat values non-empty
        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));
        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");
        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }

        // TC_A07 — stat sub-labels non-empty
        By statSub = By.cssSelector("div.stats-row div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));
        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No div.stat-sub found");
        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "div.stat-sub is blank");
        }
    }

    // TC041 — New Appointment modal opens (button click interaction)
    @Test(groups = {"regression"})
    public void TC041_doctor_new_appointment_modal() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);

        List<WebElement> btn = driver.findElements(page.newAppointmentBtn);
        assertTrue(btn.size() > 0, "+ New Appointment button should be visible");
        btn.get(0).click();

        w().until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button.btn-primary")));

        assertTrue(driver.findElements(page.modalTitle).size() > 0,
                "Modal title should be visible");
    }

    // TC_A09_A10_A13 — Toolbar filters + table columns + pagination label
    // Merged TC_A09 + TC_A10 + TC_A13
    @Test(groups = {"regression"})
    public void TC_A09_A10_A13_doctor_appointments_toolbar_columns_pagination() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC_A09 — toolbar filters (search, 2 selects, date picker)
        By toolbar = By.cssSelector("div.toolbar");
        w().until(ExpectedConditions.visibilityOfElementLocated(toolbar));
        By search = By.cssSelector("div.toolbar div.search-wrap");
        assertTrue(driver.findElements(search).size() > 0,
                "Search wrap not found in toolbar");
        List<WebElement> selects = driver.findElements(
                By.cssSelector("div.toolbar select.tb-input"));
        assertTrue(selects.size() >= 2,
                "Expected >=2 select.tb-input, found: " + selects.size());
        By datePicker = By.cssSelector("div.toolbar input[type='date'].tb-input");
        assertTrue(driver.findElements(datePicker).size() > 0,
                "Date picker input not found in toolbar");

        // TC_A10 — table column headers
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

        // TC_A13 — pagination label
        By pagination = By.cssSelector("div.pagination span");
        w().until(ExpectedConditions.visibilityOfElementLocated(pagination));
        String text = driver.findElement(pagination).getText().trim();
        assertFalse(text.isEmpty(), "Pagination span is empty");
        assertTrue(text.contains("appointments"),
                "Pagination label should contain 'appointments': '" + text + "'");
    }

    // TC078_A11_A12_A14 — Table has rows + row data validation + detail panel opens on row click
    // Merged TC078 + TC_A11 + TC_A12 + TC_A14
    @Test(groups = {"regression"})
    public void TC078_A11_A12_A14_doctor_appointments_rows_and_detail_panel() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC078 / TC_A11 — table has rows
        By rows = By.cssSelector("div.table-wrap table tbody tr.data-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));
        List<WebElement> dataRows = driver.findElements(rows);
        assertTrue(dataRows.size() > 0,
                "No tr.data-row found in appointments table");

        // TC_A12 — row data validation
        int rowsToCheck = Math.min(3, dataRows.size());
        for (int i = 0; i < rowsToCheck; i++) {
            List<WebElement> cells = dataRows.get(i)
                    .findElements(By.cssSelector("td"));
            assertTrue(cells.size() >= 5,
                    "Row " + i + " should have >=5 columns, found: " + cells.size());

            assertFalse(cells.get(0).getText().trim().isEmpty(),
                    "Row " + i + " PATIENT is empty");

            String time = cells.get(1).getText().trim();
            assertFalse(time.isEmpty(), "Row " + i + " TIME is empty");
            assertTrue(time.matches("\\d{2}:\\d{2}\\s*(AM|PM)"),
                    "Row " + i + " TIME format unexpected: '" + time + "'");

            String type = cells.get(2).getText().trim();
            assertFalse(type.isEmpty(), "Row " + i + " TYPE is empty");
            assertTrue(List.of("In-person", "Video").stream()
                            .anyMatch(type::contains),
                    "Row " + i + " TYPE unexpected: '" + type + "'");

            String status = cells.get(4).getText().trim();
            assertFalse(status.isEmpty(), "Row " + i + " STATUS is empty");
            assertTrue(List.of("Pending", "Confirmed", "Cancelled").stream()
                            .anyMatch(status::contains),
                    "Row " + i + " STATUS unexpected: '" + status + "'");
        }

        // TC_A14 — Detail panel opens on row click
        driver.findElements(rows).get(0).click();
        By panelTitle = By.cssSelector("aside.side-panel span.panel-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(panelTitle));
        assertEquals(driver.findElement(panelTitle).getText().trim(),
                "Appointment Details", "Panel title mismatch");
        By closeBtn = By.cssSelector("aside.side-panel button.panel-close");
        assertTrue(driver.findElements(closeBtn).size() > 0,
                "button.panel-close not found in side panel");
    }
}
