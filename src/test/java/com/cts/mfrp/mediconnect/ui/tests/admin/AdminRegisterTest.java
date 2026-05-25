package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.admin.AdminRegister;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class AdminRegisterTest extends UiBaseTest {

    private static final String PATH = "/admin/register";

    private void openRegisterPage() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(d -> d.getCurrentUrl().contains(PATH));
    }

    @Test(groups = {"regression"})
    public void admin_register_page_loads_and_back_link() {
        openRegisterPage();
        assertTrue(driver.getCurrentUrl().contains(PATH),
                "Browser should be on /admin/register");
        assertTrue(driver.getTitle().toLowerCase().contains("mediconnect"),
                "Tab title should still be MediConnect");
        // Look for any link/button that takes the user back to login
        assertTrue(driver.findElements(By.xpath(
                        "//a[contains(translate(normalize-space(),'BACK TO LOGINSIGNIN','back to loginsignin'),'login') " +
                                "or contains(translate(normalize-space(),'BACK TO LOGINSIGNIN','back to loginsignin'),'sign in')]"
                )).size() > 0
                || driver.findElements(By.cssSelector("a.back-link")).size() > 0,
                "There should be a way to navigate back to the admin login from the register page");
    }

    @Test(groups = {"regression"})
    public void admin_register_form_fields_and_submit() {
        openRegisterPage();
        AdminRegister adr=new AdminRegister(driver);
        assertTrue(driver.findElement(adr.firstName).isDisplayed(),"First name is not available");
        assertTrue(driver.findElement(adr.lastName).isDisplayed(),"Last name is not available");
        assertTrue(driver.findElement(adr.email).isDisplayed(),"Email is not available");
        assertTrue(driver.findElement(adr.hospitalSelect).isDisplayed(),"Hospital select is not available");
        assertTrue(driver.findElement(adr.phoneNumber).isDisplayed(),"Phone number is not available");
        assertTrue(driver.findElement(adr.pswd).isDisplayed(),"Password is not available");
        assertTrue(driver.findElement(adr.confirmPswd).isDisplayed(),"Confirm password is not available");
        assertTrue(driver.findElement(adr.terms).isDisplayed(),"Terms checkbox is not available");
        assertTrue(driver.findElement(adr.submit).isDisplayed(),"Submit button is not available");


    }
}
