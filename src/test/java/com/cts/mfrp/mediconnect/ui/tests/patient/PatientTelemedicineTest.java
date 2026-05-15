package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientTelemedicine;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC032, TC076 — Patient Telemedicine page. */
public class PatientTelemedicineTest extends BasePatientTest {

    // TC032 — Telemedicine page UI
    @Test
    public void TC032_telemedicine_ui() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
    }

    // TC076 — Telemedicine reschedule / cancel actions
    @Test
    public void TC076_patient_telemedicine_reschedule_cancel() {
        PatientTelemedicine page = new PatientTelemedicine(driver).open(loggedInUserId);
        assertNotNull(driver.findElements(page.rescheduleBtn));
        assertNotNull(driver.findElements(page.cancelBtn));
    }
}
