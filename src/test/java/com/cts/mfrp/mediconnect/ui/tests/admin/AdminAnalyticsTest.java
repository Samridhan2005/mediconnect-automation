package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminAnalytics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

/**
 * Admin Analytics & Insights (/admin/{id}/analytics).
 *
 * Tests are written against the page as it is actually rendered today.
 * Tile/chart names match what the app shows, not the original FRD wording.
 *
 * The page renders asynchronously (Angular charts populate after initial load),
 * so every assertion is preceded by `wait.until(...)` so Selenium polls for the
 * element to appear instead of failing on the first DOM snapshot.
 */
public class AdminAnalyticsTest extends BaseAdminTest {

    // Helper — polls up to 45s for an element to appear, scrolls it into view, then asserts presence.
    private void waitAndAssertVisible(By locator, String message) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(45));
        longWait.until(d -> d.findElements(locator).size() > 0);

        // Scroll the element into view — charts below the fold may need this for visibility checks.
        if (!driver.findElements(locator).isEmpty()) {
            WebElement el = driver.findElements(locator).get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        assertTrue(driver.findElements(locator).size() > 0, message);
    }

    // TC062 — Page header, sub-label, and 4 summary tiles render
    @Test(groups = {"regression"})
    public void TC062_admin_analytics_insights_ui() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        waitAndAssertVisible(page.pageHeader,
                "'Analytics & Insights' header should be visible");
        waitAndAssertVisible(page.subLabel,
                "Sub-label 'Clinical performance · All hospitals · Live data' should be visible");

        waitAndAssertVisible(page.tilePatientFlow,
                "Summary tile 'Patient Flow' should be visible");
        waitAndAssertVisible(page.tileBedOccupancy,
                "Summary tile 'Bed Occupancy' should be visible");
        waitAndAssertVisible(page.tileCompletionRate,
                "Summary tile 'Completion Rate' should be visible");
        waitAndAssertVisible(page.tileCancellationRate,
                "Summary tile 'Cancellation Rate' should be visible");
    }

    // TC063 — Filter dropdowns visible (Period + Department + Hospital).
    @Test(groups = {"regression"})
    public void TC063_admin_analytics_filters() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        // Wait for at least one dropdown to appear before counting.
        wait.until(d -> d.findElements(page.anyDropdown).size() > 0);

        int periodHits      = driver.findElements(page.periodSelect).size();
        int deptHits        = driver.findElements(page.departmentSelect).size();
        int hospitalHits    = driver.findElements(page.hospitalSelect).size();
        int anyDropdownHits = driver.findElements(page.anyDropdown).size();

        boolean specific = periodHits > 0 && deptHits > 0 && hospitalHits > 0;
        boolean generic  = anyDropdownHits >= 3;
        assertTrue(specific || generic,
                "Expected Period, Department and Hospital filters on the analytics page. " +
                        "Specific period=" + periodHits + ", dept=" + deptHits +
                        ", hospital=" + hospitalHits + ", generic dropdowns=" + anyDropdownHits);
    }

    // TC064 — Appointment Flow Heatmap is present, with Low → High legend
    @Test(groups = {"regression"})
    public void TC064_appointment_flow_heatmap_visible() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        waitAndAssertVisible(page.heatmap,
                "'Appointment Flow Heatmap' should be visible");
        waitAndAssertVisible(page.heatmapLegendLow,
                "Heatmap 'Low' legend marker should be visible");
        waitAndAssertVisible(page.heatmapLegendHigh,
                "Heatmap 'High' legend marker should be visible");
    }

    // TC065 — All four secondary charts render in the lower section
    @Test(groups = {"regression"})
    public void TC065_secondary_charts_render() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        waitAndAssertVisible(page.bedOccupancyByHospitalChart,
                "'Bed Occupancy by Hospital' chart should be visible");
        waitAndAssertVisible(page.appointmentCompletionChart,
                "'Appointment Completion Rate' chart should be visible");
        waitAndAssertVisible(page.cancellationRateTrendChart,
                "'Cancellation Rate Trend' chart should be visible");
        waitAndAssertVisible(page.appointmentsByDeptChart,
                "'Appointments by Department' chart should be visible");
    }
}
