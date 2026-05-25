package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminAnalytics extends BasePage {

    public final By pageHeader       = By.xpath("//*[contains(normalize-space(),'Analytics & Insights')]");
    public final By subLabel         = By.xpath("//*[contains(normalize-space(),'Clinical performance')]");

    public final By periodSelect     = By.xpath("//select[option[normalize-space()='Last 7 Days']]");
    public final By departmentSelect = By.xpath("//select[option[normalize-space()='Cardiology']]");
    public final By hospitalSelect   = By.xpath("//select[option[normalize-space()='City General Hospital']]");
    public final By anyDropdown      = By.cssSelector("select, [role='combobox'], [class*='dropdown']");

    public final By tilePatientFlow      = By.xpath("//*[contains(normalize-space(),'Patient Flow')]");
    public final By tileBedOccupancy     = By.xpath("//*[contains(normalize-space(),'Bed Occupancy')]");
    public final By tileCompletionRate   = By.xpath("//*[contains(normalize-space(),'Completion Rate')]");
    public final By tileCancellationRate = By.xpath("//*[contains(normalize-space(),'Cancellation Rate')]");

    public final By heatmap                       = By.xpath("//*[text()=\"Appointment Flow Heatmap\"]");
    public final By bedOccupancyByHospitalChart   = By.xpath("//*[text()=\"Bed Occupancy by Hospital\"]");
    public final By appointmentCompletionChart    = By.xpath("//*[text()=\"Appointment Completion Rate\"]");
    public final By cancellationRateTrendChart    = By.xpath("//*[text()=\"Cancellation Rate Trend\"]");
    public final By appointmentsByDeptChart       = By.xpath("//*[text()=\"Appointments by Department\"]");

    public final By heatmapLegendLow  = By.xpath("//*[normalize-space()='Low']");
    public final By heatmapLegendHigh = By.xpath("//*[normalize-space()='High']");

    public AdminAnalytics(WebDriver driver) {
        super(driver);
    }

    public AdminAnalytics open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/analytics");

        // Wait up to 30s for the page navigation to complete.
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> d.getCurrentUrl().contains("/analytics"));

        // The analytics page is chart-heavy; brief settle for Angular to render charts.
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}

        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/analytics");
    }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
