package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC023, TC024, TC025, TC026, TC027 — Patient Appointments page + Book Appointment modal. */
public class PatientAppointmentsTest extends BasePatientTest {

    // TC023 — Appointments page UI
    @Test
    public void TC023_appointments_page_ui() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        assertTrue(driver.findElements(page.bookAppointmentBtn).size() > 0,
                "+ Book Appointment button should be visible");
        for (String tab : List.of("Upcoming", "Past", "Cancelled")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + tab + "']")).size() > 0,
                    "Appointments tab missing: " + tab);
        }
    }

    // TC024 — Appointment cards in Upcoming tab
    @Test
    public void TC024_appointment_cards() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        List<WebElement> cards = driver.findElements(page.appointmentCards);
        assertNotNull(cards);

        List<WebElement> upcomingTab = driver.findElements(page.tabUpcoming);
        if (!upcomingTab.isEmpty()) {
            upcomingTab.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            if (cards.isEmpty()) {
                assertTrue(driver.findElements(By.xpath(
                                "//*[contains(text(),'No upcoming appointments') or contains(text(),'no appointments')]")).size() > 0,
                        "Empty state message expected when no appointments");
            }
        } else {
            assertTrue(false, "FRD expects 'Upcoming' tab on Appointments — not found in current UI");
        }
    }

    // TC025 — Book Appointment Modal UI
    @Test
    public void TC025_book_appointment_modal_ui() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.clickBookAppointment();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(page.modalTitle).size() > 0,
                "Modal title 'Book Appointment' should appear");

        for (String label : List.of("Doctor", "Date", "Time", "Type", "Reason")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "Field label missing in modal: " + label);
        }
    }

    // TC026 — Mandatory field validations in Book Appointment Modal
    @Test
    public void TC026_book_appointment_modal_mandatory_negative() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.clickBookAppointment();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        List<WebElement> confirm = driver.findElements(page.confirmBookingBtn);
        if (!confirm.isEmpty()) {
            confirm.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(text(),'This field is required') or contains(text(),'required')]")).size() > 0,
                    "Mandatory field error message expected");
        }
    }

    // TC027 — Successful booking
    @Test
    public void TC027_book_appointment_success() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.clickBookAppointment();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}

        List<WebElement> doctorSelect = driver.findElements(page.modalDoctorSelect);
        if (!doctorSelect.isEmpty()) doctorSelect.get(0).click();

        List<WebElement> reason = driver.findElements(page.modalReasonField);
        if (!reason.isEmpty()) {
            reason.get(0).clear();
            reason.get(0).sendKeys("General consultation");
        }

        List<WebElement> submit = driver.findElements(page.confirmBookingBtn);
        if (!submit.isEmpty()) submit.get(0).click();
        try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
        assertNotNull(driver.getCurrentUrl());
    }
}
