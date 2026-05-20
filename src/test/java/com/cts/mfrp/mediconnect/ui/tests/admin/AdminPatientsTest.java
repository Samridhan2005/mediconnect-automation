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

/**
 * FRD: TC054, TC055, TC080 — Admin Patient Management.
 * URL: /admin/{userId}/patients
 *
 * DOM structure from browser inspection:
 *   div.tb-title              → "Patient Management"
 *   div.tb-sub                → "All patients across all hospitals · May 20, 2026"
 *   button.btn-teal           → "Export CSV"
 *   div.stat.blue             → Total Patients tile
 *   div.stat.red              → Inpatients tile
 *   div.stat.teal             → Outpatients Today tile
 *   div.stat.amber            → Critical Cases tile
 *   div.stat-label            → tile label text
 *   div.stat-val              → tile numeric value
 *   div.stat-sub              → tile sub-label
 *   div.tab-bar               → tab container
 *   div.tab.active            → active tab (Inpatients)
 *   div.tab                   → all tabs
 *   div.card-title            → "Patient List"
 *   table thead th            → PATIENT | EMAIL | BLOOD GROUP | PHONE | STATUS | ACTION
 *   table tbody tr            → patient data rows
 *   div.pagination span       → "Showing X of Y patients"
 *   div.search-wrap           → search input container
 */
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

    // ─────────────────────────────────────────────────────────────────────────
    // TC054 — Patient Management page header visible
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC054_admin_patient_management_header() {
        AdminPatients page = new AdminPatients(driver).open(loggedInUserId);

        // div.tb-title → "Patient Management"
        By titleLocator = By.cssSelector("div.tb-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(titleLocator));

        WebElement title = driver.findElement(titleLocator);
        assertTrue(title.isDisplayed(), "div.tb-title not visible");
        assertEquals(title.getText().trim(), "Patient Management",
                "Page title text mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC055 — All four summary tile labels visible (div.stat-label)
    //         Total Patients | Inpatients | Outpatients Today | Critical Cases
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC055_admin_patient_summary_tile_labels() {
        new AdminPatients(driver).open(loggedInUserId);

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

    // ─────────────────────────────────────────────────────────────────────────
    // TC056 — Tile values are non-empty (div.stat-val)
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC056_admin_patient_tile_values_not_empty() {
        new AdminPatients(driver).open(loggedInUserId);

        By statVal = By.cssSelector("div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));

        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val elements found");

        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC057 — Total Patients tile (div.stat.blue) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC057_admin_patient_total_patients_tile() {
        new AdminPatients(driver).open(loggedInUserId);

        By blue = By.cssSelector("div.stat.blue");
        w().until(ExpectedConditions.visibilityOfElementLocated(blue));
        WebElement tile = driver.findElement(blue);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Total Patients", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "All hospitals", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC058 — Inpatients tile (div.stat.red) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC058_admin_patient_inpatients_tile() {
        new AdminPatients(driver).open(loggedInUserId);

        By red = By.cssSelector("div.stat.red");
        w().until(ExpectedConditions.visibilityOfElementLocated(red));
        WebElement tile = driver.findElement(red);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Inpatients", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "Admitted", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC059 — Outpatients Today tile (div.stat.teal) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC059_admin_patient_outpatients_tile() {
        new AdminPatients(driver).open(loggedInUserId);

        By teal = By.cssSelector("div.stat.teal");
        w().until(ExpectedConditions.visibilityOfElementLocated(teal));
        WebElement tile = driver.findElement(teal);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Outpatients Today", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "OPD visits", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC060 — Critical Cases tile (div.stat.amber) full validation
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC060_admin_patient_critical_cases_tile() {
        new AdminPatients(driver).open(loggedInUserId);

        By amber = By.cssSelector("div.stat.amber");
        w().until(ExpectedConditions.visibilityOfElementLocated(amber));
        WebElement tile = driver.findElement(amber);

        assertEquals(tile.findElement(By.cssSelector("div.stat-label")).getText().trim(),
                "Critical Cases", "Label mismatch");
        assertFalse(tile.findElement(By.cssSelector("div.stat-val")).getText().trim().isEmpty(),
                "Value is empty");
        assertEquals(tile.findElement(By.cssSelector("div.stat-sub")).getText().trim(),
                "ICU / HDU", "Sub-label mismatch");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC061 — Tab bar visible with Inpatients (active) + Outpatients tabs
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC061_admin_patient_tabs_visible() {
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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC062 — Patient List card title visible
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC062_admin_patient_list_card_title() {
        new AdminPatients(driver).open(loggedInUserId);

        By cardTitle = By.cssSelector("div.card-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(cardTitle));

        boolean found = driver.findElements(cardTitle).stream()
                .anyMatch(e -> e.getText().trim().equalsIgnoreCase("Patient List"));
        assertTrue(found, "'Patient List' card title not found");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC063 — Patient table column headers correct
    //         PATIENT | EMAIL | BLOOD GROUP | PHONE | STATUS | ACTION
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC063_admin_patient_table_columns() {
        new AdminPatients(driver).open(loggedInUserId);

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
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC064 — Patient table has data rows
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC064_admin_patient_table_has_rows() {
        new AdminPatients(driver).open(loggedInUserId);

        By rows = By.cssSelector("table tbody tr");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));

        assertTrue(driver.findElements(rows).size() > 0,
                "Patient table has no data rows");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC065 — Pagination label shows "Showing X of Y patients"
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC065_admin_patient_pagination_label() {
        new AdminPatients(driver).open(loggedInUserId);

        By paginationSpan = By.cssSelector("div.pagination span");
        w().until(ExpectedConditions.visibilityOfElementLocated(paginationSpan));

        String text = driver.findElement(paginationSpan).getText().trim();
        assertTrue(text.matches("Showing \\d+ of \\d+ patients"),
                "Pagination label format unexpected: '" + text + "'");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC066 — Search input is present
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC066_admin_patient_search_present() {
        new AdminPatients(driver).open(loggedInUserId);

        By searchWrap = By.cssSelector("div.search-wrap");
        w().until(ExpectedConditions.visibilityOfElementLocated(searchWrap));
        assertTrue(driver.findElement(searchWrap).isDisplayed(),
                "Search input container not visible");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC080 — Export CSV button present and enabled
    // ─────────────────────────────────────────────────────────────────────────
    @Test
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

        By exportBtn = By.cssSelector("button.btn-teal");
        w().until(ExpectedConditions.visibilityOfElementLocated(exportBtn));

        WebElement btn = driver.findElement(exportBtn);
        assertTrue(btn.isDisplayed(), "Export CSV button not visible");
        assertTrue(btn.isEnabled(), "Export CSV button is disabled");
        assertTrue(btn.getText().trim().contains("Export CSV"),
                "Export button text mismatch: '" + btn.getText() + "'");
    }
}
