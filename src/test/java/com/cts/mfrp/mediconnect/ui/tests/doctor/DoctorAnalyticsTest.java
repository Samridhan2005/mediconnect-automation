package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAnalytics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC047, TC048 — Doctor Analytics page. */
public class DoctorAnalyticsTest extends BaseDoctorTest {

    // TC047 — Analytics page UI
    @Test
    public void TC047_doctor_analytics_ui() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Total Patients", "Appointments", "Lab Tests Ordered", "Avg. Consult Time")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),\"" + tile + "\")]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC048 — Time period filter
    @Test
    public void TC048_doctor_analytics_time_period_filter() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.timePeriodSelect).size() > 0,
                "Time period dropdown should be present");
    }
}
