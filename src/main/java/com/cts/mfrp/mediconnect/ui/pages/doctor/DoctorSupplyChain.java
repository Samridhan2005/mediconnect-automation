package com.cts.mfrp.mediconnect.ui.pages.doctor;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.DoctorSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Doctor Supply Chain page — /doctor/{userId}/supply-chain */
public class DoctorSupplyChain extends BasePage {

    public final By pageHeader     = By.xpath("//*[normalize-space()='Supply Chain']");
    public final By addItemBtn     = By.xpath("//*[contains(text(),'+ Add Item')]");
    public final By lowStockFilter = By.xpath("//*[contains(normalize-space(),'Low stock')]");
    public final By inventoryTable = By.cssSelector("table");
    public final By reorderBtn     = By.cssSelector("[class*='reorder-icon'], button[title='Reorder']");

    public DoctorSupplyChain(WebDriver driver) {
        super(driver);
    }

    public DoctorSupplyChain open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/doctor/" + userId + "/supply-chain");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/supply-chain"); }

    public DoctorSidebar sidebar() {
        return new DoctorSidebar(driver);
    }
}
