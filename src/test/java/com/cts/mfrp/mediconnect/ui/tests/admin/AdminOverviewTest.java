package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertTrue;

/** FRD: TC051, TC052, TC084 — Admin System Overview page. */
public class AdminOverviewTest extends BaseAdminTest {

    // TC051 — System Overview UI validation
    @Test(groups = {"sanity", "regression"})
    public void TC051_admin_system_overview_ui() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded());

        for (String label : List.of("Admin Control","System Overview")) {
            assertTrue(driver.findElements(By.xpath("//*[text()='" + label + "']")).size() > 0,
                    "Page element missing: " + label);
        }
        for (String group : List.of("Overview", "Patients & Care", "Operations")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[text()='" + group + "']")).size() > 0,
                    "Sidebar group missing: " + group);
        }
        // Step 1: wait once until the tile area has rendered. The deployment renders
        // tile containers with various class names ('tile', 'card', 'summary', 'kpi'...)
        // so we look broadly — at least 3 such elements means the dashboard is past
        // its initial loading state and we can assert against tile labels.
        WebDriverWait tilesWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            tilesWait.until(d -> d.findElements(By.cssSelector(
                    "[class*='tile'], [class*='summary'], [class*='kpi'], [class*='stat']")).size() >= 3);
        } catch (org.openqa.selenium.TimeoutException te) {
            // fall through — let the per-tile assertions report the gap with clear messages
        }

        // Step 2: check each expected tile label. Structure-agnostic — finds the label
        // wherever it lives (div / span / h-tag / nested children). No per-tile FluentWait,
        // because the initial wait above already proved the dashboard has rendered.
        for (String tile : List.of("Total Patients", "Total Doctors", "Bed Occupancy",
                                   "Revenue (Month)", "Doctors On Duty")) {
            boolean found = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'" + tile + "')]")).size() > 0;
            assertTrue(found, "Summary tile missing: " + tile);
        }
    }

    // TC052 — Filters (Period + All Hospitals)
    @Test(groups = {"regression"})
    public void TC052_admin_filters_validation() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(driver.findElements(admin.periodSelector).size() > 0,
                "Period selector should be visible");
        assertTrue(driver.findElements(admin.allHospitalsDdl).size() > 0,
                "All Hospitals dropdown should be visible");
    }

    // TC084 — Hospital Branch Map on overview
    @Test(groups = {"regression"})
    public void TC084_admin_hospital_branch_map() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded());
        assertTrue(driver.findElements(admin.hospitalBranchMap).size() > 0,
                "Hospital Branch Map should be visible on Overview");
    }
}