package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.admin.AdminRegister;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import com.cts.mfrp.mediconnect.utils.ExcelUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

/**
 * Tests for the Admin registration page at /admin/register.
 * Reachable via the "Create admin account →" link from /admin/login.
 *
 * NOTE: Locators below are starter guesses — once we have the live DOM
 * we'll tighten them. For now this verifies the page loads and exposes
 * the basic fields you'd expect on a register form.
 */
public class AdminRegisterTest extends UiBaseTest {

    private static final String PATH = "/admin/register";

    private void openRegisterPage() {
        driver.get(ConfigReader.get("ui.baseUrl") + PATH);
        wait.until(d -> d.getCurrentUrl().contains(PATH));
    }

    // Merged admin_register_page_loads + admin_register_page_has_back_to_login_link
    @Test(groups = {"regression"})
    public void admin_register_page_loads_and_back_link() {
        openRegisterPage();
        AdminRegister admin=new AdminRegister(driver);
        assertTrue(admin.getUrl().contains(PATH),"Browser should be on /admin/register");
        assertTrue(admin.getTitle().toLowerCase().contains("mediconnect"),"Tab title should still be MediConnect");
        assertTrue(admin.isBackToLoginLinkVisible(),"There should be a way to navigate back to the admin login from the register page");
    }

    // Merged admin_register_form_has_basic_fields + admin_register_form_has_submit_button
    @DataProvider(name = "AdminRegisData")
    public Object[][] AdminRegisterData() throws IOException {
        return ExcelUtils.getTestData("C:\\Users\\2480114\\Desktop\\trash\\proj\\mediconnect-automation\\src\\test\\resources\\testdata\\TestData.xlsx","AdminRegistration");
    }

    @Test(groups = {"regression"},dataProvider = "AdminRegisData")
    public void admin_register_form_fields_and_submit(String hospital,String fn,String ln,String email,String phno,String pswd,String cpswd) {
        openRegisterPage();
        AdminRegister admin=new AdminRegister(driver);


        assertTrue(admin.isHospitalSelectVisible(), "Hospital dropdown not visible");
        assertTrue(admin.isFirstNameVisible(), "First Name field not visible");
        assertTrue(admin.isLastNameVisible(), "Last Name field not visible");
        assertTrue(admin.isEmailVisible(), "Email field not visible");
        assertTrue(admin.isPhoneNumberVisible(), "Phone Number field not visible");
        assertTrue(admin.isPasswordVisible(), "Password field not visible");
        assertTrue(admin.isConfirmPasswordVisible(), "Confirm Password field not visible");
        assertTrue(admin.isTermsCheckboxVisible(), "Terms checkbox not visible");
        assertTrue(admin.isSubmitButtonVisible(), "Submit button not visible");

        

        admin.enterHospital(hospital);
        admin.enterFirstName(fn);
        admin.enterLastName(ln);
        admin.enterEmail(email);
        admin.enterPhoneNumber(phno);
        admin.enterPassword(pswd);
        admin.enterConfirmPassword(cpswd);
        admin.clickTerms();
        admin.clickSubmit();

        assertTrue(admin.getUrl().matches("\".*/admin/\\\\d+/overview$\""),"Not landed in admin page");
    }
}
