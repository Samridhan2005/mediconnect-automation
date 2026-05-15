package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor AI Assistant page — /doctor/{userId}/ai-assistant */
public class DoctorAiAssistant extends BasePage {

    public final By pageHeader  = By.xpath("//*[contains(normalize-space(),'AI Assistant')]");
    public final By chips       = By.cssSelector("[class*='chip']");
    public final By chatInput   = By.cssSelector("textarea, input[placeholder*='Ask' i]");
    public final By chatBubbles = By.cssSelector("[class*='chat-bubble'], [class*='message']");

    public DoctorAiAssistant(WebDriver driver) {
        super(driver);
    }

    public DoctorAiAssistant open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/ai-assistant");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/ai-assistant"); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
