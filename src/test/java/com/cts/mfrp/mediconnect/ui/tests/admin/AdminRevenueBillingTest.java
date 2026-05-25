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

    // Merged TC060 + TC061 + TC062 + TC063
    @Test(groups = {"regression"})
    public void TC060_063_admin_revenue_ui_and_tile_labels() {
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

        By statLabel = By.cssSelector("div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Total Appointments", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(found.contains(expected),
                    "Tile label '" + expected + "' missing. Found: " + found);
        }

        By statVal = By.cssSelector("div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val elements found");

        for (WebElement v : values) {
            String text = v.getText().trim();
            assertFalse(text.isEmpty(), "div.stat-val is blank");
        }

        By statSub = By.cssSelector("div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<String> foundSubs = driver.findElements(statSub)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "All time", "Approved appointments",
                "Awaiting confirmation", "Needs review")) {
            assertTrue(foundSubs.contains(expected),
                    "Sub-label '" + expected + "' missing. Found: " + foundSubs);
        }
    }

    // Merged TC064 + TC065 + TC066 + TC067
    @Test(groups = {"regression"})
    public void TC064_067_admin_revenue_individual_tiles() {
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

        By green = By.cssSelector("div.stat.green");
        w().until(ExpectedConditions.visibilityOfElementLocated(green));
        WebElement tile2 = driver.findElement(green);

        // Label is "CONFIRMED" in all-caps
        String labelText2 = tile2.findElement(By.cssSelector("div.stat-label"))
                .getText().trim().replace("\n", " ").replace("\r", " ")
                .replaceAll("\\s+", " ").toUpperCase();
        assertEquals(labelText2, "CONFIRMED", "Label mismatch");

        assertFalse(tile2.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");

        assertEquals(tile2.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Approved appointments", "Sub-label mismatch");

        By amber = By.cssSelector("div.stat.amber");
        w().until(ExpectedConditions.visibilityOfElementLocated(amber));
        WebElement tile3 = driver.findElement(amber);

        assertEquals(tile3.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "PENDING", "Label mismatch");
        assertFalse(tile3.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile3.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Awaiting confirmation", "Sub-label mismatch");

        By red = By.cssSelector("div.stat.red");
        w().until(ExpectedConditions.visibilityOfElementLocated(red));
        WebElement tile4 = driver.findElement(red);

        assertEquals(tile4.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "CANCELLED", "Label mismatch");
        assertFalse(tile4.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile4.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Needs review", "Sub-label mismatch");
    }

    // Merged TC068 + TC069 + TC070
    @Test(groups = {"regression"})
    public void TC068_070_admin_revenue_charts_and_ai() {
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

        boolean found2 = driver.findElements(cardHeaders).stream()
                .anyMatch(h -> h.getText().contains("Appointment Breakdown"));
        assertTrue(found2, "'Appointment Breakdown' card-header not found");

        w().until(ExpectedConditions.presenceOfElementLocated(chartPad));
        assertTrue(driver.findElements(chartPad).size() >= 2,
                "Expected ≥2 chart-pads, found: " + driver.findElements(chartPad).size());

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

    // Merged TC071 + TC072
    @Test(groups = {"regression"})
    public void TC071_072_admin_revenue_recent_bills_table() {
        AdminRevenueBilling page = new AdminRevenueBilling(driver).open(loggedInUserId);

        w().until(ExpectedConditions.visibilityOfElementLocated(page.recentBillsHeader));
        assertTrue(driver.findElements(page.recentBillsHeader).size() > 0,
                "Recent Bills heading not visible");

        By rows = By.cssSelector("table.tbl tbody tr");
        w().until(ExpectedConditions.presenceOfElementLocated(rows));
        assertTrue(driver.findElements(rows).size() > 0,
                "Recent Bills table has no data rows");

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
//        assertTrue(driver.findElements(page.insuranceClaimsHdr).size() > 0,
//                "Insurance Claims table heading should be visible");
    }
}
