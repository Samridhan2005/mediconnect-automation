package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAnalytics;
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
 * FRD: TC047, TC048 — Doctor Analytics page.
 * URL: /doctor/{id}/analytics
 *
 * DOM locators from browser inspection:
 *   div.analytics-page              → page wrapper
 *   h1.page-title                   → "Analytics"
 *   p.page-sub                      → "Patient, appointment & lab insights"
 *   select.range-select             → time period dropdown (Last 30 days etc.)
 *   button.btn.btn-ghost            → "Export" button
 *   div.stats-row div.stat-card     → 4 stat cards
 *   div.stat-label                  → Total patients | Appointments | Lab tests ordered | Avg. consult time
 *   div.stat-val                    → numeric value
 *   span.stat-sub                   → sub-label text
 *   div.charts-grid div.chart-card  → chart cards
 *   div.chart-card div.card-title   → "Appointments over time" | "Appointment types" | "Appointments per month"
 *   div.chart-card div.card-sub     → subtitle text
 *   div.chart-wrap canvas           → line/bar chart canvas
 *   div.donut-wrap canvas           → donut chart canvas
 *   div.legend div.legend-item      → legend items (In-person | Video)
 *   div.diag-row                    → top diagnoses rows
 *   div.diag-label                  → diagnosis name
 *   div.diag-count                  → diagnosis count
 *   div.diag-bar                    → bar element
 */
public class DoctorAnalyticsTest extends BaseDoctorTest {

<<<<<<< HEAD
    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC047 — Analytics page UI (original)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
=======
    // TC047 — Analytics page UI
    @Test(groups = {"regression"})
>>>>>>> f6db4cd54a4fe28abf6baffa2fcc3643cf12044c
    public void TC047_doctor_analytics_ui() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);

        w().until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "Page header 'Analytics' not found");

        w().until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),\"Total patients\")]")));

        for (String tile : List.of(
                "Total patients", "Appointments",
                "Lab tests ordered", "Avg. consult time")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),\"" + tile + "\")]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

<<<<<<< HEAD
    // ─────────────────────────────────────────────────────────────────────────
    // TC048 — Time period filter (original)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
=======
    // TC048 — Time period filter
    @Test(groups = {"regression"})
