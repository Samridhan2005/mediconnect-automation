package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Medical Records page — /patient/{userId}/records */
public class PatientMedicalRecords extends BasePage {

    public final By pageHeader      = By.xpath("//*[normalize-space()='Medical Records']");
    public final By leftPanel       = By.cssSelector("[class*='left'], [class*='panel-left']");
    public final By rightPanel      = By.cssSelector("[class*='right'], [class*='panel-right']");
    public final By searchInput     = By.cssSelector("input[placeholder*='Search' i]");
    public final By recordItems     = By.cssSelector("[class*='record-item'], [class*='record-row']");

    public PatientMedicalRecords(WebDriver driver) {
        super(driver);
    }

    public PatientMedicalRecords open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/records");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/records");
    }

    public void search(String term) { type(searchInput, term); }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
