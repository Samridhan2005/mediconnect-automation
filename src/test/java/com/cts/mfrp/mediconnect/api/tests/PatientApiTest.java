package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.PatientEndpoints;
import com.cts.mfrp.mediconnect.api.models.Patient;
import com.cts.mfrp.mediconnect.api.models.refs.UserRef;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class PatientApiTest extends ApiBaseTest {

    private static final long SEED_PATIENT_ID = 1L;
    private static final long SEED_USER_ID = 7L;

    private PatientEndpoints patients;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        patients = new PatientEndpoints(spec);
    }

    @Test(priority = 1)
    public void getAll_shouldReturn200() {
        patients.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 2)
    public void getById_seedPatient_shouldReturnRecord() {
        patients.getById(SEED_PATIENT_ID).then()
                .statusCode(200)
                .body("patientId", equalTo((int) SEED_PATIENT_ID));
    }

    @Test(priority = 3)
    public void create_shouldReturnCreatedRecord() {
        Patient body = new Patient()
                .setUser(new UserRef(SEED_USER_ID))
                .setDateOfBirth("1990-06-12")
                .setGender("MALE")
                .setBloodGroup("O+")
                .setAddress("45 Park Road, Chennai")
                .setEmergencyContact("9733001112");

        Response response = patients.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("patientId", notNullValue())
                .body("bloodGroup", equalTo("O+"));

        createdId = response.jsonPath().getLong("patientId");
    }

    @Test(priority = 4, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void update_shouldReflectChanges() {
        Patient body = new Patient()
                .setUser(new UserRef(SEED_USER_ID))
                .setDateOfBirth("1990-06-12")
                .setGender("MALE")
                .setBloodGroup("A+")
                .setAddress("99 New Road, Chennai")
                .setEmergencyContact("9733001199");

        patients.update(createdId, body).then()
                .statusCode(200)
                .body("bloodGroup", equalTo("A+"));
    }

    @Test(priority = 5, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void delete_shouldReturnNoContent() {
        patients.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
