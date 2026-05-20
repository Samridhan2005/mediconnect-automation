package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

/**
 * Admin Diagnostics Module — /admin/{userId}/diagnostics
 *
 * Page layout (as captured from the live UI):
 *   - Header "Diagnostics Module" with sub-line "Lab Reports · Radiology · Imaging · {date}"
 *   - Top-right filters: "All Hospitals" + month dropdown + notification bell
 *   - Four summary tiles: Total Reports / Pending Review / Abnormal Flags / Imaging Requests
 *   - Tab bar: Lab Reports (active) / Radiology / Imaging
 *   - Tab body: "Lab Reports" card with search ("Search patient / test..."), "All status" dropdown,
 *     and a table (PATIENT, TEST NAME, REPORT DATE, RESULT, STATUS, ACTIONS — View / AI Explain / Upload Result)
 *   - Bottom: AI Report Summary / predictive analytics text
 */
public class AdminDiagnostics extends BasePage {

    public AdminDiagnostics(WebDriver driver) {
        super(driver);
    }

    // ===== Page-level =====
    public final By pageHeader        = By.xpath("//*[normalize-space()='Diagnostics Module']");
    public final By pageSubLine       = By.xpath("//*[contains(normalize-space(),'Lab Reports') and contains(normalize-space(),'Radiology') and contains(normalize-space(),'Imaging')]");
    public final By allHospitalsDdl   = By.xpath("//*[contains(normalize-space(),'All Hospitals')]");
    public final By notificationBell  = By.cssSelector("[class*='notif'], [class*='bell']");

    // ===== Four summary tiles =====
    public final By tileTotalReports     = By.xpath("//*[contains(normalize-space(),'Total Reports')]");
    public final By tilePendingReview    = By.xpath("//*[contains(normalize-space(),'Pending Review')]");
    public final By tileAbnormalFlags    = By.xpath("//*[contains(normalize-space(),'Abnormal Flags')]");
    public final By tileImagingRequests  = By.xpath("//*[contains(normalize-space(),'Imaging Requests')]");

    // ===== Tabs =====
    public final By tabLabReports  = By.xpath("//button[normalize-space()='Lab Reports'] | //*[contains(@class,'tab') and normalize-space()='Lab Reports']");
    public final By tabRadiology   = By.xpath("//button[normalize-space()='Radiology']   | //*[contains(@class,'tab') and normalize-space()='Radiology']");
    public final By tabImaging     = By.xpath("//button[normalize-space()='Imaging']     | //*[contains(@class,'tab') and normalize-space()='Imaging']");
    public final By activeTab      = By.cssSelector("[class*='tab'].active, [class*='tab active']");

    // ===== Lab Reports tab body =====
    public final By cardHeading        = By.xpath("(//h2|//h3|//*[contains(@class,'card-title')])[contains(normalize-space(),'Lab Reports')]");
    public final By searchInput        = By.cssSelector("input[placeholder*='Search patient' i], input[placeholder*='Search' i][placeholder*='test' i]");
    public final By statusDropdown     = By.xpath("//*[contains(normalize-space(),'All status') or contains(normalize-space(),'All Status')]");
    public final By labReportsTable    = By.cssSelector("table");
    public final By tableRows          = By.cssSelector("table tbody tr");
    public final By viewButtons        = By.xpath("//button[normalize-space()='View']");
    public final By aiExplainButtons   = By.xpath("//button[normalize-space()='AI Explain']");
    public final By uploadResultButtons= By.xpath("//button[normalize-space()='Upload Result']");

    // Status badges in the rows
    public final By statusBadgeReady    = By.xpath("//*[normalize-space()='Ready']");
    public final By statusBadgeAbnormal = By.xpath("//*[normalize-space()='Abnormal']");
    public final By statusBadgePending  = By.xpath("//*[normalize-space()='Pending']");

    // ===== AI Report Summary panel (bottom of page) =====
    public final By aiReportSummary    = By.xpath("//*[contains(normalize-space(),'AI Report Summary') or contains(normalize-space(),'AI summary') or contains(normalize-space(),'Predictive analytics')]");

    // ===== Navigation =====
    public AdminDiagnostics open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/diagnostics");
        waitUntilLoaded();
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/diagnostics"); }

    /** Long wait — the diagnostics page is slow to render, especially the table. */
    public AdminDiagnostics waitUntilLoaded() {
        longWait(60).until(d -> isLoaded() && d.findElements(pageHeader).size() > 0);
        return this;
    }

    /** Wait for ANY of the four summary tiles to be present — proves the top grid rendered. */
    public AdminDiagnostics waitForTilesGrid() {
        longWait(60).until(d -> d.findElements(tileTotalReports).size() > 0
                || d.findElements(tilePendingReview).size() > 0
                || d.findElements(tileAbnormalFlags).size() > 0
                || d.findElements(tileImagingRequests).size() > 0);
        return this;
    }

    /** Wait for the active tab body to populate (table OR an empty-state message). */
    public AdminDiagnostics waitForTabBodyReady() {
        longWait(60).until(d -> d.findElements(labReportsTable).size() > 0
                || d.findElements(By.xpath("//*[contains(normalize-space(),'No reports')]")).size() > 0);
        return this;
    }

    public void clickLabReportsTab() { click(tabLabReports); }
    public void clickRadiologyTab()  { click(tabRadiology); }
    public void clickImagingTab()    { click(tabImaging); }
    public void clickFirstView()           { click(viewButtons); }
    public void clickFirstAiExplain()      { click(aiExplainButtons); }
    public void clickFirstUploadResult()   { click(uploadResultButtons); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }

    // ===== Helpers =====
    /**
     * A FluentWait with a long timeout — diagnostics pages are slow to render data.
     * Polls every 2s, ignores stale/no-such-element exceptions.
     */
    private Wait<WebDriver> longWait(long seconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(seconds))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }
}
