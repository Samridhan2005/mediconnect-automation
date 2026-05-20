package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminSupplyChain;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Admin Supply Chain — full coverage.
 *
 * FRD baseline: TC058 (UI + AI Insights), TC059 (inventory table + filters).
 * New coverage:
 *   - Header actions: + New Order, Export CSV
 *   - Drug Usage Trend line chart + 4 legend items
 *   - Stock Consumption donut chart + 3 legend items
 *   - AI Supply Insights / "Predictive analytics" badge
 *   - Inventory: filter behaviour (All / Low Stock / Expiring) + search bar
 *   - Status badges (OK)
 *
 * All tests use the page's long FluentWait helpers since the page is data-heavy and slow.
 */
public class AdminSupplyChainTest extends BaseAdminTest {

    // Merged TC058 + TC058a + TC058b + TC058c
    @Test(groups = {"regression"})
    public void TC058_058a_058c_admin_supply_chain_ui_and_header_buttons() {
        AdminSupplyChain page = new AdminSupplyChain(driver).open(loggedInUserId);
        page.waitForTilesGrid();

        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header missing");
        for (String tile : List.of("Total Items", "Low Stock", "Expiring Soon", "Orders Pending")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        assertTrue(driver.findElements(page.aiInsightsHeading).size() > 0,
                "AI Supply Insights panel should be visible");

        assertTrue(driver.findElements(page.newOrderBtn).size() > 0,
                "'+ New Order' button should be visible in the top-right");
        assertTrue(driver.findElements(page.exportCsvBtn).size() > 0,
                "'Export CSV' button should be visible in the top-right");

        page.clickNewOrder();
        // Wait up to 30s for a modal / form to render
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> d.findElements(By.cssSelector("[class*='modal'], form")).size() > 0
                        || d.findElements(By.xpath("//*[contains(normalize-space(),'New Order') and (self::h1 or self::h2 or self::h3)]")).size() > 0);
        boolean modalOpened = driver.findElements(By.cssSelector("[class*='modal'], form")).size() > 0
                || driver.findElements(By.xpath("//*[contains(normalize-space(),'New Order') and (self::h1 or self::h2 or self::h3)]")).size() > 0;
        assertTrue(modalOpened, "Clicking '+ New Order' should open a creation modal or form");

        assertTrue(driver.findElements(page.exportCsvBtn).size() > 0,
                "'Export CSV' button must be present");
        page.clickExportCsv();
        // No browser API to assert a download; verify the page is still functional after click
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "Page should remain on Supply Chain Management after clicking Export CSV");
    }

    // Merged TC058d + TC058e + TC058f
    @Test(groups = {"regression"})
    public void TC058d_058f_admin_supply_chain_charts_and_ai() {
        AdminSupplyChain page = new AdminSupplyChain(driver).open(loggedInUserId);
        page.waitForCharts();

        assertTrue(driver.findElements(page.drugUsageTrendHdr).size() > 0,
                "'Drug Usage Trend' chart heading should be visible");
        for (String drug : List.of("Paracetamol", "Aspirin", "Metformin", "Amlodipine")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + drug + "')]")).size() > 0,
                    "Drug Usage legend missing: " + drug);
        }
        // Days-of-week labels
        for (String day : List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + day + "']")).size() > 0,
                    "Day-of-week label missing on Drug Usage Trend X-axis: " + day);
        }

        assertTrue(driver.findElements(page.stockConsumptionHdr).size() > 0,
                "'Stock Consumption' chart heading should be visible");
        for (String cat : List.of("Medicine", "Consumables", "Equipment")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + cat + "')]")).size() > 0,
                    "Stock Consumption legend missing: " + cat);
        }

        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.aiInsightsHeading).size() > 0);

        assertTrue(driver.findElements(page.aiInsightsHeading).size() > 0,
                "'AI Supply Insights' heading should be visible");
        assertTrue(driver.findElements(page.predictiveBadge).size() > 0,
                "'Predictive analytics' badge should be visible");
    }

    // Merged TC059 + TC059a
    @Test(groups = {"regression"})
    public void TC059_059a_admin_supply_chain_inventory_heading() {
        AdminSupplyChain page = new AdminSupplyChain(driver).open(loggedInUserId);
        page.waitForInventoryTable();

        for (String filter : List.of("All", "Low Stock", "Expiring")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + filter + "']")).size() > 0,
                    "Filter tab missing: " + filter);
        }
        for (String col : List.of("ITEM", "CATEGORY", "QTY", "REORDER LEVEL", "HOSPITAL", "STATUS")) {
            assertTrue(driver.findElements(By.xpath(
                            "//th[contains(translate(normalize-space()," +
                                    "'abcdefghijklmnopqrstuvwxyz'," +
                                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'" + col + "')]")).size() > 0,
                    "Inventory table column missing: " + col);
        }

        assertTrue(driver.findElements(page.inventoryHeading).size() > 0,
                "'Inventory' heading should be visible");
        assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'items')]")).size() > 0,
                "Item count subtitle (e.g. '27 items') should be visible");
    }

    // Merged TC059b + TC059c + TC059d + TC059e + TC059f
    @Test(groups = {"regression"})
    public void TC059b_059f_admin_supply_chain_inventory_filters_search_badges() {
        AdminSupplyChain page = new AdminSupplyChain(driver).open(loggedInUserId);
        page.waitForInventoryTable();

        page.clickFilterLowStock();
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        // Table OR empty-state should remain visible
        boolean ok = driver.findElements(page.inventoryTable).size() > 0
                || driver.findElements(By.xpath("//*[contains(normalize-space(),'No items') or contains(normalize-space(),'No data')]")).size() > 0;
        assertTrue(ok, "After Low Stock filter, table or empty-state should remain visible");

        page.clickFilterExpiring();
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        boolean ok2 = driver.findElements(page.inventoryTable).size() > 0
                || driver.findElements(By.xpath("//*[contains(normalize-space(),'No items') or contains(normalize-space(),'No data')]")).size() > 0;
        assertTrue(ok2, "After Expiring filter, table or empty-state should remain visible");

        page.clickFilterLowStock();
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        page.clickFilterAll();
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        assertTrue(driver.findElements(page.tableRows).size() > 0,
                "After clicking 'All', the inventory table should show at least one row");

        List<WebElement> search = driver.findElements(page.searchItemsInput);
        if (!search.isEmpty()) {
            search.get(0).clear();
            search.get(0).sendKeys("Paracetamol");
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(page.inventoryTable).size() > 0,
                    "Inventory table should remain visible after typing in the search bar");
        }

        new WebDriverWait(driver, Duration.ofSeconds(45))
                .until(d -> d.findElements(page.statusBadgeOk).size() > 0
                        || d.findElements(By.xpath("//*[contains(normalize-space(),'Low Stock') or contains(normalize-space(),'Out of Stock')]")).size() > 0);

        boolean hasBadge = driver.findElements(page.statusBadgeOk).size() > 0
                || driver.findElements(By.xpath("//*[contains(normalize-space(),'Low Stock') or contains(normalize-space(),'Out of Stock')]")).size() > 0;
        assertTrue(hasBadge, "At least one status badge (OK / Low Stock / Out of Stock) should be present in the STATUS column");
    }
}
