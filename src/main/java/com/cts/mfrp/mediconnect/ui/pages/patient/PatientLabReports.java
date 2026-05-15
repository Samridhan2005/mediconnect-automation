package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Lab Reports page — /patient/{userId}/lab-reports */
public class PatientLabReports extends BasePage {

    public final By pageHeader        = By.xpath("//*[normalize-space()='Lab Reports']");
    public final By attentionBanner   = By.xpath("//*[contains(normalize-space(),'Attention required') or contains(@class,'alert-banner')]");
    public final By reportCards       = By.cssSelector("[class*='report-card']");
    public final By askAiButton       = By.xpath("//button[contains(normalize-space(),'Ask AI to Explain')]");
    public final By downloadPdfButton = By.xpath("//button[contains(normalize-space(),'Download PDF')]");

    public PatientLabReports(WebDriver driver) {
        super(driver);
    }

    public PatientLabReports open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/lab-reports");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/lab-reports");
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
