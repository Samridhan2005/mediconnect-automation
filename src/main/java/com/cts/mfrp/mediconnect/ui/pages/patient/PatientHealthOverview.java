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

    // --- Extended locators for full health-overview coverage ---

    // Page header
    public final By pageTitle             = By.xpath("//*[normalize-space()='Health Overview']");
    public final By pageSubDate           = By.xpath("//*[contains(normalize-space(),', 20') or contains(normalize-space(),'2026')][self::p or self::span or self::div]");
    public final By bloodGroupHeaderChip  = By.xpath("//*[contains(normalize-space(),'Blood group:') or contains(normalize-space(),'Blood Group:')]");
    public final By notificationBell      = By.cssSelector("button[class*='notif'], button[class*='bell'], [aria-label*='notif' i]");

    // Sidebar profile block
    public final By sidebarPatientId      = By.xpath("//*[contains(normalize-space(),'PT-')]");
    public final By sidebarPatientAge     = By.xpath("//*[contains(normalize-space(),'Age ')]");

    // Top banner chips
    public final By bannerAgeGenderChip   = By.xpath("//*[contains(normalize-space(),'Age ') and (contains(normalize-space(),'MALE') or contains(normalize-space(),'FEMALE'))]");
    public final By bannerBloodGroupChip  = By.xpath("//*[contains(normalize-space(),'Blood Group')]");
    public final By bannerNextApptChip    = By.xpath("//*[contains(normalize-space(),'Next appt')]");

    // Health Score
    public final By healthScoreValue      = By.xpath("//*[contains(@class,'score') or contains(@class,'ring')]//*[string-length(normalize-space())>0 and string-length(normalize-space())<=3]");
    public final By healthScoreLabel      = By.xpath("//*[normalize-space()='Good' or normalize-space()='Fair' or normalize-space()='Poor' or normalize-space()='Excellent']");

    // Summary tile sub-labels
    public final By tileSubLabelNext      = By.xpath("//*[contains(normalize-space(),'Next:')]");
    public final By tileSubLabelOnSchedule= By.xpath("//*[contains(normalize-space(),'All on schedule')]");
    public final By tileSubLabelUrgent    = By.xpath("//*[contains(normalize-space(),'urgent')]");
    public final By tileSubLabelPending   = By.xpath("//*[contains(normalize-space(),'Pending review')]");

    // Health Vitals — all 4 metrics
    public final By vitalsBloodPressure   = By.xpath("//*[contains(normalize-space(),'Blood Pressure')]");
    public final By vitalsHeartRate       = By.xpath("//*[contains(normalize-space(),'Heart Rate')]");
    public final By vitalsBloodGlucose    = By.xpath("//*[contains(normalize-space(),'Blood Glucose')]");
    public final By vitalsBmi             = By.xpath("//*[normalize-space()='BMI']");
    public final By vitalsLastUpdated     = By.xpath("//*[contains(normalize-space(),'Last updated')]");

    // Recent Activity + Notifications
    public final By recentActivitySection = By.xpath("//*[normalize-space()='Recent Activity']");
    public final By notificationsBadge    = By.xpath("//*[contains(normalize-space(),'new')][self::span or contains(@class,'badge')]");

    // AI Assistant status — Use contains() because the badge may render as '● Online' with the dot character
    public final By aiAssistantOnline     = By.xpath("//*[contains(normalize-space(),'Online')]");
    public final By aiAssistantTitle      = By.xpath("//*[normalize-space()='AI Health Assistant']");

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
