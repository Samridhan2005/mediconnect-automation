package com.cts.mfrp.mediconnect.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public final class AuthSetup {

    private static final AtomicBoolean SEEDED = new AtomicBoolean(false);

    private AuthSetup() {
    }

    public static synchronized void ensureSeedUsers() {
        if (!SEEDED.compareAndSet(false, true)) return;

        RestAssured.baseURI = ConfigReader.get("api.baseUrl");

        registerIfMissing(buildUser("Admin User",   ConfigReader.get("admin.email"),   ConfigReader.get("admin.password"),   "ADMIN"));
        registerIfMissing(buildUser("Patient User", ConfigReader.get("patient.email"), ConfigReader.get("patient.password"), "PATIENT"));
    }

    private static Map<String, Object> buildUser(String name, String email, String pwd, String role) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("password", pwd);
        body.put("phone", "9999999999");
        body.put("role", role);
        body.put("gender", "MALE");
        body.put("bloodGroup", "O+");
        body.put("address", "automation seed");
        body.put("emergencyContact", "9999999998");
        body.put("dateOfBirth", "1990-01-01");
        return body;
    }

    private static void registerIfMissing(Map<String, Object> body) {
        try {
            Response r = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when()
                    .post("/api/auth/register");
            int code = r.statusCode();
            // 200/201 = newly created; anything else (409/400 etc) = likely already exists.
            System.out.println("[AuthSetup] register " + body.get("email") + " -> " + code);
        } catch (Exception e) {
            System.err.println("[AuthSetup] register failed for " + body.get("email") + ": " + e.getMessage());
        }
    }
}
