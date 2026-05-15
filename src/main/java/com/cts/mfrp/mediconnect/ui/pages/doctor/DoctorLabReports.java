package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Lab Reports page — /doctor/{userId}/lab-reports */
public class DoctorLabReports extends BasePage {

    public final By pageHeader      = By.xpath("//*[normalize-space()='Lab Reports']");
    public final By requestTestBtn  = By.xpath("//*[contains(text(),'+ Request Test') or contains(text(),'Request Test')]");
    public final By searchInput     = By.cssSelector("input[placeholder*='Search' i]");
    public final By labReportsTable = By.cssSelector("table");

    public DoctorLabReports(WebDriver driver) {
        super(driver);
    }

    public DoctorLabReports open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/lab-reports");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/lab-reports"); }

    public void clickRequestTest() { click(requestTestBtn); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
