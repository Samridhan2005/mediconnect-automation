package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Patient Lab Reports page — /patient/{userId}/lab-reports */
public class PatientLabReports extends BasePage {

    public final By pageHeader        = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='Lab Reports']");
    public final By attentionBanner   = By.cssSelector(".flagged-alert");
    public final By reportCards       = By.cssSelector(".report-card");
    public final By flaggedReportCard = By.cssSelector(".report-card.flagged");
    public final By askAiButton       = By.xpath("//button[normalize-space()='Ask AI to Explain']");
    public final By downloadPdfButton = By.xpath("//button[normalize-space()='Download PDF']");
    public final By aiPanel           = By.cssSelector(".ai-panel");
    public final By aiChips           = By.cssSelector(".ai-chip");

    public PatientLabReports(WebDriver driver) {
        super(driver);
    }

    public PatientLabReports open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/lab-reports");
        waitUntilLoaded();
        return this;
    }

    public PatientLabReports waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/lab-reports"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/lab-reports");
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
