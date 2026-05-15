package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Supply Chain Management — /admin/{userId}/supply */
public class AdminSupplyChain extends BasePage {

    public final By pageHeader     = By.xpath("//*[normalize-space()='Supply Chain Management']");
    public final By newOrderBtn    = By.xpath("//*[contains(normalize-space(),'+ New Order')]");
    public final By exportBtn      = By.xpath("//button[contains(normalize-space(),'Export')]");
    public final By aiInsights     = By.xpath("//*[contains(normalize-space(),'AI Supply Insights')]");
    public final By lowStockFilter = By.xpath("//*[normalize-space()='Low Stock']");
    public final By expiringFilter = By.xpath("//*[normalize-space()='Expiring']");

    public AdminSupplyChain(WebDriver driver) {
        super(driver);
    }

    public AdminSupplyChain open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/supply");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/supply"); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
