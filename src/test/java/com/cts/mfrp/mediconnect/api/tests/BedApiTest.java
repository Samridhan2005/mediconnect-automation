package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.BedEndpoints;
import com.cts.mfrp.mediconnect.api.models.Bed;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class BedApiTest extends ApiBaseTest {

    private static final long SEED_BED_ID = 1L;
    private static final long SEED_HOSPITAL_ID = 1L;

    private BedEndpoints beds;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        beds = new BedEndpoints(spec);
    }

    @Test(priority = 1)
    public void getAll_shouldReturn200() {
        beds.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 2)
    public void getById_seedBed_shouldReturnRecord() {
        beds.getById(SEED_BED_ID).then()
                .statusCode(200)
                .body("bedId", equalTo((int) SEED_BED_ID));
    }

    @Test(priority = 3)
    public void getByHospital_shouldReturnList() {
        beds.getByHospital(SEED_HOSPITAL_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 4)
    public void getByStatus_available_shouldReturnList() {
        beds.getByStatus(AppConstants.BedStatus.AVAILABLE).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 5)
    public void create_availableBed_shouldSucceed() {
        Bed body = new Bed()
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setWard("General Ward")
                .setBedNumber((int) (System.currentTimeMillis() % 100000))
                .setStatus(AppConstants.BedStatus.AVAILABLE)
                .setPatient(null);

        Response response = beds.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("bedId", notNullValue())
                .body("status", equalTo("AVAILABLE"));

        createdId = response.jsonPath().getLong("bedId");
    }

    @Test(priority = 6, dependsOnMethods = "create_availableBed_shouldSucceed")
    public void update_dischargeBed_shouldReflectStatus() {
        Bed body = new Bed()
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setWard("General Ward")
                .setBedNumber(101)
                .setStatus(AppConstants.BedStatus.AVAILABLE)
                .setPatient(null);

        beds.update(createdId, body).then()
                .statusCode(200)
                .body("status", equalTo("AVAILABLE"));
    }

    @Test(priority = 7, dependsOnMethods = "create_availableBed_shouldSucceed")
    public void delete_shouldReturnNoContent() {
        beds.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
