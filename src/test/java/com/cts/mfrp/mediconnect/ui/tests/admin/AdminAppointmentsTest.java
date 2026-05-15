package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC056, TC083 — Admin Appointment Management. */
public class AdminAppointmentsTest extends BaseAdminTest {

    // TC056 — Appointment management calendar + filters
    @Test
    public void TC056_admin_appointment_calendar_filters() {
        AdminAppointments page = new AdminAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        assertTrue(driver.findElements(page.calendarGrid).size() > 0,
                "Calendar grid should be visible");
        for (String filter : List.of("All Doctors", "All Departments", "Date Picker")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + filter + "')]")).size() > 0,
                    "Filter missing: " + filter);
        }
    }

    // TC083 — New Appointment creation
    @Test
    public void TC083_admin_new_appointment_creation() {
        AdminAppointments page = new AdminAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.newAppointmentBtn).size() > 0,
                "+ New Appointment button should be visible");
        page.clickNewAppointment();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(By.cssSelector("[class*='modal'], form")).size() > 0,
                "Modal/form should open");
    }
}
