package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientLabReports;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** FRD: TC030, TC031, TC082 — Patient Lab Reports page. */
public class PatientLabReportsTest extends BasePatientTest {

    // TC030 — Page UI: header, report cards rendered
    @Test(groups = {"regression"})
    public void TC030_lab_reports_ui() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Lab Reports header should be visible");
        int reports = wait.until(d -> {
            int n = d.findElements(page.reportCards).size();
            return n > 0 ? n : null;
        });
        assertTrue(reports >= 1, "At least one lab report card should be visible");
    }

    // TC031 — Ask AI to Explain + Download PDF buttons are clickable
    @Test(groups = {"regression"})
    public void TC031_lab_reports_ai_explanation_and_download() {
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

    // TC082 — Abnormal Result Alert Banner: visible and not dismissible
    @Test(groups = {"regression"})
    public void TC082_lab_reports_alert_banner_persistence() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);

        List<WebElement> banner = driver.findElements(page.attentionBanner);
        assertTrue(banner.size() > 0, "Abnormal Result attention banner should be visible");
        for (WebElement b : banner) {
            assertEquals(b.findElements(By.cssSelector(
                            "button[aria-label='close'], .close-btn, .dismiss, button.close")).size(), 0,
                    "Attention banner should not be dismissible");
        }
    }
}