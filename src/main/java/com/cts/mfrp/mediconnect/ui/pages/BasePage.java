package com.cts.mfrp.mediconnect.ui.pages;

import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void selectByText(By locator,String text){
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        Select s=new Select(driver.findElement(locator));
        s.selectByVisibleText(text);
    }

    /**
     * Robust click that:
     *   1. Waits for the element to be clickable
     *   2. Scrolls it to the centre of the viewport (handles off-screen / below-the-fold elements)
     *   3. Tries a native click
     *   4. Falls back to a JavaScript click if a CSS overlay intercepts the native one
     *
     * Eliminates most ElementClickInterceptedException flakes in parallel runs / variable viewports.
     */
    protected void click(By locator) {
        WebElement el = clickable(locator);
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", el);
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        } catch (Exception ignored) { }
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    protected String text(By locator) {
        return visible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
