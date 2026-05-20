package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAiAssistant;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC050 — Doctor AI Assistant page. */
public class DoctorAiAssistantTest extends BaseDoctorTest {

    @Test(groups = {"regression"})
    public void TC050_doctor_ai_assistant_ui_chat() {
        DoctorAiAssistant page = new DoctorAiAssistant(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        assertTrue(driver.findElements(page.chips).size() > 0,
                "AI chip buttons should be visible");
    }
}
