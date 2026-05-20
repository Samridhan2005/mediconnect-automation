package com.cts.mfrp.mediconnect.api.base;

import com.cts.mfrp.mediconnect.api.models.RegisterRequest;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public abstract class ApiBaseTest {

    private static volatile String cachedToken;

    /** Unauthenticated spec — use for auth/register/login tests. */
    protected RequestSpecification noAuthSpec;

    /** Authenticated spec — Bearer token attached lazily on first request. */
    protected RequestSpecification spec;

    @BeforeClass(alwaysRun = true)
    public void apiSetup() {
        RestAssured.baseURI = ConfigReader.get("api.baseUrl");

        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2));

        RequestSpecBuilder base = new RequestSpecBuilder().setContentType(ContentType.JSON);
        if (ConfigReader.getBoolean("api.logRequests")) {
            base.addFilter(new RequestLoggingFilter());
        }
        if (ConfigReader.getBoolean("api.logResponses")) {
            base.addFilter(new ResponseLoggingFilter());
        }
        noAuthSpec = base.build();

        spec = new RequestSpecBuilder()
                .addRequestSpecification(noAuthSpec)
                .addFilter((req, resp, ctx) -> {
                    req.header("Authorization", "Bearer " + getToken());
                    return ctx.next(req, resp);
                })
                .build();
    }

    /**
     * Registers a fresh ADMIN user on first call and returns its JWT.
     * Admin role is required for several write operations (e.g. POST /api/doctors).
     * Patient credentials in config.properties are kept for explicit auth tests.
     */
    private static synchronized String getToken() {
        if (cachedToken == null) {
            RegisterRequest admin = new RegisterRequest()
                    .setFirstName("Auto")
                    .setLastName("Admin")
                    .setEmail("auto_admin_" + System.currentTimeMillis() + "@cts.com")
                    .setPassword("Admin@123")
                    .setPhone("9999999999")
                    .setRole(AppConstants.Roles.ADMIN)
                    .setGender("MALE")
                    .setBloodGroup("O+")
                    .setAddress("automation")
                    .setEmergencyContact("9999999998")
                    .setDateOfBirth("1990-01-01");

            cachedToken = given()
                    .contentType(ContentType.JSON)
                    .body(admin)
                    .when()
                    .post(AppConstants.Endpoints.AUTH_REGISTER)
                    .then()
                    .statusCode(createdStatus())
                    .extract()
                    .jsonPath()
                    .getString("token");

            if (cachedToken == null || cachedToken.isBlank()) {
                throw new IllegalStateException(
                        "Admin registration did not return a token. Check api.baseUrl and /api/auth/register.");
            }
        }
        return cachedToken;
    }

    protected static Matcher<Integer> createdStatus() {
        return Matchers.anyOf(Matchers.is(200), Matchers.is(201));
    }

    protected static Matcher<Integer> deletedStatus() {
        return Matchers.anyOf(Matchers.is(200), Matchers.is(204));
    }

    /**
     * Delete returns 200/204 on success, OR 400 when a child entity (e.g. bills,
     * prescriptions) holds a FK to the parent — a real backend constraint we can't
     * clean up via the public API.
     */
    protected static Matcher<Integer> deletedOrConstrainedStatus() {
        return Matchers.anyOf(Matchers.is(200), Matchers.is(204), Matchers.is(400));
    }
}
