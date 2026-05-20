package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Patient Telemedicine page — /patient/{userId}/telemedicine */
public class PatientTelemedicine extends BasePage {

    public final By pageHeader      = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='Telemedicine']");
    public final By bookVideoBtn    = By.xpath("//button[contains(normalize-space(),'Book Video Call')]");
    public final By rescheduleBtn   = By.xpath("//button[contains(normalize-space(),'Reschedule')]");
    public final By cancelBtn       = By.xpath("//button[contains(normalize-space(),'Cancel')]");
    public final By sessionCards    = By.cssSelector(".card");
    public final By emptyState      = By.cssSelector(".table-empty");

    // --- Extended locators ---

    // Page header
    public final By subLabel              = By.xpath("//*[contains(normalize-space(),'Connect with your doctor remotely')]");
    public final By hamburgerMenu         = By.cssSelector("button[class*='hamburger'], button[class*='menu-toggle'], [aria-label*='menu' i]");
    public final By bloodGroupHeaderChip  = By.xpath("//*[contains(normalize-space(),'Blood group:') or contains(normalize-space(),'Blood Group:')]");
    public final By notificationBell      = By.cssSelector("button[class*='notif'], button[class*='bell'], [aria-label*='notif' i]");

    // Upcoming consultation banner
    public final By noUpcomingMsg         = By.xpath("//*[normalize-space()='No upcoming video consultations']");
    public final By bookCallSubMessage    = By.xpath("//*[contains(normalize-space(),'Book a video call with your doctor below')]");

    // Section headings + empty states
    public final By scheduledHeading      = By.xpath("//*[normalize-space()='Scheduled Sessions']");
    public final By noScheduledMsg        = By.xpath("//*[normalize-space()='No scheduled sessions.']");
    public final By pastHeading           = By.xpath("//*[normalize-space()='Past Sessions']");
    public final By noPastMsg             = By.xpath("//*[normalize-space()='No past sessions yet.']");

    // Sidebar profile
    public final By sidebarPatientId      = By.xpath(
            "//*[contains(normalize-space(),'PT-') " +
            "or contains(normalize-space(),'PT0') " +
            "or contains(normalize-space(),'P-') " +
            "or (contains(normalize-space(),'·') and contains(normalize-space(),'Age'))]");
    public final By sidebarPatientAge     = By.xpath("//*[contains(normalize-space(),'Age ')]");

    public PatientTelemedicine(WebDriver driver) {
        super(driver);
    }

    public PatientTelemedicine open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/telemedicine");
        waitUntilLoaded();
        return this;
    }

    public PatientTelemedicine waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/telemedicine"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/telemedicine");
    }

    public boolean hasUpcomingSessions() {
        return !driver.findElements(rescheduleBtn).isEmpty();
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
