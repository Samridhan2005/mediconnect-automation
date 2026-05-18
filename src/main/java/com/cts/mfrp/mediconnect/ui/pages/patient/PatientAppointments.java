package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Patient Appointments page — /patient/{userId}/appointments */
public class PatientAppointments extends BasePage {

    public final By pageHeader        = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='Appointments']");
    public final By bookAppointmentBtn = By.xpath("//button[normalize-space()='Book Appointment']");
    public final By bookNowBtn         = By.xpath("//button[normalize-space()='Book Now']");
    public final By tabUpcoming       = By.xpath("//*[contains(concat(' ',normalize-space(@class),' '),' tab ') or contains(concat(' ',normalize-space(@class),' '),' tab active ')][starts-with(normalize-space(),'Upcoming')]");
    public final By tabPast           = By.xpath("//*[contains(concat(' ',normalize-space(@class),' '),' tab ') or contains(concat(' ',normalize-space(@class),' '),' tab active ')][starts-with(normalize-space(),'Past')]");
    public final By tabCancelled      = By.xpath("//*[contains(concat(' ',normalize-space(@class),' '),' tab ') or contains(concat(' ',normalize-space(@class),' '),' tab active ')][starts-with(normalize-space(),'Cancelled')]");
    public final By activeTab         = By.cssSelector(".tab.active");
    public final By appointmentCards  = By.cssSelector("[class*='appt-card'], [class*='appointment-card']");
    public final By emptyStateMessage = By.xpath("//*[contains(normalize-space(),'No upcoming appointments') or contains(normalize-space(),'No appointments')]");

    // Book Appointment Modal
    public final By modal             = By.cssSelector(".modal, [role='dialog']");
    public final By modalTitle        = By.xpath("//*[contains(@class,'modal-title')][normalize-space()='Book Appointment']");
    public final By confirmBookingBtn = By.xpath("//button[normalize-space()='Confirm Booking']");
    public final By modalCancelBtn    = By.xpath("//button[normalize-space()='Cancel']");
    public final By modalCloseBtn     = By.cssSelector("button.close-btn");
    public final By modalDoctorSelect = By.cssSelector("select.field-input");
    public final By modalReasonField  = By.cssSelector("input[placeholder*='Cardiology'], input[placeholder*='reason' i]");
    public final By modalDateField    = By.cssSelector("input[type='date']");
    public final By modalTimeField    = By.cssSelector("input[type='time']");
    public final By validationErrors  = By.xpath("//*[contains(translate(.,'REQUIRED','required'),'required') or contains(@class,'error') or contains(@class,'invalid')]");

    public PatientAppointments(WebDriver driver) {
        super(driver);
    }

    public PatientAppointments open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/appointments");
        waitUntilLoaded();
        return this;
    }

    public PatientAppointments waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/appointments"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/appointments");
    }

    public PatientAppointments openBookModal() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(bookAppointmentBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
        return this;
    }

    public boolean isModalOpen() {
        return !driver.findElements(modalTitle).isEmpty()
                && driver.findElement(modalTitle).isDisplayed();
    }

    public void closeModal() {
        if (!driver.findElements(modalCloseBtn).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(modalCloseBtn)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
        }
    }

    public PatientAppointments selectFirstDoctor() {
        WebElement raw = wait.until(ExpectedConditions.visibilityOfElementLocated(modalDoctorSelect));
        Select dropdown = new Select(raw);
        for (WebElement opt : dropdown.getOptions()) {
            String v = opt.getAttribute("value");
            if (v != null && !v.isBlank() && !"null".equalsIgnoreCase(v)) {
                dropdown.selectByValue(v);
                return this;
            }
        }
        if (dropdown.getOptions().size() > 1) {
            dropdown.selectByIndex(1);
        }
        return this;
    }

    public PatientAppointments enterReason(String reason) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(modalReasonField));
        input.clear();
        input.sendKeys(reason);
        return this;
    }

    /** Set a future-dated date via JS to avoid native date-picker fragility. */
    public PatientAppointments setDate(LocalDate date) {
        List<WebElement> dates = driver.findElements(modalDateField);
        if (dates.isEmpty()) return this;
        String value = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        ((JavascriptExecutor) driver).executeScript(
                "const el=arguments[0]; const setter=Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype,'value').set;" +
                        "setter.call(el, arguments[1]); el.dispatchEvent(new Event('input',{bubbles:true})); el.dispatchEvent(new Event('change',{bubbles:true}));",
                dates.get(0), value);
        return this;
    }

    public PatientAppointments setTime(String hhmm) {
        List<WebElement> times = driver.findElements(modalTimeField);
        if (times.isEmpty()) return this;
        ((JavascriptExecutor) driver).executeScript(
                "const el=arguments[0]; const setter=Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype,'value').set;" +
                        "setter.call(el, arguments[1]); el.dispatchEvent(new Event('input',{bubbles:true})); el.dispatchEvent(new Event('change',{bubbles:true}));",
                times.get(0), hhmm);
        return this;
    }

    public void clickConfirmBooking() {
        WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(confirmBookingBtn));
        confirm.click();
    }

    public void clickConfirmIgnoringDisabled() {
        WebElement confirm = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmBookingBtn));
        try { confirm.click(); } catch (Exception ignored) {}
    }

    public boolean isConfirmEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmBookingBtn)).isEnabled();
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
