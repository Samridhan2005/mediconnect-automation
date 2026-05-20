package com.cts.mfrp.mediconnect.ui.pages.common;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Shared sidebar component for the Admin (Super Admin) portal.
 */
public class AdminSidebar extends BasePage {

    public final By navOverview         = By.xpath("//a[contains(@class,'ni') and normalize-space()='Overview']");
    public final By navHospitals        = By.xpath("//a[contains(@class,'ni') and normalize-space()='Hospitals']");
    public final By navPatients         = By.xpath("//a[contains(@class,'ni') and normalize-space()='Patients']");
    public final By navAppointments     = By.xpath("//a[contains(@class,'ni') and normalize-space()='Appointments']");
    public final By navDiagnostics      = By.xpath("//a[contains(@class,'ni') and normalize-space()='Diagnostics']");
    public final By navTelemedicine     = By.xpath("//a[contains(@class,'ni') and normalize-space()='Telemedicine']");
    public final By navSupplyChain      = By.xpath("//a[contains(@class,'ni') and normalize-space()='Supply Chain']");
    public final By navRevenueBilling   = By.xpath("//a[contains(@class,'ni') and normalize-space()='Revenue & Billing']");
    public final By navAnalytics        = By.xpath("//a[contains(@class,'ni') and normalize-space()='Analytics']");

    public final By adminControlLabel   = By.xpath("//*[contains(translate(normalize-space(),'admin control','ADMIN CONTROL'),'ADMIN CONTROL')]");
    // Logout is rendered as <div class="sb-foot"> containing an icon and <span>Logout</span>.
    // It is NOT a <button> or <a>, so we target the clickable container by its class.
    public final By signOutButton       = By.cssSelector("div.sb-foot");
    public final By activeNavLink       = By.cssSelector("a.ni.active");

    public AdminSidebar(WebDriver driver) {
        super(driver);
    }

    public void clickOverview()         { click(navOverview); }
    public void clickHospitals()        { click(navHospitals); }
    public void clickPatients()         { click(navPatients); }
    public void clickAppointments()     { click(navAppointments); }
    public void clickDiagnostics()      { click(navDiagnostics); }
    public void clickTelemedicine()     { click(navTelemedicine); }
    public void clickSupplyChain()      { click(navSupplyChain); }
    public void clickRevenueBilling()   { click(navRevenueBilling); }
    public void clickAnalytics()        { click(navAnalytics); }
    public void signOut()               { click(signOutButton); }

    public boolean isAdminControlLabelVisible() { return isDisplayed(adminControlLabel); }
    public String getActiveNavText()    { return text(activeNavLink); }
}
