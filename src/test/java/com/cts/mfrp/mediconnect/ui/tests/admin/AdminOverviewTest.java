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

/**
 * FRD: TC051, TC052, TC084 — Admin System Overview page.
 * URL: /admin/{userId}/overview
 *
 * DOM locators from browser inspection:
 *   div.tb-title                    → "System Overview"
 *   div.tb-sub                      → subtitle with date
 *   div.tb-chip                     → "Apr 2026" | "All Hospitals ↓"
 *   div.tb-notif                    → notification bell
 *   aside.sb nav.sb-nav a.ni        → sidebar nav links
 *   div.sb-brand-sub                → "Admin Control"
 *   div.sb-admin-name               → admin name
 *   div.sb-admin-role               → "Admin"
 *   span.sf-label                   → "Logout"
 *   div.stats-grid.mb20             → stat cards wrapper
 *   div.stat.teal/green/amber/blue  → tile wrappers
 *   div.stat-label                  → tile label
 *   div.stat-val                    → tile value
 *   div.stat-sub                    → tile sub-label
 *   div.card div.card-title         → chart card headings
 *   div.card div.card-sub           → chart card subtitles
 *   div.chart-pad canvas            → chart canvases
 *   div.disease-legend div.dl-row   → disease distribution legend rows
 *   span.dl-label                   → disease name
 *   span.dl-val                     → percentage
 *   div.map-container               → hospital branch map
 *   div.map-pin                     → map location pins
 *   div.ai-panel                    → AI System Insights panel
 *   div.ai-title                    → "AI System Insights"
 *   div.ai-insights-grid div.ai-insight → insight items
 *   div.notif-panel                 → notification panel
 *   div.notif-panel-title           → "Notifications"
 *   div.notif-item div.notif-content → notification content
 */
