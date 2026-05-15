package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.DoctorEndpoints;
import com.cts.mfrp.mediconnect.api.models.Doctor;
import com.cts.mfrp.mediconnect.api.models.refs.DepartmentRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.UserRef;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class DoctorApiTest extends ApiBaseTest {

    private static final long SEED_DOCTOR_ID = 1L;
    private static final long SEED_USER_ID = 3L;
    private static final long SEED_DEPARTMENT_ID = 1L;
    private static final long SEED_HOSPITAL_ID = 1L;

    private DoctorEndpoints doctors;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        doctors = new DoctorEndpoints(spec);
    }

    @Test(priority = 1)
    public void getAll_shouldReturn200() {
        doctors.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 2)
    public void getById_seedDoctor_shouldReturnRecord() {
        doctors.getById(SEED_DOCTOR_ID).then()
                .statusCode(200)
                .body("doctorId", equalTo((int) SEED_DOCTOR_ID));
    }

    @Test(priority = 3)
    public void getByHospital_shouldReturnList() {
        doctors.getByHospital(SEED_HOSPITAL_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 4)
    public void getBySpecialization_shouldReturnList() {
        doctors.getBySpecialization("Cardiology").then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 5)
    public void create_shouldReturnCreatedRecord() {
        Doctor body = new Doctor()
                .setUser(new UserRef(SEED_USER_ID))
                .setDepartment(new DepartmentRef(SEED_DEPARTMENT_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setSpecialization("Cardiology")
                .setAvailabilityStatus(AppConstants.DoctorAvailability.AVAILABLE);

        Response response = doctors.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("doctorId", notNullValue())
                .body("specialization", equalTo("Cardiology"));

        createdId = response.jsonPath().getLong("doctorId");
    }

    @Test(priority = 6, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void update_shouldReflectChanges() {
        Doctor body = new Doctor()
                .setUser(new UserRef(SEED_USER_ID))
                .setDepartment(new DepartmentRef(SEED_DEPARTMENT_ID))
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setSpecialization("Cardiology")
                .setAvailabilityStatus(AppConstants.DoctorAvailability.NOT_AVAILABLE);

        doctors.update(createdId, body).then()
                .statusCode(200)
                .body("availabilityStatus", equalTo("NOT_AVAILABLE"));
    }

    @Test(priority = 7, dependsOnMethods = "create_shouldReturnCreatedRecord")
    public void delete_shouldReturnNoContent() {
        doctors.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
