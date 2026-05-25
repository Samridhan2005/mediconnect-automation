package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/** FRD: TC023, TC024, TC025, TC026, TC027 — Patient Appointments page + Book Appointment modal. */
public class PatientAppointmentsTest extends BasePatientTest {

    // Merged TC023 + TC150 + TC151 + TC152
    @Test(groups = {"regression"})
    public void patient_appointments_ui_header_sidebar() {
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

        // TC150 — Page sub-label "Manage & book consultations" visible
        assertTrue(driver.findElements(page.subLabel).size() > 0,
                "Sub-label 'Manage & book consultations' should be visible");

        // TC151 — Top-right: blood group chip + notification bell + hamburger menu visible
        assertTrue(driver.findElements(page.bloodGroupHeaderChip).size() > 0,
                "Blood group chip should be visible in the top-right");
        assertTrue(driver.findElements(page.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");
        assertTrue(driver.findElements(page.hamburgerMenu).size() > 0,
                "Hamburger menu icon should be visible");

        // TC152 — Sidebar profile shows Patient ID and Age
        assertTrue(driver.findElements(page.sidebarPatientId).size() > 0,
                "Sidebar should display Patient ID (e.g., 'PT-0052')");
        assertTrue(driver.findElements(page.sidebarPatientAge).size() > 0,
                "Sidebar should display patient's age (e.g., 'Age 36')");
    }

    // Merged TC024 + TC156
    @Test(groups = {"regression"})
    public void patient_appointments_cards_or_empty_state() {
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

        // TC156 — Empty state shows specific message when no upcoming appointments
        int cardCount2 = driver.findElements(page.appointmentCards).size();
        if (cardCount2 == 0) {
            assertTrue(driver.findElements(page.emptyStateText).size() > 0,
                    "Empty-state message 'No upcoming appointments' should be visible when no appointments exist");
        } else {
            System.out.println("[TC156] Patient has " + cardCount2 + " appointment(s); skipping empty-state assertion.");
        }
    }

    // Merged TC025 + TC026 + TC027
    @Test(groups = {"regression"})
    public void patient_book_appointment_flow() {
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

        // TC026 — Mandatory field validation: confirming with empty form must not submit
        page.openBookModal();

        // Try to submit without filling. Two valid outcomes: button disabled OR modal stays open (no nav)
        boolean confirmEnabled = page.isConfirmEnabled();
        if (confirmEnabled) {
            page.clickConfirmIgnoringDisabled();
        }

        // Modal must still be open — booking should not have been accepted
        assertTrue(page.isModalOpen(),
                "Modal should remain open when mandatory fields are empty (validation must block submit)");

        // TC027 — Successful booking: fill fields, confirm, modal closes
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

    @DataProvider(name = "patientBookings")
    public Object[][] patientBookings() {
        return TestData.patientBookingIds();
    }

    // TC028 — Book Appointment end-to-end (data-driven).
    // Runs once per row in the PatientBookings sheet. Opens the modal, fills the
    // doctor / date / time slot / type / reason fields, clicks Confirm Booking,
    // and asserts the modal closes (success signal).
    @Test(groups = {"regression"}, dataProvider = "patientBookings")
    public void patient_book_appointment(String testId) {
        Map<String, String> data = TestData.patientBooking(testId);

        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        page.openBookModal();

        page.selectDoctor(data.get("doctor"))
            .setDate(LocalDate.parse(data.get("date")))
            .selectTimeSlot(data.get("time"))
            .selectType(data.get("type"))
            .enterReason(data.get("reason"));

        assertTrue(page.isConfirmEnabled(),
                "[" + testId + "] Confirm Booking should be enabled after all mandatory fields are filled");

        page.clickConfirmBooking();

        // Success signal: modal closes OR a success message appears.
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
        assertTrue(closedOrSuccess,
                "[" + testId + "] After Confirm Booking the modal should close or a success message should appear");
        assertTrue(driver.getCurrentUrl().contains("/appointments"),
                "[" + testId + "] Should remain on appointments page after booking");
    }

    // Merged TC153 + TC154 + TC155
    @Test(groups = {"regression"})
    public void patient_appointments_banner_and_tabs() {
        PatientAppointments page = new PatientAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.consultationBanner).size() > 0,
                "'Need a consultation?' promotional banner should be visible");
        assertTrue(driver.findElements(page.bannerBookNowBtn).size() > 0,
                "'Book Now' button inside the banner should be visible");

        // TC154 — Upcoming tab displays its count (e.g., "Upcoming (0)")
        assertTrue(driver.findElements(page.tabUpcomingWithCount).size() > 0,
                "Upcoming tab should display a count in parentheses (e.g., 'Upcoming (0)')");

        // TC155 — Switching to Past and Cancelled tabs activates each.
        // Uses the existing specific tab locators (page.tabPast / page.tabCancelled / page.tabUpcoming)
        // and polls for the active-tab text to change instead of using fixed Thread.sleep.
        // Click Past tab
        wait.until(ExpectedConditions.elementToBeClickable(page.tabPast)).click();
        wait.until(d -> d.findElement(page.activeTab).getText().toLowerCase().startsWith("past"));
        String activePast = driver.findElement(page.activeTab).getText().toLowerCase();
        assertTrue(activePast.startsWith("past"),
                "Past should become the active tab after click (was: '" + activePast + "')");

        // Click Cancelled tab
        wait.until(ExpectedConditions.elementToBeClickable(page.tabCancelled)).click();
        wait.until(d -> d.findElement(page.activeTab).getText().toLowerCase().startsWith("cancelled"));
        String activeCanc = driver.findElement(page.activeTab).getText().toLowerCase();
        assertTrue(activeCanc.startsWith("cancelled"),
                "Cancelled should become the active tab after click (was: '" + activeCanc + "')");

        // Click Upcoming tab to return
        wait.until(ExpectedConditions.elementToBeClickable(page.tabUpcoming)).click();
        wait.until(d -> d.findElement(page.activeTab).getText().toLowerCase().startsWith("upcoming"));
        String activeUp = driver.findElement(page.activeTab).getText().toLowerCase();
        assertTrue(activeUp.startsWith("upcoming"),
                "Upcoming should be active again after click (was: '" + activeUp + "')");
    }

    // TC157 — BUG-002 regression guard: 'null null' should NEVER appear
    @Test(groups = {"regression"})
    public void no_null_null_anywhere() {
        new PatientAppointments(driver).open(loggedInUserId);
        int nullNullCount = driver.findElements(By.xpath("//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Sidebar / profile should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) — patient record has null firstName/lastName (BUG-002).");
    }
}
