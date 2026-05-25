package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorSupplyChain;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC049, TC081 — Doctor Supply Chain page.
 * URL: /doctor/{id}/supply-chain
 */
public class DoctorSupplyChainTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }

    // TC049_SC01_SC04 — Page UI: header tiles, title, subtitle, export & add buttons
    // Merged TC049 + TC_SC01 + TC_SC02 + TC_SC03 + TC_SC04
    @Test(groups = {"regression"})
    public void doctor_supply_chain_ui_and_header_buttons() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);

        // TC049 — page header + tile labels
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

        // TC_SC01 — title
        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));
        assertEquals(driver.findElement(title).getText().trim(),
                "Supply Chain", "Page title mismatch");

        // TC_SC02 — subtitle
        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));
        assertEquals(driver.findElement(sub).getText().trim(),
                "Inventory · item name, quantity, reorder level",
                "Page subtitle mismatch");

        // TC_SC03 — Export button
        By exportBtn = By.cssSelector("button.btn.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(exportBtn));
        WebElement exp = driver.findElement(exportBtn);
        assertTrue(exp.isDisplayed(), "Export button not visible");
        assertTrue(exp.getText().trim().contains("Export"),
                "Export button text mismatch: '" + exp.getText() + "'");
        assertTrue(exp.isEnabled(), "Export button is disabled");

        // TC_SC04 — Add item button
        By addBtn = By.cssSelector("button.btn.btn-primary");
        w().until(ExpectedConditions.visibilityOfElementLocated(addBtn));
        WebElement add = driver.findElement(addBtn);
        assertTrue(add.isDisplayed(), "Add item button not visible");
        assertTrue(add.getText().trim().contains("Add item"),
                "Add item button text mismatch: '" + add.getText() + "'");
        assertTrue(add.isEnabled(), "Add item button is disabled");
    }

    // TC081_SC08 — Low stock filter + toolbar (search + 2 dropdowns)
    // Merged TC081 + TC_SC08
    @Test(groups = {"regression"})
    public void doctor_supply_chain_filters_and_toolbar() {
        DoctorSupplyChain page = new DoctorSupplyChain(driver).open(loggedInUserId);

        // TC081 — Low Stock filter present
        w().until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.lowStockFilter).size() > 0,
                "Low Stock filter/tab should be present");

        // TC_SC08 — toolbar with search + filters
        By toolbar = By.cssSelector("div.main-card div.toolbar");
        w().until(ExpectedConditions.visibilityOfElementLocated(toolbar));
        By searchWrap = By.cssSelector("div.toolbar div.search-wrap");
        assertTrue(driver.findElements(searchWrap).size() > 0,
                "Search wrap not found in toolbar");
        List<WebElement> selects = driver.findElements(
                By.cssSelector("div.toolbar select.tb-input"));
        assertTrue(selects.size() >= 2,
                "Expected >=2 select.tb-input, found: " + selects.size());
    }

    // TC_SC05_SC07 — Stat card labels, values, sub-labels
    // Merged TC_SC05 + TC_SC06 + TC_SC07
    @Test(groups = {"regression"})
    public void supply_chain_stat_cards() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        // TC_SC05 — labels (uppercase rendering)
        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));
        List<String> foundLabels = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();
        for (String expected : List.of(
                "TOTAL ITEMS", "LOW STOCK", "CATEGORIES", "ORDERS PENDING")) {
            assertTrue(foundLabels.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + foundLabels);
        }

        // TC_SC06 — values non-empty
        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));
        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");
        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }

        // TC_SC07 — sub-labels
        By statSub = By.cssSelector("div.stats-row span.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));
        List<String> foundSubs = driver.findElements(statSub)
                .stream().map(e -> e.getText().trim()).toList();
        for (String expected : List.of(
                "In inventory", "Below reorder",
                "Types tracked", "Awaiting delivery")) {
            assertTrue(foundSubs.contains(expected),
                    "Sub-label '" + expected + "' missing. Found: " + foundSubs);
        }
    }

    // TC_SC09_SC13 — Table columns, rows, row data, pagination label, pagination buttons
    // Merged TC_SC09 + TC_SC10 + TC_SC11 + TC_SC12 + TC_SC13
    @Test(groups = {"regression"})
    public void supply_chain_table_and_pagination() {
        new DoctorSupplyChain(driver).open(loggedInUserId);

        // TC_SC09 — column headers
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

        // TC_SC10 — has data rows
        By rows = By.cssSelector("div.table-wrap table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));
        List<WebElement> dataRows = driver.findElements(rows);
        assertTrue(dataRows.size() > 0, "Supply chain table has no data rows");

        // TC_SC11 — row data validation
        int rowsToCheck = Math.min(3, dataRows.size());
        for (int i = 0; i < rowsToCheck; i++) {
            List<WebElement> cells = dataRows.get(i)
                    .findElements(By.cssSelector("td"));
            assertTrue(cells.size() >= 5,
                    "Row " + i + " should have >=5 columns, found: " + cells.size());

            String itemName = cells.get(0).getText().trim();
            assertFalse(itemName.isEmpty(),
                    "Row " + i + " ITEM NAME is empty");

            String category = cells.get(1).getText().trim();
            assertFalse(category.isEmpty(),
                    "Row " + i + " CATEGORY is empty");
            assertTrue(List.of("Medication", "Equipment")
                            .stream().anyMatch(category::contains),
                    "Row " + i + " CATEGORY unexpected: '" + category + "'");

            String quantity = cells.get(2).getText().trim();
            assertFalse(quantity.isEmpty(),
                    "Row " + i + " QUANTITY is empty");
            assertTrue(quantity.matches("\\d+"),
                    "Row " + i + " QUANTITY not numeric: '" + quantity + "'");

            String reorder = cells.get(3).getText().trim();
            assertFalse(reorder.isEmpty(),
                    "Row " + i + " REORDER LEVEL is empty");
            assertTrue(reorder.matches("\\d+"),
                    "Row " + i + " REORDER LEVEL not numeric: '" + reorder + "'");

            String status = cells.get(5).getText().trim();
            assertFalse(status.isEmpty(),
                    "Row " + i + " STATUS is empty");
            assertTrue(List.of("OK", "Low", "Critical")
                            .stream().anyMatch(status::contains),
                    "Row " + i + " STATUS unexpected: '" + status + "'");
        }

        // TC_SC12 — pagination label
        By pgInfo = By.cssSelector("div.pagination span.pg-info");
        w().until(ExpectedConditions.visibilityOfElementLocated(pgInfo));
        String text = driver.findElement(pgInfo).getText().trim();
        assertFalse(text.isEmpty(), "span.pg-info is empty");
        assertTrue(text.contains("items"),
                "Pagination label should contain 'items': '" + text + "'");
        assertTrue(text.matches("Showing \\d+.\\d+ of \\d+ items"),
                "Pagination label format unexpected: '" + text + "'");

        // TC_SC13 — pagination buttons
        By pgBtns = By.cssSelector("div.pg-btns button.pg-btn");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(pgBtns, 0));
        List<WebElement> buttons = driver.findElements(pgBtns);
        assertTrue(buttons.size() >= 2,
                "Expected >=2 pagination buttons, found: " + buttons.size());

        By activePg = By.cssSelector("div.pg-btns button.pg-btn.active");
        assertTrue(driver.findElements(activePg).size() > 0,
                "No active pagination button found");
    }

    @DataProvider(name = "addItemData")
    public Object[][] addItemData() {
        return TestData.addItemIds();
    }

    @Test(groups = {"regression"}, dataProvider = "addItemData")
    public void add_item_submit_creates_record(String testId) {
        Map<String, String> data = TestData.addItem(testId);

        new DoctorSupplyChain(driver).open(loggedInUserId);

        By trigger = By.cssSelector("button.btn.btn-primary");
        w().until(ExpectedConditions.elementToBeClickable(trigger));
        WebElement triggerBtn = driver.findElement(trigger);
        assertTrue(triggerBtn.getText().trim().contains("Add item"),
                "Expected '+ Add item' trigger; got: '" + triggerBtn.getText() + "'");
        triggerBtn.click();

        By modal = By.cssSelector("app-inventory-item-form div.modal");
        WebElement dialog = w().until(ExpectedConditions.visibilityOfElementLocated(modal));

        WebElement itemNameInput = dialog.findElement(
                By.cssSelector("input[formcontrolname='itemName']"));
        WebElement categorySelect = dialog.findElement(
                By.cssSelector("select[formcontrolname='category']"));
        WebElement quantityInput = dialog.findElement(
                By.cssSelector("input[formcontrolname='quantity']"));
        WebElement reorderInput = dialog.findElement(
                By.cssSelector("input[formcontrolname='reorderLevel']"));

        assertTrue(itemNameInput.isDisplayed(),  "Item name input not visible");
        assertTrue(categorySelect.isDisplayed(), "Category select not visible");
        assertTrue(quantityInput.isDisplayed(),  "Quantity input not visible");
        assertTrue(reorderInput.isDisplayed(),   "Reorder level input not visible");

        String itemName = data.get("itemName");
        itemNameInput.clear();
        itemNameInput.sendKeys(itemName);

        String category = data.get("category");
        if (category != null && !category.isBlank()) {
            new org.openqa.selenium.support.ui.Select(categorySelect).selectByVisibleText(category);
        }

        quantityInput.clear();
        quantityInput.sendKeys(data.get("quantity"));

        reorderInput.clear();
        reorderInput.sendKeys(data.get("reorderLevel"));

        WebElement form = dialog.findElement(By.tagName("form"));
        try {
            w().until(d -> {
                String cls = form.getAttribute("class");
                return cls != null && cls.contains("ng-valid") && !cls.contains("ng-invalid");
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Form did not become ng-valid after filling required fields. "
                    + "Class: " + form.getAttribute("class"));
        }

        WebElement submit = dialog.findElement(
                By.xpath(".//button[normalize-space()='Add item']"));
        assertTrue(submit.isEnabled(), "Submit 'Add item' button disabled with valid form");
        submit.click();

        try {
            w().until(ExpectedConditions.invisibilityOfElementLocated(modal));
        } catch (org.openqa.selenium.TimeoutException e) {
            List<WebElement> errors = driver.findElements(
                    By.cssSelector("[class*='error'], [class*='alert'], [class*='toast']"));
            String errorMsg = errors.stream()
                    .filter(WebElement::isDisplayed)
                    .map(el -> el.getText().trim())
                    .filter(t -> !t.isEmpty())
                    .findFirst()
                    .orElse("(no visible error message)");
            org.testng.Assert.fail("Modal did not close after submit. "
                    + "Form class: " + form.getAttribute("class") + ". "
                    + "Error message: " + errorMsg);
        }

        driver.navigate().refresh();
        w().until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.table-wrap table tbody tr")));

        By searchInput = By.cssSelector("div.toolbar input[placeholder*='Search' i]");
        w().until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement search = driver.findElement(searchInput);
        search.clear();
        search.sendKeys(itemName);

        By matchedRow = By.xpath("//tr[contains(normalize-space(.), '" + itemName + "')]");
        try {
            w().until(d -> !d.findElements(matchedRow).isEmpty());
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Newly added item '" + itemName + "' not found in inventory "
                    + "table even after refresh + search. Total items shown: "
                    + driver.findElements(By.cssSelector("div.table-wrap table tbody tr")).size());
        }
    }
}
