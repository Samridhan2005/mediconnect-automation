package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdminRegister extends BasePage {

    public final By hospitalSelect = By.tagName("select");
    public final By firstName = By.xpath("//label[text()='First Name *']/following-sibling::input");
    public final By lastName = By.xpath("//label[text()='Last Name *']/following-sibling::input");
    public final By email = By.xpath("//label[text()=' Email address * ']/following-sibling::input");
    public final By phoneNumber = By.xpath("//label[text()='Phone Number *']/following-sibling::input");
    public final By pswd = By.xpath("//label[text()='Password *']/following-sibling::div/input");
    public final By confirmPswd = By.xpath("//label[text()='Confirm Password *']/following-sibling::div/input");
    public final By terms = By.xpath("//input[@type='checkbox']");
    public final By submit = By.cssSelector("button.submit-btn");
    public final By backToLogin = By.cssSelector("a.hint-link");
    public final By AdminDashboard = By.xpath("//div[text()='System Overview']");
    public final By emailLogin = By.xpath("//input[@type='email']");
    public final By pswdLogin = By.xpath("//input[@type='password']");
    public final By adminLogin = By.xpath("//h1[text()='Admin Sign In']");
    public final By loginBtn = By.cssSelector("button.submit-btn");
    public final By errMsg = By.cssSelector(".error-banner");

    public AdminRegister(WebDriver driver) {
        super(driver);
    }

    public boolean isHospitalSelectVisible() { return isElementVisible(hospitalSelect); }
    public boolean isFirstNameVisible() { return isElementVisible(firstName); }
    public boolean isLastNameVisible() { return isElementVisible(lastName); }
    public boolean isEmailVisible() { return isElementVisible(email); }
    public boolean isPhoneNumberVisible() { return isElementVisible(phoneNumber); }
    public boolean isPasswordVisible() { return isElementVisible(pswd); }
    public boolean isConfirmPasswordVisible() { return isElementVisible(confirmPswd); }
    public boolean isTermsCheckboxVisible() { return isElementVisible(terms); }
    public boolean isSubmitButtonVisible() { return isElementVisible(submit); }
    public boolean isBackToLoginLinkVisible(){ return isElementVisible(backToLogin); }
    public boolean isAdminDashboardVisible(){ return  isElementVisible(AdminDashboard); }
    public boolean isErrMsgVisibile(){ return isElementVisible(errMsg); }


    public void enterHospital(String value) {selectByText(hospitalSelect,value);}
    public void enterFirstName(String value) { type(firstName, value); }
    public void enterLastName(String value) { type(lastName, value); }
    public void enterEmail(String value) { type(email, value); }
    public void enterPhoneNumber(String value) { type(phoneNumber, value); }
    public void enterPassword(String value) { type(pswd, value); }
    public void enterConfirmPassword(String value) { type(confirmPswd, value); }
    public void clickTerms() { click(terms); }
    public void clickSubmit() { click(submit); }

    public void enterEmailLogin(String value){
        wait.until(ExpectedConditions.visibilityOfElementLocated(adminLogin));
        type(emailLogin,value);
    }
    public void enterPasswordLogin(String value){ type(pswdLogin,value);}
    public void submitLogin(){ click(loginBtn); }


    public void loadContents(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(AdminDashboard));
    }
    public String getUrl(){ return driver.getCurrentUrl(); }
    public String getTitle(){ return driver.getTitle();}


}
