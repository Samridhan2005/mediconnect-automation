package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Analytics & Insights — /admin/{userId}/analytics */
public class AdminAnalytics extends BasePage {

    public final By pageHeader       = By.xpath("//*[contains(normalize-space(),'Analytics')]");
    // Page exposes native <select> dropdowns whose options contain the period / department keywords.
    // We pick selects by their option content rather than by a specific name attribute (none present).
    public final By periodSelect     = By.xpath("//select[option[contains(normalize-space(),'Last') or contains(normalize-space(),'days') or contains(normalize-space(),'Month')]]");
    public final By departmentSelect = By.xpath("//select[option[contains(normalize-space(),'Department') or contains(normalize-space(),'Cardiology') or contains(normalize-space(),'All ')]]");
    // Generic fallback — any dropdown on the page.
    public final By anyDropdown      = By.cssSelector("select, [role='combobox'], [class*='dropdown']");
    public final By heatmap          = By.xpath("//*[contains(normalize-space(),'Patient Flow Heatmap') or contains(@class,'heatmap')]");

    public AdminAnalytics(WebDriver driver) {
        super(driver);
    }

    public AdminAnalytics open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/analytics");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/analytics"); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
