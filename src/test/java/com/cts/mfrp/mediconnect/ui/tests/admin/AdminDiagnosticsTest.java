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
 * FRD baseline: TC057 (tabs).
 */
public class AdminDiagnosticsTest extends BaseAdminTest {

    // TC057_057a_057b — Tabs visible, tab switching works, top summary tiles
    // Merged TC057 + TC057a + TC057b
    @Test(groups = {"regression"})
    public void TC057_057a_057b_admin_diagnostics_tabs_and_tiles() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);

        // TC057 — page header + 3 tabs
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header missing");
        for (String tab : List.of("Lab Reports", "Radiology", "Imaging")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + tab + "']")).size() > 0,
                    "Tab missing: " + tab);
        }

        // TC057a — tab switching
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

        // TC057b — 4 summary tiles
        page.waitForTilesGrid();
        for (String tile : List.of("Total Reports", "Pending Review", "Abnormal Flags", "Imaging Requests")) {
            boolean found = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'" + tile + "')]")).size() > 0;
            assertTrue(found, "Summary tile missing: " + tile);
        }
    }

    // TC057c_057d — Lab Reports filters/table + search filter
    // Merged TC057c + TC057d
    @Test(groups = {"regression"})
    public void TC057c_057d_admin_diagnostics_filters_and_search() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        page.waitForTabBodyReady();

        // TC057c — search input, status dropdown, table columns
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

        // TC057d — search filter
        List<WebElement> search = driver.findElements(page.searchInput);
        if (!search.isEmpty()) {
            search.get(0).clear();
            search.get(0).sendKeys("Rajesh");
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(page.labReportsTable).size() > 0,
                    "Table should remain visible after typing in search");
        }
    }

    // TC057e_057f_057g — Row action buttons + status badges + AI summary panel
    // Merged TC057e + TC057f + TC057g
    @Test(groups = {"regression"})
    public void TC057e_057f_057g_admin_diagnostics_actions_badges_ai() {
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
