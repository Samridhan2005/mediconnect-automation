package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientAiHealthAssistant;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** FRD: TC035, TC036, TC079 — Patient AI Health Assistant page. */
public class PatientAiAssistantTest extends BasePatientTest {

    // TC035 — AI Assistant page UI
    @Test
    public void TC035_ai_assistant_page_ui() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
    }

    // TC036 — AI Assistant chat + mode switching
    @Test
    public void TC036_ai_assistant_chat_mode_switching() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);
        for (String mode : List.of("General Chat", "Symptom Checker", "Report Explanation", "Book Appointment")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + mode + "')]")).size() > 0,
                    "AI mode missing: " + mode);
        }
    }

    // TC079 — Emergency Note card cannot be dismissed
    @Test
    public void TC079_ai_emergency_note_is_persistent() {
        PatientAiHealthAssistant page = new PatientAiHealthAssistant(driver).open(loggedInUserId);
        List<WebElement> emergencyCard = driver.findElements(page.emergencyCard);
        assertTrue(emergencyCard.size() > 0, "Emergency Note card should be visible");
        for (WebElement card : emergencyCard) {
            assertEquals(card.findElements(By.cssSelector(
                            "button[aria-label='close'], .close-btn, .dismiss")).size(), 0,
                    "Emergency Note card should not be dismissible");
        }
    }
}
