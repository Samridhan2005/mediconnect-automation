package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminDiagnostics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class AdminDiagnosticsTest extends BaseAdminTest {


    @Test(groups = {"regression"})
    public void admin_diagnostics_tabs_and_tiles() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);

        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header missing");
        for (String tab : List.of("Lab Reports", "Radiology", "Imaging")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + tab + "']")).size() > 0,
                    "Tab missing: " + tab);
        }

        page.waitForTabBodyReady();
        page.clickRadiologyTab();
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(By.cssSelector("[class*='tab'].active")).size() > 0);
        boolean radContent = driver.findElements(page.labReportsTable).size() > 0
                || driver.findElements(By.xpath("//*[contains(normalize-space(),'No reports') or contains(normalize-space(),'No data')]")).size() > 0;
        assertTrue(radContent, "Radiology tab content area should be visible");

        page.clickImagingTab();
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(By.cssSelector("[class*='tab'].active")).size() > 0);
        boolean imgContent = driver.findElements(page.labReportsTable).size() > 0
                || driver.findElements(By.xpath("//*[contains(normalize-space(),'No reports') or contains(normalize-space(),'No data')]")).size() > 0;
        assertTrue(imgContent, "Imaging tab content area should be visible");

        page.clickLabReportsTab();
        page.waitForTabBodyReady();
        assertTrue(driver.findElements(page.labReportsTable).size() > 0,
                "Lab Reports tab should re-show its table after returning");

        page.waitForTilesGrid();
        for (String tile : List.of("Total Reports", "Pending Review", "Abnormal Flags", "Imaging Requests")) {
            boolean found = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'" + tile + "')]")).size() > 0;
            assertTrue(found, "Summary tile missing: " + tile);
        }
    }

    @Test(groups = {"regression"})
    public void admin_diagnostics_filters_and_columns() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        assertTrue(driver.findElements(page.searchInput).size() > 0,
                "Search input ('Search patient / test…') should be visible");
        assertTrue(driver.findElements(page.statusDropdown).size() > 0,
                "'All status' dropdown should be visible");

        for (String col : List.of("PATIENT", "TEST NAME", "REPORT DATE", "RESULT", "STATUS", "ACTIONS")) {
            assertTrue(driver.findElements(By.xpath(
                            "//th[contains(translate(normalize-space()," +
                                    "'abcdefghijklmnopqrstuvwxyz'," +
                                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'" + col + "')]")).size() > 0,
                    "Table column missing: " + col);
        }
    }

    @DataProvider(name = "diagnosticsSearch")
    public Object[][] diagnosticsSearch() {
        return TestData.diagnosticsSearchIds();
    }


    @Test(groups = {"regression"}, dataProvider = "diagnosticsSearch")
    public void admin_diagnostics_search(String testId) {
        Map<String, String> data = TestData.diagnosticsSearch(testId);
        String query    = data.get("query");
        String expected = data.get("expectedResult");

        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();
        page.search(query);

        if ("noMatch".equalsIgnoreCase(expected)) {
            boolean emptyOrMessage = page.visibleRowCount() == 0
                    || driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'No reports') " +
                            "or contains(normalize-space(),'No data') " +
                            "or contains(normalize-space(),'No results')]")).size() > 0;
            assertTrue(emptyOrMessage,
                    "[" + testId + "] Query '" + query + "' was expected to yield no matches, "
                            + "but the table still has " + page.visibleRowCount() + " row(s)");
        } else {
            boolean anyContainsQuery = page.visibleRowTexts().stream()
                    .anyMatch(t -> t.toLowerCase().contains(query.toLowerCase()));
            assertTrue(anyContainsQuery,
                    "[" + testId + "] At least one visible row should contain '" + query + "' "
                            + "after searching, but visible cells were: " + page.visibleRowTexts());
        }

        assertEquals(driver.findElement(page.searchInput).getAttribute("value"), query,
                "[" + testId + "] Search input should retain the typed query");
    }

    @DataProvider(name = "diagnosticsStatus")
    public Object[][] diagnosticsStatus() {
        return TestData.diagnosticsStatusIds();
    }


    @Test(groups = {"regression"}, dataProvider = "diagnosticsStatus")
    public void admin_diagnostics_status_filter(String testId) {
        Map<String, String> data = TestData.diagnosticsStatus(testId);
        String status = data.get("status");

        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        page.selectStatus(status);

        List<String> badges = page.visibleStatusBadges();
        if ("All status".equalsIgnoreCase(status) || "All Status".equalsIgnoreCase(status)) {
            assertTrue(badges.size() > 0,
                    "[" + testId + "] 'All status' should leave at least one row visible");
        } else {
            assertTrue(badges.size() > 0,
                    "[" + testId + "] At least one row should remain after filtering by '" + status + "'");
            for (String badge : badges) {
                assertTrue(badge.equalsIgnoreCase(status),
                        "[" + testId + "] Every visible row should have status '" + status
                                + "', but found: " + badge + " (full list: " + badges + ")");
            }
        }
    }


    @Test(groups = {"regression"})
    public void admin_diagnostics_actions_badges_ai() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        // TC057e — action buttons
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.viewButtons).size() > 0);
        assertTrue(driver.findElements(page.viewButtons).size() > 0,
                "'View' action buttons should be visible in the ACTIONS column");
        assertTrue(driver.findElements(page.aiExplainButtons).size() > 0,
                "'AI Explain' action buttons should be visible");
        assertTrue(driver.findElements(page.uploadResultButtons).size() > 0,
                "'Upload Result' action buttons should be visible");

        // TC057f — status badges
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.statusBadgeReady).size() > 0
                        || d.findElements(page.statusBadgeAbnormal).size() > 0
                        || d.findElements(page.statusBadgePending).size() > 0);

        boolean anyBadge =
                driver.findElements(page.statusBadgeReady).size() > 0
                || driver.findElements(page.statusBadgeAbnormal).size() > 0
                || driver.findElements(page.statusBadgePending).size() > 0;
        assertTrue(anyBadge,
                "At least one status badge (Ready/Abnormal/Pending) should be present in the table");

        // TC057g — AI report summary
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.aiReportSummary).size() > 0);
        assertTrue(driver.findElements(page.aiReportSummary).size() > 0,
                "AI Report Summary / predictive-analytics section should be visible at the bottom");
    }
}
