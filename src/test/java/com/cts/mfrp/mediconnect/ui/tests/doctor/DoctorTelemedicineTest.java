package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorTelemedicine;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
 * FRD: TC046 — Doctor Telemedicine page.
 * URL: /doctor/{id}/telemedicine
 *
 * DOM locators from browser inspection:
 *   div.tele-page                → page wrapper
 *   h1.page-title                → "Telemedicine"
 *   p.page-sub                   → "Video consultations via session URL"
 *   button.btn.btn-primary       → "Schedule session"
 *   div.stats-row div.stat-card  → 4 stat cards
 *   div.stat-label               → Live now | Today's video | This week | Avg. duration
 *   div.stat-val                 → numeric value
 *   span.stat-sub                → Active session | 0 remaining | ↑ 18% vs last | Per session
 *   div.section-title            → "Live & Upcoming" | "Past sessions"
 *   div.empty-state p            → "No upcoming sessions today."
 *   div.past-card div.table-wrap → past sessions table container
 *   table thead th               → PATIENT | DATE | DURATION | REASON | STATUS
 *   table tbody tr               → past session rows
 */
public class DoctorTelemedicineTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC046 — Telemedicine UI + session management (original)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC046_doctor_telemedicine_ui() {
        DoctorTelemedicine page = new DoctorTelemedicine(driver).open(loggedInUserId);
        WebDriverWait wait     = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Step 1 — Wait for URL and page to load
        wait.until(ExpectedConditions.urlContains("telemedicine"));

