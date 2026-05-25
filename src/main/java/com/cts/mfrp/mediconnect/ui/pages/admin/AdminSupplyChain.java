package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

/**
 * Admin Supply Chain Management — /admin/{userId}/supply
 *
 * Page layout (live UI):
 *   - Header "Supply Chain Management" + sub "Inventory & procurement tracking"
 *   - Header actions: "+ New Order", "Export CSV"
 *   - Four summary tiles: Total Items / Low Stock / Expiring Soon / Orders Pending
 *   - Two charts: Drug Usage Trend (multi-line) + Stock Consumption (donut)
 *   - AI Supply Insights panel
 *   - Inventory section: title + "27 items" subtitle, filter tabs (All / Low Stock / Expiring),
 *     search "Search items…", and a table (ITEM, CATEGORY, QTY, REORDER LEVEL, HOSPITAL, STATUS)
 */
public class AdminSupplyChain extends BasePage {

    public AdminSupplyChain(WebDriver driver) {
        super(driver);
    }

    // ===== Header =====
    public final By pageHeader        = By.xpath("//*[normalize-space()='Supply Chain Management']");
    public final By pageSubtitle      = By.xpath("//*[contains(normalize-space(),'Inventory') and contains(normalize-space(),'procurement')]");
    public final By newOrderBtn       = By.xpath("//button[contains(normalize-space(),'New Order') or contains(normalize-space(),'+ New Order')]");
    public final By exportCsvBtn      = By.xpath("//button[contains(normalize-space(),'Export CSV') or contains(normalize-space(),'Export')]");

    // ===== Four summary tiles =====
    public final By tileTotalItems     = By.xpath("//*[contains(normalize-space(),'TOTAL ITEMS') or contains(normalize-space(),'Total Items')]");
    public final By tileLowStock       = By.xpath("//*[contains(normalize-space(),'LOW STOCK') or contains(normalize-space(),'Low Stock')]");
    public final By tileExpiringSoon   = By.xpath("//*[contains(normalize-space(),'EXPIRING SOON') or contains(normalize-space(),'Expiring Soon')]");
    public final By tileOrdersPending  = By.xpath("//*[contains(normalize-space(),'ORDERS PENDING') or contains(normalize-space(),'Orders Pending')]");

    // ===== Charts =====
    public final By drugUsageTrendHdr  = By.xpath("//*[contains(normalize-space(),'Drug Usage Trend')]");
    public final By stockConsumptionHdr = By.xpath("//*[contains(normalize-space(),'Stock Consumption')]");
    public final By anyChart           = By.cssSelector("svg, canvas");

    // Drug Usage Trend legend items (4 drugs)
    public final By legendParacetamol  = By.xpath("//*[contains(normalize-space(),'Paracetamol')]");
    public final By legendAspirin      = By.xpath("//*[contains(normalize-space(),'Aspirin')]");
    public final By legendMetformin    = By.xpath("//*[contains(normalize-space(),'Metformin')]");
    public final By legendAmlodipine   = By.xpath("//*[contains(normalize-space(),'Amlodipine')]");

    // Stock Consumption legend items (3 categories)
    public final By legendMedicine     = By.xpath("//*[contains(normalize-space(),'Medicine')]");
    public final By legendConsumables  = By.xpath("//*[contains(normalize-space(),'Consumables')]");
    public final By legendEquipment    = By.xpath("//*[contains(normalize-space(),'Equipment')]");

    // ===== AI Supply Insights =====
    public final By aiInsightsHeading  = By.xpath("//*[contains(normalize-space(),'AI Supply Insights')]");
    public final By predictiveBadge    = By.xpath("//*[contains(normalize-space(),'Predictive analytics')]");

    // ===== Inventory section =====
    public final By inventoryHeading   = By.xpath("//*[normalize-space()='Inventory']");
    public final By itemCountSubtitle  = By.xpath("//*[contains(normalize-space(),'items') and not(self::th) and not(self::td)]");
    public final By filterAll          = By.xpath("//button[normalize-space()='All']");
    public final By filterLowStock     = By.xpath("/html/body/app-root/app-admin-layout/div/div[2]/app-admin-supply/div[2]/div[4]/div[1]/div[2]/div[1]/button[2]");
    public final By filterExpiring     = By.xpath("//button[normalize-space()='Expiring']");
    public final By searchItemsInput   = By.cssSelector("input[placeholder*='Search items' i], input[placeholder*='Search' i]");

