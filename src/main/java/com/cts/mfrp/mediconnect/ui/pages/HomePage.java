package com.cts.mfrp.mediconnect.ui.pages;

import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Landing / Home page at the root URL "/".
 *
 * Updated for the new deployment which has:
 *   Header CTAs : "Admin Portal" (btn-ghost-nav), "Get Started" (btn-pill)
 *   Hero CTAs   : "Get Started Free" (btn-hero-primary), "Admin Portal" (btn-hero-outline)
 *   CTA section : "Get Started" (btn-cta-primary), "Contact Us" (btn-cta-secondary)
 */
public class HomePage extends BasePage {

    public final By heroHeading             = By.tagName("h1");

    // Header
    public final By headerGetStartedBtn     = By.cssSelector("button.btn-pill");
    public final By headerAdminPortalBtn    = By.cssSelector("button.btn-ghost-nav");
    public final By backLink = By.cssSelector("a.back-link");
    // Hero section
    public final By heroGetStartedFreeBtn   = By.cssSelector("button.btn-hero-primary");
    public final By heroAdminPortalBtn      = By.cssSelector("button.btn-hero-outline");

    // CTA section (bottom)
    public final By ctaGetStartedBtn        = By.cssSelector("button.btn-cta-primary");
    public final By ctaContactUsBtn         = By.cssSelector("button.btn-cta-secondary");

    // Footer
    public final By footerPatientPortalLink = By.xpath("//a[normalize-space()='Patient Portal']");
    public final By footerDoctorPortalLink  = By.xpath("//a[normalize-space()='Doctor Portal']");
    public final By footerAdminPortalLink   = By.xpath("//a[normalize-space()='Admin Portal']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(ConfigReader.get("ui.baseUrl"));
        return this;
    }

    public boolean isLoaded() {
        // Landing page is reachable when its primary hero CTAs are on screen
        return isDisplayed(heroGetStartedFreeBtn) || isDisplayed(heroAdminPortalBtn);
    }

    public String getHeroHeading()             { return text(heroHeading); }

    public boolean isHeaderGetStartedVisible()    { return isDisplayed(headerGetStartedBtn); }
    public boolean isHeaderAdminPortalVisible()   { return isDisplayed(headerAdminPortalBtn); }
    public boolean isHeroGetStartedFreeVisible()  { return isDisplayed(heroGetStartedFreeBtn); }
    public boolean isHeroAdminPortalVisible()     { return isDisplayed(heroAdminPortalBtn); }
    public boolean isCtaGetStartedVisible()       { return isDisplayed(ctaGetStartedBtn); }
    public boolean isCtaContactUsVisible()        { return isDisplayed(ctaContactUsBtn); }

    public void clickHeaderGetStarted()    { click(headerGetStartedBtn); }
    public void clickHeaderAdminPortal()   { click(headerAdminPortalBtn); }
    public void clickHeroGetStartedFree()  {
        click(backLink);
        click(heroGetStartedFreeBtn);
    }
    public void clickHeroAdminPortal()     {
        click(backLink);
        click(heroAdminPortalBtn);
    }
    public void clickCtaGetStarted()       { click(ctaGetStartedBtn); }
}
