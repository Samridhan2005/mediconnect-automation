package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Admin Multi-Hospital Management — /admin/{userId}/hospitals */
public class AdminHospitals extends BasePage {

    public final By pageHeader        = By.xpath("//*[contains(normalize-space(),'Multi-Hospital Management')]");
    public final By searchInput       = By.cssSelector("input[placeholder*='Search hospital' i]");
    public final By hospitalsTable    = By.cssSelector("table");
    public final By statusBadges      = By.cssSelector("[class*='status-badge'], [class*='badge']");
    public final By occupancyCells    = By.cssSelector("td[class*='occupancy']");
    public final By addHospitalButton = By.xpath("//button[contains(normalize-space(),'Add Hospital')]");
    public final By hospitalNameCells = By.xpath("//table//tr/td[1]");
    public final By totalbedcount=By.xpath("//table/tbody/tr/td[3]");
    public final By availbedcount=By.xpath("//table/tbody/tr/td[4]");
    // Add-Hospital modal — identified by its close button (class btn-ghost btn-sm).
    // The modal itself has no class="modal"; it uses inline styles only.
    // We match by class combination because the close-icon character (×) may vary
    // across browsers/encodings.
    public final By addHospitalModalClose = By.xpath("//button[contains(@class,'btn-ghost') and contains(@class,'btn-sm')]");

    public AdminHospitals(WebDriver driver) {
        super(driver);
    }

    public AdminHospitals open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/hospitals");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() { return driver.getCurrentUrl().contains("/hospitals"); }

    public boolean isAddHospitalButtonVisible() { return isDisplayed(addHospitalButton); }
    public void clickAddHospital() { click(addHospitalButton); }
    public boolean isAddHospitalModalOpen() { return isDisplayed(addHospitalModalClose); }

    // Add Hospital modal form fields (label-based locators)
    public final By modalHospitalName    = By.xpath("//label[contains(normalize-space(),'Hospital Name')]/following-sibling::input");
    public final By modalCity            = By.xpath("//label[normalize-space()='City']/following-sibling::input");
    public final By modalPhone           = By.xpath("//label[normalize-space()='Phone']/following-sibling::input");
    public final By modalAddress         = By.xpath("//label[normalize-space()='Address']/following-sibling::input");
    public final By modalTotalBeds       = By.xpath("//label[normalize-space()='Total Beds']/following-sibling::input");
    public final By modalAvailableBeds   = By.xpath("//label[normalize-space()='Available Beds']/following-sibling::input");
    // Disambiguated from the outer page "+ Add Hospital" button by anchoring on the
    // modal's Cancel button (which only exists inside the Add Hospital modal).
    public final By modalCancel          = By.xpath("//button[normalize-space()='Cancel']");
    public final By modalSubmit          = By.xpath("//button[normalize-space()='Cancel']/following-sibling::button[normalize-space()='Add Hospital']");

    public AdminHospitals fillAddHospitalForm(String name, String city, String phone,
                                              String address, String totalBeds, String availableBeds) {
        type(modalHospitalName, name);
        type(modalCity, city);
        type(modalPhone, phone);
        type(modalAddress, address);
        type(modalTotalBeds, totalBeds);
        type(modalAvailableBeds, availableBeds);
        return this;
    }

    public void submitAddHospital() { click(modalSubmit); }

    // --- Edit / Details (per-row) ---

    public final By editModalHeading      = By.xpath("//*[normalize-space()='Edit Hospital']");
    // Save Changes button — disambiguated by sibling-of-Cancel, same pattern as Add Hospital submit
    public final By modalSaveChanges      = By.xpath("//button[normalize-space()='Cancel']/following-sibling::button[normalize-space()='Save Changes']");
    // Expanded view header that appears below the table after clicking Details
    public final By expandedView          = By.xpath("//*[contains(normalize-space(),'Expanded view')]");

    public By editButtonFor(String hospitalName) {
        return By.xpath("//table//tr[td[normalize-space()='" + hospitalName + "']]//button[normalize-space()='Edit']");
    }

    public By detailsButtonFor(String hospitalName) {
        return By.xpath("//table//tr[td[normalize-space()='" + hospitalName + "']]//button[normalize-space()='Details']");
    }

    public String getHospitalName(){
        return driver.findElement(By.xpath("//table/tbody/tr[4]/td")).getText();
    }

    public void clickEditFor(String hospitalName)    { click(editButtonFor(hospitalName)); }
    public void clickDetailsFor(String hospitalName) { click(detailsButtonFor(hospitalName)); }
    public boolean isEditModalOpen()                 { return isDisplayed(editModalHeading); }
    public boolean isExpandedViewVisible()           { return isDisplayed(expandedView); }
    public void clickSaveChanges()                   { click(modalSaveChanges); }

    public AdminSidebar sidebar() { return new AdminSidebar(driver); }
}
