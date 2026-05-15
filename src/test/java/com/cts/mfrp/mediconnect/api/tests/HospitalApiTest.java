package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.HospitalEndpoints;
import com.cts.mfrp.mediconnect.api.models.Hospital;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class HospitalApiTest extends ApiBaseTest {

    private static final long SEED_HOSPITAL_ID = 1L;

    private HospitalEndpoints hospitals;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        hospitals = new HospitalEndpoints(spec);
    }

    @Test(priority = 1)
    public void getAll_shouldReturn200() {
        hospitals.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 2)
    public void getById_seedHospital_shouldReturnRecord() {
        hospitals.getById(SEED_HOSPITAL_ID).then()
                .statusCode(200)
                .body("hospitalId", equalTo((int) SEED_HOSPITAL_ID))
                .body("hospitalName", notNullValue());
    }

    @Test(priority = 3)
    public void create_shouldReturnCreatedRecord() {
        Hospital body = new Hospital()
                .setHospitalName("Sunrise Hospital " + System.currentTimeMillis())
                .setAddress("55 Ring Road")
                .setCity("Hyderabad")
                .setPhone("040-33001100")
                .setTotalBeds(150)
                .setAvailableBeds(40);

        Response response = hospitals.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("hospitalId", notNullValue())
                .body("hospitalName", equalTo(body.getHospitalName()))
                .body("city", equalTo("Hyderabad"));

        createdId = response.jsonPath().getLong("hospitalId");
    }

    @Test(priority = 4, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void update_shouldReflectChanges() {
        Hospital body = new Hospital()
                .setHospitalName("Sunrise Hospital Updated")
                .setAddress("55 Ring Road Updated")
                .setCity("Hyderabad")
                .setPhone("040-33001199")
                .setTotalBeds(200)
                .setAvailableBeds(60);

        hospitals.update(createdId, body).then()
                .statusCode(200)
                .body("hospitalName", equalTo("Sunrise Hospital Updated"))
                .body("totalBeds", equalTo(200));
    }

    @Test(priority = 5, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void delete_shouldReturnNoContent() {
        hospitals.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
