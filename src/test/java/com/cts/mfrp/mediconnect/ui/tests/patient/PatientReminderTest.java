package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientMedicineReminders;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC033, TC034 — Patient Medicine Reminders page. */
public class PatientReminderTest extends BasePatientTest {

    // TC033 — UI: header, stats blocks, at least one med-card with DUE NOW
    @Test
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
    @Test
    public void TC034_mark_dose_as_taken() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);

        int before = driver.findElements(page.markTakenBtns).size();
        assertTrue(before >= 1, "Should have at least one 'mark as taken' button to click");

        int after = page.markFirstDoseTaken();
        assertTrue(after != before,
                "Clicking mark-as-taken should change the row state (button count went from "
                        + before + " to " + after + ")");
    }
}
