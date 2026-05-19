package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminSupplyChain;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC058, TC059 — Admin Supply Chain Management. */
public class AdminSupplyChainTest extends BaseAdminTest {

    // TC058 — Supply Chain UI + AI insights
    @Test(groups = {"regression"})
    public void TC058_admin_supply_chain_ui_ai_insights() {
        AdminSupplyChain page = new AdminSupplyChain(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Total Items", "Low Stock", "Expiring Soon", "Orders Pending")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        assertTrue(driver.findElements(page.aiInsights).size() > 0,
                "AI Supply Insights panel should be visible");
    }

    // TC059 — Inventory table + filters
    @Test(groups = {"regression"})
    public void TC059_admin_supply_chain_inventory_table() {
        AdminSupplyChain page = new AdminSupplyChain(driver).open(loggedInUserId);
        for (String filter : List.of("All", "Low Stock", "Expiring")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + filter + "']")).size() > 0,
                    "Filter tab missing: " + filter);
        }
        for (String col : List.of("ITEM", "CATEGORY", "QTY", "REORDER LEVEL", "HOSPITAL", "STATUS")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
                                    + col + "')]")).size() > 0,
                    "Inventory table column missing: " + col);
        }
    }
}
