package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAnalytics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC047, TC048 — Doctor Analytics page. */
public class DoctorAnalyticsTest extends BaseDoctorTest {

    // TC047 — Analytics page UI
    @Test
    public void TC047_doctor_analytics_ui() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for page header before asserting tiles
        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header 'Analytics' not found");

        // Wait for at least the first tile before looping
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),\"Total patients\")]")));
        for (String tile : List.of("Total patients", "Appointments", "Lab tests ordered", "Avg. consult time")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),\"" + tile + "\")]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC048 — Time period filter
    @Test
    public void TC048_doctor_analytics_time_period_filter() {
        DoctorAnalytics page = new DoctorAnalytics(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for page header to confirm page is loaded
        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));

        // Time period filter is a <select class="range-select"> with options: Last 30/7/90 days
        assertTrue(driver.findElements(page.timePeriodSelect).size() > 0,
                "Time period 'range-select' dropdown should be present");
    }
}
