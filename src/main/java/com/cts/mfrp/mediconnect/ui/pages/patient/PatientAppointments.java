package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Patient Appointments page — /patient/{userId}/appointments */
public class PatientAppointments extends BasePage {

    public final By pageHeader        = By.xpath("//*[normalize-space()='Appointments']");
    public final By bookAppointmentBtn = By.xpath("//*[contains(text(),'+ Book Appointment') or contains(text(),'Book Appointment')]");
    public final By tabUpcoming       = By.xpath("//*[normalize-space()='Upcoming']");
    public final By tabPast           = By.xpath("//*[normalize-space()='Past']");
    public final By tabCancelled      = By.xpath("//*[normalize-space()='Cancelled']");
    public final By appointmentCards  = By.cssSelector("[class*='appointment-card'], [class*='appt-card']");

    // Book Appointment Modal
    public final By modalTitle        = By.xpath("//*[normalize-space()='Book Appointment']");
    public final By confirmBookingBtn = By.xpath("//button[normalize-space()='Confirm Booking']");
    public final By modalDoctorSelect = By.cssSelector("select, [role='combobox']");
    public final By modalReasonField  = By.cssSelector("textarea, input[placeholder*='reason' i]");
    public final By modalCloseBtn     = By.cssSelector("button.close, .modal-close, [aria-label='close']");

    public PatientAppointments(WebDriver driver) {
        super(driver);
    }

    public PatientAppointments open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/appointments");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/appointments");
    }

    public void clickBookAppointment() { click(bookAppointmentBtn); }
    public void clickConfirmBooking()  { click(confirmBookingBtn); }
    public void switchToUpcoming()     { click(tabUpcoming); }
    public void switchToPast()         { click(tabPast); }
    public void switchToCancelled()    { click(tabCancelled); }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
