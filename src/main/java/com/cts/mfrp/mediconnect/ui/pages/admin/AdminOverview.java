package com.cts.mfrp.mediconnect.ui.pages.admin;

import com.cts.mfrp.mediconnect.ui.pages.BasePage;
import com.cts.mfrp.mediconnect.ui.pages.common.AdminSidebar;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Admin System Overview — /admin/{userId}/overview */
public class AdminOverview extends BasePage {

    public final By pageHeader        = By.xpath("//div[normalize-space(text())='System Overview']");
    public final By notificationBell  = By.cssSelector("button.notif-btn, button.tb-notif, [class*='notif']");
    public final By hospitalBranchMap = By.xpath("//*[contains(normalize-space(),'Hospital Branch Map') or contains(@class,'branch-map') or contains(@class,'map-container')]");
    public final By totalPatientsHeader = By.xpath("//div[text()='Total Patients']");
    public final By totalPatientsValue = By.xpath("//div[text()='Total Patients']/following-sibling::div");
    public final By totalDoctorsHeader = By.xpath("//div[text()='Total Doctors']");
    public final By totalDoctorsValue = By.xpath("//div[text()='Total Doctors']/following-sibling::div");
    public final By bedOccupancyHeader = By.xpath("//div[text()='Bed Occupancy']");
    public final By bedOccupancyValue = By.xpath("//div[text()='Bed Occupancy']/following-sibling::div");
    public final By revenueHeader = By.xpath("//div[text()='Revenue (Month)']");
    public final By revenueValue = By.xpath("//div[text()='Revenue (Month)']/following-sibling::div");
    public final By doctorOnDutyHeader = By.xpath("//div[text()='Doctors On Duty']");
    public final By doctorOnDutyValue = By.xpath("//div[text()='Doctors On Duty']/following-sibling::div");
    public final By card = By.cssSelector("div.card-title");
    public final By mediconnectHeader = By.xpath("//span[text()='MediConnect']");
    public final By adminControlHeader = By.xpath("//div[text()='Admin Control']");
    public final By overviewDivision = By.xpath("//div[text()='Overview']");
    public final By patientsAndCareDivision = By.xpath("//div[text()='Patients & Care']");
    public final By operationsDivision = By.xpath("//div[text()='Operations']");
    public final By links = By.cssSelector("a.ni");



    public AdminOverview(WebDriver driver) {
        super(driver);
    }

    public AdminOverview open(long userId) {
        driver.get(ConfigReader.get("ui.baseUrl") + "/admin/" + userId + "/overview");
        wait.until(d -> isLoaded());
        return this;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().matches(".*/admin/\\d+/overview$");
    }

    public AdminSidebar sidebar() {
        return new AdminSidebar(driver);
    }






    public void loadContents(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(totalPatientsHeader));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(card));
    }
    public boolean isPageHeaderDisplayed(){ return isElementVisible(pageHeader); }
    public boolean isNotificationBellDisplayed(){ return isElementVisible(notificationBell); }

    public boolean isNotificationBellVisible() { return isElementVisible(notificationBell); }

    public boolean isTotalPatientsHeaderVisible() { return isElementVisible(totalPatientsHeader); }
    public boolean isTotalPatientsValueVisible() { return isElementVisible(totalPatientsValue); }
    public boolean validateTotalPatients(){
        try{
            String txt=driver.findElement(totalPatientsValue).getText().trim();
            int patients=Integer.parseInt(txt);
            return patients > -1;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isTotalDoctorsHeaderVisible() { return isElementVisible(totalDoctorsHeader); }
    public boolean isTotalDoctorsValueVisible() { return isElementVisible(totalDoctorsValue); }
    public boolean validateTotalDoctors(){
        try{
            String txt=driver.findElement(totalDoctorsValue).getText().trim();
            int doctors=Integer.parseInt(txt);
            return doctors > -1;
        }catch (Exception e){
            return false;
        }
    }


    public boolean isBedOccupancyHeaderVisible() { return isElementVisible(bedOccupancyHeader); }
    public boolean isBedOccupancyValueVisible() { return isElementVisible(bedOccupancyValue); }
    public boolean validateBedOccupancy(){
        try{
            String txt=driver.findElement(bedOccupancyValue).getText().trim();
            int occupancy =Integer.parseInt(txt.substring(0,txt.length()-1));
            return occupancy > -1;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isRevenueHeaderVisible() { return isElementVisible(revenueHeader); }
    public boolean isRevenueValueVisible() { return isElementVisible(revenueValue); }
    public boolean validateRevenueValue(){
        try{
            String txt=driver.findElement(revenueValue).getText().trim();
            double occupancy =Double.parseDouble(txt.substring(1,txt.length()-1));
            return occupancy >= 0;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isDoctorOnDutyHeaderVisible() { return isElementVisible(doctorOnDutyHeader); }
    public boolean isDoctorOnDutyValueVisible() { return isElementVisible(doctorOnDutyValue); }
    public boolean validateDoctorsOnDuty(){
        try{
            String txt=driver.findElement(doctorOnDutyValue).getText();
            int doctors =Integer.parseInt(txt);
            return doctors > -1;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isMediConnectHeaderVisible() { return isElementVisible(mediconnectHeader); }
    public boolean isAdminControlHeaderVisible() { return isElementVisible(adminControlHeader); }
    public boolean isOverviewDivisionVisible() { return isElementVisible(overviewDivision); }
    public boolean isPatientsAndCareDivisionVisible() { return isElementVisible(patientsAndCareDivision); }
    public boolean isOperationsDivisionVisible() { return isElementVisible(operationsDivision); }

    public int navLinksCount(){ return driver.findElements(links).size(); }


}
