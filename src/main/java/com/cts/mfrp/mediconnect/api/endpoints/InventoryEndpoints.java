package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Inventory;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class InventoryEndpoints {

    private final RequestSpecification spec;

    public InventoryEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.INVENTORY);
    }

    public Response getByHospital(long hospitalId) {
        return given().spec(spec).pathParam("hospitalId", hospitalId)
                .when().get(AppConstants.Endpoints.INVENTORY_BY_HOSPITAL);
    }

    public Response create(Inventory body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.INVENTORY);
    }

    public Response update(long id, Inventory body) {
        return given().spec(spec).pathParam("id", id).body(body)
                .when().put(AppConstants.Endpoints.INVENTORY_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.INVENTORY_BY_ID);
    }
}
