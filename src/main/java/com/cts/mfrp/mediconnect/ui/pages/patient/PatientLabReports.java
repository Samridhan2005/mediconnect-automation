package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    // --- Extended locators ---

    // Page header
    public final By subLabel              = By.xpath("//*[contains(normalize-space(),'Test results with AI-powered explanations')]");
    public final By hamburgerMenu         = By.cssSelector("button[class*='hamburger'], button[class*='menu-toggle'], [aria-label*='menu' i]");
    public final By bloodGroupHeaderChip  = By.xpath("//*[contains(normalize-space(),'Blood group:') or contains(normalize-space(),'Blood Group:')]");
    public final By notificationBell      = By.cssSelector("button[class*='notif'], button[class*='bell'], [aria-label*='notif' i]");

    // AI Report Explanation panel — always present on the page
    public final By aiPanelTitle          = By.xpath("//*[normalize-space()='AI Report Explanation']");
    public final By aiPanelPoweredBy      = By.xpath("//*[contains(normalize-space(),'Powered by MediConnect AI')]");
    public final By aiPanelHelpText       = By.xpath("//*[contains(normalize-space(),'Select a report and click') or contains(normalize-space(),'Ask AI to Explain')]");

    // 4 suggested question chips
    public final By chipLdl               = By.xpath("//button[normalize-space()='What is LDL cholesterol?']");
    public final By chipLowerLdl          = By.xpath("//button[normalize-space()='How to lower LDL naturally?']");
    public final By chipStatins           = By.xpath("//button[normalize-space()='What are statins?']");
    public final By chipEcg               = By.xpath("//button[normalize-space()='Is my ECG result good?']");

    // Ask input + send button + disclaimer
    public final By askInput              = By.xpath("//input[contains(@placeholder,'Ask about your results') or contains(@placeholder,'results')]");
    public final By askSendButton         = By.xpath("//button[contains(@class,'send') or contains(@aria-label,'Send') or contains(@aria-label,'send')]");
    public final By aiDisclaimer          = By.xpath("//*[contains(normalize-space(),'AI explanations are educational only') or contains(normalize-space(),'Always consult your doctor')]");

    // Sidebar profile
    public final By sidebarPatientId      = By.xpath(
            "//*[contains(normalize-space(),'PT-') " +
            "or contains(normalize-space(),'PT0') " +
            "or contains(normalize-space(),'P-') " +
            "or (contains(normalize-space(),'·') and contains(normalize-space(),'Age'))]");
    public final By sidebarPatientAge     = By.xpath("//*[contains(normalize-space(),'Age ')]");

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

    // --- AI Report Explanation panel actions ---

    /** Click a chip whose visible text matches exactly. */
    public PatientLabReports clickChip(String chipText) {
        click(By.xpath("//button[normalize-space()='" + chipText + "']"));
        return this;
    }

    /** Type a question into the 'Ask about your results...' input. */
    public PatientLabReports typeAskInput(String question) {
        type(askInput, question);
        return this;
    }

    /** Click the send button (the paper-plane arrow next to the Ask input). */
    public PatientLabReports clickAskSend() {
        click(askSendButton);
        return this;
    }

    public String getAskInputValue() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(askInput));
        String v = input.getAttribute("value");
        return v == null ? "" : v;
    }
}
