package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminHospitals;
import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class AdminHospitalsTest extends BaseAdminTest {

    @Test(groups = {"regression"})
    public void admin_hospitals_ui_and_occupancy() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);
        for (String tile : List.of("Total Hospitals", "Operational", "Under Maintenance", "Critical Capacity")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        for (String col : List.of("HOSPITAL NAME", "CITY", "TOTAL BEDS", "OCCUPANCY", "STATUS", "ACTIONS")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(translate(normalize-space(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
                                    + col + "')]")).size() > 0,
                    "Hospital table column missing: " + col);
        }

        List<WebElement> rows = driver.findElements(By.cssSelector("table tr"));
        assertTrue(rows.size() > 0, "Hospitals table should have rows");
        for (WebElement r : rows) {
            assertNotNull(r.getAttribute("outerHTML"));
        }
    }

    @Test(groups = {"regression"})
    public void hospitals_sidebar_link_navigates_to_hospitals_page() {
        // BaseAdminTest leaves us on /admin/{id}/overview after login
        AdminOverview overview = new AdminOverview(driver);
        assertTrue(overview.isLoaded(), "Should start on Admin Overview");

        overview.sidebar().clickHospitals();

        AdminHospitals page = new AdminHospitals(driver);
        wait.until(d -> page.isLoaded());
        assertTrue(driver.getCurrentUrl().contains("/admin/" + loggedInUserId + "/hospitals"),
                "URL should be /admin/" + loggedInUserId + "/hospitals after clicking Hospitals in sidebar, got: "
                        + driver.getCurrentUrl());
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "'Multi-Hospital Management' header should be visible");
    }

    @DataProvider(name = "hospitalsToAdd")
    public Object[][] hospitalsToAdd() {
        return TestData.hospitalIds();
    }

    @Test(groups = {"regression"}, dataProvider = "hospitalsToAdd")
    public void admin_add_hospital_modal_and_submit(String testId) {
        Map<String, String> data = TestData.hospital(testId);
        String newName = data.get("name"); // includes ${UNIQUE} substitution from the sheet

        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);
        assertTrue(page.isAddHospitalButtonVisible(),
                "[" + testId + "] '+ Add Hospital' button should be visible in the top-right of the Hospitals page");

        page.clickAddHospital();
        wait.until(d -> page.isAddHospitalModalOpen());
        assertTrue(page.isAddHospitalModalOpen(),
                "[" + testId + "] Clicking '+ Add Hospital' should open the Add Hospital modal");

        int rowsBefore = driver.findElements(page.hospitalNameCells).size();

        page.fillAddHospitalForm(
                newName,
                data.get("city"),
                data.get("phone"),
                data.get("address"),
                data.get("totalBeds"),
                data.get("availableBeds"));
        page.submitAddHospital();

        // Wait for the new row to appear in the table (the real success signal).
        By newRow = By.xpath("//table//td[contains(normalize-space(),'" + newName + "')]");
        wait.until(d -> d.findElements(newRow).size() > 0);
        assertTrue(driver.findElements(newRow).size() > 0,
                "[" + testId + "] Newly added hospital '" + newName + "' should appear in the All Hospitals table");

        int rowsAfter = driver.findElements(page.hospitalNameCells).size();
        assertTrue(rowsAfter >= rowsBefore + 1,
                "[" + testId + "] Hospitals table should grow by at least 1 after submit. Before=" + rowsBefore + ", After=" + rowsAfter);

        // Informational: did the modal auto-close? Not a hard fail — log only.
        if (page.isAddHospitalModalOpen()) {
            System.out.println("[" + testId + "] WARNING: row was added but modal did not auto-close after submit (UX issue).");
        }
    }

    @Test(groups = {"regression"})
    public void admin_hospitals_row_data_and_details() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);

        List<WebElement> nameCells = driver.findElements(page.hospitalNameCells);
        assertTrue(nameCells.size() > 0, "Hospitals table should have at least one data row");

        for (int i = 0; i < nameCells.size(); i++) {
            String name = nameCells.get(i).getText().trim();
            assertTrue(!name.isEmpty(),
                    "Row " + (i + 1) + " in the Hospitals table has a blank Hospital Name");
        }
        List<WebElement> totbed=driver.findElements(page.totalbedcount);
        List<WebElement> availbed=driver.findElements(page.availbedcount);

        for(int i=0;i<totbed.size();i++){
            String t1=totbed.get(i).getText();
            int s1=Integer.parseInt(t1);
            assertTrue(s1<1,"total beds cannot be negative");

            String t2=availbed.get(i).getText();
            int s2=Integer.parseInt(t2);
            assertTrue(s2<1,"available beds cannot be negative");

            assertTrue(s1>s2,"Available beds cant be greater than Total beds");

        }

        String existingHospital = page.getHospitalName();
        page.clickDetailsFor(existingHospital);

        wait.until(d -> page.isExpandedViewVisible());
        assertTrue(page.isExpandedViewVisible(),
                "Expanded view section should appear below the table after clicking Details");

        // The expanded section heading should mention the chosen hospital name
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(normalize-space(),'" + existingHospital + "')]" +
                        "[ancestor::*[contains(normalize-space(),'Expanded view')]" +
                        " or following-sibling::*[contains(normalize-space(),'Expanded view')]]")
                ).size() > 0
                || driver.findElements(By.xpath(
                        "//*[contains(normalize-space(),'" + existingHospital + "')]"
                                + "[contains(.,'Expanded view') or following::*[contains(normalize-space(),'Expanded view')][1]]")
                ).size() > 0,
                "Expanded view should reference the selected hospital '" + existingHospital + "'");
    }

    @Test(groups = {"regression"})
    public void admin_hospitals_edit_modal_and_save() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);

        String existingHospital = page.getHospitalName();
        page.clickEditFor(existingHospital);

        wait.until(d -> page.isEditModalOpen());
        assertTrue(page.isEditModalOpen(),
                "Edit Hospital modal should open after clicking Edit on the '" + existingHospital + "' row");

        String preFilledName = driver.findElement(page.modalHospitalName).getAttribute("value");
        assertTrue(existingHospital.equals(preFilledName),
                "Hospital Name field should be pre-filled with '" + existingHospital
                        + "' but contained: '" + preFilledName + "'");

        assertTrue(driver.findElements(page.modalSaveChanges).size() > 0,
                "Modal should expose a 'Save Changes' button as its primary action");

        String existingHospital2 = page.getHospitalName();   // seeded — always present
        String editedName       = existingHospital2 + " " + System.currentTimeMillis();

        page.clickEditFor(existingHospital2);
        wait.until(d -> page.isEditModalOpen());

        WebElement nameField = driver.findElement(page.modalHospitalName);
        nameField.clear();
        nameField.sendKeys(editedName);

        page.clickSaveChanges();

        By renamedRow = By.xpath("//table//td[contains(normalize-space(),'" + editedName + "')]");
        wait.until(d -> d.findElements(renamedRow).size() > 0);
        assertTrue(driver.findElements(renamedRow).size() > 0,
                "After Save Changes, the renamed hospital '" + editedName + "' should appear in the table");

        if (page.isEditModalOpen()) {
            System.out.println("[TC092] WARNING: Save Changes succeeded but Edit modal did not auto-close (UX issue).");
        }
    }
}
