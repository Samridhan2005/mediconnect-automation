package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Health Overview Dashboard — /patient/{userId}/dashboard */
public class PatientHealthOverview extends BasePage {

    public final By healthScoreRing       = By.cssSelector("[class*='health-score'], [class*='score-ring']");
    public final By summaryTiles          = By.cssSelector("[class*='summary'] [class*='tile'], .tile-card, .summary-card");
    public final By upcomingAppointmentsTile = By.xpath("//*[contains(text(),'Upcoming Appointments')]/ancestor::*[contains(@class,'tile') or contains(@class,'card')][1]");
    public final By pendingLabReportsTile    = By.xpath("//*[contains(text(),'Pending Lab Reports')]/ancestor::*[contains(@class,'tile') or contains(@class,'card')][1]");
    public final By activePrescriptionsTile  = By.xpath("//*[contains(text(),'Active Prescriptions')]/ancestor::*[contains(@class,'tile') or contains(@class,'card')][1]");
    public final By unreadNotificationsTile  = By.xpath("//*[contains(text(),'Unread Notifications')]/ancestor::*[contains(@class,'tile') or contains(@class,'card')][1]");

    // AI assistant widget on Health Overview
    public final By aiWidget              = By.cssSelector("[class*='ai-cta'], [class*='ai-widget'], [class*='ai-card']");
    public final By aiChipExplainLab      = By.xpath("//button[normalize-space()='Explain my lab report']");
    public final By aiChipSymptomChecker  = By.xpath("//button[normalize-space()='Symptom checker']");
    public final By aiChipBookAppointment = By.xpath("//button[normalize-space()='Book appointment']");
    public final By openAiAssistantCta    = By.xpath("//button[contains(normalize-space(),'Open AI Assistant')]");

    // Tile "View all" / "All" links
    public final By tileLinks             = By.xpath("//button[normalize-space()='View all →' or normalize-space()='All →']");

    public PatientHealthOverview(WebDriver driver) {
        super(driver);
    }

    public PatientHealthOverview open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/dashboard");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().matches(".*/patient/\\d+/dashboard$");
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
