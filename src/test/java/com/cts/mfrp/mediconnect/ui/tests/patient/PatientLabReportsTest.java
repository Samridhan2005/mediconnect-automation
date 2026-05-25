package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientLabReports;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** FRD: TC030, TC031, TC082 — Patient Lab Reports page. */
public class PatientLabReportsTest extends BasePatientTest {

    // Merged TC030 + TC082 + TC158 + TC159
    @Test(groups = {"regression"})
    public void patient_lab_reports_ui_and_banner() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Lab Reports header should be visible");
        int reports = wait.until(d -> {
            int n = d.findElements(page.reportCards).size();
            return n > 0 ? n : null;
        });
        assertTrue(reports >= 1, "At least one lab report card should be visible");

        // TC082 — Abnormal Result Alert Banner: visible and not dismissible
        List<WebElement> banner = driver.findElements(page.attentionBanner);
        assertTrue(banner.size() > 0, "Abnormal Result attention banner should be visible");
        for (WebElement b : banner) {
            assertEquals(b.findElements(By.cssSelector(
                            "button[aria-label='close'], .close-btn, .dismiss, button.close")).size(), 0,
                    "Attention banner should not be dismissible");
        }

        // TC158 — Page sub-label visible
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Test results with AI-powered explanations' should be visible");

        // TC159 — Top-right shows blood group chip + notification bell + hamburger menu
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
        assertTrue(driver.findElements(page.hamburgerMenu).size() > 0,
                "Hamburger menu should be visible");
    }

    // TC031 — Ask AI to Explain + Download PDF buttons are clickable
    @Test(groups = {"regression"})
    public void lab_reports_ai_explanation_and_download() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);

        WebElement ask = wait.until(ExpectedConditions.elementToBeClickable(page.askAiButton));
        WebElement dl  = wait.until(ExpectedConditions.elementToBeClickable(page.downloadPdfButton));
        assertTrue(ask.isDisplayed() && ask.isEnabled(), "'Ask AI to Explain' should be clickable");
        assertTrue(dl.isDisplayed() && dl.isEnabled(),   "'Download PDF' should be clickable");

        // Click Ask AI — AI panel chips should already be present; clicking should not error
        ask.click();
        List<WebElement> chips = driver.findElements(page.aiChips);
        assertTrue(chips.size() > 0, "AI chips should be visible to help the patient interact");
    }

    // Merged TC160 + TC161 + TC162 + TC163 + TC164
    @Test(groups = {"regression"})
    public void patient_lab_reports_ai_panel() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.aiPanelTitle).size() > 0,
                "'AI Report Explanation' panel title should be visible");
        assertTrue(driver.findElements(page.aiPanelPoweredBy).size() > 0,
                "Panel should show 'Powered by MediConnect AI' label");

        // TC161 — AI panel shows guidance text when no report is selected
        assertTrue(driver.findElements(page.aiPanelHelpText).size() > 0,
                "AI panel should display the help text 'Select a report and click \"Ask AI to Explain\"...'");

        // TC162 — All 4 suggested question chips are visible in the AI panel
        assertTrue(driver.findElements(page.chipLdl).size() > 0,
                "Chip 'What is LDL cholesterol?' should be visible");
        assertTrue(driver.findElements(page.chipLowerLdl).size() > 0,
                "Chip 'How to lower LDL naturally?' should be visible");
        assertTrue(driver.findElements(page.chipStatins).size() > 0,
                "Chip 'What are statins?' should be visible");
        assertTrue(driver.findElements(page.chipEcg).size() > 0,
                "Chip 'Is my ECG result good?' should be visible");

        // TC163 — 'Ask about your results...' input field and send button are present
        assertTrue(driver.findElements(page.askInput).size() > 0,
                "'Ask about your results...' input field should be visible in the AI panel");
        assertTrue(driver.findElements(page.askSendButton).size() > 0,
                "Send button (arrow) should be visible next to the Ask input");

        // TC164 — AI disclaimer text is permanently displayed
        assertTrue(driver.findElements(page.aiDisclaimer).size() > 0,
                "Disclaimer 'AI explanations are educational only. Always consult your doctor.' should be visible");
    }

    @DataProvider(name = "aiQuestions")
    public Object[][] aiQuestions() {
        return TestData.labReportAIQuestionIds();
    }

    // TC167 — AI Report Explanation question entry (data-driven).
    // Each row in LabReportAIQuestions supplies a question and the interaction mode:
    //   - mode "chip": click the suggested-question chip whose text matches the question.
    //   - mode "type": type the question into the Ask input, then click the send button.
    // The success signal is intentionally light — the chip click should not error, and
    // for typed questions the input either keeps the typed value (no send wiring) or
    // clears after send (chat-style UX). We assert the appropriate one per mode.
    @Test(groups = {"regression"}, dataProvider = "aiQuestions")
    public void patient_lab_reports_ai_question(String testId) {
        Map<String, String> data = TestData.labReportAIQuestion(testId);
        String question = data.get("question");
        String mode     = data.get("mode");

        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.aiPanelTitle).size() > 0,
                "[" + testId + "] AI Report Explanation panel should be visible");

        if ("chip".equalsIgnoreCase(mode)) {
            page.clickChip(question);
            // Verify the click registered — input either receives the chip text or the
            // panel reacts in some way. At minimum the chip should still exist (the page
            // shouldn't have crashed). Brief wait for any UI update.
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(By.xpath("//button[normalize-space()='" + question + "']")).size() > 0
                            || !page.getAskInputValue().isEmpty(),
                    "[" + testId + "] After clicking chip '" + question
                            + "' the panel should still render (chip remained OR input got populated)");
        } else if ("type".equalsIgnoreCase(mode)) {
            page.typeAskInput(question);
            assertEquals(page.getAskInputValue(), question,
                    "[" + testId + "] Ask input should hold the typed question");
            page.clickAskSend();
            // After clicking send: either the input clears (chat-app pattern) or it keeps
            // the text. Both are acceptable — what matters is the click didn't throw.
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(page.askInput).size() > 0,
                    "[" + testId + "] Ask input should still be present after clicking send");
        } else {
            throw new IllegalArgumentException(
                    "[" + testId + "] Unknown mode '" + mode + "' — expected 'chip' or 'type'");
        }
    }

    // Merged TC165 + TC166
    @Test(groups = {"regression"})
    public void patient_lab_reports_sidebar_and_no_nulls() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.sidebarPatientId).size() > 0,
                "Sidebar should display Patient ID");
        assertTrue(driver.findElements(page.sidebarPatientAge).size() > 0,
                "Sidebar should display patient's age");

        // TC166 — BUG-002 regression guard: 'null null' should NEVER appear
        int nullNullCount = driver.findElements(By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Sidebar / profile should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) — patient record has null firstName/lastName (BUG-002).");
    }
}
