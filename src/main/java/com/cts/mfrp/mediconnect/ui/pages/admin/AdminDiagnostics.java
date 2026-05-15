package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Diagnostics Module — /admin/{userId}/diagnostics */
public class AdminDiagnostics extends BasePage {

    public final By pageHeader     = By.xpath("//*[normalize-space()='Diagnostics Module']");
    public final By tabLabReports  = By.xpath("//*[normalize-space()='Lab Reports']");
    public final By tabRadiology   = By.xpath("//*[normalize-space()='Radiology']");
    public final By tabImaging     = By.xpath("//*[normalize-space()='Imaging']");

    public AdminDiagnostics(WebDriver driver) {
        super(driver);
    }

    public AdminDiagnostics open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/diagnostics");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/diagnostics"); }

    public void clickRadiology() { click(tabRadiology); }
    public void clickImaging()   { click(tabImaging); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
