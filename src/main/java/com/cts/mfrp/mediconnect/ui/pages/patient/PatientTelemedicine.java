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
    public final By bookVideoBtn    = By.xpath("//button[normalize-space()='Book Video Call']");
    public final By rescheduleBtn   = By.xpath("//button[contains(normalize-space(),'Reschedule')]");
    public final By cancelBtn       = By.xpath("//button[contains(normalize-space(),'Cancel')]");
    public final By sessionCards    = By.cssSelector(".card");
    public final By emptyState      = By.cssSelector(".table-empty");

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
