package com.cts.mfrp.mediconnect.ui.pages.auth;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the unified Patient / Doctor registration form at /register.
 * Patient role is selected by default; switching to Doctor uses a slider.
 */
public class PatientRegister extends BasePage {

    public static final String PATH = "/register";

    private final By firstName       = By.name("firstName");
    private final By lastName        = By.name("lastName");
    private final By email           = By.name("email");
    private final By phone           = By.name("phone");
    private final By dateOfBirth     = By.name("dateOfBirth");
    private final By bloodGroup      = By.xpath("//*[text()='Blood group']/parent::div/div/select");
    private final By gender          = By.xpath("//*[text()='Gender']/parent::div/div/select");
    private final By password        = By.xpath("//*[text()='Password']/parent::div/div/input");
    private final By confirmPassword = By.xpath("//*[text()='Confirm password']/parent::div/div/input");
    private final By termsCheckbox   = By.xpath("//input[@id='terms']");
    private final By submitButton    = By.xpath("//button[@type='submit']");

    public PatientRegister(WebDriver driver) {
        super(driver);
    }

    public PatientRegister open() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));
        return this;
    }

    public PatientRegister enterFirstName(String v)       { type(firstName, v); return this; }
    public PatientRegister enterLastName(String v)        { type(lastName, v); return this; }
    public PatientRegister enterEmail(String v)           { type(email, v); return this; }
    public PatientRegister enterPhone(String v)           { type(phone, v); return this; }
    public PatientRegister enterPassword(String v)        { type(password, v); return this; }
    public PatientRegister enterConfirmPassword(String v) { type(confirmPassword, v); return this; }

    // HTML5 <input type="date"> is brittle with sendKeys: the browser parses each digit into
    // the locale's DD/MM/YYYY segments, so "1988-03-12" via sendKeys produces garbage.
    // Set the value via JS using the React-native setter so the onChange handler fires correctly.
    // Input must be in ISO yyyy-MM-dd format.
    public PatientRegister enterDateOfBirth(String isoDate) {
        WebElement el = visible(dateOfBirth);
        ((JavascriptExecutor) driver).executeScript(
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;"
                        + "setter.call(arguments[0], arguments[1]);"
                        + "arguments[0].dispatchEvent(new Event('input',  {bubbles:true}));"
                        + "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));"
                        + "arguments[0].dispatchEvent(new Event('blur',   {bubbles:true}));",
                el, isoDate);
        return this;
    }

    public PatientRegister selectBloodGroup(String visibleText) {
        new Select(visible(bloodGroup)).selectByVisibleText(visibleText);
        return this;
    }

    public PatientRegister selectGender(String visibleText) {
        new Select(visible(gender)).selectByVisibleText(visibleText);
        return this;
    }

    public PatientRegister acceptTerms() {
        WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(termsCheckbox));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", cb);
        if (!cb.isSelected()) {
            js.executeScript(
                    "arguments[0].checked = true;"
                            + "arguments[0].dispatchEvent(new Event('input',  {bubbles:true}));"
                            + "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));"
                            + "arguments[0].dispatchEvent(new Event('blur',   {bubbles:true}));",
                    cb);
        }
        return this;
    }

    public void submit() {
        click(submitButton);
    }
}
