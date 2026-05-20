package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Patient Medical Records page — /patient/{userId}/records */
public class PatientMedicalRecords extends BasePage {

    public final By pageHeader  = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='Medical Records']");
    public final By leftPanel   = By.cssSelector(".list-col");
    public final By rightPanel  = By.cssSelector(".layout-content .content");
    public final By searchInput = By.cssSelector("input.search-input, input[placeholder*='Search' i]");
    public final By recordItems = By.cssSelector(".record-item");

    // --- Extended locators ---

    // Page header
    public final By subLabel              = By.xpath("//*[contains(normalize-space(),'Your health history')]");
    public final By hamburgerMenu         = By.cssSelector("button[class*='hamburger'], button[class*='menu-toggle'], [aria-label*='menu' i]");
    public final By bloodGroupHeaderChip  = By.xpath("//*[contains(normalize-space(),'Blood group:') or contains(normalize-space(),'Blood Group:')]");
    public final By notificationBell      = By.cssSelector("button[class*='notif'], button[class*='bell'], [aria-label*='notif' i]");

    // Empty state messages
    public final By emptyRecordsLeftMsg   = By.xpath("//*[normalize-space()='No records found']");
    public final By selectRecordRightMsg  = By.xpath("//*[contains(normalize-space(),'Select a record to view details')]");

    // Sidebar profile
    public final By sidebarPatientId      = By.xpath(
            "//*[contains(normalize-space(),'PT-') " +
            "or contains(normalize-space(),'PT0') " +
            "or contains(normalize-space(),'P-') " +
            "or (contains(normalize-space(),'·') and contains(normalize-space(),'Age'))]");
    public final By sidebarPatientAge     = By.xpath("//*[contains(normalize-space(),'Age ')]");

    public PatientMedicalRecords(WebDriver driver) {
        super(driver);
    }

    public PatientMedicalRecords open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/records");
        waitUntilLoaded();
        return this;
    }

    public PatientMedicalRecords waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/records"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/records");
    }

    public int recordCount() {
        return driver.findElements(recordItems).size();
    }

    public int waitForRecordsLoaded() {
        return wait.until(d -> {
            List<WebElement> items = d.findElements(recordItems);
            return items.isEmpty() ? null : items.size();
        });
    }

    public PatientMedicalRecords search(String term) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(term);
        return this;
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
