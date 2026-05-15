package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Telemedicine page — /patient/{userId}/telemedicine */
public class PatientTelemedicine extends BasePage {

    public final By pageHeader      = By.xpath("//*[normalize-space()='Telemedicine']");
    public final By rescheduleBtn   = By.xpath("//button[contains(normalize-space(),'Reschedule')]");
    public final By cancelBtn       = By.xpath("//button[contains(normalize-space(),'Cancel')]");
    public final By sessionCards    = By.cssSelector("[class*='session-card'], [class*='video-card']");

    public PatientTelemedicine(WebDriver driver) {
        super(driver);
    }

    public PatientTelemedicine open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/telemedicine");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/telemedicine");
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
