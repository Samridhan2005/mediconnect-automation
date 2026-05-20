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
