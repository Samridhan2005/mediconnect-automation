package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Multi-Hospital Management — /admin/{userId}/hospitals */
public class AdminHospitals extends BasePage {

    public final By pageHeader     = By.xpath("//*[contains(normalize-space(),'Multi-Hospital Management')]");
    public final By searchInput    = By.cssSelector("input[placeholder*='Search hospital' i]");
    public final By hospitalsTable = By.cssSelector("table");
    public final By statusBadges   = By.cssSelector("[class*='status-badge'], [class*='badge']");
    public final By occupancyCells = By.cssSelector("td[class*='occupancy']");

    public AdminHospitals(WebDriver driver) {
        super(driver);
    }

    public AdminHospitals open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/hospitals");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/hospitals"); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
