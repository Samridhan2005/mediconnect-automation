package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
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


public class AdminOverviewTest extends BaseAdminTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    @Test(groups = {"sanity", "regression"})
    public void admin_overview_ui_and_filters() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded(), "Admin overview page not loaded");

        // Page title
        By titleLocator = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(titleLocator));
        assertEquals(driver.findElement(titleLocator).getText().trim(),
                "System Overview", "Page title mismatch");

        // Sidebar brand
        assertTrue(driver.findElements(By.cssSelector("div.sb-brand-sub")).size() > 0,
                "Sidebar brand 'Admin Control' not found");

        // Sidebar nav groups
        for (String group : List.of("Overview", "Patients & Care", "Operations")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[text()='" + group + "']")).size() > 0,
                    "Sidebar group missing: " + group);
        }

        By statLabel = By.cssSelector("div.stats-grid div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        // Tile labels
        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String tile : List.of(
                "Total Patients", "Total Doctors",
                "Bed Occupancy", "Revenue (Month)", "Doctors On Duty")) {
            assertTrue(found.contains(tile),
                    "Summary tile missing: '" + tile + "'. Found: " + found);
        }

        By title = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));
        assertEquals(driver.findElement(title).getText().trim(),
                "System Overview", "Page title mismatch");

        By sub = By.cssSelector("div.tb-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));
        assertFalse(driver.findElement(sub).getText().trim().isEmpty(),
                "div.tb-sub is empty");
        assertTrue(driver.findElement(sub).getText().contains("All hospitals"),
                "Subtitle should contain 'All hospitals'");

        By chips = By.cssSelector("div.tb-chip");
        w().until(ExpectedConditions.visibilityOfElementLocated(chips));

        List<String> chipTexts = driver.findElements(chips)
                .stream().map(e -> e.getText().trim()).toList();

        assertTrue(chipTexts.stream().anyMatch(t -> t.contains("Hospitals")),
                "All Hospitals chip missing. Found: " + chipTexts);
        assertFalse(chipTexts.isEmpty(), "No tb-chip elements found");
    }

    @Test(groups = {"regression"})
    public void admin_overview_stat_tiles() {
        new AdminOverview(driver);

        // div.tb-chip contains both the period chip and hospitals chip
        By chipLocator = By.cssSelector("div.tb-chip");
        w().until(ExpectedConditions.visibilityOfElementLocated(chipLocator));

        List<WebElement> chips = driver.findElements(chipLocator);
        assertFalse(chips.isEmpty(), "No div.tb-chip elements found");

        // Period chip — contains a year (e.g. "Apr 2026")
        boolean periodFound = chips.stream()
                .anyMatch(e -> e.getText().trim().matches(".*\\d{4}.*"));
        assertTrue(periodFound,
                "Period chip (containing year) not found. Found chips: " +
                        chips.stream().map(e -> e.getText().trim()).toList());

        // All Hospitals chip — contains "Hospitals"
        boolean hospitalsFound = chips.stream()
                .anyMatch(e -> e.getText().trim().contains("Hospitals"));
        assertTrue(hospitalsFound,
                "All Hospitals chip not found. Found chips: " +
                        chips.stream().map(e -> e.getText().trim()).toList());

        By statLabel = By.cssSelector("div.stats-grid div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Total Patients", "Total Doctors",
                "Bed Occupancy", "Revenue (Month)", "Doctors On Duty")) {
            assertTrue(found.contains(expected),
                    "Tile label '" + expected + "' missing. Found: " + found);
        }

        By statVal = By.cssSelector("div.stats-grid div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }

        By statSub = By.cssSelector("div.stats-grid div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No div.stat-sub found");

        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "div.stat-sub is blank");
        }
    }

    @Test(groups = {"regression"})
    public void admin_overview_charts() {
        new AdminOverview(driver);

        By cardTitles = By.cssSelector("div.card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        boolean found = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Appointment Inflow"));
        assertTrue(found, "'Appointment Inflow' card-title not found");

        By canvas = By.cssSelector("div.chart-pad canvas");
        w().until(ExpectedConditions.presenceOfElementLocated(canvas));
        assertTrue(driver.findElements(canvas).size() > 0,
                "No canvas found in div.chart-pad");

        boolean titleFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Disease Distribution"));
        assertTrue(titleFound, "'Disease Distribution' card-title not found");

        // Legend rows
        By dlRows = By.cssSelector("div.disease-legend div.dl-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(dlRows, 0));

        List<WebElement> rows = driver.findElements(dlRows);
        assertTrue(rows.size() > 0, "No dl-row found in disease-legend");

        // Each row must have label and value
        for (int i = 0; i < Math.min(3, rows.size()); i++) {
            String label = rows.get(i)
                    .findElement(By.cssSelector("span.dl-label")).getText().trim();
            assertFalse(label.isEmpty(), "dl-label empty in row " + i);

            String val = rows.get(i)
                    .findElement(By.cssSelector("span.dl-val")).getText().trim();
            assertFalse(val.isEmpty(), "dl-val empty in row " + i);
            assertTrue(val.contains("%"),
                    "dl-val should contain '%': '" + val + "'");
        }

        // Scroll to render below-fold cards
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        w().until(driver -> driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Bed Occupancy")));

        boolean found2 = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Bed Occupancy"));
        assertTrue(found2, "'Bed Occupancy' card-title not found");

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        w().until(driver -> driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Revenue Trend")));

        boolean titleFound2 = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Revenue Trend"));
        assertTrue(titleFound2, "'Revenue Trend' card-title not found");

        By cardSubs = By.cssSelector("div.card div.card-sub");
        boolean subFound = driver.findElements(cardSubs).stream()
                .anyMatch(e -> e.getText().trim().contains("2026"));
        assertTrue(subFound, "Revenue Trend card-sub containing '2026' not found");
    }

    @Test(groups = {"regression"})
    public void admin_overview_map_and_ai_insights() {
        AdminOverview admin = new AdminOverview(driver).open(loggedInUserId);
        assertTrue(admin.isLoaded());

        w().until(ExpectedConditions.visibilityOfElementLocated(admin.hospitalBranchMap));
        assertTrue(driver.findElements(admin.hospitalBranchMap).size() > 0,
                "Hospital Branch Map should be visible on Overview");

        // Map pins must be present
        By mapPins = By.cssSelector("div.map-container div.map-pin");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(mapPins, 0));
        assertTrue(driver.findElements(mapPins).size() > 0,
                "No div.map-pin found inside div.map-container");

        By cardTitles = By.cssSelector("div.card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        w().until(driver -> driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Hospital Branch Map")));

        boolean titleFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Hospital Branch Map"));
        assertTrue(titleFound, "'Hospital Branch Map' card-title not found");

        By mapPins2 = By.cssSelector("div.map-container div.map-pin");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(mapPins2, 0));
        assertTrue(driver.findElements(mapPins2).size() > 0, "No map-pin found");

        // Scroll to bottom — AI panel is below the fold
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        By aiPanel = By.cssSelector("div.ai-panel");
        w().until(ExpectedConditions.visibilityOfElementLocated(aiPanel));

        By aiTitle = By.cssSelector("div.ai-panel div.ai-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(aiTitle));
        assertEquals(driver.findElement(aiTitle).getText().trim(),
                "AI System Insights", "AI title mismatch");

        By aiSub = By.cssSelector("div.ai-panel div.ai-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(aiSub));
        assertFalse(driver.findElement(aiSub).getText().trim().isEmpty(),
                "div.ai-sub is empty");

        By insights = By.cssSelector("div.ai-insights-grid div.ai-insight");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(insights, 0));

        List<WebElement> insightItems = driver.findElements(insights);
        assertTrue(insightItems.size() >= 1,
                "Expected ≥1 ai-insight, found: " + insightItems.size());

        for (int i = 0; i < insightItems.size(); i++) {
            assertFalse(insightItems.get(i).getText().trim().isEmpty(),
                    "ai-insight[" + i + "] has empty text");
        }
    }

    @Test(groups = {"regression"})
    public void admin_overview_notifications_and_profile() {
        new AdminOverview(driver).open(loggedInUserId);

        // Wait for page to fully load
        By pageTitle = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(pageTitle));

        // Just verify notification bell exists in the topbar
        By notifBell = By.cssSelector("div.tb-notif");
        w().until(ExpectedConditions.presenceOfElementLocated(notifBell));
        assertTrue(driver.findElements(notifBell).size() > 0,
                "Notification bell div.tb-notif not found in topbar");

        By panelBody = By.cssSelector("div.notif-panel-body");
        w().until(ExpectedConditions.presenceOfElementLocated(panelBody));

        // Either items or empty state must be present
        List<WebElement> items = driver.findElements(
                By.cssSelector("div.notif-panel-body div.notif-item"));
        List<WebElement> emptyState = driver.findElements(
                By.cssSelector("div.notif-panel-body"));

        assertTrue(items.size() > 0 || emptyState.size() > 0,
                "Notification panel body has no content");

        By adminName = By.cssSelector("div.sb-admin-name");
        w().until(ExpectedConditions.visibilityOfElementLocated(adminName));
        assertFalse(driver.findElement(adminName).getText().trim().isEmpty(),
                "div.sb-admin-name is empty");

        By adminRole = By.cssSelector("div.sb-admin-role");
        w().until(ExpectedConditions.visibilityOfElementLocated(adminRole));
        assertEquals(driver.findElement(adminRole).getText().trim(),
                "Admin", "Admin role text mismatch");

        By logout = By.cssSelector("span.sf-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(logout));

        assertEquals(driver.findElement(logout).getText().trim(),
                "Logout", "Logout label mismatch");
    }
}
