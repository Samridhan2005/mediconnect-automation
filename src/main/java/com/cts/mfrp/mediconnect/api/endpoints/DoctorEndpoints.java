package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Doctor;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class DoctorEndpoints {

    private final RequestSpecification spec;

    public DoctorEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.DOCTORS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.DOCTOR_BY_ID);
    }

    public Response getByHospital(long hospitalId) {
        return given().spec(spec).pathParam("hospitalId", hospitalId)
                .when().get(AppConstants.Endpoints.DOCTORS_BY_HOSPITAL);
    }

    public Response getBySpecialization(String specialization) {
        return given().spec(spec).pathParam("spec", specialization)
                .when().get(AppConstants.Endpoints.DOCTORS_BY_SPECIALIZATION);
    }

    public Response create(Doctor body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.DOCTORS);
    }

    public Response update(long id, Doctor body) {
        return given().spec(spec).pathParam("id", id).body(body).when().put(AppConstants.Endpoints.DOCTOR_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.DOCTOR_BY_ID);
    }
}
