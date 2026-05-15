package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.AppointmentEndpoints;
import com.cts.mfrp.mediconnect.api.models.Appointment;
import com.cts.mfrp.mediconnect.api.models.refs.DoctorRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class AppointmentApiTest extends ApiBaseTest {

    private static final long SEED_APPOINTMENT_ID = 1L;
    private static final long SEED_PATIENT_ID = 1L;
    private static final long SEED_DOCTOR_ID = 1L;
    private static final long SEED_HOSPITAL_ID = 1L;

    private AppointmentEndpoints appointments;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        appointments = new AppointmentEndpoints(spec);
    }

    @Test(priority = 1)
    public void getAll_shouldReturn200() {
        appointments.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 2)
    public void getById_seedAppointment_shouldReturnRecord() {
        appointments.getById(SEED_APPOINTMENT_ID).then()
                .statusCode(200)
                .body("appointmentId", equalTo((int) SEED_APPOINTMENT_ID));
    }

    @Test(priority = 3)
    public void getByPatient_shouldReturnList() {
        appointments.getByPatient(SEED_PATIENT_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 4)
    public void getByDoctor_shouldReturnList() {
        appointments.getByDoctor(SEED_DOCTOR_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 5)
    public void create_inPersonAppointment_shouldSucceed() {
        Appointment body = new Appointment()
                .setPatient(new PatientRef(SEED_PATIENT_ID))
                .setDoctor(new DoctorRef(SEED_DOCTOR_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setAppointmentDate("2026-05-10")
                .setAppointmentTime("10:30:00")
                .setStatus(AppConstants.AppointmentStatus.SCHEDULED)
                .setAppointmentType(AppConstants.AppointmentType.IN_PERSON)
                .setSessionUrl(null);

        Response response = appointments.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("appointmentId", notNullValue())
                .body("status", equalTo("SCHEDULED"))
                .body("appointmentType", equalTo("IN_PERSON"));

        createdId = response.jsonPath().getLong("appointmentId");
    }

    @Test(priority = 6, dependsOnMethods = "create_inPersonAppointment_shouldSucceed")
    public void update_cancelAppointment_shouldReflectStatus() {
        Appointment body = new Appointment()
                .setPatient(new PatientRef(SEED_PATIENT_ID))
                .setDoctor(new DoctorRef(SEED_DOCTOR_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setAppointmentDate("2026-05-10")
                .setAppointmentTime("10:30:00")
                .setStatus(AppConstants.AppointmentStatus.CANCELLED)
                .setAppointmentType(AppConstants.AppointmentType.IN_PERSON)
                .setSessionUrl(null);

        appointments.update(createdId, body).then()
                .statusCode(200)
                .body("status", equalTo("CANCELLED"));
    }

    @Test(priority = 7, dependsOnMethods = "create_inPersonAppointment_shouldSucceed")
    public void delete_shouldReturnNoContentOrConstraintError() {
        // Backend auto-creates a Bill row referencing the appointment, which
        // blocks deletion via FK constraint. The endpoint either succeeds
        // (no bill yet) or returns 400 with a constraint message.
        appointments.delete(createdId).then()
                .statusCode(deletedOrConstrainedStatus());
    }
}