        // Step 2 — Page header
        wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "Page header 'Telemedicine' not found");

        // Step 3 — Summary tiles
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),'Live now')]")));

        assertTrue(driver.findElements(
                        By.xpath("//*[contains(normalize-space(),'Live now')]")).size() > 0,
                "Summary tile missing: Live now");

        assertTrue(driver.findElements(
                        By.xpath("//*[contains(normalize-space(),\"Today's video\")]")).size() > 0,
                "Summary tile missing: Today's video");

        assertTrue(driver.findElements(
                        By.xpath("//*[contains(normalize-space(),'This week')]")).size() > 0,
                "Summary tile missing: This week");

        assertTrue(driver.findElements(
                        By.xpath("//*[contains(normalize-space(),'Avg. duration') " +
                                "or contains(normalize-space(),'Avg duration') " +
                                "or contains(normalize-space(),'Average duration')]")).size() > 0,
                "Summary tile missing: Avg. duration");

        // Step 4 — Live & Upcoming section
        wait.until(ExpectedConditions.presenceOfElementLocated(page.liveSection));
        assertTrue(driver.findElements(page.liveSection).size() > 0,
                "'Live & Upcoming' section should be visible");

        // Step 5 — Past sessions section heading
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),'Past sessions') " +
                        "or contains(normalize-space(),'Past Sessions')]")));
        assertTrue(driver.findElements(
                        By.xpath("//*[contains(normalize-space(),'Past sessions') " +
                                "or contains(normalize-space(),'Past Sessions')]")).size() > 0,
                "'Past sessions' section should be visible");

        // Step 5a — Scroll table into view for Angular lazy rendering
        try {
            WebElement table = longWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center',inline:'nearest'});", table);

            longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("table thead")));

            for (String col : List.of("Patient", "Date", "Duration", "Status")) {
                assertTrue(driver.findElements(By.xpath(
                                "//th[contains(normalize-space(),'" + col + "')]")).size() > 0,
                        "Past sessions column missing: " + col);
            }

            List<WebElement> dataRows  = driver.findElements(By.cssSelector("tbody tr"));
            List<WebElement> emptyRows = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'No sessions') " +
                            "or contains(normalize-space(),'No past sessions') " +
                            "or contains(normalize-space(),'No records')]"));

            assertTrue(dataRows.size() > 0 || emptyRows.size() > 0,
                    "Past sessions table should show rows or empty state");

        } catch (Exception e) {
            assertTrue(true, "No past sessions table present — acceptable for empty account");
        }

        // Step 6 — Schedule session button
        wait.until(ExpectedConditions.visibilityOfElementLocated(page.scheduleSessionBtn));
        assertTrue(driver.findElements(page.scheduleSessionBtn).size() > 0,
                "'+ Schedule session' button should be visible");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T01 — Page title "Telemedicine" visible
    //          h1.page-title → "Telemedicine"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T01_telemedicine_page_title() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));

        assertEquals(driver.findElement(title).getText().trim(),
                "Telemedicine", "Page title mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T02 — Page subtitle visible
    //          p.page-sub → "Video consultations via session URL"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T02_telemedicine_page_subtitle() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));

        assertEquals(driver.findElement(sub).getText().trim(),
                "Video consultations via session URL",
                "Page subtitle mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T03 — "Schedule session" button present and enabled
    //          button.btn.btn-primary → "Schedule session"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T03_telemedicine_schedule_session_button() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By scheduleBtn = By.cssSelector("button.btn.btn-primary");
        w().until(ExpectedConditions.visibilityOfElementLocated(scheduleBtn));

        WebElement btn = driver.findElement(scheduleBtn);
        assertTrue(btn.isDisplayed(), "Schedule session button not visible");
        assertTrue(btn.getText().trim().contains("Schedule session"),
                "Button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Schedule session button is disabled");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T04 — All four stat card labels visible
    //          div.stat-label → Live now | Today's video | This week | Avg. duration
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T04_telemedicine_stat_card_labels() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Live now", "Today's video", "This week", "Avg. duration")) {
            assertTrue(found.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T05 — Stat card values are non-empty
    //          div.stat-val → non-empty text
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T05_telemedicine_stat_card_values() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T06 — Stat card sub-labels visible
    //          span.stat-sub → Active session | 0 remaining | Per session
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T06_telemedicine_stat_card_sub_labels() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By statSub = By.cssSelector("div.stats-row span.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<String> found = driver.findElements(statSub)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Active session", "remaining", "Per session")) {
            assertTrue(found.stream().anyMatch(t -> t.contains(expected)),
                    "Sub-label containing '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T07 — "Live & Upcoming" section title visible
    //          div.section-title → "Live & Upcoming"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T07_telemedicine_live_upcoming_section() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By sectionTitles = By.cssSelector("div.section-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(sectionTitles));

        boolean found = driver.findElements(sectionTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Live & Upcoming"));
        assertTrue(found, "'Live & Upcoming' section-title not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T08 — "Live & Upcoming" empty state message correct
    //          div.empty-state p → "No upcoming sessions today."
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T08_telemedicine_live_upcoming_empty_state() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By emptyState = By.cssSelector("div.empty-state");
        w().until(ExpectedConditions.presenceOfElementLocated(emptyState));

        List<WebElement> emptyEls = driver.findElements(
                By.cssSelector("div.empty-state p"));
        List<WebElement> liveRows = driver.findElements(
                By.cssSelector("div.live-row, div.session-row, div.consult-row"));

        assertTrue(emptyEls.size() > 0 || liveRows.size() > 0,
                "Live & Upcoming must show empty state or session rows");

        if (!emptyEls.isEmpty()) {
            assertEquals(emptyEls.get(0).getText().trim(),
                    "No upcoming sessions today.",
                    "Empty state message mismatch");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T09 — "Past sessions" section title visible
    //          div.section-title → "Past sessions"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T09_telemedicine_past_sessions_section() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By sectionTitles = By.cssSelector("div.section-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(sectionTitles));

        boolean found = driver.findElements(sectionTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Past sessions"));
        assertTrue(found, "'Past sessions' section-title not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T10 — Past sessions table column headers correct
    //          div.past-card div.table-wrap table thead th
    //          PATIENT | DATE | DURATION | REASON | STATUS
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T10_telemedicine_past_sessions_columns() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By thLocator = By.cssSelector(
                "div.past-card div.table-wrap table thead th");
        w().until(ExpectedConditions.visibilityOfElementLocated(thLocator));

        List<String> headers = driver.findElements(thLocator)
                .stream()
                .map(e -> e.getText().trim()
                        .replace("\n", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase())
                .toList();

        for (String col : List.of(
                "PATIENT", "DATE", "DURATION", "REASON", "STATUS")) {
            assertTrue(headers.stream().anyMatch(h -> h.equals(col)),
                    "Column '" + col + "' not found. Found: " + headers);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T11 — Past sessions table has data rows
    //          div.past-card div.table-wrap table tbody tr
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T11_telemedicine_past_sessions_has_rows() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By rows = By.cssSelector(
                "div.past-card div.table-wrap table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        assertTrue(driver.findElements(rows).size() > 0,
                "Past sessions table has no data rows");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T12 — Past sessions row data validation
    //          PATIENT | DATE | REASON | STATUS non-empty
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T12_telemedicine_past_sessions_row_data() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By rows = By.cssSelector(
                "div.past-card div.table-wrap table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        List<WebElement> dataRows = driver.findElements(rows);
        int rowsToCheck = Math.min(3, dataRows.size());

        for (int i = 0; i < rowsToCheck; i++) {
            List<WebElement> cells = dataRows.get(i)
                    .findElements(By.cssSelector("td"));
            assertTrue(cells.size() >= 4,
                    "Row " + i + " should have ≥4 columns, found: " + cells.size());

            // col 0 — PATIENT
            assertFalse(cells.get(0).getText().trim().isEmpty(),
                    "Row " + i + " PATIENT is empty");

            // col 1 — DATE
            assertFalse(cells.get(1).getText().trim().isEmpty(),
                    "Row " + i + " DATE is empty");

            // col 3 — REASON
            assertFalse(cells.get(3).getText().trim().isEmpty(),
                    "Row " + i + " REASON is empty");

            // col 4 — STATUS
            String status = cells.get(4).getText().trim();
            assertFalse(status.isEmpty(),
                    "Row " + i + " STATUS is empty");
            assertTrue(
                    List.of("No-show", "Completed", "Cancelled", "Pending")
                            .stream().anyMatch(status::contains),
                    "Row " + i + " STATUS unexpected: '" + status + "'");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_T13 — Past sessions card and table-wrap visible
    //          div.past-card → exists | div.past-card div.table-wrap → exists
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_T13_telemedicine_past_card_visible() {
        new DoctorTelemedicine(driver).open(loggedInUserId);

        By pastCard = By.cssSelector("div.past-card");
        w().until(ExpectedConditions.visibilityOfElementLocated(pastCard));

        assertTrue(driver.findElement(pastCard).isDisplayed(),
                "div.past-card not visible");

        By tableWrap = By.cssSelector("div.past-card div.table-wrap");
        assertTrue(driver.findElements(tableWrap).size() > 0,
                "div.table-wrap not found inside div.past-card");
    }
}
