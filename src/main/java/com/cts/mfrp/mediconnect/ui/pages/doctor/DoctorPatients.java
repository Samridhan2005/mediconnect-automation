package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Patients (registry) — /doctor/{userId}/patients */
public class DoctorPatients extends BasePage {

    public final By pageHeader     = By.xpath("//*[normalize-space()='Patients']");
    public final By searchInput    = By.cssSelector("input[placeholder*='Search' i]");
    public final By addPatientBtn  = By.xpath("//*[contains(normalize-space(),'+ Add Patient')]");
    public final By patientRows    = By.cssSelector("table tr, [class*='patient-row']");
    public final By viewActionIcon = By.cssSelector("[class*='view-icon'], button[title='View'], [class*='action-view']");
    public final By editActionIcon = By.cssSelector("[class*='edit-icon'], button[title='Edit']");

    public DoctorPatients(WebDriver driver) {
        super(driver);
    }

    public DoctorPatients open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/patients");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/patients"); }

    public void search(String term) { type(searchInput, term); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
