package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.Home;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Home / landing page tests.
 * Verifies that both admin entry points on "/" navigate to /admin/login.
 *   - "Admin" link in the top-right navbar
 *   - "Admin Portal" button in the hero section
 */
public class HomePageTest extends UiBaseTest {

    // Both admin entry points should be present on the landing page.
    @Test
    public void TH001_admin_entry_points_are_visible() {
        Home home = new Home(driver);
        assertTrue(home.isAdminNavLinkVisible(),
                "'Admin' link should be visible in the top-right navbar");
        assertTrue(home.isAdminPortalButtonVisible(),
                "'Admin Portal' button should be visible in the hero section");
    }

    // Clicking the navbar "Admin" link must land on /admin/login.
    @Test
    public void TH002_admin_nav_link_navigates_to_admin_login() {
        Home home = new Home(driver);
        home.clickAdminNavLink();

        wait.until(d -> d.getCurrentUrl().contains(AdminLogin.PATH));
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "Clicking 'Admin' should navigate to " + AdminLogin.PATH);

        AdminLogin adminLogin = new AdminLogin(driver);
        assertTrue(adminLogin.isLoginButtonVisible(),
                "Admin Sign In form should be rendered after clicking 'Admin'");
    }

    // Clicking the hero "Admin Portal" button must land on /admin/login.
    @Test
    public void TH003_admin_portal_button_navigates_to_admin_login() {
        Home home = new Home(driver);
        home.clickAdminPortalButton();

        wait.until(d -> d.getCurrentUrl().contains(AdminLogin.PATH));
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "Clicking 'Admin Portal' should navigate to " + AdminLogin.PATH);

        AdminLogin adminLogin = new AdminLogin(driver);
        assertTrue(adminLogin.isLoginButtonVisible(),
                "Admin Sign In form should be rendered after clicking 'Admin Portal'");
    }
}
