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

/** Patient AI Health Assistant page — /patient/{userId}/ai */
public class PatientAiHealthAssistant extends BasePage {

    public final By pageHeader     = By.xpath("//div[contains(@class,'tb-title')][normalize-space()='AI Health Assistant']");
    public final By modeButtons    = By.cssSelector("button.mode-btn");
    public final By activeMode     = By.cssSelector("button.mode-btn.active");
    public final By emergencyCard  = By.xpath("//*[contains(normalize-space(),'Emergency Note')]");
    public final By emergencyClose = By.xpath("//*[contains(normalize-space(),'Emergency Note')]/ancestor::*[1]" +
            "//*[self::button or self::a][contains(@class,'close') or contains(@class,'dismiss') or @aria-label='close']");
    public final By quickActionBtns = By.cssSelector("button.quick-btn");
    public final By chips           = By.cssSelector("button.chip");

    public PatientAiHealthAssistant(WebDriver driver) {
        super(driver);
    }

    public PatientAiHealthAssistant open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/patient/" + userId + "/ai");
        waitUntilLoaded();
        return this;
    }

    public PatientAiHealthAssistant waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/ai"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(activeMode));
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/ai");
    }

    public By modeButtonByText(String text) {
        return By.xpath("//button[contains(@class,'mode-btn')][contains(normalize-space(),'" + text + "')]");
    }

    public PatientAiHealthAssistant selectMode(String text) {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(modeButtonByText(text)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();
        wait.until(d -> {
            List<WebElement> active = d.findElements(activeMode);
            return !active.isEmpty() && active.get(0).getText().toLowerCase().contains(text.toLowerCase());
        });
        return this;
    }

    public String activeModeText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeMode))
                .getText().replaceAll("\\s+", " ").trim();
    }

    public PatientSidebar sidebar() {
        return new PatientSidebar(driver);
    }
}
