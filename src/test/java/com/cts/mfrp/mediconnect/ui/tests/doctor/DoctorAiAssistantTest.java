package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAiAssistant;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/** FRD: TC050 — Doctor AI Assistant page. */
public class DoctorAiAssistantTest extends BaseDoctorTest {

    @Test(groups = {"regression"})
    public void doctor_ai_assistant_ui_chat() {
        DoctorAiAssistant page = new DoctorAiAssistant(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        assertTrue(driver.findElements(page.chips).size() > 0,
                "AI chip buttons should be visible");
    }

    @DataProvider(name = "aiAssistantData")
    public Object[][] aiAssistantData() {
        return TestData.aiAssistantIds();
    }

    @Test(groups = {"regression"}, dataProvider = "aiAssistantData")
    public void ai_assistant_send_question_and_receive_response(String testId) {
        Map<String, String> data = TestData.aiAssistant(testId);
        String question = data.get("question");

        new DoctorAiAssistant(driver).open(loggedInUserId);
        WebDriverWait wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebDriverWait longAi = new WebDriverWait(driver, Duration.ofSeconds(90));

        By inputLoc  = By.cssSelector("textarea.chat-input");
        By sendLoc   = By.cssSelector("button.send-btn");
        By chipsLoc  = By.cssSelector("div.chip-row [class*='chip'], div.chip-row button");
        By bubbleLoc = By.cssSelector("div.messages > *");

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLoc));
        assertTrue(input.isDisplayed(), "Chat textarea not visible");
        assertTrue(input.isEnabled(),   "Chat textarea is disabled");

        WebElement send = wait.until(ExpectedConditions.visibilityOfElementLocated(sendLoc));
        assertTrue(send.isDisplayed(), "Send button not visible");

        assertFalse(driver.findElements(chipsLoc).isEmpty(),
                "Suggestion chips should be visible");

        int bubblesBefore = driver.findElements(bubbleLoc).size();

        input.click();
        input.sendKeys(question);

        wait.until(ExpectedConditions.elementToBeClickable(sendLoc));
        driver.findElement(sendLoc).click();

        wait.until(d -> d.findElements(bubbleLoc).size() > bubblesBefore);
        int afterUserSend = driver.findElements(bubbleLoc).size();
        assertTrue(afterUserSend > bubblesBefore,
                "User's message bubble did not appear after clicking send");

        try {
            longAi.until(d -> d.findElements(bubbleLoc).size() > afterUserSend);
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("AI did not respond within 90 seconds to question: '"
                    + question + "'");
        }

        List<WebElement> bubbles = driver.findElements(bubbleLoc);
        WebElement aiResponse = bubbles.get(bubbles.size() - 1);
        String aiText = aiResponse.getText().trim();
        assertFalse(aiText.isEmpty(), "AI response bubble is empty");
        assertFalse(aiText.equalsIgnoreCase(question),
                "AI response is just an echo of the question — likely no real AI response");
    }
}