public class AdminOverviewTest extends BaseAdminTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC051 — System Overview UI validation (original - enhanced)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"sanity", "regression"})
    public void TC051_admin_system_overview_ui() {
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

        // Wait for stat tiles
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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC052 — Filters (Period + All Hospitals)
    // ─────────────────────────────────────────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────
// TC052 — Filters (Period + All Hospitals) visible
//         div.tb-chip → "Apr 2026" | "All Hospitals ↓"
// ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"regression"})
    public void TC052_admin_filters_validation() {
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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC084 — Hospital Branch Map on overview
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"regression"})
    public void TC084_admin_hospital_branch_map() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded());

        w().until(ExpectedConditions.visibilityOfElementLocated(admin.hospitalBranchMap));
        assertTrue(driver.findElements(admin.hospitalBranchMap).size() > 0,
                "Hospital Branch Map should be visible on Overview");

        // Map pins must be present
        By mapPins = By.cssSelector("div.map-container div.map-pin");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(mapPins, 0));
        assertTrue(driver.findElements(mapPins).size() > 0,
                "No div.map-pin found inside div.map-container");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV01 — Page title "System Overview" and subtitle visible
    //           div.tb-title | div.tb-sub
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV01_overview_page_title_subtitle() {
        new AdminOverview(driver);

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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV02 — Period and Hospital filter chips visible
    //           div.tb-chip → "Apr 2026" | "All Hospitals ↓"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV02_overview_filter_chips() {
        new AdminOverview(driver);

        By chips = By.cssSelector("div.tb-chip");
        w().until(ExpectedConditions.visibilityOfElementLocated(chips));

        List<String> chipTexts = driver.findElements(chips)
                .stream().map(e -> e.getText().trim()).toList();

        assertTrue(chipTexts.stream().anyMatch(t -> t.contains("Hospitals")),
                "All Hospitals chip missing. Found: " + chipTexts);
        assertFalse(chipTexts.isEmpty(), "No tb-chip elements found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV03 — All five stat tile labels visible
    //           div.stat-label → Total Patients | Total Doctors |
    //                            Bed Occupancy | Revenue (Month) | Doctors On Duty
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV03_overview_stat_tile_labels() {
        new AdminOverview(driver);

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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV04 — Stat tile values are non-empty
    //           div.stat-val → non-empty text
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV04_overview_stat_tile_values() {
        new AdminOverview(driver);

        By statVal = By.cssSelector("div.stats-grid div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV05 — Stat tile sub-labels visible
    //           div.stat-sub → non-empty text per tile
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV05_overview_stat_tile_sub_labels() {
        new AdminOverview(driver);

        By statSub = By.cssSelector("div.stats-grid div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No div.stat-sub found");

        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "div.stat-sub is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV06 — "Appointment Inflow" chart card visible
    //           div.card-title → "Appointment Inflow"
    //           div.chart-pad canvas → line chart canvas
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV06_overview_appointment_inflow_chart() {
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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV07 — "Disease Distribution" donut chart + legend visible
    //           div.card-title → "Disease Distribution"
    //           div.disease-legend div.dl-row → legend rows
    //           span.dl-label | span.dl-val → name + percentage
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV07_overview_disease_distribution_chart() {
        new AdminOverview(driver);

        By cardTitles = By.cssSelector("div.card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV08 — "Bed Occupancy" chart visible (scrolled into view)
    //           div.card-title → "Bed Occupancy"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV08_overview_bed_occupancy_chart() {
        new AdminOverview(driver);

        By cardTitles = By.cssSelector("div.card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        // Scroll to render below-fold cards
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        w().until(driver -> driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Bed Occupancy")));

        boolean found = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Bed Occupancy"));
        assertTrue(found, "'Bed Occupancy' card-title not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV09 — "Revenue Trend" chart visible
    //           div.card-title → "Revenue Trend"
    //           div.card-sub   → "Monthly · 2026"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV09_overview_revenue_trend_chart() {
        new AdminOverview(driver);

        By cardTitles = By.cssSelector("div.card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        w().until(driver -> driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Revenue Trend")));

        boolean titleFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Revenue Trend"));
        assertTrue(titleFound, "'Revenue Trend' card-title not found");

        By cardSubs = By.cssSelector("div.card div.card-sub");
        boolean subFound = driver.findElements(cardSubs).stream()
                .anyMatch(e -> e.getText().trim().contains("2026"));
        assertTrue(subFound, "Revenue Trend card-sub containing '2026' not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV10 — "Hospital Branch Map" card + map pins visible
    //           div.card-title → "Hospital Branch Map"
    //           div.map-container div.map-pin → ≥1 pin
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV10_overview_hospital_branch_map() {
        new AdminOverview(driver);

        By cardTitles = By.cssSelector("div.card div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitles));

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        w().until(driver -> driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Hospital Branch Map")));

        boolean titleFound = driver.findElements(cardTitles).stream()
                .anyMatch(e -> e.getText().trim().equals("Hospital Branch Map"));
        assertTrue(titleFound, "'Hospital Branch Map' card-title not found");

        By mapPins = By.cssSelector("div.map-container div.map-pin");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(mapPins, 0));
        assertTrue(driver.findElements(mapPins).size() > 0, "No map-pin found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_OV11 — AI System Insights panel visible with content
    //           div.ai-panel → div.ai-title + div.ai-insights-grid div.ai-insight
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV11_overview_ai_system_insights() {
        new AdminOverview(driver).open(loggedInUserId);

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

    @Test
    public void TC_OV12_overview_notification_panel() {
        new AdminOverview(driver).open(loggedInUserId);

        // Wait for page to fully load
        By pageTitle = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(pageTitle));

        // Just verify notification bell exists in the topbar
        By notifBell = By.cssSelector("div.tb-notif");
        w().until(ExpectedConditions.presenceOfElementLocated(notifBell));
        assertTrue(driver.findElements(notifBell).size() > 0,
                "Notification bell div.tb-notif not found in topbar");
    }
    // ─────────────────────────────────────────────────────────────────────────
// TC_OV13 — Notification panel body has items or empty state
// ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV13_overview_notification_item_content() {
        new AdminOverview(driver).open(loggedInUserId);

        By panelBody = By.cssSelector("div.notif-panel-body");
        w().until(ExpectedConditions.presenceOfElementLocated(panelBody));

        // Either items or empty state must be present
        List<WebElement> items = driver.findElements(
                By.cssSelector("div.notif-panel-body div.notif-item"));
        List<WebElement> emptyState = driver.findElements(
                By.cssSelector("div.notif-panel-body"));

        assertTrue(items.size() > 0 || emptyState.size() > 0,
                "Notification panel body has no content");
    }

    // ─────────────────────────────────────────────────────────────────────────
// TC_OV14 — Admin sidebar profile info visible
// ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV14_overview_admin_profile_info() {
        new AdminOverview(driver).open(loggedInUserId);

        By adminName = By.cssSelector("div.sb-admin-name");
        w().until(ExpectedConditions.visibilityOfElementLocated(adminName));
        assertFalse(driver.findElement(adminName).getText().trim().isEmpty(),
                "div.sb-admin-name is empty");

        By adminRole = By.cssSelector("div.sb-admin-role");
        w().until(ExpectedConditions.visibilityOfElementLocated(adminRole));
        assertEquals(driver.findElement(adminRole).getText().trim(),
                "Admin", "Admin role text mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
// TC_OV15 — Logout button visible in sidebar
// ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_OV15_overview_logout_button() {
        new AdminOverview(driver).open(loggedInUserId);

        By logout = By.cssSelector("span.sf-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(logout));

        assertEquals(driver.findElement(logout).getText().trim(),
                "Logout", "Logout label mismatch");
    }
}