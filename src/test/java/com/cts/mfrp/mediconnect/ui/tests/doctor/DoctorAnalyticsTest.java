package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAnalytics;
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
 * FRD: TC047, TC048 — Doctor Analytics page.
 * URL: /doctor/{id}/analytics
 */
public class DoctorAnalyticsTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // TC047 — Analytics page UI (top-level summary tiles + header)
    @Test(groups = {"regression"})
    public void doctor_analytics_ui() {
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

    // TC048_AN01_AN04 — Header elements: title, subtitle, export button, range-select
    // Merged TC048 (time period filter) + TC_AN01 + TC_AN02 + TC_AN03 + TC_AN04
    @Test(groups = {"regression"})
    public void analytics_header_and_filter() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);

        // TC_AN01 — Page title
        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));
        assertEquals(driver.findElement(title).getText().trim(),
                "Analytics", "Page title mismatch");

        // TC_AN02 — Page subtitle
        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));
        assertEquals(driver.findElement(sub).getText().trim(),
                "Patient, appointment & lab insights",
                "Page subtitle mismatch");

        // TC_AN03 — Export button
        By exportBtn = By.cssSelector("button.btn.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(exportBtn));
        WebElement btn = driver.findElement(exportBtn);
        assertTrue(btn.isDisplayed(), "Export button not visible");
        assertTrue(btn.getText().trim().contains("Export"),
                "Export button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Export button is disabled");

        // TC048 — Time period selector present (from page object)
        assertTrue(driver.findElements(page.timePeriodSelect).size() > 0,
                "Time period 'range-select' dropdown should be present");

        // TC_AN04 — Range select has options
        By rangeSelect = By.cssSelector("select.range-select");
        w().until(ExpectedConditions.visibilityOfElementLocated(rangeSelect));
        WebElement select = driver.findElement(rangeSelect);
        assertTrue(select.isDisplayed(), "select.range-select not visible");
        assertTrue(select.isEnabled(), "select.range-select is disabled");
        List<WebElement> options = select.findElements(By.tagName("option"));
        assertTrue(options.size() > 0, "range-select has no options");
        boolean hasDaysOption = options.stream()
                .anyMatch(o -> o.getText().toLowerCase().contains("days"));
        assertTrue(hasDaysOption,
                "No 'days' option found in range-select. Options: " +
                        options.stream().map(WebElement::getText).toList());
    }

    // TC_AN05_AN07 — Stat card labels, values, sub-labels
    // Merged TC_AN05 + TC_AN06 + TC_AN07
    @Test(groups = {"regression"})
    public void analytics_stat_cards() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        // TC_AN05 — labels
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

        // TC_AN06 — values non-empty
        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));
        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");
        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }

        // TC_AN07 — sub-labels non-empty
        By statSub = By.cssSelector("div.stats-row span.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));
        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No span.stat-sub found");
        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "span.stat-sub is blank");
        }
    }

    // TC_AN08_AN13 — All charts and diagnoses section
    // Merged TC_AN08 + TC_AN09 + TC_AN10 + TC_AN11 + TC_AN12 + TC_AN13
    @Test(groups = {"regression"})
    public void analytics_charts_and_diagnoses() {
        new DoctorAnalytics(driver).open(loggedInUserId);

        // TC_AN08 — Appointments over time chart
        By cardTitles = By.cssSelector("div.charts-grid div.chart-card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));
        boolean overTimeFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointments over time"));
        assertTrue(overTimeFound, "'Appointments over time' card-title not found");

        By canvas = By.cssSelector("div.chart-wrap canvas");
        w().until(ExpectedConditions.presenceOfElementLocated(canvas));
        assertTrue(driver.findElements(canvas).size() > 0,
                "No canvas found in div.chart-wrap");

        // TC_AN09 — Card subtitle
        By cardSubs = By.cssSelector("div.charts-grid div.chart-card div.card-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardSubs));
        boolean subFound = driver.findElements(cardSubs).stream()
                .anyMatch(e -> e.getText().trim()
                        .equals("In-person vs video · last 6 months"));
        assertTrue(subFound,
                "'In-person vs video · last 6 months' card-sub not found");

        // TC_AN10 — Appointment types donut + legend
        boolean typesTitleFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointment types"));
        assertTrue(typesTitleFound, "'Appointment types' card-title not found");

        By donutCanvas = By.cssSelector("div.donut-wrap canvas");
        w().until(ExpectedConditions.presenceOfElementLocated(donutCanvas));
        assertTrue(driver.findElements(donutCanvas).size() > 0,
                "No canvas found in div.donut-wrap");

        By legendItems = By.cssSelector("div.legend div.legend-item");
        w().until(ExpectedConditions.visibilityOfElementLocated(legendItems));
        List<String> legendTexts = driver.findElements(legendItems)
                .stream().map(e -> e.getText().trim()).toList();
        assertTrue(legendTexts.stream().anyMatch(t -> t.contains("In-person")),
                "Legend missing 'In-person'. Found: " + legendTexts);
        assertTrue(legendTexts.stream().anyMatch(t -> t.contains("Video")),
                "Legend missing 'Video'. Found: " + legendTexts);

        // TC_AN11 — Appointments per month
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        By allCardTitles = By.cssSelector("div.card-title");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(allCardTitles, 2));
        w().until(driver -> driver.findElements(allCardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointments per month")));
        boolean perMonthFound = driver.findElements(allCardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointments per month"));
        assertTrue(perMonthFound,
                "'Appointments per month' card-title not found. Found titles: " +
                        driver.findElements(allCardTitles).stream()
                                .map(e -> e.getText().trim()).toList());

        // TC_AN12 — Top diagnoses section
        By diagRows = By.cssSelector("div.diag-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(diagRows, 0));
        List<WebElement> rows = driver.findElements(diagRows);
        assertTrue(rows.size() > 0, "No div.diag-row found");
        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            String label = row.findElement(
                    By.cssSelector("div.diag-label")).getText().trim();
            assertFalse(label.isEmpty(), "diag-label is empty in row " + i);
            List<WebElement> bar = row.findElements(
                    By.cssSelector("div.diag-bar-wrap div.diag-bar"));
            assertTrue(bar.size() > 0, "No div.diag-bar found in row " + i);
            String count = row.findElement(
                    By.cssSelector("div.diag-count")).getText().trim();
            assertFalse(count.isEmpty(), "diag-count is empty in row " + i);
            assertTrue(count.matches("\\d+"),
                    "diag-count not numeric in row " + i + ": '" + count + "'");
        }

        // TC_AN13 — Charts grid has >=2 cards
        By chartCards = By.cssSelector("div.charts-grid div.chart-card");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(chartCards, 0));
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(chartCards, 1));
        int count = driver.findElements(chartCards).size();
        assertTrue(count >= 2,
                "Expected >=2 chart cards in charts-grid, found: " + count);
    }
}
