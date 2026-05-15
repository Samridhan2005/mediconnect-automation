package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Medicine Reminders page — /patient/{userId}/reminders */
public class PatientMedicineReminders extends BasePage {

    public final By pageHeader   = By.xpath("//*[contains(normalize-space(),'Medicine Reminders')]");
    public final By dueNowRow    = By.xpath("//*[contains(text(),'DUE NOW')]/ancestor::*[contains(@class,'row') or contains(@class,'card')][1]");
    public final By markTakenBtn = By.xpath("//*[contains(text(),'DUE NOW')]/ancestor::*[1]//button[contains(@class,'mark') or normalize-space()='+']");

    public PatientMedicineReminders(WebDriver driver) {
        super(driver);
    }

    public PatientMedicineReminders open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/reminders");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/reminders");
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
