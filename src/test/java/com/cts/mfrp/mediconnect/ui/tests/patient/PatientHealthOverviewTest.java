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
    @Test(groups = {"smoke", "sanity", "regression"})
    public void TC015_patient_login_lands_on_health_overview() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(dash.isLoaded(), "User should land on Patient Health Overview dashboard");
    }

    // Merged TC017 + TC130 + TC131 + TC132
    @Test(groups = {"regression"})
    public void TC017_130_132_patient_dashboard_header_and_sidebar() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(dash.isLoaded(), "Health Overview should be the default home screen");

        List<String> expected = List.of(
                "Health Overview", "Appointments", "Medical Records", "Lab Reports",
                "Telemedicine", "Medicine Reminders", "AI Health Assistant", "Sign out");
        for (String label : expected) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + label + "']")).size() >= 1,
                    "Sidebar item missing: " + label);
        }

        WebElement activeLink = driver.findElement(dash.sidebar().activeNavLink);
        assertEquals(activeLink.getText().trim(), "Health Overview");

        By greetingLocator = By.cssSelector(".hh-greeting");
        String greeting = wait.until(d -> {
            List<WebElement> els = d.findElements(greetingLocator);
            if (els.isEmpty()) return null;
            String t = els.get(0).getText();
            return (t != null && !t.isBlank()) ? t.toLowerCase() : null;
        });
        assertTrue(greeting.contains("good morning") || greeting.contains("good afternoon")
                        || greeting.contains("good evening"),
                "Top banner should include a time-of-day greeting (was: '" + greeting + "')");

        assertTrue(driver.findElements(dash.healthScoreRing).size() > 0,
                "Health Score circular indicator missing");
        assertTrue(driver.findElements(dash.summaryTiles).size() >= 4,
                "FRD: 4 summary tiles required");

        // TC130 — Page header shows title + current date sub-line
        assertTrue(driver.findElements(dash.pageTitle).size() > 0,
                "Page title 'Health Overview' should be visible");
        assertTrue(driver.findElements(dash.pageSubDate).size() > 0,
                "Page header should display the current date (e.g., 'Wednesday, May 20, 2026')");

        // TC131 — Top-right shows blood group chip and notification bell
        assertTrue(driver.findElements(dash.bloodGroupHeaderChip).size() > 0,
                "Blood group chip (e.g., 'Blood group: O+') should be visible in the top-right");
        assertTrue(driver.findElements(dash.notificationBell).size() > 0,
                "Notification bell should be visible in the top-right");

        // TC132 — Sidebar profile block shows Patient ID and Age
        assertTrue(driver.findElements(dash.sidebarPatientId).size() > 0,
                "Sidebar profile block should display the Patient ID (e.g., 'PT-0052')");
        assertTrue(driver.findElements(dash.sidebarPatientAge).size() > 0,
                "Sidebar profile block should display the patient's age (e.g., 'Age 36')");
    }

    // Merged TC018 + TC133 + TC134
    @Test(groups = {"regression"})
    public void TC018_133_134_patient_summary_tiles_and_banner() {
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        List<WebElement> viewAllLinks = driver.findElements(dash.tileLinks);
        assertTrue(viewAllLinks.size() >= 1, "Each summary tile should have a 'View all' / 'All' link");
        viewAllLinks.get(0).click();
        wait.until(d -> !d.getCurrentUrl().matches(".*/patient/\\d+/dashboard$"));
        assertTrue(!driver.getCurrentUrl().matches(".*/patient/\\d+/dashboard$"),
                "Clicking a tile link should navigate away from the dashboard");

        // TC133 — Top banner has Age/Gender, Blood Group, and Next Appt chips
        assertTrue(driver.findElements(dash.bannerAgeGenderChip).size() > 0,
                "Top banner should display Age + Gender chip");
        assertTrue(driver.findElements(dash.bannerBloodGroupChip).size() > 0,
                "Top banner should display Blood Group chip");
        assertTrue(driver.findElements(dash.bannerNextApptChip).size() > 0,
                "Top banner should display 'Next appt:' chip");

        // TC134 — Each of the 4 summary tiles renders its sub-label
        assertTrue(driver.findElements(dash.tileSubLabelNext).size() > 0,
                "Upcoming Appointments tile should show 'Next: ...' sub-label");
        assertTrue(driver.findElements(dash.tileSubLabelPending).size() > 0,
                "Pending Lab Reports tile should show 'Pending review' sub-label");
        assertTrue(driver.findElements(dash.tileSubLabelOnSchedule).size() > 0,
                "Active Prescriptions tile should show 'All on schedule' sub-label");
        assertTrue(driver.findElements(dash.tileSubLabelUrgent).size() > 0,
                "Unread Notifications tile should show '... urgent' sub-label");
    }

    // Merged TC019 + TC135
    @Test(groups = {"regression"})
    public void TC019_135_upcoming_appointments_and_health_score() {
        WebElement section = wait.until(d -> {
            List<WebElement> sections = d.findElements(By.xpath(
                    "//div[contains(concat(' ',normalize-space(@class),' '),' card ')]" +
                            "[.//*[normalize-space()='Upcoming Appointments']]"));
            for (WebElement el : sections) {
                if (el.isDisplayed()) return el;
            }
            return null;
        });
        assertTrue(section.isDisplayed(), "Upcoming Appointments card should be visible");
        assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'View all')]")).size() >= 1,
                "FRD: 'View all' hyperlink should be visible");

        // TC135 — Health Score ring shows a qualitative label (Good / Fair / Excellent / Poor)
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(driver.findElements(dash.healthScoreRing).size() > 0,
                "Health Score circular ring should be visible");
        assertTrue(driver.findElements(dash.healthScoreLabel).size() > 0,
                "Health Score should display a qualitative label (Good / Fair / Excellent / Poor)");
    }

    // Merged TC020 + TC136 + TC137
    @Test(groups = {"regression"})
    public void TC020_136_137_patient_health_vitals_and_activity() {
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(text(),'Health Vitals') or contains(text(),'Vitals')]")).size() > 0,
                "Health Vitals panel title should be visible");
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(text(),'BP') or contains(text(),'Blood Pressure')]")).size() > 0,
                "Blood Pressure reading should be visible");
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'bpm')]")).size() > 0,
                "Heart Rate value (bpm) should be visible");

        // TC136 — Health Vitals shows all 4 metrics: BP, Heart Rate, Blood Glucose, BMI + Last updated timestamp
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        assertTrue(driver.findElements(dash.vitalsBloodPressure).size() > 0,
                "Health Vitals: Blood Pressure metric should be visible");
        assertTrue(driver.findElements(dash.vitalsHeartRate).size() > 0,
                "Health Vitals: Heart Rate metric should be visible");
        assertTrue(driver.findElements(dash.vitalsBloodGlucose).size() > 0,
                "Health Vitals: Blood Glucose metric should be visible");
        assertTrue(driver.findElements(dash.vitalsBmi).size() > 0,
                "Health Vitals: BMI metric should be visible");
        assertTrue(driver.findElements(dash.vitalsLastUpdated).size() > 0,
                "Health Vitals should show a 'Last updated [date]' timestamp");

        // TC137 — Recent Activity section is rendered
        assertTrue(driver.findElements(dash.recentActivitySection).size() > 0,
                "'Recent Activity' section should be visible on the dashboard");
    }

    // Merged TC021 + TC022 + TC138
    // FIX: contains(text(),...) is too strict — it doesn't traverse children. Use
    // contains(normalize-space(.),...) so a title nested inside a <span> or icon still matches.
    @Test(groups = {"regression"})
    public void TC021_022_138_patient_medicines_notifications_and_ai() {
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(normalize-space(.),\"Today's Medicines\") or contains(normalize-space(.),\"Today’s Medicines\")]")).size() > 0,
                "Today's Medicines panel title should be visible");
        assertTrue(driver.findElements(By.xpath(
                        "//*[contains(normalize-space(.),'Notifications')]")).size() > 0,
                "Notifications panel should be visible");

        // TC022 — AI Health Assistant widget on dashboard
        PatientHealthOverview dash = new PatientHealthOverview(driver);
        // The AI CTA itself counts as the widget — broaden the locator.
        boolean widgetVisible = driver.findElements(dash.aiWidget).size() > 0
                || driver.findElements(dash.openAiAssistantCta).size() > 0;
        assertTrue(widgetVisible, "AI Health Assistant widget / CTA should be visible");

        assertTrue(driver.findElements(dash.aiChipExplainLab).size() > 0,
                "Quick action chip 'Explain my lab report' missing");
        assertTrue(driver.findElements(dash.aiChipSymptomChecker).size() > 0,
                "Quick action chip 'Symptom checker' missing");
        assertTrue(driver.findElements(dash.aiChipBookAppointment).size() > 0,
                "Quick action chip 'Book appointment' missing");

        // TC138 — AI Health Assistant widget shows the 'Online' status badge
        // AI Assistant widget sits below the fold — scroll so Angular renders it before asserting.
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}

        wait.until(d -> d.findElements(dash.aiAssistantTitle).size() > 0);
        assertTrue(driver.findElements(dash.aiAssistantTitle).size() > 0,
                "AI Health Assistant widget title should be visible");
        assertTrue(driver.findElements(dash.aiAssistantOnline).size() > 0,
                "AI Health Assistant widget should display 'Online' status badge");

        driver.findElement(dash.openAiAssistantCta).click();
        wait.until(d -> d.getCurrentUrl().matches(".*/patient/\\d+/ai.*"));
        assertTrue(driver.getCurrentUrl().matches(".*/patient/\\d+/ai.*"),
                "Open AI Assistant should navigate to AI Health Assistant page");
    }

    // Merged TC074 + TC139
    // TC074 — Patient Sign Out
    // The new deployment's patient sidebar may not show a "Sign Out" text link;
    // sign-out may be reachable only via a profile/hamburger menu. Accept either:
    //   a) a direct Sign Out element, OR
    //   b) clicking the hamburger / profile icon exposes a sign-out option.
    // If neither exists the test fails with a clear FRD-vs-UI gap message.
    @Test(groups = {"regression"})
    public void TC074_139_patient_signout_and_no_nulls() {
        // TC139 — BUG-002 regression guard: greeting and sidebar must NOT contain 'null null'
        // (Indicates the patient record's firstName/lastName are null in the database.)
        // Find any element whose visible text contains 'null null' on the dashboard.
        int nullNullCount = driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'null null')]")).size();
        assertTrue(nullNullCount == 0,
                "Greeting / sidebar / profile should never render 'null null'. " +
                "Found " + nullNullCount + " element(s) containing 'null null' — " +
                "indicates the patient record has null firstName/lastName fields (BUG-002).");

        List<WebElement> signOut = driver.findElements(By.xpath(
                "//*[normalize-space()='Sign Out' or normalize-space()='Sign out' " +
                "or normalize-space()='Logout' or normalize-space()='Log out']"));
        if (signOut.isEmpty()) {
            // Try opening the hamburger / profile menu first
            List<WebElement> opener = driver.findElements(By.cssSelector(
                    ".hamburger, [class*='profile'], [class*='avatar']"));
            if (!opener.isEmpty()) {
                opener.get(0).click();
                try { Thread.sleep(700); } catch (InterruptedException ignored) {}
                signOut = driver.findElements(By.xpath(
                        "//*[normalize-space()='Sign Out' or normalize-space()='Sign out' " +
                        "or normalize-space()='Logout' or normalize-space()='Log out']"));
            }
        }
        assertTrue(signOut.size() > 0,
                "FRD: Patient should be able to sign out. No Sign Out / Logout control was found " +
                "directly or in the hamburger/profile menu — possible UI gap.");
        signOut.get(0).click();
        wait.until(d -> d.getCurrentUrl().contains("/login") || d.getCurrentUrl().endsWith("/"));
        assertTrue(driver.getCurrentUrl().contains("/login") || driver.getCurrentUrl().endsWith("/"),
                "User should be redirected to /login or landing page after sign out");
    }
}
