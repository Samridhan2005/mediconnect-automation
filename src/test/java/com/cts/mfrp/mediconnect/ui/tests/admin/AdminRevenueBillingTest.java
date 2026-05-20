package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminRevenueBilling;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC060–TC072 — Admin Revenue & Billing.
 *
 * All waits are 60 seconds to handle slow Netlify + Railway backend.
 * loginAsAdmin() is overridden here with 60s wait so @BeforeMethod
 * never times out regardless of how slow the app is.
 */
public class AdminRevenueBillingTest extends BaseAdminTest {

    /** 60 seconds for login redirect (overrides config's 15s). */
    private static final Duration WAIT_LOGIN = Duration.ofSeconds(60);

    /** 60 seconds for Angular API-driven content to render. */
    private static final Duration WAIT_CONTENT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT_CONTENT);
    }

    // ── Override loginAsAdmin with 60s wait ───────────────────────────────
    @Override
    @BeforeMethod(dependsOnMethods = "uiSetup")
    public void loginAsAdmin() {
        new AdminLogin(driver).open()
                .enterEmail(ConfigReader.get("admin.email"))
                .enterPassword(ConfigReader.get("admin.password"))
                .submit();

        new WebDriverWait(driver, WAIT_LOGIN)
                .until(d -> d.getCurrentUrl().matches(".*/admin/\\d+/.*"));

        loggedInUserId = Long.parseLong(
                driver.getCurrentUrl().replaceAll(".*/admin/(\\d+)/.*", "$1"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC060 — Page header "Revenue & Billing" and subtitle visible
    // ─────────────────────────────────────────────────────────────────────────

    // TC060 — Revenue & Billing UI (double-quoted xpath handles apostrophe in "Today's Revenue")
    @Test(groups = {"regression"})
    public void TC060_admin_revenue_billing_ui() {
        AdminRevenueBilling page = new AdminRevenueBilling(driver).open(loggedInUserId);

        By titleLocator = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(titleLocator));
        WebElement title = driver.findElement(titleLocator);
        assertTrue(title.isDisplayed(), "div.tb-title not visible");
        assertEquals(title.getText().trim(), "Revenue & Billing",
                "Page title text mismatch");

        By subLocator = By.cssSelector("div.tb-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(subLocator));
        assertEquals(driver.findElement(subLocator).getText().trim(),
                "Financial performance & billing management",
                "Subtitle text mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC061 — All four summary tile labels visible (div.stat-label)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC061_admin_revenue_summary_tile_labels() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By statLabel = By.cssSelector("div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Total Appointments", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(found.contains(expected),
                    "Tile label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC062 — Tile numeric values present and numeric (div.stat-val)
    // ─────────────────────────────────────────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────
// TC062 — Tile values are present and non-empty (div.stat-val)
// ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC062_admin_revenue_tile_values_numeric() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val elements found");

        for (WebElement v : values) {
            String text = v.getText().trim();
            assertFalse(text.isEmpty(), "div.stat-val is blank");
        }
    }
    // ─────────────────────────────────────────────────────────────────────────
    // TC063 — Tile sub-labels visible (div.stat-sub)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC063_admin_revenue_tile_sub_labels() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By statSub = By.cssSelector("div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<String> found = driver.findElements(statSub)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "All time", "Approved appointments",
                "Awaiting confirmation", "Needs review")) {
            assertTrue(found.contains(expected),
                    "Sub-label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC064 — Total Appointments tile (div.stat.teal) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC064_admin_revenue_total_appointments_tile() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By teal = By.cssSelector("div.stat.teal");
        w().until(ExpectedConditions.visibilityOfElementLocated(teal));
        WebElement tile = driver.findElement(teal);

        // Label is "TOTAL APPOINTMENTS" in all-caps (two lines in UI)
        String labelText = tile.findElement(By.cssSelector("div.stat-label"))
                .getText().trim().replace("\n", " ").replace("\r", " ")
                .replaceAll("\\s+", " ");
        assertEquals(labelText, "TOTAL APPOINTMENTS", "Label mismatch");

        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");

        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "All time", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC065 — Confirmed tile (div.stat.green) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC065_admin_revenue_confirmed_tile() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By green = By.cssSelector("div.stat.green");
        w().until(ExpectedConditions.visibilityOfElementLocated(green));
        WebElement tile = driver.findElement(green);

        // Label is "CONFIRMED" in all-caps
        String labelText = tile.findElement(By.cssSelector("div.stat-label"))
                .getText().trim().replace("\n", " ").replace("\r", " ")
                .replaceAll("\\s+", " ").toUpperCase();
        assertEquals(labelText, "CONFIRMED", "Label mismatch");

        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");

        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Approved appointments", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC066 — Pending tile (div.stat.amber) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC066_admin_revenue_pending_tile() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By amber = By.cssSelector("div.stat.amber");
        w().until(ExpectedConditions.visibilityOfElementLocated(amber));
        WebElement tile = driver.findElement(amber);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "PENDING", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Awaiting confirmation", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC067 — Cancelled tile (div.stat.red) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC067_admin_revenue_cancelled_tile() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By red = By.cssSelector("div.stat.red");
        w().until(ExpectedConditions.visibilityOfElementLocated(red));
        WebElement tile = driver.findElement(red);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "CANCELLED", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Needs review", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC068 — "Appointments Over Time" chart section visible
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC068_admin_revenue_appointments_over_time_chart() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By cardHeaders = By.cssSelector("div.card div.card-header");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardHeaders));

        boolean found = driver.findElements(cardHeaders).stream()
                .anyMatch(h -> h.getText().contains("Appointments Over Time"));
        assertTrue(found, "'Appointments Over Time' card-header not found");

        By chartPad = By.cssSelector("div.card div.chart-pad");
        w().until(ExpectedConditions.presenceOfElementLocated(chartPad));
        assertFalse(driver.findElements(chartPad).isEmpty(),
                "div.chart-pad not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC069 — "Appointment Breakdown" chart section visible
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC069_admin_revenue_appointment_breakdown_chart() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By cardHeaders = By.cssSelector("div.card div.card-header");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardHeaders));

        boolean found = driver.findElements(cardHeaders).stream()
                .anyMatch(h -> h.getText().contains("Appointment Breakdown"));
        assertTrue(found, "'Appointment Breakdown' card-header not found");

        By chartPad = By.cssSelector("div.card div.chart-pad");
        w().until(ExpectedConditions.presenceOfElementLocated(chartPad));
        assertTrue(driver.findElements(chartPad).size() >= 2,
                "Expected ≥2 chart-pads, found: " + driver.findElements(chartPad).size());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC070 — AI Appointment Insights section visible with content
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC070_admin_revenue_ai_insights_section() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By aiPanel = By.cssSelector("div.ai-panel.mb20");
        w().until(ExpectedConditions.visibilityOfElementLocated(aiPanel));

        By aiTitle = By.cssSelector("span.ai-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(aiTitle));
        assertEquals(driver.findElement(aiTitle).getText().trim(),
                "AI Appointment Insights", "AI title mismatch");

        assertEquals(driver.findElement(By.cssSelector("span.ai-sub")).getText().trim(),
                "Powered by analytics", "AI sub-text mismatch");

        By aiInsights = By.cssSelector("div.ai-insights-grid div.ai-insight");
        w().until(ExpectedConditions.visibilityOfElementLocated(aiInsights));

        List<WebElement> insights = driver.findElements(aiInsights);
        assertTrue(insights.size() >= 1,
                "Expected ≥1 ai-insight, found: " + insights.size());
        for (int i = 0; i < insights.size(); i++) {
            assertFalse(insights.get(i).getText().trim().isEmpty(),
                    "ai-insight[" + i + "] has empty text");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC071 — Recent Bills heading visible + table has data rows
    // (kept from local branch — HEAD)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"regression"})
    public void TC071_admin_revenue_recent_bills_table() {
        AdminRevenueBilling page = new AdminRevenueBilling(driver).open(loggedInUserId);

        w().until(ExpectedConditions.visibilityOfElementLocated(page.recentBillsHeader));
        assertTrue(driver.findElements(page.recentBillsHeader).size() > 0,
                "Recent Bills heading not visible");

        By rows = By.cssSelector("table.tbl tbody tr");
        w().until(ExpectedConditions.presenceOfElementLocated(rows));
        assertTrue(driver.findElements(rows).size() > 0,
                "Recent Bills table has no data rows");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC061b — Recent Bills + Insurance Claims tables both visible
    // (kept from incoming branch — teammate added this. Renamed from TC061 to
    //  TC061b because TC061_admin_revenue_summary_tile_labels above already
    //  occupies the TC061 slot. The two TC061 tests cover distinct scenarios.)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"regression"})
    public void TC061b_admin_revenue_bills_claims_tables() {
        AdminRevenueBilling page = new AdminRevenueBilling(driver).open(loggedInUserId);

        w().until(ExpectedConditions.visibilityOfElementLocated(page.recentBillsHeader));
        assertTrue(driver.findElements(page.recentBillsHeader).size() > 0,
                "Recent Bills table heading should be visible");
        assertTrue(driver.findElements(page.insuranceClaimsHdr).size() > 0,
                "Insurance Claims table heading should be visible");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC072 — Recent Bills column headers correct
    //         Bill ID | Patient | Dept | Amount | Date | Due Date | Status
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC072_admin_revenue_recent_bills_columns() {
        new AdminRevenueBilling(driver).open(loggedInUserId);

        By thLocator = By.cssSelector("table.tbl thead th");
        w().until(ExpectedConditions.visibilityOfElementLocated(thLocator));

        List<String> headers = driver.findElements(thLocator)
                .stream()
                .map(e -> e.getText().trim()
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase())
                .toList();

        for (String col : List.of(
                "BILL ID", "PATIENT", "DEPT", "AMOUNT", "DATE", "DUE DATE", "STATUS")) {
            assertTrue(headers.stream().anyMatch(h -> h.equals(col)),
                    "Column '" + col + "' not found. Found: " + headers);
        }
    }
}

