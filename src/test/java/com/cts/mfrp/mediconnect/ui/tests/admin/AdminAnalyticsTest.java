package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminAnalytics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class AdminAnalyticsTest extends BaseAdminTest {

    private void waitAndAssertVisible(By locator, String message) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(45));
        longWait.until(d -> d.findElements(locator).size() > 0);

        if (!driver.findElements(locator).isEmpty()) {
            WebElement el = driver.findElements(locator).get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        assertTrue(driver.findElements(locator).size() > 0, message);
    }

    @Test(groups = {"regression"})
    public void admin_analytics_ui_and_filters() {
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

    @Test(groups = {"regression"})
    public void admin_analytics_charts() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        waitAndAssertVisible(page.heatmap,
                "'Appointment Flow Heatmap' should be visible");
        waitAndAssertVisible(page.heatmapLegendLow,
                "Heatmap 'Low' legend marker should be visible");
        waitAndAssertVisible(page.heatmapLegendHigh,
                "Heatmap 'High' legend marker should be visible");

        waitAndAssertVisible(page.bedOccupancyByHospitalChart,
                "'Bed Occupancy by Hospital' chart should be visible");
        waitAndAssertVisible(page.appointmentCompletionChart,
                "'Appointment Completion Rate' chart should be visible");
        waitAndAssertVisible(page.cancellationRateTrendChart,
                "'Cancellation Rate Trend' chart should be visible");
        waitAndAssertVisible(page.appointmentsByDeptChart,
                "'Appointments by Department' chart should be visible");
    }

    private WebDriverWait longDropdownWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(60));
    }

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

    @Test(groups = {"regression"})
    public void admin_analytics_dropdown_options() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        java.util.List<String> optionTexts = readDropdownOptions(page.periodSelect, "Last 7 Days");

        for (String expected : java.util.List.of("Last 7 Days", "Last 30 Days", "Last 90 Days", "Last Year")) {
            assertTrue(optionTexts.contains(expected),
                    "Period dropdown should contain option '" + expected + "'. Actual options: " + optionTexts);
        }

        java.util.List<String> optionTexts2 = readDropdownOptions(page.departmentSelect, "Cardiology");

        for (String expected : java.util.List.of("All Departments", "Cardiology", "Neurology",
                "Pediatrics", "Orthopedics", "Oncology", "General Surgery")) {
            assertTrue(optionTexts2.contains(expected),
                    "Departments dropdown should contain option '" + expected + "'. Actual: " + optionTexts2);
        }

        java.util.List<String> optionTexts3 = readDropdownOptions(page.hospitalSelect, "City General Hospital");

        for (String expected : java.util.List.of("All Hospitals", "City General Hospital",
                "Apollo Medical Centre", "MediCare Specialty Hospital")) {
            assertTrue(optionTexts3.contains(expected),
                    "Hospitals dropdown should contain option '" + expected + "'. Actual: " + optionTexts3);
        }
    }

    @Test(groups = {"regression"})
    public void admin_analytics_filter_refresh() {
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

        String beforeValue2 = readPatientFlowValue(page);

        selectFromDropdown(page.departmentSelect, "Cardiology");

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        waitAndAssertVisible(page.tilePatientFlow,
                "Patient Flow tile should still render after Department filter change");
        waitAndAssertVisible(page.appointmentsByDeptChart,
                "Appointments by Department chart should still render after Department filter change");

        String afterValue2 = readPatientFlowValue(page);
        System.out.println("[TC070] Patient Flow before='" + beforeValue2 + "', after Cardiology='" + afterValue2 + "'");

        String beforeValue3 = readPatientFlowValue(page);

        selectFromDropdown(page.hospitalSelect, "City General Hospital");

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        waitAndAssertVisible(page.tilePatientFlow,
                "Patient Flow tile should still render after Hospital filter change");
        waitAndAssertVisible(page.bedOccupancyByHospitalChart,
                "Bed Occupancy by Hospital chart should still render after Hospital filter change");

        String afterValue3 = readPatientFlowValue(page);
        System.out.println("[TC071] Patient Flow before='" + beforeValue3 + "', after City General Hospital='" + afterValue3 + "'");
    }

    // Helper — read the Patient Flow tile's numeric value, return "" if not yet rendered.
    private String readPatientFlowValue(AdminAnalytics page) {
        var elements = driver.findElements(page.tilePatientFlow);
        return elements.isEmpty() ? "" : elements.get(0).getText().trim();
    }

    @DataProvider(name = "analyticsFilters")
    public Object[][] analyticsFilters() {
        return TestData.analyticsFilterIds();
    }

    @Test(groups = {"regression"}, dataProvider = "analyticsFilters")
    public void admin_analytics_filter_combinations(String testId) {
        Map<String, String> data = TestData.analyticsFilter(testId);
        String hospital   = data.get("hospital");
        String department = data.get("department");
        String period     = data.get("period");

        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);

        // Wait for each option to be present in its select before selecting it. The
        // hospital list comes from a backend call so this is the slow one.
        selectFromDropdown(page.hospitalSelect, hospital);
        selectFromDropdown(page.departmentSelect, department);
        selectFromDropdown(page.periodSelect, period);

        // Give Angular time to re-render charts after the three filter changes.
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        assertEquals(page.getSelectedHospital(), hospital,
                "[" + testId + "] Hospital filter should reflect the selected value");
        assertEquals(page.getSelectedDepartment(), department,
                "[" + testId + "] Department filter should reflect the selected value");
        assertEquals(page.getSelectedPeriod(), period,
                "[" + testId + "] Period filter should reflect the selected value");

        waitAndAssertVisible(page.tilePatientFlow,
                "[" + testId + "] Patient Flow tile should still render after applying all filters");
        waitAndAssertVisible(page.heatmap,
                "[" + testId + "] Appointment Flow Heatmap should still render after applying all filters");
    }
}
