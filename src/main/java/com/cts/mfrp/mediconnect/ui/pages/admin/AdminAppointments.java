package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Appointment Management — /admin/{userId}/appointments */
public class AdminAppointments extends BasePage {

    // Header
    public final By pageHeader         = By.xpath("//*[normalize-space()='Appointment Management']");
    public final By subLabel           = By.xpath("//*[contains(normalize-space(),'Calendar view') and contains(normalize-space(),'hospitals')]");
    public final By newAppointmentBtn  = By.xpath("//button[contains(normalize-space(),'New Appointment')]");
    public final By notificationBell   = By.cssSelector("[class*='notif'], [class*='bell']");

    // 4 summary tiles
    public final By tileTotalAppts     = By.xpath("//*[contains(normalize-space(),'Total Appointments')]");
    public final By tileConfirmed      = By.xpath("//*[contains(normalize-space(),'Confirmed')]");
    public final By tilePending        = By.xpath("//*[contains(normalize-space(),'Pending')]");
    public final By tileCancelled      = By.xpath("//*[contains(normalize-space(),'Cancelled')]");

    // Calendar — identified by its weekday column headers (Mon...Sun) which are
    // a 100% reliable indicator regardless of the wrapping element's CSS class.
    public final By dayHeaderMon       = By.xpath("//*[normalize-space()='Mon']");
    public final By dayHeaderSun       = By.xpath("//*[normalize-space()='Sun']");
    public final By calendarGrid       = By.xpath("//*[normalize-space()='Mon']/ancestor::*[self::div or self::table or self::section][1]");
    public final By calendarMonthHead  = By.xpath("//*[contains(normalize-space(),'May 2026') or contains(normalize-space(),'2026') or contains(@class,'cal-month')]");
    public final By calendarPrev       = By.xpath("//button[normalize-space()='‹' or normalize-space()='<' or contains(@class,'prev')]");
    public final By calendarNext       = By.xpath("//button[normalize-space()='›' or normalize-space()='>' or contains(@class,'next')]");

    // Filters panel
    public final By filtersHeading     = By.xpath("//*[normalize-space()='Filters']");
    public final By clearAllFilters    = By.xpath("//*[normalize-space()='Clear all']");
    public final By allDoctorsFilter   = By.xpath("//select[option[normalize-space()='All Doctors']]");
    public final By allHospitalsFilter = By.xpath("//select[option[normalize-space()='All Hospitals']]");
    public final By dateFilter         = By.xpath("//input[@type='date' or contains(@placeholder,'dd-mm-yyyy')]");

    // Recent Appointments panel
    public final By recentApptsHeading = By.xpath("//*[normalize-space()='Recent Appointments']");

    // Reschedule Requests panel — uses contains() since heading may include a "(1 pending)" badge inline
    public final By rescheduleHeading  = By.xpath("//*[contains(normalize-space(),'Reschedule Requests')]");
    public final By forwardToDoctorBtn = By.xpath("//button[normalize-space()='Forward to Doctor']");
    public final By rejectBtn          = By.xpath("//button[normalize-space()='Reject']");

    // All Appointments table
    public final By allApptsHeading    = By.xpath("//*[contains(normalize-space(),'All Appointments')]");
    public final By allApptsSearch     = By.xpath("//input[contains(@placeholder,'Search')]");

    // New Appointment Modal
    public final By modalHeading       = By.xpath("//*[normalize-space()='New Appointment']");
    public final By modalSubLabel      = By.xpath("//*[contains(normalize-space(),'Schedule a new patient appointment')]");
    public final By modalPatientSelect = By.xpath("//label[contains(normalize-space(),'Patient')]/following-sibling::select");
    public final By modalDoctorSelect  = By.xpath("//label[contains(normalize-space(),'Doctor')]/following-sibling::select");
    public final By modalHospitalSelect= By.xpath("//label[contains(normalize-space(),'Hospital')]/following-sibling::select");
    public final By modalDateInput     = By.xpath("//label[contains(normalize-space(),'Date')]/following-sibling::input");
    public final By modalTimeInput     = By.xpath("//label[contains(normalize-space(),'Time')]/following-sibling::input");
    public final By modalTypeSelect    = By.xpath("//label[contains(normalize-space(),'Type')]/following-sibling::select");
    public final By modalNotesTextarea = By.xpath("//label[contains(normalize-space(),'Notes')]/following-sibling::textarea");
    public final By modalCancelBtn     = By.xpath("//button[normalize-space()='Cancel']");
    public final By modalCreateBtn     = By.xpath("//button[normalize-space()='Cancel']/following-sibling::button[contains(normalize-space(),'Create Appointment')]");
    public final By patientRequiredErr = By.xpath("//*[contains(normalize-space(),'Patient is required')]");
    public final By doctorRequiredErr  = By.xpath("//*[contains(normalize-space(),'Doctor is required')]");

    public AdminAppointments(WebDriver driver) {
        super(driver);
    }

    public AdminAppointments open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/appointments");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/appointments"); }

    public void clickNewAppointment()  { click(newAppointmentBtn); }
    public boolean isModalOpen()       { return isDisplayed(modalHeading); }
    public void clickModalCreate()     { click(modalCreateBtn); }
    public void clickModalCancel()     { click(modalCancelBtn); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
