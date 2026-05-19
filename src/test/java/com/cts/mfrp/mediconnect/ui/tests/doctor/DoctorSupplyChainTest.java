package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorSupplyChain;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC049, TC081 — Doctor Supply Chain page. */
public class DoctorSupplyChainTest extends BaseDoctorTest {

    // TC049 — Supply Chain page UI
    @Test(groups = {"regression"})
    public void TC049_doctor_supply_chain_ui() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for page header before asserting tiles
        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header 'Supply Chain' not found");

        // Wait for at least the first tile before looping
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),'Total items')]")));
        for (String tile : List.of("Total items", "Low stock", "Categories", "Orders pending")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC081 — Low Stock filter
    @Test(groups = {"regression"})
    public void TC081_doctor_supply_chain_low_stock() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.lowStockFilter).size() > 0,
                "Low Stock filter/tab should be present");
    }
}
