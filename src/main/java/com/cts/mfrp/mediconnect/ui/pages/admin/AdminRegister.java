package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Base64;

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



    public AdminRegister(WebDriver driver) {
        super(driver);
    }
}
