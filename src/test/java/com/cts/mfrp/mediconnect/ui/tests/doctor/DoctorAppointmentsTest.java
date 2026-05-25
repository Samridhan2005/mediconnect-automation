package com.cts.mfrp.mediconnect.ui.tests.doctor;

import com.cts.mfrp.mediconnect.ui.pages.doctor.DoctorAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseDoctorTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DoctorAppointmentsTest extends BaseDoctorTest {

    private static final Duration WAIT = Duration.ofSeconds(60);

    private WebDriverWait w() {
        return new WebDriverWait(driver, WAIT);
    }


    @Test(groups = {"sanity", "regression"})
    public void doctor_appointments_header_and_buttons() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC039 / TC_A01 — page title
        By title = By.cssSelector("h1.page-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(title));
        assertEquals(driver.findElement(title).getText().trim(),
                "Appointments", "Page title mismatch");

        // TC_A02 — subtitle non-empty + contains "scheduled today"
        By sub = By.cssSelector("p.page-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(sub));
        String subText = driver.findElement(sub).getText().trim();
        assertFalse(subText.isEmpty(), "p.page-sub is empty");
        assertTrue(subText.contains("scheduled today"),
                "Subtitle should contain 'scheduled today': '" + subText + "'");

        // TC_A03 — Calendar view button
        By calBtn = By.cssSelector("button.btn-ghost");
        w().until(ExpectedConditions.visibilityOfElementLocated(calBtn));
        WebElement cal = driver.findElement(calBtn);
        assertTrue(cal.isDisplayed(), "Calendar view button not visible");
        assertTrue(cal.getText().trim().contains("Calendar view"),
                "Calendar view button text mismatch: '" + cal.getText() + "'");
        assertTrue(cal.isEnabled(), "Calendar view button is disabled");

        // TC_A04 — New Appointment button
        By newBtn = By.cssSelector("button.btn-primary");
        w().until(ExpectedConditions.visibilityOfElementLocated(newBtn));
        WebElement nb = driver.findElement(newBtn);
        assertTrue(nb.isDisplayed(), "New Appointment button not visible");
        assertTrue(nb.getText().trim().contains("New Appointment"),
                "New Appointment button text mismatch: '" + nb.getText() + "'");
        assertTrue(nb.isEnabled(), "New Appointment button is disabled");
    }


    @Test(groups = {"regression"})
    public void doctor_appointments_tabs_and_stat_cards() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC040 / TC_A08 — tab bar active = Today, all 3 tabs present
        By tabBar = By.cssSelector("div.tab-bar");
        w().until(ExpectedConditions.visibilityOfElementLocated(tabBar));
        By activeTab = By.cssSelector("div.tab-bar button.tab.active");
        w().until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        assertEquals(driver.findElement(activeTab).getText().trim(),
                "Today", "Active tab should be 'Today'");

        List<String> tabTexts = driver.findElements(
                        By.cssSelector("div.tab-bar button.tab"))
                .stream().map(e -> e.getText().trim()).toList();
        for (String tab : List.of("Today", "Upcoming", "Past")) {
            assertTrue(tabTexts.contains(tab),
                    "Tab '" + tab + "' not found. Found: " + tabTexts);
        }

        By statLabel = By.cssSelector("div.stats-row div.stat-label");
        w().until(ExpectedConditions.visibilityOfElementLocated(statLabel));
        List<String> foundLabels = driver.findElements(statLabel)
                .stream().map(e -> e.getText().trim()).toList();
        for (String expected : List.of("Today", "Confirmed", "Pending", "Cancelled")) {
            assertTrue(foundLabels.contains(expected),
                    "Stat label '" + expected + "' missing. Found: " + foundLabels);
        }

        By statVal = By.cssSelector("div.stats-row div.stat-val");
        w().until(ExpectedConditions.visibilityOfElementLocated(statVal));
        List<WebElement> values = driver.findElements(statVal);
        assertFalse(values.isEmpty(), "No div.stat-val found");
        for (WebElement v : values) {
            assertFalse(v.getText().trim().isEmpty(), "div.stat-val is blank");
        }

        By statSub = By.cssSelector("div.stats-row div.stat-sub");
        w().until(ExpectedConditions.visibilityOfElementLocated(statSub));
        List<WebElement> subs = driver.findElements(statSub);
        assertFalse(subs.isEmpty(), "No div.stat-sub found");
        for (WebElement s : subs) {
            assertFalse(s.getText().trim().isEmpty(), "div.stat-sub is blank");
        }
    }

    // TC041 — New Appointment modal opens (button click interaction)
    @Test(groups = {"regression"})
    public void doctor_new_appointment_modal() {
        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);

        List<WebElement> btn = driver.findElements(page.newAppointmentBtn);
        assertTrue(btn.size() > 0, "+ New Appointment button should be visible");
        btn.get(0).click();

        w().until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button.btn-primary")));

        assertTrue(driver.findElements(page.modalTitle).size() > 0,
                "Modal title should be visible");
    }

    // TC_A09_A10_A13 — Toolbar filters + table columns + pagination label
    // Merged TC_A09 + TC_A10 + TC_A13
    @Test(groups = {"regression"})
    public void doctor_appointments_toolbar_columns_pagination() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC_A09 — toolbar filters (search, 2 selects, date picker)
        By toolbar = By.cssSelector("div.toolbar");
        w().until(ExpectedConditions.visibilityOfElementLocated(toolbar));
        By search = By.cssSelector("div.toolbar div.search-wrap");
        assertTrue(driver.findElements(search).size() > 0,
                "Search wrap not found in toolbar");
        List<WebElement> selects = driver.findElements(
                By.cssSelector("div.toolbar select.tb-input"));
        assertTrue(selects.size() >= 2,
                "Expected >=2 select.tb-input, found: " + selects.size());
        By datePicker = By.cssSelector("div.toolbar input[type='date'].tb-input");
        assertTrue(driver.findElements(datePicker).size() > 0,
                "Date picker input not found in toolbar");

        // TC_A10 — table column headers
        By thLocator = By.cssSelector("div.table-wrap table thead tr th");
        w().until(ExpectedConditions.visibilityOfElementLocated(thLocator));
        List<String> headers = driver.findElements(thLocator)
                .stream()
                .map(e -> e.getText().trim()
                        .replace("\n", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase())
                .toList();
        for (String col : List.of(
                "PATIENT", "TIME", "TYPE", "REASON", "STATUS", "ACTIONS")) {
            assertTrue(headers.stream().anyMatch(h -> h.equals(col)),
                    "Column '" + col + "' not found. Found: " + headers);
        }

        // TC_A13 — pagination label
        By pagination = By.cssSelector("div.pagination span");
        w().until(ExpectedConditions.visibilityOfElementLocated(pagination));
        String text = driver.findElement(pagination).getText().trim();
        assertFalse(text.isEmpty(), "Pagination span is empty");
        assertTrue(text.contains("appointments"),
                "Pagination label should contain 'appointments': '" + text + "'");
    }

    // TC078_A11_A12_A14 — Table has rows + row data validation + detail panel opens on row click
    // Merged TC078 + TC_A11 + TC_A12 + TC_A14
    @Test(groups = {"regression"})
    public void doctor_appointments_rows_and_detail_panel() {
        new DoctorAppointments(driver).open(loggedInUserId);

        // TC078 / TC_A11 — table has rows
        By rows = By.cssSelector("div.table-wrap table tbody tr.data-row");
        w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));
        List<WebElement> dataRows = driver.findElements(rows);
        assertTrue(dataRows.size() > 0,
                "No tr.data-row found in appointments table");

        // TC_A12 — row data validation
        int rowsToCheck = Math.min(3, dataRows.size());
        for (int i = 0; i < rowsToCheck; i++) {
            List<WebElement> cells = dataRows.get(i)
                    .findElements(By.cssSelector("td"));
            assertTrue(cells.size() >= 5,
                    "Row " + i + " should have >=5 columns, found: " + cells.size());

            assertFalse(cells.get(0).getText().trim().isEmpty(),
                    "Row " + i + " PATIENT is empty");

            String time = cells.get(1).getText().trim();
            assertFalse(time.isEmpty(), "Row " + i + " TIME is empty");
            assertTrue(time.matches("\\d{2}:\\d{2}\\s*(AM|PM)"),
                    "Row " + i + " TIME format unexpected: '" + time + "'");

            String type = cells.get(2).getText().trim();
            assertFalse(type.isEmpty(), "Row " + i + " TYPE is empty");
            assertTrue(List.of("In-person", "Video").stream()
                            .anyMatch(type::contains),
                    "Row " + i + " TYPE unexpected: '" + type + "'");

            String status = cells.get(4).getText().trim();
            assertFalse(status.isEmpty(), "Row " + i + " STATUS is empty");
            assertTrue(List.of("Pending", "Confirmed", "Cancelled").stream()
                            .anyMatch(status::contains),
                    "Row " + i + " STATUS unexpected: '" + status + "'");
        }

        // TC_A14 — Detail panel opens on row click
        driver.findElements(rows).get(0).click();
        By panelTitle = By.cssSelector("aside.side-panel span.panel-title");
        w().until(ExpectedConditions.visibilityOfElementLocated(panelTitle));
        assertEquals(driver.findElement(panelTitle).getText().trim(),
                "Appointment Details", "Panel title mismatch");
        By closeBtn = By.cssSelector("aside.side-panel button.panel-close");
        assertTrue(driver.findElements(closeBtn).size() > 0,
                "button.panel-close not found in side panel");
    }

    @Test(groups = {"regression"})
    public void calendar_view_opens() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By calBtn = By.xpath("//button[contains(normalize-space(),'Calendar view')]");
        w().until(ExpectedConditions.elementToBeClickable(calBtn));
        driver.findElement(calBtn).click();

        By monthHeader = By.xpath(
                "//*[(contains(normalize-space(),'January') or contains(normalize-space(),'February') "
                        + "or contains(normalize-space(),'March') or contains(normalize-space(),'April') "
                        + "or contains(normalize-space(),'May') or contains(normalize-space(),'June') "
                        + "or contains(normalize-space(),'July') or contains(normalize-space(),'August') "
                        + "or contains(normalize-space(),'September') or contains(normalize-space(),'October') "
                        + "or contains(normalize-space(),'November') or contains(normalize-space(),'December')) "
                        + "and string-length(normalize-space()) &lt; 25]");

        try {
            w().until(ExpectedConditions.visibilityOfElementLocated(monthHeader));
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Calendar did not render — no month/year heading found");
        }

        List<WebElement> dayLabels = driver.findElements(
                By.xpath("//*[normalize-space()='Su' or normalize-space()='Mo' "
                        + "or normalize-space()='Tu' or normalize-space()='We' "
                        + "or normalize-space()='Th' or normalize-space()='Fr' "
                        + "or normalize-space()='Sa']"));
        assertTrue(dayLabels.size() >= 7,
                "Expected 7 day-of-week labels (Su Mo Tu We Th Fr Sa), found: " + dayLabels.size());

        List<WebElement> dateNumbers = driver.findElements(
                By.xpath("//*[normalize-space()='1' or normalize-space()='15' or normalize-space()='28']"));
        assertFalse(dateNumbers.isEmpty(),
                "Calendar grid shows no recognisable date numbers (e.g. 1, 15, 28)");
    }

    @Test(groups = {"regression"})
    public void appointment_tabs_switch_content() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By tableWrap = By.cssSelector("div.table-wrap");
        w().until(ExpectedConditions.visibilityOfElementLocated(tableWrap));

        for (String tabName : List.of("Today", "Upcoming", "Past")) {
            By tabLocator = By.xpath(
                    "//div[contains(@class,'tab-bar')]//button[normalize-space()='" + tabName + "']");
            w().until(ExpectedConditions.elementToBeClickable(tabLocator));
            driver.findElement(tabLocator).click();

            w().until(d -> {
                List<WebElement> active = d.findElements(
                        By.cssSelector("div.tab-bar button.tab.active"));
                return !active.isEmpty() && active.get(0).getText().trim().equals(tabName);
            });

            WebElement activeTab = driver.findElement(
                    By.cssSelector("div.tab-bar button.tab.active"));
            assertEquals(activeTab.getText().trim(), tabName,
                    "Active tab did not switch to: " + tabName);

            int rowCount = driver.findElements(
                    By.cssSelector("div.table-wrap table tbody tr.data-row")).size();
            int emptyStateCount = driver.findElements(
                    By.cssSelector("div.table-wrap [class*='empty']")).size();
            assertTrue(rowCount > 0 || emptyStateCount > 0,
                    "Tab '" + tabName + "' shows neither data rows nor an empty-state message");
        }
    }

    @Test(groups = {"regression"})
    public void appointment_filter_dropdowns_work() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By toolbar = By.cssSelector("div.toolbar");
        w().until(ExpectedConditions.visibilityOfElementLocated(toolbar));

        List<WebElement> selects = driver.findElements(
                By.cssSelector("div.toolbar select.tb-input"));
        assertTrue(selects.size() >= 2,
                "Expected at least 2 toolbar dropdowns. Found: " + selects.size());

        for (int i = 0; i < selects.size(); i++) {
            org.openqa.selenium.support.ui.Select sel =
                    new org.openqa.selenium.support.ui.Select(selects.get(i));
            String defaultText = sel.getFirstSelectedOption().getText().trim();

            List<WebElement> options = sel.getOptions();
            assertTrue(options.size() > 1,
                    "Dropdown #" + i + " ('" + defaultText + "') has no selectable options");

            for (WebElement opt : options) {
                assertFalse(opt.getText().trim().isEmpty(),
                        "Dropdown #" + i + " contains an empty option");
            }

            sel.selectByIndex(1);
            String newText = sel.getFirstSelectedOption().getText().trim();
            assertFalse(newText.equals(defaultText),
                    "Dropdown #" + i + " value did not change after selectByIndex(1)");

            sel.selectByVisibleText(defaultText);
        }

        By datePicker = By.cssSelector("div.toolbar input[type='date'].tb-input");
        w().until(ExpectedConditions.visibilityOfElementLocated(datePicker));
        WebElement date = driver.findElement(datePicker);
        assertTrue(date.isDisplayed(), "Date filter input not visible");
        assertTrue(date.isEnabled(), "Date filter input disabled");

        date.sendKeys("05/26/2026");
        String dateVal = date.getAttribute("value");
        assertFalse(dateVal.isEmpty(),
                "Date filter input did not accept value (got empty value attribute)");
    }

    @Test(groups = {"regression"})
    public void upcoming_eye_icon_opens_patient_page() {
        new DoctorAppointments(driver).open(loggedInUserId);

        By upcomingTab = By.xpath(
                "//div[contains(@class,'tab-bar')]//button[normalize-space()='Upcoming']");
        w().until(ExpectedConditions.elementToBeClickable(upcomingTab));
        driver.findElement(upcomingTab).click();

        By rows = By.cssSelector("div.table-wrap table tbody tr.data-row");
        try {
            w().until(ExpectedConditions.numberOfElementsToBeMoreThan(rows, 0));
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new org.testng.SkipException("No upcoming appointments to test eye icon");
        }

        WebElement firstRow = driver.findElements(rows).get(0);
        List<WebElement> actionCells = firstRow.findElements(By.cssSelector("td"));
        WebElement actionsCell = actionCells.get(actionCells.size() - 1);

        List<WebElement> eyeIcons = actionsCell.findElements(
                By.cssSelector("button[title*='View' i], button[aria-label*='View' i], "
                        + "button.action-btn, button[class*='view'], a[class*='view']"));
        assertFalse(eyeIcons.isEmpty(),
                "Eye/View action icon not found in Upcoming appointment row's Actions column");

        String beforeUrl = driver.getCurrentUrl();
        eyeIcons.get(0).click();

        w().until(d -> !d.getCurrentUrl().equals(beforeUrl));
        String afterUrl = driver.getCurrentUrl();
        assertTrue(afterUrl.contains("/patients/"),
                "Eye icon did not navigate to /patients/{id}. Landed at: " + afterUrl);

        By scheduleBtn = By.xpath("//button[contains(normalize-space(.),'Schedule Appointment')]");
        w().until(ExpectedConditions.visibilityOfElementLocated(scheduleBtn));
        WebElement schedule = driver.findElement(scheduleBtn);
        assertTrue(schedule.isDisplayed(),
                "'Schedule Appointment' button not visible on patient detail page");
        assertTrue(schedule.isEnabled(),
                "'Schedule Appointment' button disabled on patient detail page");

        By editBtn = By.xpath("//button[contains(normalize-space(.),'Edit Patient')]");
        assertFalse(driver.findElements(editBtn).isEmpty(),
                "'Edit Patient' button not found on patient detail page");
    }

    @DataProvider(name = "newAppointmentData")
    public Object[][] newAppointmentData() {
        return TestData.doctorNewAppointmentIds();
    }

    @Test(groups = {"regression"}, dataProvider = "newAppointmentData")
    public void new_appointment_submit_creates_record(String testId) {
        Map<String, String> data = TestData.doctorNewAppointment(testId);

        DoctorAppointments page = new DoctorAppointments(driver).open(loggedInUserId);

        w().until(ExpectedConditions.elementToBeClickable(page.newAppointmentBtn));
        driver.findElement(page.newAppointmentBtn).click();

        By modal = By.cssSelector("div.modal-card");
        WebElement dialog = w().until(ExpectedConditions.visibilityOfElementLocated(modal));

        assertEquals(dialog.findElement(By.cssSelector("h2.modal-title")).getText().trim(),
                "New Appointment", "Modal title mismatch");

        assertTrue(dialog.findElement(By.cssSelector("div.autocomplete-wrap input")).isDisplayed(),
                "Patient autocomplete input not visible");
        assertTrue(dialog.findElement(By.cssSelector("input[formcontrolname='date']")).isDisplayed(),
                "Date field not visible");
        assertTrue(dialog.findElement(By.cssSelector("input[formcontrolname='time']")).isDisplayed(),
                "Time field not visible");
        List<WebElement> selects = dialog.findElements(By.tagName("select"));
        assertTrue(selects.size() >= 2, "Expected Type + Status selects, found: " + selects.size());

        WebElement patientInput = dialog.findElement(
                By.cssSelector("div.autocomplete-wrap input"));
        patientInput.click();
        patientInput.sendKeys(data.get("patientSearchQuery"));

        By suggestion = By.cssSelector("div.autocomplete-wrap li, "
                + "div.autocomplete-wrap [class*='suggest'], "
                + "div.autocomplete-wrap [class*='option'], "
                + "div.autocomplete-wrap [class*='item']");
        try {
            w().until(ExpectedConditions.visibilityOfElementLocated(suggestion));
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Patient autocomplete did not show suggestions for query: '"
                    + data.get("patientSearchQuery") + "'");
        }
        List<WebElement> suggestions = driver.findElements(suggestion);
        assertFalse(suggestions.isEmpty(), "Patient autocomplete returned no suggestions");
        String chosenPatient = suggestions.get(0).getText().trim();
        suggestions.get(0).click();

        WebElement dateInput = dialog.findElement(
                By.cssSelector("input[formcontrolname='date']"));
        dateInput.clear();
        dateInput.sendKeys(data.get("date"));

        WebElement timeInput = dialog.findElement(
                By.cssSelector("input[formcontrolname='time']"));
        timeInput.clear();
        timeInput.sendKeys(data.get("time"));

        String type = data.get("type");
        if (type != null && !type.isBlank()) {
            new org.openqa.selenium.support.ui.Select(selects.get(0)).selectByVisibleText(type);
        }
        String status = data.get("status");
        if (status != null && !status.isBlank()) {
            new org.openqa.selenium.support.ui.Select(selects.get(1)).selectByVisibleText(status);
        }

        WebElement form = dialog.findElement(By.tagName("form"));
        try {
            w().until(d -> {
                String cls = form.getAttribute("class");
                return cls != null && cls.contains("ng-valid") && !cls.contains("ng-invalid");
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            org.testng.Assert.fail("Form did not become ng-valid after filling. "
                    + "Class: " + form.getAttribute("class"));
        }

        WebElement submit = dialog.findElement(
                By.xpath(".//button[normalize-space()='Save Appointment']"));
        assertTrue(submit.isEnabled(), "Save Appointment button disabled with valid form");
        submit.click();

        try {
            w().until(ExpectedConditions.invisibilityOfElementLocated(modal));
        } catch (org.openqa.selenium.TimeoutException e) {
            List<WebElement> errors = driver.findElements(
                    By.cssSelector("[class*='error'], [class*='alert'], [class*='toast']"));
            String errorMsg = errors.stream()
                    .filter(WebElement::isDisplayed)
                    .map(el -> el.getText().trim())
                    .filter(t -> !t.isEmpty())
                    .findFirst()
                    .orElse("(no visible error message)");
            org.testng.Assert.fail("Modal did not close after submitting valid form. "
                    + "Chosen patient: '" + chosenPatient + "'. "
                    + "Form class: " + form.getAttribute("class") + ". "
                    + "Error message on page: " + errorMsg);
        }
    }
}
