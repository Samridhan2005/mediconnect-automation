package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Telemedicine page — /doctor/{userId}/telemedicine */
public class DoctorTelemedicine extends BasePage {

    public final By pageHeader        = By.xpath("//*[normalize-space()='Telemedicine']");
    public final By scheduleSessionBtn = By.xpath("//button[contains(normalize-space(),'Schedule session')]");
    public final By liveSection       = By.xpath("//*[contains(normalize-space(),'Live & Upcoming')]");
    public final By pastSessionsTable = By.xpath("//*[contains(normalize-space(),'Past Sessions')]/following-sibling::*[1]");

    public DoctorTelemedicine(WebDriver driver) {
        super(driver);
    }

    public DoctorTelemedicine open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/telemedicine");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/telemedicine"); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
