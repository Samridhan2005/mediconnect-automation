package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Appointment Management — /admin/{userId}/appointments */
public class AdminAppointments extends BasePage {

    public final By pageHeader        = By.xpath("//*[normalize-space()='Appointment Management']");
    public final By newAppointmentBtn = By.xpath("//*[contains(normalize-space(),'+ New Appointment') or contains(normalize-space(),'New Appointment')]");
    public final By calendarGrid      = By.cssSelector("[class*='calendar']");
    public final By allDoctorsFilter  = By.xpath("//*[contains(normalize-space(),'All Doctors')]");
    public final By allDeptsFilter    = By.xpath("//*[contains(normalize-space(),'All Departments')]");
    public final By datePicker        = By.cssSelector("input[type='date'], [class*='date-picker']");
    public final By filter = By.xpath("//*[text()=\"Filters\"]");
    public final By recentAppointments = By.xpath("//*[text()=\" Recent Appointments \"]");
    public final By app = By.xpath("//*[text()=\"All Appointments\"]");

    public AdminAppointments(WebDriver driver) {
        super(driver);
    }

    public AdminAppointments open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/appointments");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/appointments"); }

    public void clickNewAppointment() { click(newAppointmentBtn); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
