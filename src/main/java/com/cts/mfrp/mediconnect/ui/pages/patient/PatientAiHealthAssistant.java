package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Patient AI Health Assistant page — /patient/{userId}/ai */
public class PatientAiHealthAssistant extends BasePage {

    public final By pageHeader     = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='AI Health Assistant']");
    public final By modeButtons    = By.cssSelector("button.mode-btn");
    public final By activeMode     = By.cssSelector("button.mode-btn.active");
    public final By emergencyCard  = By.xpath("//*[contains(normalize-space(),'Emergency Note')]");
    public final By emergencyClose = By.xpath("//*[contains(normalize-space(),'Emergency Note')]/ancestor::*[1]" +
            "//*[self::button or self::a][contains(@class,'close') or contains(@class,'dismiss') or @aria-label='close']");
    public final By quickActionBtns = By.cssSelector("button.quick-btn");
    public final By chips           = By.cssSelector("button.chip");

    // --- Extended locators ---

    // Page header
    public final By subLabel              = By.xpath("//*[contains(normalize-space(),'Your personal health guide')]");
    public final By hamburgerMenu         = By.cssSelector("button[class*='hamburger'], button[class*='menu-toggle'], [aria-label*='menu' i]");
    public final By bloodGroupHeaderChip  = By.xpath("//*[contains(normalize-space(),'Blood group:') or contains(normalize-space(),'Blood Group:')]");
    public final By notificationBell      = By.cssSelector("button[class*='notif'], button[class*='bell'], [aria-label*='notif' i]");

    // Left panel — MODE section sub-labels.
    // Headings render in ALL CAPS via CSS text-transform; DOM text may be lower/mixed case.
    public final By modeHeading           = By.xpath(
            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'MODE') " +
            "and string-length(normalize-space())<=10]");
    public final By modeAskAnything       = By.xpath("//*[contains(normalize-space(),'Ask anything')]");
    public final By modeDescribeSymptoms  = By.xpath("//*[contains(normalize-space(),'Describe symptoms')]");
    public final By modeUnderstandResults = By.xpath("//*[contains(normalize-space(),'Understand your results')]");
    public final By modeAiAssistedBooking = By.xpath("//*[contains(normalize-space(),'AI-assisted booking')]");

    // Left panel — YOUR CONTEXT section (case-insensitive + contains, limited length to avoid matching parents)
    public final By yourContextHeading    = By.xpath(
            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'CONTEXT') " +
            "and string-length(normalize-space())<=20]");
    public final By contextPrescriptions  = By.xpath("//*[contains(normalize-space(),'active prescriptions')]");
    public final By contextNextAppt       = By.xpath("//*[contains(normalize-space(),'Next appt:')]");

    // Center — chat area
    public final By aiGreetingBubble      = By.xpath("//*[contains(normalize-space(),\"I'm your MediConnect AI assistant\")]");
    public final By greetingTimestamp     = By.xpath("//*[contains(normalize-space(),'AM') or contains(normalize-space(),'PM')][string-length(normalize-space())<10]");

    // Input + send button — match either <input> or <textarea> with a placeholder mentioning 'Ask'
    // and any of the topic words (health / symptom / result).
    public final By askInput              = By.xpath(
            "//*[self::input or self::textarea]" +
            "[contains(@placeholder,'Ask') " +
            " and (contains(@placeholder,'health') " +
            "      or contains(@placeholder,'symptom') " +
            "      or contains(@placeholder,'symptoms') " +
            "      or contains(@placeholder,'result') " +
            "      or contains(@placeholder,'results'))]");
    // Send button — locate by class OR aria-label OR as the first button after the Ask input
    public final By sendButton            = By.xpath(
            "//button[contains(@class,'send') or contains(@class,'submit')]" +
            " | //button[contains(@aria-label,'Send') or contains(@aria-label,'send')]" +
            " | (//*[self::input or self::textarea][contains(@placeholder,'Ask')])[1]/following::button[1]");
    public final By aiDisclaimer          = By.xpath("//*[contains(normalize-space(),'AI responses are for informational purposes only') or contains(normalize-space(),'Always consult your doctor for medical decisions')]");

    // Right panel — QUICK ACTIONS section (case-insensitive + contains, length-limited)
    public final By quickActionsHeading   = By.xpath(
            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'QUICK ACTION') " +
            "and string-length(normalize-space())<=20]");
    public final By quickSymptomChecker   = By.xpath("//*[contains(normalize-space(),'Symptom Checker')]");
    public final By quickExplainLab       = By.xpath("//*[contains(normalize-space(),'Explain My Lab Report')]");
    public final By quickBookAppt         = By.xpath("//*[normalize-space()='Book Appointment']");

    // Right panel — COMMON QUESTIONS section (case-insensitive + contains, length-limited)
    public final By commonQuestionsHead   = By.xpath(
            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'COMMON QUESTION') " +
            "and string-length(normalize-space())<=25]");

    // Sidebar profile
    public final By sidebarPatientId      = By.xpath(
            "//*[contains(normalize-space(),'PT-') " +
            "or contains(normalize-space(),'PT0') " +
            "or contains(normalize-space(),'P-') " +
            "or (contains(normalize-space(),'·') and contains(normalize-space(),'Age'))]");
    public final By sidebarPatientAge     = By.xpath("//*[contains(normalize-space(),'Age ')]");

    public PatientAiHealthAssistant(WebDriver driver) {
        super(driver);
    }

    public PatientAiHealthAssistant open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/ai");
        waitUntilLoaded();
        return this;
    }

    public PatientAiHealthAssistant waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/ai"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(activeMode));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/ai");
    }

    public By modeButtonByText(String text) {
        return By.xpath("//button[contains(@class,'mode-btn')][contains(normalize-space(),'" + text + "')]");
    }

    public PatientAiHealthAssistant selectMode(String text) {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(modeButtonByText(text)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();
        wait.until(d -> {
            List<WebElement> active = d.findElements(activeMode);
            return !active.isEmpty() && active.get(0).getText().toLowerCase().contains(text.toLowerCase());
        });
        return this;
    }

    public String activeModeText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeMode))
                .getText().replaceAll("\\s+", " ").trim();
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }

    // --- Interaction helpers ---

    /** Click a chat suggestion chip (button.chip) whose text matches the given value. */
    public PatientAiHealthAssistant clickChip(String chipText) {
        By chip = By.xpath("//button[contains(@class,'chip') and normalize-space()='" + chipText + "']");
        click(chip);
        return this;
    }

    /** Type a question into the 'Ask about your health…' input/textarea. */
    public PatientAiHealthAssistant typeAsk(String question) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(askInput));
        input.clear();
        input.sendKeys(question);
        return this;
    }

    public PatientAiHealthAssistant clickSend() {
        click(sendButton);
        return this;
    }

    public String getAskInputValue() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(askInput));
        String v = input.getAttribute("value");
        return v == null ? "" : v;
    }

    /**
     * Click a right-panel Quick Action card by its title (e.g. 'Symptom Checker').
     * Walks the DOM in JS: finds an element whose text equals (or contains) the title,
     * then clicks its nearest clickable ancestor — covers buttons, anchors, role=button,
     * and class-based card patterns without relying on a single brittle xpath.
     *
     * The title text also appears in the left-side MODE buttons, so we scope to the
     * QUICK ACTIONS region by preferring elements after the 'QUICK ACTIONS' heading.
     */
    public PatientAiHealthAssistant clickQuickAction(String title) {
        Boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "const target = arguments[0].trim();"
                        + "const norm = s => (s || '').replace(/\\s+/g,' ').trim();"
                        // Find the QUICK ACTIONS heading so we can prefer descendants/siblings within its section.
                        + "let region = null;"
                        + "for (const el of document.querySelectorAll('*')) {"
                        + "  const t = norm(el.textContent).toUpperCase();"
                        + "  if (t === 'QUICK ACTIONS' || t === 'QUICK ACTION') {"
                        + "    region = el.parentElement; break;"
                        + "  }"
                        + "}"
                        + "const scope = region || document.body;"
                        // Find a text-bearing element inside that scope whose text matches the title.
                        + "for (const el of scope.querySelectorAll('*')) {"
                        + "  if (el.children.length === 0 && norm(el.textContent) === target) {"
                        + "    const click = el.closest('button, a, [role=\"button\"], .quick-btn, [class*=\"quick\"], [class*=\"action-card\"], [class*=\"card\"]');"
                        + "    if (click) { click.click(); return true; }"
                        + "  }"
                        + "}"
                        // Fallback — any text-bearing element in document with matching text and a clickable ancestor.
                        + "for (const el of document.querySelectorAll('*')) {"
                        + "  if (el.children.length === 0 && norm(el.textContent) === target) {"
                        + "    const click = el.closest('button, a, [role=\"button\"], .quick-btn, [class*=\"quick\"]');"
                        + "    if (click) { click.click(); return true; }"
                        + "  }"
                        + "}"
                        + "return false;",
                title);
        if (clicked == null || !clicked) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "Quick action '" + title + "' not found on the page. Available quick-btn titles: "
                            + quickActionDiagnostic());
        }
        return this;
    }

    /** Click a right-panel Common Question entry by its visible text. */
    public PatientAiHealthAssistant clickCommonQuestion(String questionText) {
        Boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "const target = arguments[0].trim();"
                        + "const norm = s => (s || '').replace(/\\s+/g,' ').trim();"
                        + "let region = null;"
                        + "for (const el of document.querySelectorAll('*')) {"
                        + "  const t = norm(el.textContent).toUpperCase();"
                        + "  if (t === 'COMMON QUESTIONS' || t === 'COMMON QUESTION') {"
                        + "    region = el.parentElement; break;"
                        + "  }"
                        + "}"
                        + "const scope = region || document.body;"
                        + "for (const el of scope.querySelectorAll('*')) {"
                        + "  if (el.children.length === 0 && norm(el.textContent) === target) {"
                        + "    const click = el.closest('button, a, [role=\"button\"], [class*=\"common\"], [class*=\"question\"]');"
                        + "    if (click) { click.click(); return true; }"
                        + "    el.click(); return true;"
                        + "  }"
                        + "}"
                        + "return false;",
                questionText);
        if (clicked == null || !clicked) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "Common question '" + questionText + "' not found on the page.");
        }
        return this;
    }

    /** Diagnostic — return the visible text of every button.quick-btn for failure messages. */
    private String quickActionDiagnostic() {
        try {
            Object r = ((JavascriptExecutor) driver).executeScript(
                    "return Array.from(document.querySelectorAll('button, [role=\"button\"], [class*=\"quick\"]'))"
                            + ".map(el => (el.textContent || '').replace(/\\s+/g,' ').trim())"
                            + ".filter(t => t.length > 0 && t.length < 100);");
            return String.valueOf(r);
        } catch (Exception e) {
            return "(diagnostic failed: " + e.getMessage() + ")";
        }
    }
}
