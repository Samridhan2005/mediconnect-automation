package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminTelemedicine;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * Admin Telemedicine page — /admin/{id}/telemedicine
 * Bugs caught: BUG-009 — Join button unresponsive (TC096)
 */
public class AdminTelemedicineTest extends BaseAdminTest {

    // TC093_094_095 — Page header + summary tiles + section headings
    // Merged TC093 + TC094 + TC095
    @Test(groups = {"regression"})
    public void TC093_094_095_admin_telemedicine_ui_and_sections() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        // TC093 — header, sub-label, Schedule Session button
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "Telemedicine page title should be visible");
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Virtual consultation management' should be visible");
        assertTrue(page.isScheduleSessionButtonVisible(),
                "'+ Schedule Session' button should be visible in the top-right");

        // TC094 — four summary tiles
        assertTrue(driver.findElements(page.tileDoctorsOnline).size() > 0,
                "Summary tile 'Doctors Online' should be visible");
        assertTrue(driver.findElements(page.tileActiveSessions).size() > 0,
                "Summary tile 'Active Sessions' should be visible");
        assertTrue(driver.findElements(page.tileTodayScheduled).size() > 0,
                "Summary tile 'Today Scheduled' should be visible");
        assertTrue(driver.findElements(page.tileThisWeek).size() > 0,
                "Summary tile 'This Week' should be visible");

        // TC095 — three main sections
        assertTrue(driver.findElements(page.doctorAvailabilityHeading).size() > 0,
                "'Doctor Availability' section heading should be visible");
        assertTrue(driver.findElements(page.todaysVideoSection).size() > 0,
                "'Today's Video Appointments' section should be visible");
        assertTrue(driver.findElements(page.sessionRecordsSection).size() > 0,
                "'Session Records' section should be visible");
    }

    // TC096 — Join button should open video session (BUG-009 detection)
    @Test(groups = {"regression"})
    public void TC096_join_button_in_todays_appointments_opens_session() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        wait.until(d -> page.joinButtonCount() > 0);
        int joinCount = page.joinButtonCount();
        assertTrue(joinCount > 0,
                "At least one 'Join' button should be present in Today's Video Appointments");

        String urlBefore = driver.getCurrentUrl();
        int windowsBefore = driver.getWindowHandles().size();

        page.clickFirstJoinButton();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

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

    @DataProvider(name = "telemedicineSessions")
    public Object[][] telemedicineSessions() {
        return TestData.telemedicineSessionIds();
    }

    // TC099 — Schedule Video Session end-to-end (data-driven).
    // Runs once per row in the TelemedicineSessions sheet. Fills the modal with
    // doctor / patient / date / time / sessionType / notes, clicks Schedule
    // Session, and asserts the modal closes (success signal).
    @Test(groups = {"regression"}, dataProvider = "telemedicineSessions")
    public void TC099_admin_telemedicine_schedule_session(String testId) {
        Map<String, String> data = TestData.telemedicineSession(testId);

        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);
        assertTrue(page.isScheduleSessionButtonVisible(),
                "[" + testId + "] '+ Schedule Session' button should be visible");

        page.clickScheduleSession();
        wait.until(d -> page.isScheduleModalOpen());
        assertTrue(page.isScheduleModalOpen(),
                "[" + testId + "] 'Schedule Video Session' modal should open");

        page.fillScheduleSessionForm(
                data.get("doctor"),
                data.get("patient"),
                data.get("date"),
                data.get("time"),
                data.get("sessionType"),
                data.get("notes"));
        page.clickScheduleSubmit();

        // Success signal: modal closes after submit.
        wait.until(d -> !page.isScheduleModalOpen());
        assertTrue(!page.isScheduleModalOpen(),
                "[" + testId + "] Modal should close after clicking Schedule Session");
    }

    // TC097_098 — Schedule Session modal opens + has all expected fields
    // Merged TC097 + TC098
    @Test(groups = {"regression"})
    public void TC097_098_admin_telemedicine_schedule_modal() {
        AdminTelemedicine page = new AdminTelemedicine(driver).open(loggedInUserId);

        // TC097 — modal opens
        page.clickScheduleSession();
        wait.until(d -> page.isScheduleModalOpen());
        assertTrue(page.isScheduleModalOpen(),
                "'Schedule Video Session' modal should open after clicking '+ Schedule Session'");

        // TC098 — modal has all fields
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
