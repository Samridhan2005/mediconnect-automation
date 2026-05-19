package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminPatients;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC054, TC055, TC080 — Admin Patient Management. */
public class AdminPatientsTest extends BaseAdminTest {

    // TC054 — Patient Management UI
    // FIX: actual tab text contains a count, e.g. "Inpatients (55)" / "Outpatients (1)".
    // Switched from normalize-space() equality to contains() so the count suffix doesn't break the match.
    @Test(groups = {"sanity", "regression"})
    public void TC054_admin_patient_management_ui() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tab : List.of("Inpatients", "Outpatients")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'" + tab + "')]")).size() > 0,
                    "Tab missing: " + tab);
        }
    }

    // TC055 — AI Summarize button on patient detail
    @Test(groups = {"regression"})
    public void TC055_admin_patient_ai_summarize() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);
        List<WebElement> viewBtn = driver.findElements(page.viewBtn);
        if (!viewBtn.isEmpty()) {
            viewBtn.get(0).click();
            try { Thread.sleep(700); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(page.aiSummarizeBtn).size() > 0,
                    "+ AI Summarize button should be visible");
        }
    }

    // TC080 — Export
    @Test(groups = {"regression"})
    public void TC080_admin_patient_management_export() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.exportBtn).size() > 0,
                "Export button should be visible on Patient Management");
    }
}
