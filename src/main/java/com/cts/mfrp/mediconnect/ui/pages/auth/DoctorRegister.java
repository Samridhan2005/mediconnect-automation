package com.cts.mfrp.mediconnect.ui.pages.auth;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the Doctor side of the unified registration form at /register.
 * Clicking the "Doctor" button switches the form from Patient (default) to Doctor.
 */
public class DoctorRegister extends BasePage {

    public static final String PATH = "/register";

    private final By doctorTab       = By.xpath("//button[text()=' Doctor ']");
    private final By firstName       = By.name("firstName");
    private final By lastName        = By.name("lastName");
    private final By email           = By.name("email");
    private final By phone           = By.name("phone");
    private final By specialization  = By.name("specialization");
    private final By hospital        = By.name("hospitalId");
    private final By password        = By.xpath("//*[text()='Password']/parent::div/div/input");
    private final By confirmPassword = By.xpath("//*[text()='Confirm password']/parent::div/div/input");
    private final By termsCheckbox   = By.xpath("//input[@id='terms']");
    private final By submitButton    = By.xpath("//button[@type='submit']");

    public DoctorRegister(WebDriver driver) {
        super(driver);
    }

    public DoctorRegister open() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        // Default tab is Patient — switch to Doctor before the doctor-specific fields render.
        click(doctorTab);
        wait.until(ExpectedConditions.visibilityOfElementLocated(specialization));
        return this;
    }

    public DoctorRegister enterFirstName(String v)       { type(firstName, v); return this; }
    public DoctorRegister enterLastName(String v)        { type(lastName, v); return this; }
    public DoctorRegister enterEmail(String v)           { type(email, v); return this; }
    public DoctorRegister enterPhone(String v)           { type(phone, v); return this; }
    public DoctorRegister enterPassword(String v)        { type(password, v); return this; }
    public DoctorRegister enterConfirmPassword(String v) { type(confirmPassword, v); return this; }

    public DoctorRegister selectSpecialization(String visibleText) {
        new Select(visible(specialization)).selectByVisibleText(visibleText);
        return this;
    }

    public DoctorRegister selectHospital(String visibleText) {
        new Select(visible(hospital)).selectByVisibleText(visibleText);
        return this;
    }

    public DoctorRegister acceptTerms() {
        WebElement cb = visible(termsCheckbox);
        if (!cb.isSelected()) cb.click();
        return this;
    }

    public void submit() {
        click(submitButton);
    }
}
