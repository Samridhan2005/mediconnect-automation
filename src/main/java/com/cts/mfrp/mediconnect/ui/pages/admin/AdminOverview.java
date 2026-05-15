package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin System Overview — /admin/{userId}/overview */
public class AdminOverview extends BasePage {

    public final By pageHeader        = By.xpath("//*[contains(normalize-space(),'System Overview')]");
    public final By periodSelector    = By.cssSelector("select[name*='period'], [class*='period-selector']");
    public final By allHospitalsDdl   = By.cssSelector("select[name*='hospital'], [class*='hospitals-dropdown']");
    public final By notificationBell  = By.cssSelector("button.notif-btn, button.tb-notif, [class*='notif']");
    public final By summaryTiles      = By.cssSelector("[class*='summary'] [class*='tile'], .stat-tile");
    public final By patientInflowChart = By.cssSelector("[class*='patient-inflow'], [class*='line-chart']");
    public final By hospitalBranchMap = By.xpath("//*[contains(normalize-space(),'Hospital Branch Map') or contains(@class,'branch-map') or contains(@class,'map-container')]");

    public AdminOverview(WebDriver driver) {
        super(driver);
    }

    public AdminOverview open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/overview");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().matches(".*/admin/\\d+/overview$");
    }

    public AdminSidebar sidebar() {
        return new AdminSidebar(driver);
    }
}
