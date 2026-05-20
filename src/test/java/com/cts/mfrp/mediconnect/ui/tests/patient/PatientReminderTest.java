package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientMedicineReminders;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC033, TC034 — Patient Medicine Reminders page. */
public class PatientReminderTest extends BasePatientTest {

    // TC033 — UI: header, stats blocks, at least one med-card with DUE NOW
    @Test(groups = {"regression"})
    public void TC033_medicine_reminders_ui() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Medicine Reminders header should be visible");
        assertTrue(driver.findElements(page.statBlocks).size() >= 4,
                "All 4 stat blocks (Active medicines, Taken today, Due today, Adherence) should be visible");
        int cards = wait.until(d -> {
            int n = d.findElements(page.medCards).size();
            return n > 0 ? n : null;
        });
        assertTrue(cards >= 1, "At least one medicine card should be visible");
        assertTrue(page.dueNowCount() >= 1, "At least one DUE NOW indicator should be present");
    }

    // TC034 — Mark dose as taken: clicking the mp-check button updates the row
    @Test(groups = {"regression"})
    public void TC034_mark_dose_as_taken() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);

        int before = driver.findElements(page.markTakenBtns).size();
        assertTrue(before >= 1, "Should have at least one 'mark as taken' button to click");

        int after = page.markFirstDoseTaken();
        assertTrue(after != before,
                "Clicking mark-as-taken should change the row state (button count went from "
                        + before + " to " + after + ")");
    }

    // ============== Extended coverage — TC035..TC042 ==============
    // These tests are robust against empty-state patients (no medicines).

    // TC035 — Page sub-label visible
    @Test(groups = {"regression"})
    public void TC035_sub_label_visible() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Track your daily medications' should be visible");
    }

    // TC036 — Top-right shows blood group chip + notification bell
    @Test(groups = {"regression"})
    public void TC036_top_right_chip_and_bell() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
    }

    // TC037 — All 4 stat tile LABELS render (robust to zero-data patients)
    @Test(groups = {"regression"})
    public void TC037_all_four_stat_tile_labels() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.tileActiveMeds).size() > 0,
                "Tile 'Active medicines' should be visible");
        assertTrue(driver.findElements(page.tileTakenToday).size() > 0,
                "Tile 'Taken today' should be visible");
        assertTrue(driver.findElements(page.tileDueToday).size() > 0,
                "Tile 'Due today' should be visible");
        assertTrue(driver.findElements(page.tileAdherence).size() > 0,
                "Tile 'Adherence (30d)' should be visible");
    }

    // TC038 — Stat tile sub-labels render (All prescribed / % done / Remaining / quality label)
    @Test(groups = {"regression"})
    public void TC038_stat_tile_sub_labels() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.subLabelAllPrescribed).size() > 0,
                "Tile sub-label 'All prescribed' should be visible");
        assertTrue(driver.findElements(page.subLabelPercentDone).size() > 0,
                "Tile sub-label containing '% done' should be visible");
        assertTrue(driver.findElements(page.subLabelRemaining).size() > 0,
                "Tile sub-label 'Remaining' should be visible");
        assertTrue(driver.findElements(page.subLabelAdherenceQual).size() > 0,
                "Adherence tile should display a qualitative label (Poor / Fair / Good / Excellent)");
    }

    // TC039 — Today's Schedule section + current weekday-date label visible
    @Test(groups = {"regression"})
    public void TC039_todays_schedule_section_and_date() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.todaysScheduleHeading).size() > 0,
                "'Today's Schedule' section heading should be visible");
        assertTrue(driver.findElements(page.scheduleDateLabel).size() > 0,
                "Current weekday date label (e.g., 'Wednesday, May 20') should be visible");
    }

    // TC040 — All Prescriptions section visible
    @Test(groups = {"regression"})
    public void TC040_all_prescriptions_section_visible() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.allPrescriptionsHead).size() > 0,
                "'All Prescriptions' section heading should be visible");
    }

    // TC041 — Empty state messages render correctly when no medicines / prescriptions exist
    @Test(groups = {"regression"})
    public void TC041_empty_states_render_when_no_data() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);

        int medCardCount = driver.findElements(page.medCards).size();
        if (medCardCount == 0) {
            assertTrue(driver.findElements(page.emptyScheduleMsg).size() > 0,
                    "When no medicines scheduled, empty-state message 'No medicines scheduled for today.' should be displayed");
            assertTrue(driver.findElements(page.emptyPrescriptionsMsg).size() > 0,
                    "When no prescriptions on file, empty-state message 'No prescriptions on file yet.' should be displayed");
        } else {
            System.out.println("[TC041] Patient has " + medCardCount + " medicine card(s); skipping empty-state assertions.");
        }
    }

    // TC042 — BUG-002 regression guard: 'null null' should NEVER appear on this page
    @Test(groups = {"regression"})
    public void TC042_no_null_null_anywhere() {
        new PatientMedicineReminders(driver).open(loggedInUserId);
        int nullNullCount = driver.findElements(
                org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Sidebar / profile / greeting should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) — patient record has null firstName/lastName (BUG-002).");
    }
}
