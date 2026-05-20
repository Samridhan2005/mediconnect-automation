package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminTelemedicine;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Admin Telemedicine page — /admin/{id}/telemedicine
 * Coverage was previously missing entirely for this page.
 *
 * Bugs caught by this file:
 *   - BUG-009 — "Join" button in Today's Video Appointments table is unresponsive (TC096)
 */
public class AdminTelemedicineTest extends BaseAdminTest {

    // TC093 — Page header, sub-label, and Schedule Session button all visible
    @Test(groups = {"regression"})
    public void TC093_admin_telemedicine_ui_header() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "Telemedicine page title should be visible");
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Virtual consultation management' should be visible");
        assertTrue(page.isScheduleSessionButtonVisible(),
                "'+ Schedule Session' button should be visible in the top-right");
    }

    // TC094 — All four summary tiles render with expected labels
    @Test(groups = {"regression"})
    public void TC094_admin_telemedicine_summary_tiles() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        assertTrue(driver.findElements(page.tileDoctorsOnline).size() > 0,
                "Summary tile 'Doctors Online' should be visible");
        assertTrue(driver.findElements(page.tileActiveSessions).size() > 0,
                "Summary tile 'Active Sessions' should be visible");
        assertTrue(driver.findElements(page.tileTodayScheduled).size() > 0,
                "Summary tile 'Today Scheduled' should be visible");
        assertTrue(driver.findElements(page.tileThisWeek).size() > 0,
                "Summary tile 'This Week' should be visible");
    }

    // TC095 — Doctor Availability + Today's Video Appointments + Session Records sections render
    @Test(groups = {"regression"})
    public void TC095_admin_telemedicine_sections_render() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        assertTrue(driver.findElements(page.doctorAvailabilityHeading).size() > 0,
                "'Doctor Availability' section heading should be visible");
        assertTrue(driver.findElements(page.todaysVideoSection).size() > 0,
                "'Today's Video Appointments' section should be visible");
        assertTrue(driver.findElements(page.sessionRecordsSection).size() > 0,
                "'Session Records' section should be visible");
    }

    // TC096 — Clicking 'Join' in Today's Video Appointments should open the video session.
    // BUG-009: the click is unresponsive — no navigation, no modal, no error.
    @Test(groups = {"regression"})
    public void TC096_join_button_in_todays_appointments_opens_session() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        // Wait for at least one Join button to appear in the Today's Video Appointments table
        wait.until(d -> page.joinButtonCount() > 0);
        int joinCount = page.joinButtonCount();
        assertTrue(joinCount > 0, "At least one 'Join' button should be present in Today's Video Appointments");

        String urlBefore = driver.getCurrentUrl();
        int windowsBefore = driver.getWindowHandles().size();

        // Click the first available Join button
        page.clickFirstJoinButton();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        // After click, ONE of these signals success:
        //   - URL changed (navigated to a session/video route)
        //   - A new browser tab/window opened (video call in new window)
        //   - A video session modal appeared
        boolean urlChanged = !driver.getCurrentUrl().equals(urlBefore);
        boolean newWindow  = driver.getWindowHandles().size() > windowsBefore;
        boolean modalOpened = driver.findElements(By.xpath(
                "//*[contains(translate(normalize-space(),'video','VIDEO'),'VIDEO SESSION')" +
                " or contains(translate(normalize-space(),'consult','CONSULT'),'CONSULTATION')]")).size() > 0;

        assertTrue(urlChanged || newWindow || modalOpened,
                "Clicking 'Join' should open the video session (URL change, new window, or session modal). " +
                "None happened — Join button is unresponsive. " +
                "URL before=" + urlBefore + ", after=" + driver.getCurrentUrl() +
                ", windows=" + driver.getWindowHandles().size() + " (was " + windowsBefore + ")");
    }

    // TC097 — '+ Schedule Session' button opens the Schedule Video Session modal
    @Test(groups = {"regression"})
    public void TC097_schedule_session_button_opens_modal() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);
        page.clickScheduleSession();

        wait.until(d -> page.isScheduleModalOpen());
        assertTrue(page.isScheduleModalOpen(),
                "'Schedule Video Session' modal should open after clicking '+ Schedule Session'");
    }

    // TC098 — Schedule Video Session modal exposes all expected form fields
    @Test(groups = {"regression"})
    public void TC098_schedule_session_modal_has_all_fields() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);
        page.clickScheduleSession();
        wait.until(d -> page.isScheduleModalOpen());

        assertTrue(driver.findElements(page.modalDoctorSelect).size() > 0,
                "Modal should have a Doctor dropdown");
        assertTrue(driver.findElements(page.modalPatientSelect).size() > 0,
                "Modal should have a Patient dropdown");
        assertTrue(driver.findElements(page.modalDateInput).size() > 0,
                "Modal should have a Date input");
        assertTrue(driver.findElements(page.modalTimeInput).size() > 0,
                "Modal should have a Time input");
        assertTrue(driver.findElements(page.modalSessionTypeSelect).size() > 0,
                "Modal should have a Session Type dropdown");
        assertTrue(driver.findElements(page.modalNotesTextarea).size() > 0,
                "Modal should have a Notes textarea");
        assertTrue(driver.findElements(page.modalScheduleButton).size() > 0,
                "Modal should have a 'Schedule Session' submit button at the bottom");
        assertTrue(driver.findElements(page.modalCancelButton).size() > 0,
                "Modal should have a Cancel button");
    }
}
