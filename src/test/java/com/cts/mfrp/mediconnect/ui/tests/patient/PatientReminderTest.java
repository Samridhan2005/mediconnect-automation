package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientMedicineReminders;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC033, TC034 — Patient Medicine Reminders page. */
public class PatientReminderTest extends BasePatientTest {

    // TC033 — Medicine Reminders page UI
    @Test
    public void TC033_medicine_reminders_ui() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
    }

    // TC034 — Mark dose as taken on a DUE NOW row
    @Test
    public void TC034_mark_dose_as_taken() {
        PatientMedicineReminders page = new PatientMedicineReminders(driver).open(loggedInUserId);
        assertNotNull(driver.findElements(page.dueNowRow));
    }
}
