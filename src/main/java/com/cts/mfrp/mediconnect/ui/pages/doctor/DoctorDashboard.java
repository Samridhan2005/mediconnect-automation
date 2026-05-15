package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Dashboard — /doctor/{userId}/dashboard */
public class DoctorDashboard extends BasePage {

    public final By heading              = By.tagName("h1");
    public final By hospitalSelector     = By.cssSelector("button.hospital-btn");
    public final By notificationBell     = By.cssSelector("button.notif-btn, button.tb-notif");
    public final By globalSearch         = By.cssSelector("input[placeholder*='Search'], input[type='search']");
    public final By profileDropdown      = By.cssSelector("[class*='profile'], .avatar, .user-menu");

    // Main page widgets
    public final By statsSummaryCards    = By.cssSelector("[class*='stats'] [class*='card'], .stat-card, .summary-card");
    public final By revenueTodayCard     = By.xpath("//*[contains(text(),'Revenue Today')]/ancestor::*[contains(@class,'card') or contains(@class,'tile')][1]");
    public final By recentAppointmentsList = By.xpath("//*[contains(text(),'Recent Appointments')]/following-sibling::*[1]");
    public final By upcomingConsultsPanel  = By.xpath("//*[contains(text(),'Upcoming Consultations')]/ancestor::*[contains(@class,'panel') or contains(@class,'card')][1]");
    public final By bedAvailabilitySection = By.xpath("//*[contains(text(),'Bed availability')]/ancestor::*[contains(@class,'section') or contains(@class,'card')][1]");
    public final By joinButtons          = By.cssSelector("button.join-btn");

    public DoctorDashboard(WebDriver driver) {
        super(driver);
    }

    public DoctorDashboard open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/dashboard");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().matches(".*/doctor/\\d+/dashboard$");
    }

    public String getHeadingText()             { return text(heading); }
    public boolean isHospitalSelectorVisible() { return isDisplayed(hospitalSelector); }
    public boolean isNotificationBellVisible() { return isDisplayed(notificationBell); }
    public boolean isGlobalSearchVisible()     { return isDisplayed(globalSearch); }
    public void clickHospitalSelector()        { click(hospitalSelector); }
    public void clickNotificationBell()        { click(notificationBell); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
