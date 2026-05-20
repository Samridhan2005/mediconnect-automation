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

    // TC035 — UI: header + 4 mode buttons + at least one quick action
    @Test(groups = {"regression"})
    public void TC035_ai_assistant_page_ui() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "AI Health Assistant header should be visible");

        int modes = driver.findElements(page.modeButtons).size();
        assertEquals(modes, 4, "There should be 4 AI mode buttons");

        assertTrue(driver.findElements(page.quickActionBtns).size() >= 1, "Quick actions should be present");
        assertTrue(driver.findElements(page.chips).size() >= 1, "Suggestion chips should be present");
    }

    // TC036 — Mode switching: clicking each mode button activates it
    @Test(groups = {"regression"})
    public void TC036_ai_assistant_chat_mode_switching() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);

        for (String mode : List.of("Symptom Checker", "Report Explanation", "Book Appointment", "General Chat")) {
            page.selectMode(mode);
            String active = page.activeModeText().toLowerCase();
            assertTrue(active.contains(mode.toLowerCase()),
                    "After clicking '" + mode + "', it should become active (active text was: '" + active + "')");
        }
    }

    // TC079 — Emergency Note card cannot be dismissed
    @Test(groups = {"regression"})
    public void TC079_ai_emergency_note_is_persistent() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);

        List<WebElement> emerg = driver.findElements(page.emergencyCard);
        assertTrue(emerg.size() > 0, "Emergency Note element should be visible on AI page");

        assertEquals(driver.findElements(page.emergencyClose).size(), 0,
                "Emergency Note must not expose a close/dismiss control");
    }
}
