package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Analytics page — /doctor/{userId}/analytics */
public class DoctorAnalytics extends BasePage {

    public final By pageHeader       = By.xpath("//*[normalize-space()='Analytics']");
    public final By timePeriodSelect  = By.cssSelector("select.range-select");
    public final By timePeriodButtons = By.cssSelector("select.range-select"); // same element, kept for test compatibility
    public final By exportBtn        = By.xpath("//*[contains(text(),'Export')]");
    public final By appointmentChart = By.cssSelector("[class*='appointment-chart'], svg, canvas");

    public DoctorAnalytics(WebDriver driver) {
        super(driver);
    }

    public DoctorAnalytics open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/analytics");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/analytics"); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
