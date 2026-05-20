package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorSupplyChain;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC049, TC081 — Doctor Supply Chain page.
 * URL: /doctor/{id}/supply-chain
 *
 * DOM locators from browser inspection:
 *   div.sc-page                  → page wrapper
 *   h1.page-title                → "Supply Chain"
 *   p.page-sub                   → "Inventory · item name, quantity, reorder level"
 *   button.btn.btn-ghost         → "Export"
 *   button.btn.btn-primary       → "Add item"
 *   div.stats-row div.stat-card  → 4 stat cards
 *   div.stat-label               → Total items | Low stock | Categories | Orders pending
 *   div.stat-val                 → numeric value
 *   span.stat-sub                → In inventory | Below reorder | Types tracked | Awaiting delivery
 *   div.main-card div.toolbar    → toolbar wrapper
 *   div.toolbar div.search-wrap  → search input
 *   div.toolbar select.tb-input  → All categories | All status dropdowns
 *   div.table-wrap table         → inventory table
 *   thead tr th                  → ITEM NAME | CATEGORY | QUANTITY | REORDER LEVEL |
 *                                  STOCK LEVEL | STATUS | ACTIONS
 *   tbody tr                     → inventory data rows
 *   div.pagination span.pg-info  → "Showing 1–10 of 11 items"
 *   div.pg-btns button.pg-btn    → pagination buttons
 */
