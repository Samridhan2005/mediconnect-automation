package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Revenue & Billing — /admin/{userId}/revenue */
public class AdminRevenueBilling extends BasePage {

    public final By pageHeader         = By.xpath("//*[contains(normalize-space(),'Revenue') and contains(normalize-space(),'Billing')]");
    public final By downloadReportBtn  = By.xpath("//button[contains(normalize-space(),'Download Report') or contains(normalize-space(),'Export')]");
    // Broader heading match — also catches "Recent Billings" / "Latest Bills" variants.
    public final By recentBillsHeader  = By.xpath("//*[contains(normalize-space(),'Recent Bills') or contains(normalize-space(),'Recent Billing') or contains(normalize-space(),'Latest Bills')]");
    public final By insuranceClaimsHdr = By.xpath("//*[contains(normalize-space(),'Insurance Claims') or contains(normalize-space(),'Claims')]");
    public final By dataTables         = By.cssSelector("table");

    public AdminRevenueBilling(WebDriver driver) {
        super(driver);
    }

    public AdminRevenueBilling open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/revenue");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/revenue"); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
