package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Health Overview Dashboard — /patient/{userId}/dashboard */
public class PatientHealthOverview extends BasePage {

    public final By healthScoreRing       = By.cssSelector("[class*='health-score'], [class*='score-ring'], .hs-ring");
    public final By summaryTiles          = By.cssSelector(".stat-card");
    public final By upcomingAppointmentsTile = By.xpath("//div[contains(@class,'stat-card')][.//*[normalize-space()='Upcoming Appointments']]");
    public final By pendingLabReportsTile    = By.xpath("//div[contains(@class,'stat-card')][.//*[normalize-space()='Pending Lab Reports']]");
    public final By activePrescriptionsTile  = By.xpath("//div[contains(@class,'stat-card')][.//*[normalize-space()='Active Prescriptions']]");
    public final By unreadNotificationsTile  = By.xpath("//div[contains(@class,'stat-card')][.//*[normalize-space()='Unread Notifications']]");

    // AI assistant widget on Health Overview
    public final By aiWidget              = By.cssSelector(".ai-panel, [class*='ai-cta'], [class*='ai-widget'], [class*='ai-card']");
    public final By aiChipExplainLab      = By.xpath("//button[normalize-space()='Explain my lab report']");
    public final By aiChipSymptomChecker  = By.xpath("//button[normalize-space()='Symptom checker']");
    public final By aiChipBookAppointment = By.xpath("//button[normalize-space()='Book appointment']");
    public final By openAiAssistantCta    = By.xpath("//button[contains(normalize-space(),'Open AI Assistant')]");

    // Card "View all" / "All" links inside the panel headers
    public final By tileLinks             = By.cssSelector("a.card-link, button.card-link");

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
