package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient AI Health Assistant page — /patient/{userId}/ai */
public class PatientAiHealthAssistant extends BasePage {

    public final By pageHeader     = By.xpath("//*[contains(normalize-space(),'AI Health Assistant')]");
    public final By modeGeneral    = By.xpath("//*[contains(normalize-space(),'General Chat')]");
    public final By modeSymptom    = By.xpath("//*[contains(normalize-space(),'Symptom Checker')]");
    public final By modeReport     = By.xpath("//*[contains(normalize-space(),'Report Explanation')]");
    public final By modeBook       = By.xpath("//*[contains(normalize-space(),'Book Appointment')]");
    public final By emergencyCard  = By.xpath("//*[contains(normalize-space(),'Emergency Note') or contains(normalize-space(),'emergency')]");
    public final By chatInput      = By.cssSelector("textarea, input[placeholder*='Ask about' i]");

    public PatientAiHealthAssistant(WebDriver driver) {
        super(driver);
    }

    public PatientAiHealthAssistant open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/ai");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/ai");
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
