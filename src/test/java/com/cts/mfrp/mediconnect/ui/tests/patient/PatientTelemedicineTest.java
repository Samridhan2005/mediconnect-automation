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
    @Test(groups = {"regression"})
    public void TC032_telemedicine_ui() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Telemedicine header should be visible");
        WebElement bookBtn = wait.until(ExpectedConditions.elementToBeClickable(page.bookVideoBtn));
        assertTrue(bookBtn.isDisplayed() && bookBtn.isEnabled(),
                "'Book Video Call' button should be visible and clickable");
    }

    // TC076 — Sessions area: either reschedule/cancel actions exist on sessions, or empty state shows
    @Test(groups = {"regression"})
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

    // ============== Extended coverage — TC175..TC182 ==============

    // TC175 — Page sub-label visible
    @Test(groups = {"regression"})
    public void TC175_sub_label_visible() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Connect with your doctor remotely' should be visible");
    }

    // TC176 — Top-right shows blood group chip + notification bell + hamburger menu
    @Test(groups = {"regression"})
    public void TC176_top_right_chip_and_bell() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
        assertTrue(driver.findElements(page.hamburgerMenu).size() > 0,
                "Hamburger menu should be visible");
    }

    // TC177 — Upcoming consultation banner shows empty-state message when no consultations
    @Test(groups = {"regression"})
    public void TC177_upcoming_empty_state_when_no_consultations() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);

        boolean hasSession = !driver.findElements(page.rescheduleBtn).isEmpty();
        if (!hasSession) {
            assertTrue(driver.findElements(page.noUpcomingMsg).size() > 0,
                    "Empty-state message 'No upcoming video consultations' should be displayed");
            assertTrue(driver.findElements(page.bookCallSubMessage).size() > 0,
                    "Sub-message 'Book a video call with your doctor below.' should be displayed");
        } else {
            System.out.println("[TC177] Patient has an upcoming session; skipping empty-state assertion.");
        }
    }

    // TC178 — Scheduled Sessions section heading is always visible
    @Test(groups = {"regression"})
    public void TC178_scheduled_sessions_section_visible() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.scheduledHeading).size() > 0,
                "'Scheduled Sessions' section heading should be visible");
    }

    // TC179 — Past Sessions section heading is always visible
    @Test(groups = {"regression"})
    public void TC179_past_sessions_section_visible() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pastHeading).size() > 0,
                "'Past Sessions' section heading should be visible");
    }

    // TC180 — Empty-state messages render when patient has no scheduled / past sessions
    @Test(groups = {"regression"})
    public void TC180_empty_state_messages_when_no_sessions() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);

        // Scroll to bottom so Angular renders the lower section
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try { Thread.sleep(600); } catch (InterruptedException ignored) {}

        int sessionsCount = driver.findElements(page.sessionCards).size();
        if (sessionsCount == 0) {
            assertTrue(driver.findElements(page.noScheduledMsg).size() > 0,
                    "'No scheduled sessions.' message should be shown when patient has no scheduled sessions");
            assertTrue(driver.findElements(page.noPastMsg).size() > 0,
                    "'No past sessions yet.' message should be shown when patient has no past sessions");
        } else {
            System.out.println("[TC180] Patient has " + sessionsCount + " session(s); skipping empty-state assertion.");
        }
    }

    // TC181 — Sidebar profile shows Patient ID and Age
    @Test(groups = {"regression"})
    public void TC181_sidebar_profile_shows_patient_id_and_age() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.sidebarPatientId).size() > 0,
                "Sidebar should display Patient ID");
        assertTrue(driver.findElements(page.sidebarPatientAge).size() > 0,
                "Sidebar should display patient's age");
    }

    // TC182 — BUG-002 regression guard: 'null null' should NEVER appear
    @Test(groups = {"regression"})
    public void TC182_no_null_null_anywhere() {
        new PatientTelemedicine(driver).open(loggedInUserId);
        int nullNullCount = driver.findElements(
                org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Sidebar / profile should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) — patient record has null firstName/lastName (BUG-002).");
    }
}
