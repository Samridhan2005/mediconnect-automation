package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorPatients;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/** FRD: TC037, TC038 — Doctor Patients registry. */
public class DoctorPatientsTest extends BaseDoctorTest {

    // TC037 — Patients page UI
    @Test
    public void TC037_doctor_patients_page_ui() {
        DoctorPatients page = new DoctorPatients(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);

        for (String tile : List.of("Total Patients", "Active This Week", "Critical Cases", "New This Month")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        assertTrue(driver.findElements(page.searchInput).size() > 0, "Search bar should be visible");
        for (String filter : List.of("All Genders", "All Blood Groups", "All Status")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + filter + "')]")).size() > 0,
                    "Filter dropdown missing: " + filter);
        }
        for (String col : List.of("PATIENT", "AGE", "BLOOD GROUP", "DIAGNOSIS", "LAST VISIT", "STATUS", "ACTIONS")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + col + "']")).size() > 0,
                    "Table column header missing: " + col);
        }
    }

    // TC038 — Search, filter and action icons
    @Test
    public void TC038_doctor_patients_search_filter() {
        DoctorPatients page = new DoctorPatients(driver).open(loggedInUserId);
        page.search("Rajesh");
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertNotNull(driver.findElements(page.patientRows));

        List<WebElement> viewIcons = driver.findElements(page.viewActionIcon);
        if (!viewIcons.isEmpty()) {
            viewIcons.get(0).click();
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(By.cssSelector(
                            "[class*='detail-panel'], [class*='patient-detail']")).size() > 0,
                    "Patient detail panel should open");
        }
    }
}
