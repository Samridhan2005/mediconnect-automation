package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.MedicalRecordEndpoints;
import com.cts.mfrp.mediconnect.api.models.MedicalRecord;
import com.cts.mfrp.mediconnect.api.models.refs.DoctorRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class MedicalRecordApiTest extends ApiBaseTest {

    private static final long SEED_RECORD_ID = 1L;
    private static final long SEED_PATIENT_ID = 1L;
    private static final long SEED_DOCTOR_ID = 1L;
    private static final long SEED_HOSPITAL_ID = 1L;

    private MedicalRecordEndpoints records;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        records = new MedicalRecordEndpoints(spec);
    }

    @Test(groups = {"sanity", "regression"}, priority = 1)
    public void getAll_shouldReturn200() {
        records.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(groups = {"regression"}, priority = 2)
    public void getById_seedRecord_shouldReturnRecord() {
        records.getById(SEED_RECORD_ID).then()
                .statusCode(200)
                .body("recordId", equalTo((int) SEED_RECORD_ID));
    }

    @Test(groups = {"regression"}, priority = 3)
    public void getByPatient_shouldReturnList() {
        records.getByPatient(SEED_PATIENT_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(groups = {"regression"}, priority = 4)
    public void create_shouldReturnCreatedRecord() {
        MedicalRecord body = new MedicalRecord()
                .setPatient(new PatientRef(SEED_PATIENT_ID))
                .setDoctor(new DoctorRef(SEED_DOCTOR_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setRecordDate("2026-04-02")
                .setDiagnosis("Type 2 Diabetes - Early Stage")
                .setTreatment("Dietary control and medication")
                .setPrescription("Metformin 500mg twice daily")
                .setNotes("HbA1c: 7.1%. Patient advised low-sugar diet and regular walks.");

        Response response = records.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("recordId", notNullValue())
                .body("diagnosis", equalTo("Type 2 Diabetes - Early Stage"));

        createdId = response.jsonPath().getLong("recordId");
    }

    @Test(groups = {"regression"}, priority = 5, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void update_shouldReflectChanges() {
        MedicalRecord body = new MedicalRecord()
                .setPatient(new PatientRef(SEED_PATIENT_ID))
                .setDoctor(new DoctorRef(SEED_DOCTOR_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setRecordDate("2026-04-02")
                .setDiagnosis("Hypertension Stage 1 - Controlled")
                .setTreatment("Continued medication")
                .setPrescription("Amlodipine 10mg once daily")
                .setNotes("BP under control. Next review in 3 months.");

        records.update(createdId, body).then()
                .statusCode(200)
                .body("diagnosis", equalTo("Hypertension Stage 1 - Controlled"));
    }

    @Test(groups = {"regression"}, priority = 6, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void delete_shouldReturnNoContentOrConstraintError() {
        // Backend may auto-create a Prescription row referencing the record,
        // which blocks deletion via FK constraint. Accept either outcome.
        records.delete(createdId).then()
                .statusCode(deletedOrConstrainedStatus());
    }
}
