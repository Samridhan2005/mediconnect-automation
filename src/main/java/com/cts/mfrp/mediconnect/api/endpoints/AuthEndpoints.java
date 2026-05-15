package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.LoginRequest;
import com.cts.mfrp.mediconnect.api.models.RegisterRequest;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AuthEndpoints {

    private final RequestSpecification spec;

    public AuthEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response register(RegisterRequest body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.AUTH_REGISTER);
    }

    public Response login(LoginRequest body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.AUTH_LOGIN);
    }
}
