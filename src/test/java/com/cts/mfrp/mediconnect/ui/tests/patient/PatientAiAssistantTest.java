package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientAiHealthAssistant;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** FRD: TC035, TC036, TC079 — Patient AI Health Assistant page. */
public class PatientAiAssistantTest extends BasePatientTest {

    // Merged TC035 + TC036 + TC183 + TC184 + TC185
    @Test(groups = {"regression"})
    public void TC035_036_183_185_patient_ai_assistant_ui_and_modes() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "AI Health Assistant header should be visible");

        int modes = driver.findElements(page.modeButtons).size();
        assertEquals(modes, 4, "There should be 4 AI mode buttons");

        assertTrue(driver.findElements(page.quickActionBtns).size() >= 1, "Quick actions should be present");
        assertTrue(driver.findElements(page.chips).size() >= 1, "Suggestion chips should be present");

        // TC036 — Mode switching: clicking each mode button activates it
        for (String mode : List.of("Symptom Checker", "Report Explanation", "Book Appointment", "General Chat")) {
            page.selectMode(mode);
            String active = page.activeModeText().toLowerCase();
            assertTrue(active.contains(mode.toLowerCase()),
                    "After clicking '" + mode + "', it should become active (active text was: '" + active + "')");
        }

        // TC183 — Page sub-label visible
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Your personal health guide' should be visible");

        // TC184 — Top-right: blood group chip + notification bell + hamburger
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
        assertTrue(driver.findElements(page.hamburgerMenu).size() > 0,
                "Hamburger menu should be visible");

        // TC185 — MODE section has all 4 mode sub-labels
        assertTrue(driver.findElements(page.modeHeading).size() > 0,
                "'MODE' section heading should be visible in the left panel");
        assertTrue(driver.findElements(page.modeAskAnything).size() > 0,
                "General Chat mode sub-label 'Ask anything' should be visible");
        assertTrue(driver.findElements(page.modeDescribeSymptoms).size() > 0,
                "Symptom Checker mode sub-label 'Describe symptoms' should be visible");
        assertTrue(driver.findElements(page.modeUnderstandResults).size() > 0,
                "Report Explanation mode sub-label 'Understand your results' should be visible");
        assertTrue(driver.findElements(page.modeAiAssistedBooking).size() > 0,
                "Book Appointment mode sub-label 'AI-assisted booking' should be visible");
    }

    // Merged TC079 + TC186 + TC187
    @Test(groups = {"regression"})
    public void TC079_186_187_patient_ai_emergency_and_context() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);

        List<WebElement> emerg = driver.findElements(page.emergencyCard);
        assertTrue(emerg.size() > 0, "Emergency Note element should be visible on AI page");

        assertEquals(driver.findElements(page.emergencyClose).size(), 0,
                "Emergency Note must not expose a close/dismiss control");

        // TC186 — YOUR CONTEXT section visible with prescription and appointment info
        assertTrue(driver.findElements(page.yourContextHeading).size() > 0,
                "'YOUR CONTEXT' section heading should be visible");
        assertTrue(driver.findElements(page.contextPrescriptions).size() > 0,
                "Context item showing 'X active prescriptions' should be visible");
        assertTrue(driver.findElements(page.contextNextAppt).size() > 0,
                "Context item showing 'Next appt:' should be visible");

        // TC187 — AI greeting bubble visible on first load
        assertTrue(driver.findElements(page.aiGreetingBubble).size() > 0,
                "AI greeting message should be visible in the conversation area on first load");
    }

    // Merged TC188 + TC189
    @Test(groups = {"regression"})
    public void TC188_189_patient_ai_ask_input_and_disclaimer() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.askInput).size() > 0,
                "'Ask about your health, symptoms, or results...' input field should be visible");
        assertTrue(driver.findElements(page.sendButton).size() > 0,
                "Send button should be visible next to the Ask input");

        // TC189 — AI disclaimer permanently visible
        assertTrue(driver.findElements(page.aiDisclaimer).size() > 0,
                "Disclaimer 'AI responses are for informational purposes only. Always consult your doctor for medical decisions.' should be visible");
    }

    // Merged TC190 + TC191
    @Test(groups = {"regression"})
    public void TC190_191_patient_ai_quick_actions_and_common_questions() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.quickActionsHeading).size() > 0,
                "'QUICK ACTIONS' section heading should be visible in the right panel");
        assertTrue(driver.findElements(page.quickSymptomChecker).size() > 0,
                "Quick Action card 'Symptom Checker' should be visible");
        assertTrue(driver.findElements(page.quickExplainLab).size() > 0,
                "Quick Action card 'Explain My Lab Report' should be visible");
        assertTrue(driver.findElements(page.quickBookAppt).size() > 0,
                "Quick Action card 'Book Appointment' should be visible");

        // TC191 — Right panel: COMMON QUESTIONS section visible
        assertTrue(driver.findElements(page.commonQuestionsHead).size() > 0,
                "'COMMON QUESTIONS' section heading should be visible in the right panel");
    }

    // TC192 — BUG-002 regression guard: 'Hello null!' / 'null null' should NEVER appear
    @Test(groups = {"regression"})
    public void TC192_no_null_in_greeting_or_anywhere() {
        new PatientAiHealthAssistant(driver).open(loggedInUserId);

        int nullNullCount = driver.findElements(
                org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        int helloNullCount = driver.findElements(
                org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'Hello null')]")).size();

        assertTrue(nullNullCount == 0 && helloNullCount == 0,
                "Greeting / profile should never render 'null' as a name. " +
                "Found 'null null'=" + nullNullCount + ", 'Hello null'=" + helloNullCount +
                " — patient record has null firstName/lastName (BUG-002).");
    }
}
