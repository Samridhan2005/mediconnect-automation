package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.AuthEndpoints;
import com.cts.mfrp.mediconnect.api.models.LoginRequest;
import com.cts.mfrp.mediconnect.api.models.RegisterRequest;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertNotNull;

public class AuthApiTest extends ApiBaseTest {

    private AuthEndpoints auth;

    @BeforeClass
    public void initEndpoints() {
        auth = new AuthEndpoints(noAuthSpec);
    }

    @Test(priority = 1)
    public void registerNewPatient_shouldReturnCreatedRecord() {
        long unique = System.currentTimeMillis();
        RegisterRequest body = new RegisterRequest()
                .setName("Test User " + unique)
                .setEmail("test" + unique + "@gmail.com")
                .setPassword("Test@123")
                .setPhone("9876543210")
                .setRole(AppConstants.Roles.PATIENT)
                .setGender("MALE")
                .setBloodGroup("O+")
                .setAddress("123 Test Street, Chennai")
                .setEmergencyContact("9876543211")
                .setDateOfBirth("1995-06-15");

        auth.register(body).then()
                .statusCode(createdStatus())
                .body("email", equalTo(body.getEmail()))
                .body("role", equalTo("PATIENT"));
    }

    @Test(priority = 2)
    public void login_withValidCredentials_shouldReturnToken() {
        LoginRequest body = new LoginRequest(
                ConfigReader.get("valid.email"),
                ConfigReader.get("valid.password")
        );

        Response response = auth.login(body);

        response.then()
                .statusCode(200)
                .body("email", equalTo(body.getEmail()))
                .body("userId", notNullValue())
                .body("token", notNullValue())
                .body("token", not(equalTo("")));

        String token = response.jsonPath().getString("token");
        Long userId = response.jsonPath().getLong("userId");
        assertNotNull(token, "JWT token should be returned");
        assertNotNull(userId, "userId should be returned");
        System.out.println("Logged-in userId=" + userId + " token=" + token.substring(0, 20) + "...");
    }

    @Test(priority = 3)
    public void login_withInvalidPassword_shouldFail() {
        LoginRequest body = new LoginRequest(
                ConfigReader.get("valid.email"),
                "wrong-password"
        );

        auth.login(body).then()
                .statusCode(anyOf(is(400), is(401), is(403)));
    }
}
