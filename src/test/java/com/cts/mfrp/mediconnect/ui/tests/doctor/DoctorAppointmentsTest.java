package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC039, TC040, TC041, TC078 — Doctor Appointments page + New Appointment modal. */
public class DoctorAppointmentsTest extends BaseDoctorTest {

    // TC039 — Appointments page UI
    @Test
    public void TC039_doctor_appointments_list_ui() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);

        for (String tile : List.of("Today", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        for (String tab : List.of("Today", "Upcoming", "Past")) {
            assertTrue(driver.findElements(By.xpath("//button[normalize-space()='" + tab + "']")).size() > 0,
                    "Tab missing: " + tab);
        }
    }

    // TC040 — Tabs + Calendar view
    @Test
    public void TC040_doctor_appointments_tabs_and_calendar() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        List<WebElement> upcoming = driver.findElements(page.tabUpcoming);
        if (!upcoming.isEmpty()) upcoming.get(0).click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        assertTrue(driver.findElements(page.calendarToggle).size() > 0,
                "Calendar view toggle should be present");
    }

    // TC041 — New Appointment Modal field validation
    @Test
    public void TC041_doctor_new_appointment_modal() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        List<WebElement> btn = driver.findElements(page.newAppointmentBtn);
        assertTrue(btn.size() > 0, "+ New Appointment button should be visible");
        btn.get(0).click();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(page.modalTitle).size() > 0,
                "Modal title should be visible");

        for (String label : List.of("Patient", "Date", "Time", "Type", "Status")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "Modal field label missing: " + label);
        }
    }

    // TC078 — Unknown patient search negative
    @Test
    public void TC078_new_appointment_patient_search_negative() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        List<WebElement> btn = driver.findElements(page.newAppointmentBtn);
        if (!btn.isEmpty()) {
            btn.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        }
        List<WebElement> patientField = driver.findElements(page.modalPatientField);
        if (!patientField.isEmpty()) {
            patientField.get(0).clear();
            patientField.get(0).sendKeys("XYZ999");
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(text(),'No matching patients') or contains(text(),'No patients')]")).size() > 0,
                    "Empty-state message expected for unknown patient");
        }
    }
}
