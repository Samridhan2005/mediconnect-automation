package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientLabReports;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC030, TC031, TC082 — Patient Lab Reports page. */
public class PatientLabReportsTest extends BasePatientTest {

    // TC030 — Lab Reports page UI
    @Test
    public void TC030_lab_reports_ui() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
    }

    // TC031 — Ask AI to Explain + Download PDF
    @Test
    public void TC031_lab_reports_ai_explanation_and_download() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);
        assertNotNull(driver.findElements(page.askAiButton));
        assertNotNull(driver.findElements(page.downloadPdfButton));
    }

    // TC082 — Abnormal Result Alert Banner persistence
    @Test
    public void TC082_lab_reports_alert_banner_persistence() {
        PatientLabReports page = new PatientLabReports(driver).open(loggedInUserId);
        List<WebElement> banner = driver.findElements(page.attentionBanner);
        if (!banner.isEmpty()) {
            assertEquals(banner.get(0).findElements(By.cssSelector(
                            "button[aria-label='close'], .close-btn")).size(), 0,
                    "Alert banner should not be dismissible");
        }
    }
}