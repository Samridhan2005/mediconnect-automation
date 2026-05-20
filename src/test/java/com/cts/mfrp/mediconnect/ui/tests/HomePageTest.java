package com.cts.mfrp.mediconnect.ui.tests;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.HomePage;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Landing / home page tests for the new deployment.
 * The root "/" page has:
 *   - h1 "Intelligent Hospital | Management System"
 *   - Header CTAs: "Admin Portal" + "Get Started"
 *   - Hero CTAs : "Get Started Free" + "Admin Portal"
 *   - CTA       : "Get Started" + "Contact Us"
 *   - Footer    : Patient Portal / Doctor Portal / Admin Portal links
 */
public class HomePageTest extends UiBaseTest {

    // Merged home_url_loads_mediconnect_site + home_hero_heading_visible
    @Test(groups = {"sanity", "regression"})
    public void home_url_loads_and_hero_visible() {
        HomePage home = new HomePage(driver).open();
        assertEquals(driver.getTitle(), "MediConnect", "Tab title should be 'MediConnect'");
        assertTrue(driver.getCurrentUrl().contains(
                ConfigReader.get("ui.baseUrl").replaceAll("/$", "")),
                "Browser should be on the MediConnect site");
        assertTrue(home.isLoaded(), "Landing page should display its primary hero CTAs");

        String heading = home.getHeroHeading();
        assertTrue(heading != null && !heading.isBlank(),
                "Hero <h1> heading should be visible on landing");
        assertTrue(heading.toLowerCase().contains("hospital")
                || heading.toLowerCase().contains("mediconnect"),
                "Hero heading should reference the product. Got: '" + heading + "'");
    }

    // Merged home_shows_header_ctas + home_shows_hero_ctas + home_shows_cta_section_buttons
    @Test(groups = {"regression"})
    public void home_shows_all_ctas() {
        HomePage home = new HomePage(driver).open();
        assertTrue(home.isHeaderAdminPortalVisible(),
                "'Admin Portal' button in header should be visible");
        assertTrue(home.isHeaderGetStartedVisible(),
                "'Get Started' button in header should be visible");
        assertTrue(home.isHeroGetStartedFreeVisible(),
                "'Get Started Free' hero button should be visible");
        assertTrue(home.isHeroAdminPortalVisible(),
                "'Admin Portal' hero button should be visible");
        assertTrue(home.isCtaGetStartedVisible(),
                "'Get Started' CTA section button should be visible");
        assertTrue(home.isCtaContactUsVisible(),
                "'Contact Us' CTA section button should be visible");
    }

    // Merged home_header_get_started_routes_to_login + home_hero_get_started_free_routes_to_login
    @Test(groups = {"regression"})
    public void home_get_started_routes_to_login() {
        HomePage home = new HomePage(driver).open();
        home.clickHeaderGetStarted();
        wait.until(d -> d.getCurrentUrl().contains(Login.PATH));
        assertTrue(driver.getCurrentUrl().contains(Login.PATH),
                "Header 'Get Started' should land user on /login");

        home.clickHeroGetStartedFree();
        wait.until(d -> d.getCurrentUrl().contains(Login.PATH));
        assertTrue(driver.getCurrentUrl().contains(Login.PATH),
                "Hero 'Get Started Free' should land user on /login");
    }

    // Merged home_header_admin_portal_routes_to_admin_login + home_hero_admin_portal_routes_to_admin_login
    @Test(groups = {"regression"})
    public void home_admin_portal_routes_to_admin_login() {
        HomePage home = new HomePage(driver).open();
        home.clickHeaderAdminPortal();
        wait.until(d -> d.getCurrentUrl().contains(AdminLogin.PATH));
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "Header 'Admin Portal' should land user on /admin/login");

        home.clickHeroAdminPortal();
        wait.until(d -> d.getCurrentUrl().contains(AdminLogin.PATH));
        assertTrue(driver.getCurrentUrl().contains(AdminLogin.PATH),
                "Hero 'Admin Portal' should land user on /admin/login");
    }
}
