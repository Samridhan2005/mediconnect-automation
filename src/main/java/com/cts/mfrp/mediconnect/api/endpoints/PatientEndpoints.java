package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Patient;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PatientEndpoints {

    private final RequestSpecification spec;

    public PatientEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.PATIENTS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.PATIENT_BY_ID);
    }

    public Response create(Patient body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.PATIENTS);
    }

    public Response update(long id, Patient body) {
        return given().spec(spec).pathParam("id", id).body(body).when().put(AppConstants.Endpoints.PATIENT_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.PATIENT_BY_ID);
    }
}
