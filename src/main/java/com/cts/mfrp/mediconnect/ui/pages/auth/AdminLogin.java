package com.cts.mfrp.mediconnect.ui.pages.auth;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Admin login form at /admin/login.
 */
public class AdminLogin extends BasePage {

    public static final String PATH = "/admin/login";

    private final By heading            = By.tagName("h1");
    private final By subtitle           = By.xpath("//*[contains(normalize-space(),'Access restricted to authorised personnel only')]");
    private final By adminPanelLabel    = By.xpath("//div[normalize-space()='Admin Control Panel']");
    private final By featureBullet1     = By.xpath("//*[contains(normalize-space(),'Real-time hospital network monitoring')]");
    private final By featureBullet2     = By.xpath("//*[contains(normalize-space(),'AI-powered operational insights')]");
    private final By featureBullet3     = By.xpath("//*[contains(normalize-space(),'Multi-branch administration')]");
    private final By emailField         = By.xpath("//label[normalize-space()='Email address']/following-sibling::input");
    private final By passwordField      = By.xpath("//label[normalize-space()='Password']/following-sibling::div/input");
    private final By loginButton        = By.cssSelector("button.submit-btn");
    private final By errorAlert         = By.cssSelector(".error-alert, [class*='error']");
    private final By backHomeLink       = By.cssSelector("a.back-link");
    private final By registerLink       = By.cssSelector("a.hint-link");
    private final By pwToggle           = By.cssSelector("button.pw-toggle");

    public AdminLogin(WebDriver driver) {
        super(driver);
    }

    public AdminLogin open() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        return this;
    }

    public String getHeading()                      { return text(heading); }
    public String getSubtitle()                     { return text(subtitle); }
    public boolean isSubtitleVisible()              { return isDisplayed(subtitle); }
    public boolean isAdminPanelLabelVisible()       { return isDisplayed(adminPanelLabel); }
    public boolean areFeatureBulletsVisible() {
        return isDisplayed(featureBullet1)
                && isDisplayed(featureBullet2)
                && isDisplayed(featureBullet3);
    }

    public boolean isLoginButtonVisible()   { return isDisplayed(loginButton); }
    public boolean isBackHomeLinkVisible()  { return isDisplayed(backHomeLink); }
    public boolean isRegisterLinkVisible()  { return isDisplayed(registerLink); }
    public boolean isEmailFieldVisible()    { return isDisplayed(emailField); }
    public boolean isPasswordMasked() {
        return "password".equals(visible(passwordField).getAttribute("type"));
    }
    public String getEmailFieldType()       { return visible(emailField).getAttribute("type"); }

    public AdminLogin enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public AdminLogin enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public void submit() {
        click(loginButton);
    }

    public void loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submit();
    }

    public AdminLogin togglePasswordVisibility() {
        click(pwToggle);
        return this;
    }

    public void clickBackHome() { click(backHomeLink); }
    public void clickRegister() { click(registerLink); }

    public boolean isErrorDisplayed()  { return isDisplayed(errorAlert); }
    public String getErrorMessage()    { return text(errorAlert); }
}
