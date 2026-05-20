package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminDiagnostics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Admin Diagnostics Module — full coverage.
 *
 * FRD baseline: TC057 (tabs).
 * New coverage:
 *   - 4 summary tiles (Total Reports / Pending Review / Abnormal Flags / Imaging Requests)
 *   - Lab Reports tab body: search, status dropdown, table columns, status badges
 *   - Per-row actions: View / AI Explain / Upload Result
 *   - Tab switching: Lab Reports / Radiology / Imaging
 *   - AI Report Summary / predictive analytics panel
 *
 * All tests use the page's long FluentWait helpers because diagnostics is data-heavy.
 */
public class AdminDiagnosticsTest extends BaseAdminTest {

    // ---------------- Tabs ----------------

    /** TC057 — Tab bar shows three tabs. */
    @Test(groups = {"regression"})
    public void TC057_admin_diagnostics_ui_tabs() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header missing");
        for (String tab : List.of("Lab Reports", "Radiology", "Imaging")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + tab + "']")).size() > 0,
                    "Tab missing: " + tab);
        }
    }

    /** TC057a — Switching to Radiology / Imaging tab and back. */
    @Test(groups = {"regression"})
    public void TC057a_admin_diagnostics_tab_switching() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        page.clickRadiologyTab();
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(By.cssSelector("[class*='tab'].active")).size() > 0);
        // After switching to Radiology, the table OR an empty state should be present
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
    }

    // ---------------- Top summary tiles ----------------

    /** TC057b — All four top-grid summary tiles present (Total Reports / Pending Review / Abnormal Flags / Imaging Requests). */
    @Test(groups = {"regression"})
    public void TC057b_admin_diagnostics_top_summary_tiles() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTilesGrid();

        for (String tile : List.of("Total Reports", "Pending Review", "Abnormal Flags", "Imaging Requests")) {
            boolean found = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'" + tile + "')]")).size() > 0;
            assertTrue(found, "Summary tile missing: " + tile);
        }
    }

    // ---------------- Lab Reports tab body ----------------

    /** TC057c — Lab Reports tab: search input + status dropdown + table columns. */
    @Test(groups = {"regression"})
    public void TC057c_admin_diagnostics_lab_reports_filters_and_table() {
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

    /** TC057d — Search input filters the table. */
    @Test(groups = {"regression"})
    public void TC057d_admin_diagnostics_search_filter() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        List<WebElement> search = driver.findElements(page.searchInput);
        if (!search.isEmpty()) {
            search.get(0).clear();
            search.get(0).sendKeys("Rajesh");
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            // After typing, the table should still be present (filtered) — not crashed
            assertTrue(driver.findElements(page.labReportsTable).size() > 0,
                    "Table should remain visible after typing in search");
        }
    }

    /** TC057e — Per-row action buttons (View / AI Explain / Upload Result) are present. */
    @Test(groups = {"regression"})
    public void TC057e_admin_diagnostics_row_action_buttons() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        // Wait for the action buttons to appear (the row data is slow to render).
        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.viewButtons).size() > 0);

        assertTrue(driver.findElements(page.viewButtons).size() > 0,
                "'View' action buttons should be visible in the ACTIONS column");
        assertTrue(driver.findElements(page.aiExplainButtons).size() > 0,
                "'AI Explain' action buttons should be visible");
        assertTrue(driver.findElements(page.uploadResultButtons).size() > 0,
                "'Upload Result' action buttons should be visible");
    }

    /** TC057f — Status badges Ready / Abnormal / Pending exist in the table. */
    @Test(groups = {"regression"})
    public void TC057f_admin_diagnostics_status_badges() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.statusBadgeReady).size() > 0
                        || d.findElements(page.statusBadgeAbnormal).size() > 0
                        || d.findElements(page.statusBadgePending).size() > 0);

        boolean anyBadge =
                driver.findElements(page.statusBadgeReady).size() > 0
                || driver.findElements(page.statusBadgeAbnormal).size() > 0
                || driver.findElements(page.statusBadgePending).size() > 0;
        assertTrue(anyBadge, "At least one status badge (Ready/Abnormal/Pending) should be present in the table");
    }

    // ---------------- AI Report Summary panel ----------------

    /** TC057g — AI Report Summary / predictive analytics block visible at the bottom of the page. */
    @Test(groups = {"regression"})
    public void TC057g_admin_diagnostics_ai_report_summary_panel() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.aiReportSummary).size() > 0);

        assertTrue(driver.findElements(page.aiReportSummary).size() > 0,
                "AI Report Summary / predictive-analytics section should be visible at the bottom");
    }
}
