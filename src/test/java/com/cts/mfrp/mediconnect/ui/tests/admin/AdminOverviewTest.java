package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC051, TC052, TC084 — Admin System Overview page. */
public class AdminOverviewTest extends BaseAdminTest {

    // TC051 — System Overview UI validation
    @Test
    public void TC051_admin_system_overview_ui() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded());

        for (String label : List.of("System Overview", "Period", "All Hospitals", "Notification")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "Page element missing: " + label);
        }
        for (String group : List.of("OVERVIEW", "PATIENTS & CARE", "OPERATIONS")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
                                    + group + "')]")).size() > 0,
                    "Sidebar group missing: " + group);
        }
        for (String tile : List.of("Total Hospitals", "Active Patients", "ICU Occupancy", "Inventory items", "Doctors On Duty")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC052 — Filters (Period + All Hospitals)
    @Test
    public void TC052_admin_filters_validation() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(driver.findElements(admin.periodSelector).size() > 0,
                "Period selector should be visible");
        assertTrue(driver.findElements(admin.allHospitalsDdl).size() > 0,
                "All Hospitals dropdown should be visible");
    }

    // TC084 — Hospital Branch Map on overview
    @Test
    public void TC084_admin_hospital_branch_map() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded());
        assertTrue(driver.findElements(admin.hospitalBranchMap).size() > 0,
                "Hospital Branch Map should be visible on Overview");
    }
}
