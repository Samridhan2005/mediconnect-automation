package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Medical Records page — /doctor/{userId}/medical-records */
public class DoctorMedicalRecords extends BasePage {

    public final By pageHeader      = By.xpath("//*[normalize-space()='Medical Records']");
    public final By searchInput     = By.cssSelector("input[placeholder*='Search' i]");
    public final By newRecordBtn    = By.xpath("//button[contains(normalize-space(),'New Record')]");
    public final By modalTitle      = By.xpath("//*[normalize-space()='New Medical Record']");
    public final By saveRecordBtn   = By.xpath("//button[normalize-space()='Create Record']");
    public final By patientRows     = By.cssSelector("[class*='patient-row'], .patient-card");

    public DoctorMedicalRecords(WebDriver driver) {
        super(driver);
    }

    public DoctorMedicalRecords open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/medical-records");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/medical-records"); }

    public void search(String term) { type(searchInput, term); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
