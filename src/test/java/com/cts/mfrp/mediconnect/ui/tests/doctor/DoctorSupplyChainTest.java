package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorSupplyChain;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC049, TC081 — Doctor Supply Chain page. */
public class DoctorSupplyChainTest extends BaseDoctorTest {

    // TC049 — Supply Chain page UI
    @Test
    public void TC049_doctor_supply_chain_ui() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Total Items", "Low Stock", "Categories", "Orders Pending")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC081 — Low Stock filter
    @Test
    public void TC081_doctor_supply_chain_low_stock() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.lowStockFilter).size() > 0,
                "Low Stock filter/tab should be present");
    }
}
