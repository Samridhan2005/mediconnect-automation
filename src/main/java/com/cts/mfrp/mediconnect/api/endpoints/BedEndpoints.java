package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Bed;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BedEndpoints {

    private final RequestSpecification spec;

    public BedEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.BEDS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.BED_BY_ID);
    }

    public Response getByHospital(long hospitalId) {
        return given().spec(spec).pathParam("hospitalId", hospitalId)
                .when().get(AppConstants.Endpoints.BEDS_BY_HOSPITAL);
    }

    public Response getByStatus(String status) {
        return given().spec(spec).pathParam("status", status)
                .when().get(AppConstants.Endpoints.BEDS_BY_STATUS);
    }

    public Response create(Bed body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.BEDS);
    }

    public Response update(long id, Bed body) {
        return given().spec(spec).pathParam("id", id).body(body)
                .when().put(AppConstants.Endpoints.BED_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.BED_BY_ID);
    }
}
