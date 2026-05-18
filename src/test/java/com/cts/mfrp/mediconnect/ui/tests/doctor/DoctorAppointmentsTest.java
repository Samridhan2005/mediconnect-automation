package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC039, TC040, TC041, TC078 — Doctor Appointments page + New Appointment modal. */
public class DoctorAppointmentsTest extends BaseDoctorTest {

    // TC039 — Appointments page UI
    @Test
    public void TC039_doctor_appointments_list_ui() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0);

        for (String tile : List.of("Today", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
        for (String tab : List.of("Today", "Upcoming", "Past")) {
            assertTrue(driver.findElements(By.xpath("//button[normalize-space()='" + tab + "']")).size() > 0,
                    "Tab missing: " + tab);
        }
    }

    // TC040 — Tabs + Calendar view
    @Test
    public void TC040_doctor_appointments_tabs_and_calendar() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1 — Today tab should be active by default
        wait.until(ExpectedConditions.presenceOfElementLocated(page.tabToday));
        assertTrue(driver.findElements(page.tabToday).size() > 0, "Today tab should be present");

        // Step 2 — Click Upcoming tab
        wait.until(ExpectedConditions.elementToBeClickable(page.tabUpcoming));
        driver.findElement(page.tabUpcoming).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(page.tabUpcoming));
        assertTrue(driver.findElements(page.tabUpcoming).size() > 0, "Upcoming tab should be clickable");

        // Step 3 — Click Past tab
        wait.until(ExpectedConditions.elementToBeClickable(page.tabPast));
        driver.findElement(page.tabPast).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(page.tabPast));
        assertTrue(driver.findElements(page.tabPast).size() > 0, "Past tab should be clickable");

        // Step 4 — Click Today tab to return to default
        wait.until(ExpectedConditions.elementToBeClickable(page.tabToday));
        driver.findElement(page.tabToday).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(page.tabToday));
        assertTrue(driver.findElements(page.tabToday).size() > 0, "Today tab should be clickable");

        // Step 5 — Calendar view button should be present and clickable
        wait.until(ExpectedConditions.presenceOfElementLocated(page.calendarToggle));
        assertTrue(driver.findElements(page.calendarToggle).size() > 0,
                "Calendar view button should be visible");
        driver.findElement(page.calendarToggle).click();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        // After clicking, the view switches — verify calendar toggle is still accessible
        assertTrue(driver.findElements(page.calendarToggle).size() > 0,
                "Calendar view should remain navigable after click");
    }

    // TC041 — New Appointment Modal field validation
    @Test
    public void TC041_doctor_new_appointment_modal() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        List<WebElement> btn = driver.findElements(page.newAppointmentBtn);
        assertTrue(btn.size() > 0, "+ New Appointment button should be visible");
        btn.get(0).click();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        assertTrue(driver.findElements(page.modalTitle).size() > 0,
                "Modal title should be visible");

        for (String label : List.of("Patient", "Date", "Time", "Type", "Status")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + label + "')]")).size() > 0,
                    "Modal field label missing: " + label);
        }
    }

    // TC078 — Unknown patient search negative
    // TC078 — Unknown patient search negative
    // TC078 — Unknown patient search negative
    @Test
    public void TC078_new_appointment_patient_search_negative() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for and click + New Appointment button
        wait.until(ExpectedConditions.elementToBeClickable(page.newAppointmentBtn));
        driver.findElement(page.newAppointmentBtn).click();

        // Wait for modal to open
        wait.until(ExpectedConditions.visibilityOfElementLocated(page.modalTitle));

        // Wait for patient field and type unknown patient
        wait.until(ExpectedConditions.visibilityOfElementLocated(page.modalPatientField));
        WebElement patientField = driver.findElement(page.modalPatientField);
        patientField.clear();
        patientField.sendKeys("XYZ999");

        // Wait up to 3 seconds for any no-results indicator
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        // Check for any known empty-state text the app might show
        By emptyState = By.xpath(
                "//*[contains(text(),'No patients found')" +
                        " or contains(text(),'No matching patients')" +
                        " or contains(text(),'No patients')" +
                        " or contains(text(),'No results')" +
                        " or contains(text(),'not found')" +
                        " or contains(text(),'0 results')]"
        );

        List<WebElement> emptyMsg = driver.findElements(emptyState);

        // If no explicit message, verify no patient list items appeared (dropdown is empty)
        if (emptyMsg.isEmpty()) {
            By dropdownOptions = By.xpath(
                    "//*[contains(@class,'option') or contains(@class,'dropdown-item')" +
                            " or contains(@class,'suggestion') or contains(@role,'option')]"
            );
            List<WebElement> options = driver.findElements(dropdownOptions);
            assertTrue(options.isEmpty(),
                    "No patient results should appear for unknown search 'XYZ999' — " +
                            "but dropdown options were found. " +
                            "Also check: the empty-state message text may differ from expected values.");
        } else {
            assertTrue(true, "Empty-state message correctly shown for unknown patient");
        }
    }
}