    // Inventory table
    public final By inventoryTable     = By.cssSelector("table");
    public final By tableHeaderCells   = By.cssSelector("table thead th");
    public final By tableRows          = By.cssSelector("table tbody tr");
    public final By statusBadgeOk      = By.xpath("//*[normalize-space()='OK']");

    // ===== Navigation =====
    public AdminSupplyChain open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/supply");
        waitUntilLoaded();
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/supply"); }

    /** Long wait until URL + page header are present. Supply Chain is data-heavy and slow. */
    public AdminSupplyChain waitUntilLoaded() {
        longWait(60).until(d -> isLoaded() && d.findElements(pageHeader).size() > 0);
        return this;
    }

    /** Wait for the four summary tiles row to appear. */
    public AdminSupplyChain waitForTilesGrid() {
        longWait(60).until(d -> d.findElements(tileTotalItems).size() > 0
                || d.findElements(tileLowStock).size() > 0
                || d.findElements(tileExpiringSoon).size() > 0
                || d.findElements(tileOrdersPending).size() > 0);
        return this;
    }

    /** Wait for at least one chart svg/canvas to render. */
    public AdminSupplyChain waitForCharts() {
        longWait(60).until(d -> d.findElements(anyChart).size() > 0);
        return this;
    }

    /** Wait for the inventory table to populate. */
    public AdminSupplyChain waitForInventoryTable() {
        longWait(60).until(d -> d.findElements(inventoryTable).size() > 0
                && d.findElements(tableRows).size() > 0);
        return this;
    }

    public void clickNewOrder()       { click(newOrderBtn); }
    public void clickExportCsv()      { click(exportCsvBtn); }

    // ===== New Purchase Order modal =====
    // Heading + form fields located by their label text (resilient to class-name churn).
    public final By orderModalHeading      = By.xpath("//*[normalize-space()='New Purchase Order']");
    public final By orderItemName          = By.xpath("//label[contains(normalize-space(),'Item Name')]/following-sibling::input");
    public final By orderCategorySelect    = By.xpath("//label[normalize-space()='Category']/following-sibling::select");
    public final By orderQuantityInput     = By.xpath("//label[contains(normalize-space(),'Quantity')]/following-sibling::input");
    public final By orderReorderLevelInput = By.xpath("//label[contains(normalize-space(),'Reorder Level')]/following-sibling::input");
    public final By orderHospitalSelect    = By.xpath("//label[normalize-space()='Hospital']/following-sibling::select");
    public final By orderNotesTextarea     = By.xpath("//label[normalize-space()='Notes']/following-sibling::textarea");
    public final By orderPlaceBtn          = By.xpath("//button[normalize-space()='Place Order']");
    public final By orderCancelBtn         = By.xpath("//button[normalize-space()='Cancel']");

    public boolean isOrderModalOpen() { return isDisplayed(orderModalHeading); }

    public AdminSupplyChain fillNewOrderForm(String itemName, String category, String quantity,
                                             String reorderLevel, String hospital, String notes) {
        type(orderItemName, itemName);
        new Select(visible(orderCategorySelect)).selectByVisibleText(category);
        // Quantity defaults to 1 and Reorder Level to 10 — clear before typing.
        WebElement qty = visible(orderQuantityInput);
        qty.clear();
        qty.sendKeys(quantity);
        WebElement reorder = visible(orderReorderLevelInput);
        reorder.clear();
        reorder.sendKeys(reorderLevel);
        new Select(visible(orderHospitalSelect)).selectByVisibleText(hospital);
        if (notes != null && !notes.isEmpty()) {
            type(orderNotesTextarea, notes);
        }
        return this;
    }

    public void clickPlaceOrder() { click(orderPlaceBtn); }
    public void clickFilterAll()      { click(filterAll); }
    public void clickFilterLowStock() {
//        click(filterLowStock);
        JavascriptExecutor js= (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",driver.findElement(filterLowStock));
    }
    public void clickFilterExpiring() {
//        click(filterExpiring);
        JavascriptExecutor js= (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",driver.findElement(filterExpiring));
    }
    public void searchItem(String q)  { type(searchItemsInput, q); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }

    private Wait<WebDriver> longWait(long seconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(seconds))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }
}
