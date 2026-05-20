package com.cts.mfrp.mediconnect.ui.pages.auth;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the Create Admin Account form at /admin/register.
 *
 * Fields (all required):
 *   - Hospital (dropdown)
 *   - First Name, Last Name (text)
 *   - Email address (text, disabled until a hospital is selected)
 *   - Phone Number (text)
 *   - Password, Confirm Password (password + eye-toggle)
 *   - Terms checkbox
 */
public class AdminRegister extends BasePage {

    public static final String PATH = "/admin/register";

    private final By heading            = By.tagName("h1");
    private final By subtitle           = By.xpath("//*[contains(normalize-space(),'Register as an authorised administrator')]");

    // form fields
    private final By hospitalSelect     = By.tagName("select");
    private final By emailDomainHint    = By.xpath("//*[contains(normalize-space(),'Must end with')]");
    private final By firstNameField     = By.xpath("//*[text()='First Name *']/parent::div/input");
    private final By lastNameField      = By.xpath("//*[text()='Last Name *']/parent::div/input");
    private final By emailField         = By.xpath("//*[text()=' Email address * ']/parent::div/input");
    private final By phoneField         = By.xpath("//*[text()='Phone Number *']/parent::div/input");
    private final By passwordField      = By.xpath("//*[text()='Password *']/parent::div/div/input");
    private final By confirmPwField     = By.xpath("//*[text()='Confirm Password *']/parent::div/div/input");
    private final By termsCheckbox      = By.xpath("//input[@type='checkbox']");
    private final By submitButton       = By.xpath("//button[contains(@class,'submit')]");
    private final By errorAlert         = By.cssSelector(".error-alert, [class*='error']");

    public AdminRegister(WebDriver driver) {
        super(driver);
    }

    public AdminRegister open() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
        return this;
    }

    public String getHeading()                  { return text(heading); }
    public boolean isSubtitleVisible()          { return isDisplayed(subtitle); }
    public boolean isHospitalSelectVisible()    { return isDisplayed(hospitalSelect); }
    public boolean isFirstNameFieldVisible()    { return isDisplayed(firstNameField); }
    public boolean isLastNameFieldVisible()     { return isDisplayed(lastNameField); }
    public boolean isEmailFieldVisible()        { return isDisplayed(emailField); }
    public boolean isPhoneFieldVisible()        { return isDisplayed(phoneField); }
    public boolean isPasswordFieldVisible()     { return isDisplayed(passwordField); }
    public boolean isConfirmPwFieldVisible()    { return isDisplayed(confirmPwField); }
    public boolean isTermsCheckboxVisible()     { return isDisplayed(termsCheckbox); }
    public boolean isSubmitButtonVisible()      { return isDisplayed(submitButton); }

    public boolean isEmailFieldEnabled()        { return visible(emailField).isEnabled(); }
    public boolean isTermsChecked()             { return visible(termsCheckbox).isSelected(); }
    public String getEmailPlaceholder()         { return visible(emailField).getAttribute("placeholder"); }
    public boolean isEmailDomainHintVisible()   { return isDisplayed(emailDomainHint); }
    public String getEmailDomainHintText()      { return text(emailDomainHint); }

    public AdminRegister selectHospital(String hospitalName) {
        new Select(clickable(hospitalSelect)).selectByVisibleText(hospitalName);
        return this;
    }

    public AdminRegister enterFirstName(String v)     { type(firstNameField, v); return this; }
    public AdminRegister enterLastName(String v)      { type(lastNameField, v); return this; }
    public AdminRegister enterEmail(String v)         { type(emailField, v); return this; }
    public AdminRegister enterPhone(String v)         { type(phoneField, v); return this; }
    public AdminRegister enterPassword(String v)      { type(passwordField, v); return this; }
    public AdminRegister enterConfirmPassword(String v) { type(confirmPwField, v); return this; }

    public AdminRegister checkTerms() {
        WebElement cb = visible(termsCheckbox);
        if (!cb.isSelected()) cb.click();
        return this;
    }

    public AdminRegister uncheckTerms() {
        WebElement cb = visible(termsCheckbox);
        if (cb.isSelected()) cb.click();
        return this;
    }

    public void submit() {
        click(submitButton);
    }

    public boolean isErrorDisplayed()  { return isDisplayed(errorAlert); }
    public String getErrorMessage()    { return text(errorAlert); }
}