>>>>>>> f6db4cd54a4fe28abf6baffa2fcc3643cf12044c
    public void TC048_doctor_analytics_time_period_filter() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);

        w().until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.timePeriodSelect).size() > 0,
                "Time period 'range-select' dropdown should be present");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN01 — Page title "Analytics" visible
    //           h1.page-title → "Analytics"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN01_analytics_page_title() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));

        assertEquals(driver.findElement(title).getText().trim(),
                "Analytics", "Page title mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN02 — Page subtitle visible
    //           p.page-sub → "Patient, appointment & lab insights"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN02_analytics_page_subtitle() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));

        assertEquals(driver.findElement(sub).getText().trim(),
                "Patient, appointment & lab insights",
                "Page subtitle mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN03 — Export button present and enabled
    //           button.btn.btn-ghost → "Export"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN03_analytics_export_button() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By exportBtn = By.cssSelector("button.btn.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(exportBtn));

        WebElement btn = driver.findElement(exportBtn);
        assertTrue(btn.isDisplayed(), "Export button not visible");
        assertTrue(btn.getText().trim().contains("Export"),
                "Export button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Export button is disabled");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN04 — Range select dropdown has options
    //           select.range-select → Last 30 days | Last 7 days | Last 90 days
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN04_analytics_range_select_options() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By rangeSelect = By.cssSelector("select.range-select");
        w().until(ExpectedConditions.visibilityOfElementLocated(rangeSelect));

        WebElement select = driver.findElement(rangeSelect);
        assertTrue(select.isDisplayed(), "select.range-select not visible");
        assertTrue(select.isEnabled(), "select.range-select is disabled");

        List<WebElement> options = select.findElements(By.tagName("option"));
        assertTrue(options.size() > 0,
                "range-select has no options");

        // At least one option must contain "days"
        boolean hasDaysOption = options.stream()
                .anyMatch(o -> o.getText().toLowerCase().contains("days"));
        assertTrue(hasDaysOption,
                "No 'days' option found in range-select. Options: " +
                        options.stream().map(WebElement::getText).toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN05 — All four stat card labels visible
    //           div.stat-label → Total patients | Appointments |
    //                            Lab tests ordered | Avg. consult time
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN05_analytics_stat_card_labels() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Total patients", "Appointments",
                "Lab tests ordered", "Avg. consult time")) {
            assertTrue(found.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN06 — Stat card values are non-empty
    //           div.stat-val → non-empty text
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN06_analytics_stat_card_values() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN07 — Stat card sub-labels visible
    //           span.stat-sub → non-empty text per card
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN07_analytics_stat_card_sub_labels() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By statSub = By.cssSelector("div.stats-row span.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No span.stat-sub found");

        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "span.stat-sub is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN08 — "Appointments over time" chart card visible
    //           div.chart-card div.card-title → "Appointments over time"
    //           div.chart-wrap canvas         → chart canvas rendered
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN08_analytics_appointments_over_time_chart() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By cardTitles = By.cssSelector("div.charts-grid div.chart-card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        boolean found = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointments over time"));
        assertTrue(found, "'Appointments over time' card-title not found");

        // Canvas inside chart-wrap must be present
        By canvas = By.cssSelector("div.chart-wrap canvas");
        w().until(ExpectedConditions.presenceOfElementLocated(canvas));
        assertTrue(driver.findElements(canvas).size() > 0,
                "No canvas found in div.chart-wrap");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN09 — "Appointments over time" card subtitle visible
    //           div.card-sub → "In-person vs video · last 6 months"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN09_analytics_appointments_over_time_subtitle() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By cardSubs = By.cssSelector(
                "div.charts-grid div.chart-card div.card-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardSubs));

        boolean found = driver.findElements(cardSubs).stream()
                .anyMatch(e -> e.getText().trim()
                        .equals("In-person vs video · last 6 months"));
        assertTrue(found,
                "'In-person vs video · last 6 months' card-sub not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN10 — "Appointment types" donut chart visible with legend
    //           div.chart-card div.card-title → "Appointment types"
    //           div.donut-wrap canvas         → donut canvas
    //           div.legend div.legend-item    → In-person | Video
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN10_analytics_appointment_types_chart() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By cardTitles = By.cssSelector(
                "div.charts-grid div.chart-card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        boolean titleFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointment types"));
        assertTrue(titleFound, "'Appointment types' card-title not found");

        // Donut canvas
        By donutCanvas = By.cssSelector("div.donut-wrap canvas");
        w().until(ExpectedConditions.presenceOfElementLocated(donutCanvas));
        assertTrue(driver.findElements(donutCanvas).size() > 0,
                "No canvas found in div.donut-wrap");

        // Legend items — In-person and Video
        By legendItems = By.cssSelector("div.legend div.legend-item");
        w().until(ExpectedConditions.visibilityOfElementLocated(legendItems));

        List<String> legendTexts = driver.findElements(legendItems)
                .stream().map(e -> e.getText().trim()).toList();

        assertTrue(legendTexts.stream().anyMatch(t -> t.contains("In-person")),
                "Legend missing 'In-person'. Found: " + legendTexts);
        assertTrue(legendTexts.stream().anyMatch(t -> t.contains("Video")),
                "Legend missing 'Video'. Found: " + legendTexts);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN11 — "Appointments per month" chart card visible
    //           div.chart-card div.card-title → "Appointments per month"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN11_analytics_appointments_per_month_chart() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        // Wait for page to render — anchor on charts-grid first card
        By chartsGrid = By.cssSelector("div.charts-grid div.chart-card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(chartsGrid));

        // Scroll to bottom — "Appointments per month" is below the fold
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        // Use page-wide card-title selector — bottom cards are outside charts-grid
        By allCardTitles = By.cssSelector("div.card-title");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(allCardTitles, 2));

        // Wait specifically until "Appointments per month" text is present
        w().until(driver -> driver.findElements(allCardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointments per month")));
        boolean found = driver.findElements(allCardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointments per month"));
        assertTrue(found,
                "'Appointments per month' card-title not found. Found titles: " +
                        driver.findElements(allCardTitles).stream()
                                .map(e -> e.getText().trim()).toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN12 — "Top diagnoses" section visible with rows
    //           div.diag-row → at least 1 row
    //           div.diag-label → diagnosis name non-empty
    //           div.diag-count → count non-empty
    //           div.diag-bar   → bar element present
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN12_analytics_top_diagnoses_section() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By diagRows = By.cssSelector("div.diag-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(diagRows, 0));

        List<WebElement> rows = driver.findElements(diagRows);
        assertTrue(rows.size() > 0, "No div.diag-row found");

        // Validate each diagnosis row
        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);

            // Label
            String label = row.findElement(
                    By.cssSelector("div.diag-label")).getText().trim();
            assertFalse(label.isEmpty(),
                    "diag-label is empty in row " + i);

            // Bar
            List<WebElement> bar = row.findElements(
                    By.cssSelector("div.diag-bar-wrap div.diag-bar"));
            assertTrue(bar.size() > 0,
                    "No div.diag-bar found in row " + i);

            // Count
            String count = row.findElement(
                    By.cssSelector("div.diag-count")).getText().trim();
            assertFalse(count.isEmpty(),
                    "diag-count is empty in row " + i);
            assertTrue(count.matches("\\d+"),
                    "diag-count not numeric in row " + i + ": '" + count + "'");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_AN13 — Charts grid has at least 3 chart cards
    //           div.charts-grid div.chart-card → count ≥ 3
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_AN13_analytics_charts_grid_has_cards() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        By chartCards = By.cssSelector("div.charts-grid div.chart-card");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(chartCards, 0));

        // Scroll to bottom to trigger lazy rendering of below-fold cards
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        // Wait for cards to stabilise after scroll
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(chartCards, 1));

        int count = driver.findElements(chartCards).size();
        assertTrue(count >= 2,
                "Expected ≥2 chart cards in charts-grid, found: " + count);
    }
}
