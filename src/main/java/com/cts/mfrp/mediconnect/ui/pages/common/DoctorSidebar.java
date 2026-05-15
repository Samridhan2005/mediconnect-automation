package com.cts.mfrp.mediconnect.ui.pages.common;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Shared sidebar component for the Doctor portal.
 */
public class DoctorSidebar extends BasePage {

    public final By navDashboard       = By.xpath("//a[contains(@class,'ni') and normalize-space()='Dashboard']");
    public final By navPatients        = By.xpath("//a[contains(@class,'ni') and normalize-space()='Patients']");
    public final By navAppointments    = By.xpath("//a[contains(@class,'ni') and normalize-space()='Appointments']");
    public final By navLabReports      = By.xpath("//a[contains(@class,'ni') and normalize-space()='Lab Reports']");
    public final By navMedicalRecords  = By.xpath("//a[contains(@class,'ni') and normalize-space()='Medical Records']");
    public final By navTelemedicine    = By.xpath("//a[contains(@class,'ni') and normalize-space()='Telemedicine']");
    public final By navAnalytics       = By.xpath("//a[contains(@class,'ni') and normalize-space()='Analytics']");
    public final By navSupplyChain     = By.xpath("//a[contains(@class,'ni') and normalize-space()='Supply Chain']");
    public final By navAiAssistant     = By.xpath("//a[contains(@class,'ni') and normalize-space()='AI Assistant']");

    // FRD-expected (will be absent in the current UI)
    public final By navDiagnostics     = By.xpath("//a[contains(@class,'ni') and normalize-space()='Diagnostics']");
    public final By navBilling         = By.xpath("//a[contains(@class,'ni') and normalize-space()='Billing']");
    public final By navSettings        = By.xpath("//a[contains(@class,'ni') and normalize-space()='Settings']");

    public final By signOutButton      = By.cssSelector("button.sb-logout");
    public final By activeNavLink      = By.cssSelector("a.ni.active");

    public DoctorSidebar(WebDriver driver) {
        super(driver);
    }

    public void clickDashboard()       { click(navDashboard); }
    public void clickPatients()        { click(navPatients); }
    public void clickAppointments()    { click(navAppointments); }
    public void clickLabReports()      { click(navLabReports); }
    public void clickMedicalRecords()  { click(navMedicalRecords); }
    public void clickTelemedicine()    { click(navTelemedicine); }
    public void clickAnalytics()       { click(navAnalytics); }
    public void clickSupplyChain()     { click(navSupplyChain); }
    public void clickAiAssistant()     { click(navAiAssistant); }
    public void signOut()              { click(signOutButton); }

    public boolean isSignOutVisible()  { return isDisplayed(signOutButton); }
    public String getActiveNavText()   { return text(activeNavLink); }
}
