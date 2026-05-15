package com.cts.mfrp.mediconnect.ui.pages.common;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Shared sidebar component for the Patient portal.
 * Used by every patient page so navigation locators live in one place.
 */
public class PatientSidebar extends BasePage {

    public final By navHealthOverview     = By.xpath("//a[contains(@class,'ni') and normalize-space()='Health Overview']");
    public final By navAppointments       = By.xpath("//a[contains(@class,'ni') and contains(normalize-space(),'Appointments')]");
    public final By navMedicalRecords     = By.xpath("//a[contains(@class,'ni') and normalize-space()='Medical Records']");
    public final By navLabReports         = By.xpath("//a[contains(@class,'ni') and normalize-space()='Lab Reports']");
    public final By navTelemedicine       = By.xpath("//a[contains(@class,'ni') and normalize-space()='Telemedicine']");
    public final By navMedicineReminders  = By.xpath("//a[contains(@class,'ni') and normalize-space()='Medicine Reminders']");
    public final By navAiHealthAssistant  = By.xpath("//a[contains(@class,'ni') and normalize-space()='AI Health Assistant']");
    public final By signOutLink           = By.xpath("//*[normalize-space()='Sign Out' or normalize-space()='Sign out']");
    public final By activeNavLink         = By.cssSelector("a.ni.active");

    public PatientSidebar(WebDriver driver) {
        super(driver);
    }

    public void clickHealthOverview()     { click(navHealthOverview); }
    public void clickAppointments()       { click(navAppointments); }
    public void clickMedicalRecords()     { click(navMedicalRecords); }
    public void clickLabReports()         { click(navLabReports); }
    public void clickTelemedicine()       { click(navTelemedicine); }
    public void clickMedicineReminders()  { click(navMedicineReminders); }
    public void clickAiHealthAssistant()  { click(navAiHealthAssistant); }
    public void signOut()                 { click(signOutLink); }

    public String getActiveNavText()      { return text(activeNavLink); }
}
