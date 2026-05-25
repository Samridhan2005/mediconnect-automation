package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminPatients;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class AdminPatientsTest extends BaseAdminTest {

    /** 60s for slow Netlify + Railway backend */
    private static final Duration WAIT_LOGIN   = Duration.ofSeconds(60);
    private static final Duration WAIT_CONTENT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT_CONTENT);
    }

    // ── Override loginAsAdmin with 60s wait ───────────────────────────────
    @Override
    @BeforeMethod(dependsOnMethods = "uiSetup")
    public void loginAsAdmin() {
        new AdminLogin(driver).open()
                .enterEmail(ConfigReader.get("admin.email"))
                .enterPassword(ConfigReader.get("admin.password"))
                .submit();

        new WebDriverWait(driver, WAIT_LOGIN)
                .until(d -> d.getCurrentUrl().matches(".*/admin/\\d+/.*"));

        loggedInUserId = Long.parseLong(
                driver.getCurrentUrl().replaceAll(".*/admin/(\\d+)/.*", "$1"));
    }

    @Test(groups = {"sanity", "regression"})
    public void admin_patient_management_ui_and_tiles() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tab : List.of("Inpatients", "Outpatients")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),'" + tab + "')]")).size() > 0,
                    "Tab missing: " + tab);
        }

        // div.tb-title → "Patient Management"
        By titleLocator = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(titleLocator));

        WebElement title = driver.findElement(titleLocator);
        assertTrue(title.isDisplayed(), "div.tb-title not visible");
        assertEquals(title.getText().trim(), "Patient Management",
                "Page title text mismatch");

        By statLabel = By.cssSelector("div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));

        List<String> found = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();

        for (String expected : List.of(
                "Total Patients", "Inpatients", "Outpatients Today", "Critical Cases")) {
            assertTrue(found.contains(expected),
                    "Tile label '" + expected + "' missing. Found: " + found);
        }
    }

    @Test(groups = {"regression"})
    public void admin_patient_ai_summarize() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);
        List<WebElement> viewBtn = driver.findElements(page.viewBtn);
        if (!viewBtn.isEmpty()) {
            viewBtn.get(0).click();
            try { Thread.sleep(700); } catch (InterruptedException ignored) {}
            assertTrue(driver.findElements(page.aiSummarizeBtn).size() > 0,
                    "+ AI Summarize button should be visible");
        }
    }

    @Test(groups = {"regression"})
    public void admin_patient_tile_values() {
        new AdminPatients(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val elements found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }

        By blue = By.cssSelector("div.stat.blue");
        w().until(ExpectedConditions.visibilityOfElementLocated(blue));
        WebElement tile = driver.findElement(blue);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Total Patients", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "All hospitals", "Sub-label mismatch");

        By red = By.cssSelector("div.stat.red");
        w().until(ExpectedConditions.visibilityOfElementLocated(red));
        WebElement tile2 = driver.findElement(red);

        assertEquals(tile2.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Inpatients", "Label mismatch");
        assertFalse(tile2.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile2.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Admitted", "Sub-label mismatch");

        By teal = By.cssSelector("div.stat.teal");
        w().until(ExpectedConditions.visibilityOfElementLocated(teal));
        WebElement tile3 = driver.findElement(teal);

        assertEquals(tile3.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Outpatients Today", "Label mismatch");
        assertFalse(tile3.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile3.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "OPD visits", "Sub-label mismatch");

        By amber = By.cssSelector("div.stat.amber");
        w().until(ExpectedConditions.visibilityOfElementLocated(amber));
        WebElement tile4 = driver.findElement(amber);

        assertEquals(tile4.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Critical Cases", "Label mismatch");
        assertFalse(tile4.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile4.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "ICU / HDU", "Sub-label mismatch");
    }

    @Test(groups = {"regression"})
    public void admin_patient_table_and_pagination() {
        new AdminPatients(driver).open(loggedInUserId);

        By tabBar = By.cssSelector("div.tab-bar");
        w().until(ExpectedConditions.visibilityOfElementLocated(tabBar));

        // Active tab contains "Inpatients"
        By activeTab = By.cssSelector("div.tab-bar div.tab.active");
        w().until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        String activeText = driver.findElement(activeTab).getText().trim();
        assertTrue(activeText.contains("Inpatients"),
                "Active tab should contain 'Inpatients', found: '" + activeText + "'");

        // All tabs text contains both Inpatients and Outpatients
        List<String> tabTexts = driver.findElements(By.cssSelector("div.tab-bar div.tab"))
                .stream().map(e -> e.getText().trim()).toList();
        assertTrue(tabTexts.stream().anyMatch(t -> t.contains("Inpatients")),
                "Inpatients tab not found. Found: " + tabTexts);
        assertTrue(tabTexts.stream().anyMatch(t -> t.contains("Outpatients")),
                "Outpatients tab not found. Found: " + tabTexts);

        By cardTitle = By.cssSelector("div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitle));

        boolean found = driver.findElements(cardTitle).stream()
                .anyMatch(e -> e.getText().trim().equalsIgnoreCase("Patient List"));
        assertTrue(found, "'Patient List' card title not found");

        By thLocator = By.cssSelector("table thead th");
        w().until(ExpectedConditions.visibilityOfElementLocated(thLocator));

        List<String> headers = driver.findElements(thLocator)
                .stream()
                .map(e -> e.getText().trim()
                        .replace("\n", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase())
                .toList();

        for (String col : List.of(
                "PATIENT", "EMAIL", "BLOOD GROUP", "PHONE", "STATUS", "ACTION")) {
            assertTrue(headers.stream().anyMatch(h -> h.equals(col)),
                    "Column '" + col + "' not found. Found: " + headers);
        }

        By rows = By.cssSelector("table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        assertTrue(driver.findElements(rows).size() > 0,
                "Patient table has no data rows");

        By paginationSpan = By.cssSelector("div.pagination span");
        w().until(ExpectedConditions.visibilityOfElementLocated(paginationSpan));

        String text = driver.findElement(paginationSpan).getText().trim();
        assertTrue(text.matches("Showing \\d+ of \\d+ patients"),
                "Pagination label format unexpected: '" + text + "'");

        By searchWrap = By.cssSelector("div.search-wrap");
        w().until(ExpectedConditions.visibilityOfElementLocated(searchWrap));
        assertTrue(driver.findElement(searchWrap).isDisplayed(),
                "Search input container not visible");
    }

    @Test(groups = {"regression"})
    public void admin_patient_management_export() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);

        By exportBtn = By.cssSelector("button.btn-teal");
        w().until(ExpectedConditions.visibilityOfElementLocated(exportBtn));

        WebElement btn = driver.findElement(exportBtn);
        assertTrue(btn.isDisplayed(), "Export CSV button not visible");
        assertTrue(btn.isEnabled(), "Export CSV button is disabled");
        assertTrue(btn.getText().trim().contains("Export CSV"),
                "Export button text mismatch: '" + btn.getText() + "'");
    }


}
