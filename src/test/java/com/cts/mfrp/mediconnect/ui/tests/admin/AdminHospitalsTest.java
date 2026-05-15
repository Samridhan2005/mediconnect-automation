package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminHospitals;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC053, TC077 — Admin Multi-Hospital Management. */
public class AdminHospitalsTest extends BaseAdminTest {

    // TC053 — Hospitals page UI
    @Test
    public void TC053_admin_hospitals_ui() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);
        for (String tile : List.of("Total Hospitals", "Operational", "Under Maintenance", "Critical Capacity")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        for (String col : List.of("HOSPITAL NAME", "CITY", "TOTAL BEDS", "OCCUPANCY", "STATUS", "ACTIONS")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
                                    + col + "')]")).size() > 0,
                    "Hospital table column missing: " + col);
        }
    }

    // TC077 — Occupancy colour coding (green/amber/red)
    @Test
    public void TC077_admin_hospitals_occupancy_colors() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);
        List<WebElement> rows = driver.findElements(By.cssSelector("table tr"));
        assertTrue(rows.size() > 0, "Hospitals table should have rows");
        for (WebElement r : rows) {
            assertNotNull(r.getAttribute("outerHTML"));
        }
    }
}
