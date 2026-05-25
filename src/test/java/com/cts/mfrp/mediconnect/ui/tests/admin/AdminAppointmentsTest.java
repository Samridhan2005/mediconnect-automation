package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminAppointments;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import com.cts.mfrp.mediconnect.utils.TestData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AdminAppointmentsTest extends BaseAdminTest {

    @DataProvider(name = "appointmentFilters")
    public Object[][] appointmentFilters() {
        return TestData.appointmentIds();
    }

    @Test(groups = {"regression"}, dataProvider = "appointmentFilters")
    public void admin_appointment_calendar_filters(String testId) {
        Map<String, String> data = TestData.appointment(testId);
        String doctor   = data.get("doctor");
        String hospital = data.get("hospital");
        String date     = data.get("date");

        AdminAppointments page = new AdminAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0,
                "[" + testId + "] Appointment Management header should be visible");
        assertTrue(driver.findElements(page.calendarGrid).size() > 0,
                "[" + testId + "] Calendar grid should be visible");

        page.selectDoctor(doctor)
            .selectHospital(hospital)
            .setDateFilter(date);

        assertEquals(page.getSelectedDoctor(), doctor,
                "[" + testId + "] Doctor filter should reflect the selected value");
        assertEquals(page.getSelectedHospital(), hospital,
                "[" + testId + "] Hospital filter should reflect the selected value");
        assertEquals(page.getDateFilterValue(), date,
                "[" + testId + "] Date filter should reflect the selected value");

        // After applying filters the calendar grid must still be rendered.
        assertTrue(driver.findElements(page.calendarGrid).size() > 0,
                "[" + testId + "] Calendar grid should remain visible after applying filters");
    }

    @DataProvider(name = "newAppointments")
    public Object[][] newAppointments() {
        return TestData.newAppointmentIds();
    }

    @Test(groups = {"regression"}, dataProvider = "newAppointments")
    public void admin_new_appointment_creation(String testId) {
        Map<String, String> data = TestData.newAppointment(testId);

        AdminAppointments page = new AdminAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.newAppointmentBtn).size() > 0,
                "[" + testId + "] + New Appointment button should be visible");

        page.clickNewAppointment();
        wait.until(d -> page.isModalOpen());
        assertTrue(page.isModalOpen(),
                "[" + testId + "] New Appointment modal should open");

        page.fillNewAppointmentForm(
                data.get("patient"),
                data.get("doctor"),
                data.get("hospital"),
                data.get("date"),
                data.get("time"),
                data.get("type"),
                data.get("notes"));
        page.clickModalCreate();

        // Success signal: modal closes after a successful create.
        wait.until(d -> !page.isModalOpen());
        assertTrue(!page.isModalOpen(),
                "[" + testId + "] Modal should close after creating the appointment");
    }

    @Test(groups = {"regression"})
    public void Admin_AppointmentsValidation(){
        AdminAppointments page=new AdminAppointments(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.filtersHeading).size() > 0,"Filter table not available");
        assertTrue(driver.findElements(page.recentApptsHeading).size() > 0,"Recent Appointments window not available");
        assertTrue(driver.findElements(page.allApptsHeading).size() > 0,"Appointments table not available");
    }
}
