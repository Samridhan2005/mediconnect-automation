package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientTelemedicine;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC032, TC076 — Patient Telemedicine page. */
public class PatientTelemedicineTest extends BasePatientTest {

    // TC032 — Telemedicine page UI: header + Book Video Call CTA
    @Test
    public void TC032_telemedicine_ui() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Telemedicine header should be visible");
        WebElement bookBtn = wait.until(ExpectedConditions.elementToBeClickable(page.bookVideoBtn));
        assertTrue(bookBtn.isDisplayed() && bookBtn.isEnabled(),
                "'Book Video Call' button should be visible and clickable");
    }

    // TC076 — Sessions area: either reschedule/cancel actions exist on sessions, or empty state shows
    @Test
    public void TC076_patient_telemedicine_sessions_or_empty() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);

        boolean hasSession = !driver.findElements(page.rescheduleBtn).isEmpty()
                && !driver.findElements(page.cancelBtn).isEmpty();
        boolean hasEmptyState = !driver.findElements(page.emptyState).isEmpty();

        assertTrue(hasSession || hasEmptyState,
                "Telemedicine page should show either a session with Reschedule/Cancel actions OR an empty-state — neither was found");

        if (hasSession) {
            assertTrue(driver.findElement(page.rescheduleBtn).isEnabled(), "Reschedule should be enabled");
            assertTrue(driver.findElement(page.cancelBtn).isEnabled(),     "Cancel should be enabled");
        }
    }
}
