package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.LabReportEndpoints;
import com.cts.mfrp.mediconnect.api.models.LabReport;
import com.cts.mfrp.mediconnect.api.models.refs.DoctorRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class LabReportApiTest extends ApiBaseTest {

    private static final long SEED_REPORT_ID = 1L;
    private static final long SEED_PATIENT_ID = 1L;
    private static final long SEED_DOCTOR_ID = 1L;
    private static final long SEED_HOSPITAL_ID = 1L;

    private LabReportEndpoints reports;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        reports = new LabReportEndpoints(spec);
    }

    @Test(groups = {"sanity", "regression"}, priority = 1)
    public void getAll_shouldReturn200() {
        reports.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(groups = {"regression"}, priority = 2)
    public void getById_seedReport_shouldReturnRecord() {
        reports.getById(SEED_REPORT_ID).then()
                .statusCode(200)
                .body("reportId", equalTo((int) SEED_REPORT_ID));
    }

    @Test(groups = {"regression"}, priority = 3)
    public void getByPatient_shouldReturnList() {
        reports.getByPatient(SEED_PATIENT_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(groups = {"regression"}, priority = 4)
    public void create_shouldReturnCreatedReport() {
        LabReport body = new LabReport()
                .setPatient(new PatientRef(SEED_PATIENT_ID))
                .setDoctor(new DoctorRef(SEED_DOCTOR_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setTestName("Thyroid Function Test (TFT)")
                .setResult("TSH: 2.5 mIU/L — Normal Range")
                .setReportUrl("https://reports.mediconnect.com/lab/2001.pdf")
                .setReportDate("2026-04-02");

        Response response = reports.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("reportId", notNullValue())
                .body("testName", equalTo("Thyroid Function Test (TFT)"));

        createdId = response.jsonPath().getLong("reportId");
    }

    @Test(groups = {"regression"}, priority = 5, dependsOnMethods = "create_shouldReturnCreatedReport")
    public void update_shouldReflectChanges() {
        LabReport body = new LabReport()
                .setPatient(new PatientRef(SEED_PATIENT_ID))
                .setDoctor(new DoctorRef(SEED_DOCTOR_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setTestName("Complete Blood Count (CBC)")
                .setResult("Hb: 13.8 g/dL — Slightly Low")
                .setReportUrl("https://reports.mediconnect.com/lab/1001-v2.pdf")
                .setReportDate("2026-04-02");

        reports.update(createdId, body).then()
                .statusCode(200)
                .body("testName", equalTo("Complete Blood Count (CBC)"));
    }

    @Test(groups = {"regression"}, priority = 6, dependsOnMethods = "create_shouldReturnCreatedReport")
    public void delete_shouldReturnNoContent() {
        reports.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