public class DoctorSupplyChainTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC049 — Supply Chain page UI (original)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC049_doctor_supply_chain_ui() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);

        w().until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "Page header 'Supply Chain' not found");

        w().until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),'Total items')]")));

        for (String tile : List.of(
                "Total items", "Low stock", "Categories", "Orders pending")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC081 — Low Stock filter present (original)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC081_doctor_supply_chain_low_stock() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);

        w().until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.lowStockFilter).size() > 0,
                "Low Stock filter/tab should be present");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC01 — Page title "Supply Chain" visible
    //           h1.page-title → "Supply Chain"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC01_supply_chain_page_title() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));

        assertEquals(driver.findElement(title).getText().trim(),
                "Supply Chain", "Page title mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC02 — Page subtitle visible
    //           p.page-sub → "Inventory · item name, quantity, reorder level"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC02_supply_chain_page_subtitle() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));

        assertEquals(driver.findElement(sub).getText().trim(),
                "Inventory · item name, quantity, reorder level",
                "Page subtitle mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC03 — Export button present and enabled
    //           button.btn.btn-ghost → "Export"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC03_supply_chain_export_button() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By exportBtn = By.cssSelector("button.btn.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(exportBtn));

        WebElement btn = driver.findElement(exportBtn);
        assertTrue(btn.isDisplayed(), "Export button not visible");
        assertTrue(btn.getText().trim().contains("Export"),
                "Export button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Export button is disabled");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC04 — "Add item" button present and enabled
    //           button.btn.btn-primary → "Add item"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC04_supply_chain_add_item_button() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By addBtn = By.cssSelector("button.btn.btn-primary");
        w().until(ExpectedConditions.visibilityOfElementLocated(addBtn));

        WebElement btn = driver.findElement(addBtn);
        assertTrue(btn.isDisplayed(), "Add item button not visible");
        assertTrue(btn.getText().trim().contains("Add item"),
                "Add item button text mismatch: '" + btn.getText() + "'");
        assertTrue(btn.isEnabled(), "Add item button is disabled");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC05 — All four stat card labels visible
    //           div.stat-label → Total items | Low stock | Categories | Orders pending
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC05_supply_chain_stat_card_labels() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "TOTAL ITEMS", "LOW STOCK", "CATEGORIES", "ORDERS PENDING")) {
            assertTrue(found.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC06 — Stat card values are non-empty
    //           div.stat-val → non-empty text
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC06_supply_chain_stat_card_values() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC07 — Stat card sub-labels visible
    //           span.stat-sub → In inventory | Below reorder |
    //                           Types tracked | Awaiting delivery
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC07_supply_chain_stat_card_sub_labels() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By statSub = By.cssSelector("div.stats-row span.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));

        List<String> found = driver.findElements(statSub)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "In inventory", "Below reorder",
                "Types tracked", "Awaiting delivery")) {
            assertTrue(found.contains(expected),
                    "Sub-label '" + expected + "' missing. Found: " + found);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC08 — Toolbar search and filters present
    //           div.search-wrap | 2x select.tb-input
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC08_supply_chain_toolbar_filters() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By toolbar = By.cssSelector("div.main-card div.toolbar");
        w().until(ExpectedConditions.visibilityOfElementLocated(toolbar));

        // Search input
        By searchWrap = By.cssSelector("div.toolbar div.search-wrap");
        assertTrue(driver.findElements(searchWrap).size() > 0,
                "Search wrap not found in toolbar");

        // Two select dropdowns (All categories + All status)
        List<WebElement> selects = driver.findElements(
                By.cssSelector("div.toolbar select.tb-input"));
        assertTrue(selects.size() >= 2,
                "Expected ≥2 select.tb-input, found: " + selects.size());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC09 — Table column headers correct
    //           ITEM NAME | CATEGORY | QUANTITY | REORDER LEVEL |
    //           STOCK LEVEL | STATUS | ACTIONS
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC09_supply_chain_table_columns() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By thLocator = By.cssSelector("div.table-wrap table thead th");
        w().until(ExpectedConditions.visibilityOfElementLocated(thLocator));

        List<String> headers = driver.findElements(thLocator)
                .stream()
                .map(e -> e.getText().trim()
                        .replace("\n", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase())
                .toList();

        for (String col : List.of(
                "ITEM NAME", "CATEGORY", "QUANTITY",
                "REORDER LEVEL", "STOCK LEVEL", "STATUS", "ACTIONS")) {
            assertTrue(headers.stream().anyMatch(h -> h.equals(col)),
                    "Column '" + col + "' not found. Found: " + headers);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC10 — Table has data rows
    //           div.table-wrap table tbody tr
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC10_supply_chain_table_has_rows() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By rows = By.cssSelector("div.table-wrap table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        assertTrue(driver.findElements(rows).size() > 0,
                "Supply chain table has no data rows");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC11 — Table row data validation
    //           ITEM NAME | CATEGORY | QUANTITY | REORDER LEVEL | STATUS non-empty
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC11_supply_chain_table_row_data() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By rows = By.cssSelector("div.table-wrap table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        List<WebElement> dataRows = driver.findElements(rows);
        int rowsToCheck = Math.min(3, dataRows.size());

        for (int i = 0; i < rowsToCheck; i++) {
            List<WebElement> cells = dataRows.get(i)
                    .findElements(By.cssSelector("td"));
            assertTrue(cells.size() >= 5,
                    "Row " + i + " should have ≥5 columns, found: " + cells.size());

            // col 0 — ITEM NAME
            String itemName = cells.get(0).getText().trim();
            assertFalse(itemName.isEmpty(),
                    "Row " + i + " ITEM NAME is empty");

            // col 1 — CATEGORY (Medication / Equipment)
            String category = cells.get(1).getText().trim();
            assertFalse(category.isEmpty(),
                    "Row " + i + " CATEGORY is empty");
            assertTrue(List.of("Medication", "Equipment")
                            .stream().anyMatch(category::contains),
                    "Row " + i + " CATEGORY unexpected: '" + category + "'");

            // col 2 — QUANTITY (numeric)
            String quantity = cells.get(2).getText().trim();
            assertFalse(quantity.isEmpty(),
                    "Row " + i + " QUANTITY is empty");
            assertTrue(quantity.matches("\\d+"),
                    "Row " + i + " QUANTITY not numeric: '" + quantity + "'");

            // col 3 — REORDER LEVEL (numeric)
            String reorder = cells.get(3).getText().trim();
            assertFalse(reorder.isEmpty(),
                    "Row " + i + " REORDER LEVEL is empty");
            assertTrue(reorder.matches("\\d+"),
                    "Row " + i + " REORDER LEVEL not numeric: '" + reorder + "'");

            // col 5 — STATUS (OK / Low)
            String status = cells.get(5).getText().trim();
            assertFalse(status.isEmpty(),
                    "Row " + i + " STATUS is empty");
            assertTrue(List.of("OK", "Low", "Critical")
                            .stream().anyMatch(status::contains),
                    "Row " + i + " STATUS unexpected: '" + status + "'");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC12 — Pagination label visible
    //           span.pg-info → "Showing 1–10 of 11 items"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC12_supply_chain_pagination_label() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By pgInfo = By.cssSelector("div.pagination span.pg-info");
        w().until(ExpectedConditions.visibilityOfElementLocated(pgInfo));

        String text = driver.findElement(pgInfo).getText().trim();
        assertFalse(text.isEmpty(), "span.pg-info is empty");
        assertTrue(text.contains("items"),
                "Pagination label should contain 'items': '" + text + "'");
        assertTrue(text.matches("Showing \\d+.\\d+ of \\d+ items"),
                "Pagination label format unexpected: '" + text + "'");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC_SC13 — Pagination buttons present
    //           div.pg-btns button.pg-btn → at least 2 buttons
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC_SC13_supply_chain_pagination_buttons() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        By pgBtns = By.cssSelector("div.pg-btns button.pg-btn");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(pgBtns, 0));

        List<WebElement> buttons = driver.findElements(pgBtns);
        assertTrue(buttons.size() >= 2,
                "Expected ≥2 pagination buttons, found: " + buttons.size());

        // Active page button must exist
        By activePg = By.cssSelector("div.pg-btns button.pg-btn.active");
        assertTrue(driver.findElements(activePg).size() > 0,
                "No active pagination button found");
    }
}
