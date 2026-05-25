package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Telemedicine page — /admin/{userId}/telemedicine */
public class AdminTelemedicine extends BasePage {

    public final By pageHeader        = By.xpath("//*[normalize-space()='Telemedicine']");
    public final By subLabel          = By.xpath("//*[contains(normalize-space(),'Virtual consultation management')]");
    public final By scheduleSessionBtn= By.xpath("//button[contains(normalize-space(),'Schedule Session')]");

    // 4 summary tiles
    public final By tileDoctorsOnline   = By.xpath("//*[contains(normalize-space(),'Doctors Online')]");
    public final By tileActiveSessions  = By.xpath("//*[contains(normalize-space(),'Active Sessions')]");
    public final By tileTodayScheduled  = By.xpath("//*[contains(normalize-space(),'Today Scheduled')]");
    public final By tileThisWeek        = By.xpath("//*[contains(normalize-space(),'This Week')]");

    // Doctor Availability section
    public final By doctorAvailabilityHeading = By.xpath("//*[contains(normalize-space(),'Doctor Availability')]");
    public final By doctorCards               = By.cssSelector("[class*='doctor'][class*='card'], .doctor-card");

    // Today's Video Appointments + Join buttons
    public final By todaysVideoSection  = By.xpath("//*[contains(normalize-space(),\"Today's Video Appointments\")]");
    public final By joinButtons         = By.xpath("//button[normalize-space()='Join']");

    // Session Records section
    public final By sessionRecordsSection = By.xpath("//*[contains(normalize-space(),'Session Records')]");

    // Schedule Video Session modal (opens after clicking + Schedule Session)
    public final By scheduleModalHeading = By.xpath("//*[normalize-space()='Schedule Video Session']");
    public final By modalDoctorSelect    = By.xpath("//label[contains(normalize-space(),'Doctor')]/following-sibling::select");
    public final By modalPatientSelect   = By.xpath("//label[contains(normalize-space(),'Patient')]/following-sibling::select");
    public final By modalDateInput       = By.xpath("//label[contains(normalize-space(),'Date')]/following-sibling::input");
    public final By modalTimeInput       = By.xpath("//label[contains(normalize-space(),'Time')]/following-sibling::input");
    public final By modalSessionTypeSelect = By.xpath("//label[contains(normalize-space(),'Session Type')]/following-sibling::select");
    public final By modalNotesTextarea   = By.xpath("//label[contains(normalize-space(),'Notes')]/following-sibling::textarea");
    public final By modalCancelButton    = By.xpath("//button[normalize-space()='Cancel']");
    public final By modalScheduleButton  = By.xpath("//button[normalize-space()='Cancel']/following-sibling::button[contains(normalize-space(),'Schedule Session')]");

    public AdminTelemedicine(WebDriver driver) {
        super(driver);
    }

    public AdminTelemedicine open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/telemedicine");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/telemedicine"); }

    public boolean isScheduleSessionButtonVisible() { return isDisplayed(scheduleSessionBtn); }
    public void clickScheduleSession()              { click(scheduleSessionBtn); }
    public boolean isScheduleModalOpen()            { return isDisplayed(scheduleModalHeading); }

    public int joinButtonCount() {
        return driver.findElements(joinButtons).size();
    }

    public void clickFirstJoinButton() {
        java.util.List<org.openqa.selenium.WebElement> btns = driver.findElements(joinButtons);
        if (btns.isEmpty()) return;

        org.openqa.selenium.WebElement btn = btns.get(0);
        // Scroll the Join button into the centre of the viewport before clicking —
        // it sits inside a scrollable table and may be off-screen / intercepted otherwise.
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        // Wait until the button is clickable instead of using a fixed sleep
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .elementToBeClickable(btn));

        try {
            btn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // Fallback: force a JavaScript click if a CSS overlay still blocks the native click.
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", btn);
        }
    }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}