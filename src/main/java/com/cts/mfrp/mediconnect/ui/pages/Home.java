package com.cts.mfrp.mediconnect.ui.pages;

import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class Home extends BasePage {

    public static final String PATH = "/";

    private final By heading           = By.tagName("h1");
    private final By adminNavLink      = By.xpath("//button[normalize-space()='Admin']");
    private final By adminPortalBtn    = By.xpath("//button[normalize-space()='Admin Portal']");
    private final By getStartedBtn     = By.xpath("//button[normalize-space()='Get Started']");
    private final By patientDoctorBtn  = By.xpath("//button[normalize-space()='Patient / Doctor']");

    public Home(WebDriver driver) {
        super(driver);
    }

    public Home open() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
        return this;
    }

    public String getHeading()                  { return text(heading); }
    public boolean isAdminNavLinkVisible()      { return isDisplayed(adminNavLink); }
    public boolean isAdminPortalButtonVisible() { return isDisplayed(adminPortalBtn); }
    public boolean isGetStartedButtonVisible()  { return isDisplayed(getStartedBtn); }
    public boolean isPatientDoctorButtonVisible() { return isDisplayed(patientDoctorBtn); }

    public void clickAdminNavLink()      { click(adminNavLink); }
    public void clickAdminPortalButton() { click(adminPortalBtn); }
}
