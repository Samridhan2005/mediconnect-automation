package com.cts.mfrp.mediconnect.ui.pages.auth;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Patient / Doctor login form at /login.
 * Admin login is on a separate route — see {@link AdminLogin}.
 */
public class Login extends BasePage {

    public static final String PATH = "/login";

    private final By heading        = By.tagName("h1");
    private final By patientTab     = By.xpath("//button[normalize-space()='Patient Login']");
    private final By doctorTab      = By.xpath("//button[normalize-space()='Doctor Login']");
    private final By activeTab      = By.cssSelector("button.tab-btn.active");
    private final By emailField     = By.name("email");
    private final By passwordField  = By.name("password");
    private final By eyeToggle      = By.cssSelector("button.eye-btn");
    private final By loginButton    = By.cssSelector("button.btn-login");
    private final By errorAlert     = By.cssSelector(".error-alert");
    private final By forgotLink     = By.cssSelector("a.forgot-link");
    private final By registerLink   = By.cssSelector("a.register-link");
    private final By backHomeLink   = By.cssSelector("a.back-link");

    public Login(WebDriver driver) {
        super(driver);
    }

    public Login open() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        return this;
    }

    public String getHeading()              { return text(heading); }
    public boolean isPatientTabActive()     { return text(activeTab).equalsIgnoreCase("Patient Login"); }
    public boolean isDoctorTabActive()      { return text(activeTab).equalsIgnoreCase("Doctor Login"); }
    public Login selectPatientTab()         { click(patientTab); return this; }
    public Login selectDoctorTab()          { click(doctorTab); return this; }

    public Login enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public Login enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public boolean isPasswordMasked() {
        return "password".equals(visible(passwordField).getAttribute("type"));
    }

    public Login togglePasswordVisibility() {
        click(eyeToggle);
        return this;
    }

    public String getEmailFieldType()       { return visible(emailField).getAttribute("type"); }
    public String getEmailPlaceholder()     { return visible(emailField).getAttribute("placeholder"); }
    public String getPasswordPlaceholder()  { return visible(passwordField).getAttribute("placeholder"); }
    public boolean isLoginButtonVisible()   { return isDisplayed(loginButton); }
    public boolean isForgotLinkVisible()    { return isDisplayed(forgotLink); }
    public boolean isRegisterLinkVisible()  { return isDisplayed(registerLink); }
    public boolean isBackHomeLinkVisible()  { return isDisplayed(backHomeLink); }
    public boolean isPatientTabVisible()    { return isDisplayed(patientTab); }
    public boolean isDoctorTabVisible()     { return isDisplayed(doctorTab); }

    public void submit() {
        click(loginButton);
    }

    public void loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submit();
    }

    public boolean isErrorDisplayed()       { return isDisplayed(errorAlert); }
    public String getErrorMessage()         { return text(errorAlert); }
    public void clickForgotPassword()       { click(forgotLink); }
    public void clickRegister()             { click(registerLink); }
    public void clickBackHome()             { click(backHomeLink); }

    public WebElement getEmailField()       { return visible(emailField); }
    public WebElement getPasswordField()    { return visible(passwordField); }
}
