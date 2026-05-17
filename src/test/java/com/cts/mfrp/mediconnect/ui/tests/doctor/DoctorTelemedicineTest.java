package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorTelemedicine;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC046 — Doctor Telemedicine page. */
public class DoctorTelemedicineTest extends BaseDoctorTest {

    // TC046 — Telemedicine UI + session management
    @Test
    public void TC046_doctor_telemedicine_ui() {
        DoctorTelemedicine page = new DoctorTelemedicine(driver).open(loggedInUserId);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 2 — Page header
        wait.until(ExpectedConditions.presenceOfElementLocated(page.pageHeader));
        assertTrue(driver.findElements(page.pageHeader).size() > 0, "Page header 'Telemedicine' not found");

        // Step 3 — Four summary tiles (sentence case matches actual app)
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),\"Live now\")]")));
        for (String tile : List.of("Live now", "Today's video", "This week", "Avg. duration")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),\"" + tile + "\")]")).size() > 0,
                    "Summary tile missing: " + tile);
        }

        // Step 4 — Live & Upcoming section
        assertTrue(driver.findElements(page.liveSection).size() > 0,
                "'Live & Upcoming' section should be visible");

        // Step 5 — Past sessions table with correct columns
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(normalize-space(),'Past sessions')]")));
        assertTrue(driver.findElements(
                By.xpath("//*[contains(normalize-space(),'Past sessions')]")).size() > 0,
                "'Past sessions' section should be visible");

        // Scroll the <table> element itself into view so Angular renders the thead
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement table = longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", table);
        try { Thread.sleep(1200); } catch (InterruptedException ignored) {}

        // Wait for thead to become visible, then check column headers
        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table thead")));

        for (String col : List.of("Patient", "Date", "Duration", "Reason", "Status")) {
            assertTrue(driver.findElements(
                    By.xpath("//th[contains(normalize-space(),'" + col + "')]")).size() > 0,
                    "Past sessions column missing: " + col);
        }
        // Verify data rows exist in the table body
        assertTrue(driver.findElements(By.cssSelector("tbody tr")).size() > 0,
                "Past sessions table should have data rows");

        // Step 6 — Schedule session button
        assertTrue(driver.findElements(page.scheduleSessionBtn).size() > 0,
                "'+ Schedule session' button should be visible");
    }
}
