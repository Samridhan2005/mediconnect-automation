package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorLabReports;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC042, TC043 — Doctor Lab Reports page. */
public class DoctorLabReportsTest extends BaseDoctorTest {

    // TC042 — Lab Reports page UI
    @Test
    public void TC042_doctor_lab_reports_ui() {
        DoctorLabReports page = new DoctorLabReports(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Total Reports", "Pending", "Abnormal Flags", "Completed Today")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC043 — Request Test form
    @Test
    public void TC043_doctor_request_test() {
        DoctorLabReports page = new DoctorLabReports(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.requestTestBtn).size() > 0,
                "+ Request Test button should be visible");
        page.clickRequestTest();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(By.cssSelector("form, [class*='modal']")).size() > 0,
                "Request Test form should open");
    }
}
