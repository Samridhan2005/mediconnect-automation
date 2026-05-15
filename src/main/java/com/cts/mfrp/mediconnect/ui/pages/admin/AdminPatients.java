package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Patient Management — /admin/{userId}/patients */
public class AdminPatients extends BasePage {

    public final By pageHeader   = By.xpath("//*[normalize-space()='Patient Management']");
    public final By inpatientTab = By.xpath("//*[normalize-space()='Inpatients']");
    public final By outpatientTab = By.xpath("//*[normalize-space()='Outpatients']");
    public final By exportBtn    = By.xpath("//button[contains(normalize-space(),'Export') or contains(@class,'export')]");
    public final By viewBtn      = By.cssSelector("[class*='view'], button[title='View']");
    public final By aiSummarizeBtn = By.xpath("//button[contains(normalize-space(),'+ AI Summarize') or contains(normalize-space(),'AI Summarize')]");
    public final By editBtn      = By.xpath("//button[contains(@class,'edit') or normalize-space()='Edit']");

    public AdminPatients(WebDriver driver) {
        super(driver);
    }

    public AdminPatients open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/patients");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/patients"); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
