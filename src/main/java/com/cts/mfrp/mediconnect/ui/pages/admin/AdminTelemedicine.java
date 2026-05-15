package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Telemedicine page — /admin/{userId}/telemedicine */
public class AdminTelemedicine extends BasePage {

    public final By pageHeader = By.xpath("//*[normalize-space()='Telemedicine']");

    public AdminTelemedicine(WebDriver driver) {
        super(driver);
    }

    public AdminTelemedicine open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/telemedicine");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/telemedicine"); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
