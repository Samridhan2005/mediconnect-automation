package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Hospital;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class HospitalEndpoints {

    private final RequestSpecification spec;

    public HospitalEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.HOSPITALS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.HOSPITAL_BY_ID);
    }

    public Response create(Hospital body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.HOSPITALS);
    }

    public Response update(long id, Hospital body) {
        return given().spec(spec).pathParam("id", id).body(body).when().put(AppConstants.Endpoints.HOSPITAL_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.HOSPITAL_BY_ID);
    }
}
