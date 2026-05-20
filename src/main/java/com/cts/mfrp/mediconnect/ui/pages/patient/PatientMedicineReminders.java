package com.cts.mfrp.mediconnect.ui.pages.patient;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.PatientSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Patient Medicine Reminders page — /patient/{userId}/reminders */
public class PatientMedicineReminders extends BasePage {

    public final By pageHeader      = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='Medicine Reminders']");
    public final By todaysSchedule  = By.xpath("//*[contains(normalize-space(),\"Today's Schedule\")]");
    public final By medCards        = By.cssSelector(".med-card");
    public final By dueNowMarkers   = By.cssSelector(".due-now");
    public final By markTakenBtns   = By.cssSelector("button.mp-check.next-btn");
    public final By dueNowMedCards  = By.xpath("//div[contains(@class,'med-card')][.//*[contains(@class,'due-now')]]");
    public final By statBlocks      = By.xpath("//*[contains(normalize-space(),'Active medicines') or contains(normalize-space(),'Taken today') or contains(normalize-space(),'Due today') or contains(normalize-space(),'Adherence')]");

    // --- Extended locators ---

    // Page header
    public final By subLabel              = By.xpath("//*[contains(normalize-space(),'Track your daily medications')]");
    public final By hamburgerMenu         = By.cssSelector("button[class*='hamburger'], button[class*='menu-toggle'], [aria-label*='menu' i]");
    public final By bloodGroupHeaderChip  = By.xpath("//*[contains(normalize-space(),'Blood group:') or contains(normalize-space(),'Blood Group:')]");
    public final By notificationBell      = By.cssSelector("button[class*='notif'], button[class*='bell'], [aria-label*='notif' i]");

    // 4 individual stat tiles (labels)
    public final By tileActiveMeds        = By.xpath("//*[contains(normalize-space(),'Active medicines')]");
    public final By tileTakenToday        = By.xpath("//*[contains(normalize-space(),'Taken today')]");
    public final By tileDueToday          = By.xpath("//*[contains(normalize-space(),'Due today')]");
    public final By tileAdherence         = By.xpath("//*[contains(normalize-space(),'Adherence')]");

    // 4 stat tile sub-labels
    public final By subLabelAllPrescribed = By.xpath("//*[contains(normalize-space(),'All prescribed')]");
    public final By subLabelPercentDone   = By.xpath("//*[contains(normalize-space(),'% done')]");
    public final By subLabelRemaining     = By.xpath("//*[contains(normalize-space(),'Remaining')]");
    public final By subLabelAdherenceQual = By.xpath("//*[normalize-space()='Poor' or normalize-space()='Fair' or normalize-space()='Good' or normalize-space()='Excellent']");

    // Sections
    public final By todaysScheduleHeading = By.xpath("//*[normalize-space()=\"Today's Schedule\"]");
    public final By scheduleDateLabel     = By.xpath("//*[contains(normalize-space(),'Wednesday,') or contains(normalize-space(),'Monday,') or contains(normalize-space(),'Tuesday,') or contains(normalize-space(),'Thursday,') or contains(normalize-space(),'Friday,') or contains(normalize-space(),'Saturday,') or contains(normalize-space(),'Sunday,')]");
    public final By allPrescriptionsHead  = By.xpath("//*[normalize-space()='All Prescriptions']");

    // Empty state messages
    public final By emptyScheduleMsg      = By.xpath("//*[contains(normalize-space(),'No medicines scheduled for today')]");
    public final By emptyPrescriptionsMsg = By.xpath("//*[contains(normalize-space(),'No prescriptions on file')]");

    // Sidebar profile
    public final By sidebarPatientId      = By.xpath("//*[contains(normalize-space(),'PT-')]");
    public final By sidebarPatientAge     = By.xpath("//*[contains(normalize-space(),'Age ')]");

    public PatientMedicineReminders(WebDriver driver) {
        super(driver);
    }

    public PatientMedicineReminders open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/reminders");
        waitUntilLoaded();
        return this;
    }

    public PatientMedicineReminders waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/reminders"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/reminders");
    }

    public int dueNowCount() {
        return driver.findElements(dueNowMarkers).size();
    }

    /** Click the first 'mark as taken' button on a DUE NOW row. Returns the number of remaining DUE NOW after click. */
    public int markFirstDoseTaken() {
        WebElement btn = wait.until(d -> {
            List<WebElement> btns = d.findElements(markTakenBtns);
            for (WebElement b : btns) if (b.isDisplayed() && b.isEnabled()) return b;
            return null;
        });
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        int before = driver.findElements(markTakenBtns).size();
        btn.click();
        wait.until(d -> d.findElements(markTakenBtns).size() != before);
        return driver.findElements(markTakenBtns).size();
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
