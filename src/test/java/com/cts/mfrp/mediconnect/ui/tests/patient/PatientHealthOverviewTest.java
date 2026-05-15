package com.cts.mfrp.mediconnect.ui.tests.patient;

import com.cts.mfrp.mediconnect.ui.pages.patient.PatientHealthOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BasePatientTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * FRD: TC015, TC017, TC018, TC019, TC020, TC021, TC022, TC074.
 * Patient Health Overview dashboard validations.
 */
public class PatientHealthOverviewTest extends BasePatientTest {

    // TC015 — Patient login lands on Health Overview
    @Test
    public void TC015_patient_login_lands_on_health_overview() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(dash.isLoaded(), "User should land on Patient Health Overview dashboard");
    }

    // TC017 — Patient Dashboard UI validation
    @Test
    public void TC017_patient_dashboard_ui_validation() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(dash.isLoaded(), "Health Overview should be the default home screen");

        List<String> expected = List.of(
                "Health Overview", "Appointments", "Medical Records", "Lab Reports",
                "Telemedicine", "Medicine Reminders", "AI Health Assistant", "Sign Out");
        for (String label : expected) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + label + "']")).size() >= 1,
                    "Sidebar item missing: " + label);
        }

        WebElement activeLink = driver.findElement(dash.sidebar().activeNavLink);
        assertEquals(activeLink.getText().trim(), "Health Overview");

        String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        assertTrue(bodyText.contains("good morning") || bodyText.contains("good afternoon")
                        || bodyText.contains("good evening"),
                "Top banner should include a time-of-day greeting");

        assertTrue(driver.findElements(dash.healthScoreRing).size() > 0,
                "Health Score circular indicator missing");
        assertTrue(driver.findElements(dash.summaryTiles).size() >= 4,
                "FRD: 4 summary tiles required");
    }

    // TC018 — Summary Tiles navigation via "View all" links
    @Test
    public void TC018_summary_tiles_navigation() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        List<WebElement> viewAllLinks = driver.findElements(dash.tileLinks);
        assertTrue(viewAllLinks.size() >= 1, "Each summary tile should have a 'View all' / 'All' link");
        viewAllLinks.get(0).click();
        wait.until(d -> !d.getCurrentUrl().matches(".*/patient/\\d+/dashboard$"));
        assertTrue(!driver.getCurrentUrl().matches(".*/patient/\\d+/dashboard$"),
                "Clicking a tile link should navigate away from the dashboard");
    }

    // TC019 — Upcoming Appointments section on dashboard
    @Test
    public void TC019_upcoming_appointments_section() {
        WebElement section = driver.findElement(By.xpath(
                "//*[contains(normalize-space(),'Upcoming Appointments')]/ancestor::*[contains(@class,'section') or contains(@class,'card') or contains(@class,'panel')][1]"));
        assertTrue(section.isDisplayed());
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'View all')]")).size() >= 1,
                "FRD: 'View all' hyperlink should be visible");
    }

    // TC020 — Health Vitals Panel
    @Test
    public void TC020_health_vitals_panel() {
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(text(),'Health Vitals') or contains(text(),'Vitals')]")).size() > 0,
                "Health Vitals panel title should be visible");
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(text(),'BP') or contains(text(),'Blood Pressure')]")).size() > 0,
                "Blood Pressure reading should be visible");
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'bpm')]")).size() > 0,
                "Heart Rate value (bpm) should be visible");
    }

    // TC021 — Today's Medicines + Notifications panels
    @Test
    public void TC021_medicines_and_notifications_panels() {
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),\"Today's Medicines\")]")).size() > 0,
                "Today's Medicines panel title should be visible");
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'Notifications')]")).size() > 0,
                "Notifications panel should be visible");
    }

    // TC022 — AI Health Assistant widget on dashboard
    @Test
    public void TC022_ai_widget_validation() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(driver.findElements(dash.aiWidget).size() > 0,
                "AI Health Assistant widget should be visible");
        assertTrue(driver.findElements(dash.aiChipExplainLab).size() > 0,
                "Quick action chip 'Explain my lab report' missing");
        assertTrue(driver.findElements(dash.aiChipSymptomChecker).size() > 0,
                "Quick action chip 'Symptom checker' missing");
        assertTrue(driver.findElements(dash.aiChipBookAppointment).size() > 0,
                "Quick action chip 'Book appointment' missing");

        driver.findElement(dash.openAiAssistantCta).click();
        wait.until(d -> d.getCurrentUrl().contains("/ai"));
        assertTrue(driver.getCurrentUrl().contains("/ai"),
                "Open AI Assistant should navigate to AI Health Assistant page");
    }

    // TC074 — Patient Sign Out
    @Test
    public void TC074_patient_sign_out() {
        List<WebElement> signOut = driver.findElements(By.xpath(
                "//*[normalize-space()='Sign Out' or normalize-space()='Sign out']"));
        assertTrue(signOut.size() > 0, "Sign Out link should be available in sidebar");
        signOut.get(0).click();
        wait.until(d -> d.getCurrentUrl().contains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should be redirected to /login after sign out");
    }
}
