package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Appointments page — /doctor/{userId}/appointments */
public class DoctorAppointments extends BasePage {

    public final By pageHeader        = By.xpath("//*[normalize-space()='Appointments']");
    public final By newAppointmentBtn = By.xpath("//*[contains(text(),'+ New Appointment') or contains(text(),'New Appointment')]");
    public final By tabToday          = By.xpath("//button[normalize-space()='Today']");
    public final By tabUpcoming       = By.xpath("//button[normalize-space()='Upcoming']");
    public final By tabPast           = By.xpath("//button[normalize-space()='Past']");
    public final By calendarToggle    = By.xpath("//button[contains(normalize-space(),'Calendar view')]");

    // New Appointment Modal
    public final By modalTitle        = By.xpath("//*[normalize-space()='New Appointment']");
    public final By modalPatientField = By.cssSelector("input[placeholder*='patient' i], input[name='patient']");
    public final By modalDateField    = By.cssSelector("input[type='date'], input[placeholder*='date' i]");
    public final By modalSaveBtn      = By.xpath("//button[normalize-space()='Save Appointment' or normalize-space()='Save']");
    public final By modalCancelBtn    = By.xpath("//button[normalize-space()='Cancel']");

    public DoctorAppointments(WebDriver driver) {
        super(driver);
    }

    public DoctorAppointments open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/appointments");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/appointments"); }

    public void clickNewAppointment() { click(newAppointmentBtn); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
