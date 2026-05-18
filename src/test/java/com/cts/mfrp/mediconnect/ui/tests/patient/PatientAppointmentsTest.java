package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/** FRD: TC023, TC024, TC025, TC026, TC027 — Patient Appointments page + Book Appointment modal. */
public class PatientAppointmentsTest extends BasePatientTest {

    // TC023 — Appointments page UI: header, primary CTA, tabs
    @Test
    public void TC023_appointments_page_ui() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(page.pageHeader)).isDisplayed(),
                "Page header 'Appointments' should be visible");

        WebElement bookBtn = wait.until(ExpectedConditions.elementToBeClickable(page.bookAppointmentBtn));
        assertTrue(bookBtn.isDisplayed() && bookBtn.isEnabled(), "+ Book Appointment button should be clickable");

        assertTrue(driver.findElements(page.tabUpcoming).size() > 0,  "Upcoming tab missing");
        assertTrue(driver.findElements(page.tabPast).size() > 0,      "Past tab missing");
        assertTrue(driver.findElements(page.tabCancelled).size() > 0, "Cancelled tab missing");

        String active = wait.until(ExpectedConditions.visibilityOfElementLocated(page.activeTab))
                .getText().toLowerCase();
        assertTrue(active.startsWith("upcoming"),
                "Upcoming should be the default active tab (was: '" + active + "')");
    }

    // TC024 — Upcoming tab content: either appointment cards OR empty state message
    @Test
    public void TC024_appointment_cards_or_empty_state() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);

        // Make Upcoming the active tab (click it explicitly to validate the click works)
        WebElement upcoming = wait.until(ExpectedConditions.elementToBeClickable(page.tabUpcoming));
        upcoming.click();
        wait.until(d -> d.findElement(page.activeTab).getText().toLowerCase().startsWith("upcoming"));

        int cardCount = driver.findElements(page.appointmentCards).size();
        int emptyCount = driver.findElements(page.emptyStateMessage).size();
        assertTrue(cardCount > 0 || emptyCount > 0,
                "Upcoming tab should show either appointment cards or empty-state message — found cards="
                        + cardCount + ", emptyMsg=" + emptyCount);
    }

    // TC025 — Book Appointment Modal UI: title + fields visible
    @Test
    public void TC025_book_appointment_modal_ui() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.openBookModal();

        assertTrue(driver.findElement(page.modalTitle).isDisplayed(),
                "Modal title 'Book Appointment' should appear");

        // Field labels — appear in modal as label text near inputs
        for (String label : List.of("Doctor", "Date", "Time", "Reason")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(@class,'modal') or @role='dialog']" +
                                    "//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "Field label missing in modal: " + label);
        }

        assertTrue(driver.findElements(page.modalDoctorSelect).size() > 0,
                "Doctor select should be present");
        assertTrue(driver.findElements(page.modalReasonField).size() > 0,
                "Reason input should be present");

        // Sanity: cancel/close should work to dismiss the modal
        page.closeModal();
        assertFalse(driver.findElement(By.tagName("body")).getText().contains("Confirm Booking")
                        && driver.findElements(page.modalTitle).size() > 0
                        && driver.findElement(page.modalTitle).isDisplayed(),
                "Modal should be dismissable via close button");
    }

    // TC026 — Mandatory field validation: confirming with empty form must not submit
    @Test
    public void TC026_book_appointment_mandatory_validation() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.openBookModal();

        // Try to submit without filling. Two valid outcomes: button disabled OR modal stays open (no nav)
        boolean confirmEnabled = page.isConfirmEnabled();
        if (confirmEnabled) {
            page.clickConfirmIgnoringDisabled();
        }

        // Modal must still be open — booking should not have been accepted
        assertTrue(page.isModalOpen(),
                "Modal should remain open when mandatory fields are empty (validation must block submit)");
    }

    // TC027 — Successful booking: fill fields, confirm, modal closes
    @Test
    public void TC027_book_appointment_success() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.openBookModal();

        page.selectFirstDoctor()
            .setDate(LocalDate.now().plusDays(7))
            .setTime("10:00")
            .enterReason("General consultation - automated test " + System.currentTimeMillis());

        // Confirm should be enabled now
        assertTrue(page.isConfirmEnabled(),
                "Confirm Booking should be enabled after all mandatory fields are filled");

        page.clickConfirmBooking();

        // Modal should eventually close OR a success indicator should appear
        boolean closedOrSuccess = wait.until(d -> {
            boolean modalGone = d.findElements(page.modalTitle).isEmpty()
                    || !d.findElement(page.modalTitle).isDisplayed();
            boolean success = !d.findElements(By.xpath(
                    "//*[contains(translate(.,'SUCCESS','success'),'success') " +
                            "or contains(translate(.,'BOOKED','booked'),'booked') " +
                            "or contains(translate(.,'CONFIRMED','confirmed'),'confirmed')]"))
                    .isEmpty();
            return modalGone || success;
        });
        assertTrue(closedOrSuccess, "After confirm: modal should close or a success message should appear");

        assertEquals(driver.getCurrentUrl().contains("/appointments"), true,
                "Should remain on appointments page after booking");
    }
}
