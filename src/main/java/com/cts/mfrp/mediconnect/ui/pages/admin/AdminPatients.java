package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Patient Management — /admin/{userId}/patients */
public class AdminPatients extends BasePage {

    // Header: deployment shows "Patient List" card title (no top-level "Patient Management" h1).
    public final By pageHeader     = By.xpath("//*[contains(normalize-space(),'Patient List') or contains(normalize-space(),'Patient Management')]");
    // Tabs include a row count: "Inpatients (55)" / "Outpatients (1)" — use contains.
    public final By inpatientTab   = By.xpath("//*[contains(normalize-space(),'Inpatients')]");
    public final By outpatientTab  = By.xpath("//*[contains(normalize-space(),'Outpatients')]");
    public final By exportBtn      = By.xpath("//button[contains(normalize-space(),'Export')]");
    // Buttons are plain "View" / "Edit" / "Delete" — match by visible text.
    public final By viewBtn        = By.xpath("//button[normalize-space()='View']");
    public final By editBtn        = By.xpath("//button[normalize-space()='Edit']");
    public final By deleteBtn      = By.xpath("//button[normalize-space()='Delete']");
    public final By aiSummarizeBtn = By.xpath("//button[contains(normalize-space(),'AI Summarize')]");

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
