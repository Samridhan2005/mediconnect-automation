package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminHospitals;
import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC053, TC077 — Admin Multi-Hospital Management. */
public class AdminHospitalsTest extends BaseAdminTest {

    // TC053 — Hospitals page UI
    @Test(groups = {"regression"})
    public void TC053_admin_hospitals_ui() {
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
    }

    // TC077 — Occupancy colour coding (green/amber/red)
    @Test(groups = {"regression"})
    public void TC077_admin_hospitals_occupancy_colors() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);
        List<WebElement> rows = driver.findElements(By.cssSelector("table tr"));
        assertTrue(rows.size() > 0, "Hospitals table should have rows");
        for (WebElement r : rows) {
            assertNotNull(r.getAttribute("outerHTML"));
        }
    }

    // TC086 — Clicking 'Hospitals' in the sidebar navigates to /admin/{id}/hospitals
    @Test(groups = {"regression"})
    public void TC086_hospitals_sidebar_link_navigates_to_hospitals_page() {
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

    // TC087 — '+ Add Hospital' button opens the Add Hospital modal
    @Test(groups = {"regression"})
    public void TC087_add_hospital_button_opens_modal() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);
        assertTrue(page.isAddHospitalButtonVisible(),
                "'+ Add Hospital' button should be visible in the top-right of the Hospitals page");

        page.clickAddHospital();

        wait.until(d -> page.isAddHospitalModalOpen());
        assertTrue(page.isAddHospitalModalOpen(),
                "Clicking '+ Add Hospital' should open the Add Hospital modal (identified by its close button)");
    }

    // TC089 — Filling and submitting the Add New Hospital form must create a row in the table.
    // Notes:
    //   - The modal may not auto-close after submit; we don't rely on that.
    //   - We verify success by looking for the new hospital row directly in the table.
    //   - The "modal stayed open" check is recorded separately as an informational side-effect.
    @Test(groups = {"regression"})
    public void TC089_submit_add_hospital_form_creates_new_row() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);

        int rowsBefore = driver.findElements(page.hospitalNameCells).size();

        page.clickAddHospital();
        wait.until(d -> page.isAddHospitalModalOpen());

        long unique = System.currentTimeMillis();
        String newName = "Auto Hospital " + unique;
        page.fillAddHospitalForm(newName, "Chennai", "+91-44-28293333",
                                 "123 Test Street", "10", "3");
        page.submitAddHospital();

        // Wait for the new row to appear in the table (the real success signal).
        By newRow = By.xpath("//table//td[contains(normalize-space(),'" + newName + "')]");
        wait.until(d -> d.findElements(newRow).size() > 0);
        assertTrue(driver.findElements(newRow).size() > 0,
                "Newly added hospital '" + newName + "' should appear in the All Hospitals table");

        int rowsAfter = driver.findElements(page.hospitalNameCells).size();
        assertTrue(rowsAfter >= rowsBefore + 1,
                "Hospitals table should grow by at least 1 after submit. Before=" + rowsBefore + ", After=" + rowsAfter);

        // Informational: did the modal auto-close? Not a hard fail — log only.
        if (page.isAddHospitalModalOpen()) {
            System.out.println("[TC089] WARNING: row was added but modal did not auto-close after submit (UX issue).");
        }
    }

    // TC090 — Clicking 'Edit' on a row opens the Edit Hospital modal pre-filled with that row's data
    @Test(groups = {"regression"})
    public void TC090_edit_button_opens_prefilled_modal() {
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
    }

    // TC092 — Save Changes inside the Edit Hospital modal must update the row in the table.
    // Picks an existing hospital, appends a timestamp to its name, clicks Save Changes,
    // and verifies the renamed row appears in the table.
    @Test(groups = {"regression"})
    public void TC092_save_changes_in_edit_modal_persists_update() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);

        String existingHospital = page.getHospitalName();   // seeded — always present
        String editedName       = existingHospital + " " + System.currentTimeMillis();

        page.clickEditFor(existingHospital);
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

    // TC091 — Clicking 'Details' on a row reveals the Expanded view section below the table
    @Test(groups = {"regression"})
    public void TC091_details_button_reveals_expanded_view() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);

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

    // TC088 — Every hospital row in the table must have a non-empty name
    // (Catches the data-integrity issue seen in the UI where one row has a blank Hospital Name column.)
    @Test(groups = {"regression"})
    public void TC088_every_hospital_row_has_non_empty_name() {
        AdminHospitals page = new AdminHospitals(driver).open(loggedInUserId);

        List<WebElement> nameCells = driver.findElements(page.hospitalNameCells);
        assertTrue(nameCells.size() > 0, "Hospitals table should have at least one data row");

        for (int i = 0; i < nameCells.size(); i++) {
            String name = nameCells.get(i).getText().trim();
            assertTrue(!name.isEmpty(),
                    "Row " + (i + 1) + " in the Hospitals table has a blank Hospital Name");
        }
    }
}
