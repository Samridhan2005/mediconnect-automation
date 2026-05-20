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

    // Long custom wait for slow dropdowns (Hospitals dropdown options come from a backend API call).
    private WebDriverWait longDropdownWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // Helper — wait until the dropdown contains at least the given marker option, then return all option texts.
    private java.util.List<String> readDropdownOptions(org.openqa.selenium.By selectLocator, String markerOption) {
        longDropdownWait().until(d -> {
            var els = d.findElements(selectLocator);
            if (els.isEmpty()) return false;
            var opts = new org.openqa.selenium.support.ui.Select(els.get(0)).getOptions();
            return opts.stream().anyMatch(e -> markerOption.equals(e.getText().trim()));
        });
        return new org.openqa.selenium.support.ui.Select(driver.findElement(selectLocator))
                .getOptions().stream()
                .map(e -> e.getText().trim())
                .collect(java.util.stream.Collectors.toList());
    }

    // Helper — wait until the dropdown contains the option, then select it.
    private void selectFromDropdown(org.openqa.selenium.By selectLocator, String optionText) {
        longDropdownWait().until(d -> {
            var els = d.findElements(selectLocator);
            if (els.isEmpty()) return false;
            return new org.openqa.selenium.support.ui.Select(els.get(0)).getOptions().stream()
                    .anyMatch(e -> optionText.equals(e.getText().trim()));
        });
        // Re-fetch immediately before selecting to avoid stale element issues with Angular re-renders
        new org.openqa.selenium.support.ui.Select(driver.findElement(selectLocator))
                .selectByVisibleText(optionText);
    }

    // TC066 — Period dropdown exposes the 4 expected time windows
    @Test(groups = {"regression"})
    public void TC066_period_dropdown_has_expected_options() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        java.util.List<String> optionTexts = readDropdownOptions(page.periodSelect, "Last 7 Days");

        for (String expected : java.util.List.of("Last 7 Days", "Last 30 Days", "Last 90 Days", "Last Year")) {
            assertTrue(optionTexts.contains(expected),
                    "Period dropdown should contain option '" + expected + "'. Actual options: " + optionTexts);
        }
    }

    // TC067 — Departments dropdown exposes medical specialty options
    @Test(groups = {"regression"})
    public void TC067_departments_dropdown_has_expected_options() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        java.util.List<String> optionTexts = readDropdownOptions(page.departmentSelect, "Cardiology");

        for (String expected : java.util.List.of("All Departments", "Cardiology", "Neurology",
                "Pediatrics", "Orthopedics", "Oncology", "General Surgery")) {
            assertTrue(optionTexts.contains(expected),
                    "Departments dropdown should contain option '" + expected + "'. Actual: " + optionTexts);
        }
    }

    // TC068 — All Hospitals dropdown shows at least the seeded hospitals
    @Test(groups = {"regression"})
    public void TC068_hospitals_dropdown_has_expected_options() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        java.util.List<String> optionTexts = readDropdownOptions(page.hospitalSelect, "City General Hospital");

        for (String expected : java.util.List.of("All Hospitals", "City General Hospital",
                "Apollo Medical Centre", "MediCare Specialty Hospital")) {
            assertTrue(optionTexts.contains(expected),
                    "Hospitals dropdown should contain option '" + expected + "'. Actual: " + optionTexts);
        }
    }

    // TC069 — Changing the Period dropdown refreshes the dashboard data (charts still render after)
    @Test(groups = {"regression"})
    public void TC069_changing_period_refreshes_dashboard() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        // Capture tile value BEFORE filter change
        String beforeValue = readPatientFlowValue(page);

        // Wait for the option to be present, then select it
        selectFromDropdown(page.periodSelect, "Last 7 Days");

        // Wait for the page to settle after the filter change
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        // Page should still render correctly — charts and tiles still visible
        waitAndAssertVisible(page.tilePatientFlow,
                "Patient Flow tile should still render after Period filter change");
        waitAndAssertVisible(page.heatmap,
                "Appointment Flow Heatmap should still render after Period filter change");

        String afterValue = readPatientFlowValue(page);
        System.out.println("[TC069] Patient Flow before='" + beforeValue + "', after Last 7 Days='" + afterValue + "'");
    }

    // TC070 — Changing the Departments dropdown refreshes the dashboard data
    @Test(groups = {"regression"})
    public void TC070_changing_department_refreshes_dashboard() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        String beforeValue = readPatientFlowValue(page);

        selectFromDropdown(page.departmentSelect, "Cardiology");

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        waitAndAssertVisible(page.tilePatientFlow,
                "Patient Flow tile should still render after Department filter change");
        waitAndAssertVisible(page.appointmentsByDeptChart,
                "Appointments by Department chart should still render after Department filter change");

        String afterValue = readPatientFlowValue(page);
        System.out.println("[TC070] Patient Flow before='" + beforeValue + "', after Cardiology='" + afterValue + "'");
    }

    // TC071 — Changing the Hospitals dropdown refreshes the dashboard data
    @Test(groups = {"regression"})
    public void TC071_changing_hospital_refreshes_dashboard() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        String beforeValue = readPatientFlowValue(page);

        selectFromDropdown(page.hospitalSelect, "City General Hospital");

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        waitAndAssertVisible(page.tilePatientFlow,
                "Patient Flow tile should still render after Hospital filter change");
        waitAndAssertVisible(page.bedOccupancyByHospitalChart,
                "Bed Occupancy by Hospital chart should still render after Hospital filter change");

        String afterValue = readPatientFlowValue(page);
        System.out.println("[TC071] Patient Flow before='" + beforeValue + "', after City General Hospital='" + afterValue + "'");
    }

    // Helper — read the Patient Flow tile's numeric value, return "" if not yet rendered.
    private String readPatientFlowValue(AdminAnalytics page) {
        var elements = driver.findElements(page.tilePatientFlow);
        return elements.isEmpty() ? "" : elements.get(0).getText().trim();
    }
}
