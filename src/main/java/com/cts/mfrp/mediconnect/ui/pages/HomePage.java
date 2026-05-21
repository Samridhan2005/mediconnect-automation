package com.cts.mfrp.mediconnect.ui.pages;

import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public void clickHeaderGetStarted()    { safeClick(headerGetStartedBtn); }
    public void clickHeaderAdminPortal()   { safeClick(headerAdminPortalBtn); }
    public void clickHeroGetStartedFree()  { safeClick(heroGetStartedFreeBtn); }
    public void clickHeroAdminPortal()     { safeClick(heroAdminPortalBtn); }
    public void clickCtaGetStarted()       { safeClick(ctaGetStartedBtn); }

    /**
     * Click that survives ElementClickInterceptedException for off-screen / overlapped buttons.
     * Scrolls the target to the centre of the viewport, then attempts a native click;
     * falls back to a JavaScript click if a CSS overlay intercepts the native one.
     */
    private void safeClick(By locator) {
        WebElement el = clickable(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el);
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        try {
            el.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
