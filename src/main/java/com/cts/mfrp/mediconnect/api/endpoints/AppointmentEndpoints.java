package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Appointment;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AppointmentEndpoints {

    private final RequestSpecification spec;

    public AppointmentEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.APPOINTMENTS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.APPOINTMENT_BY_ID);
    }

    public Response getByPatient(long patientId) {
        return given().spec(spec).pathParam("patientId", patientId)
                .when().get(AppConstants.Endpoints.APPOINTMENTS_BY_PATIENT);
    }

    public Response getByDoctor(long doctorId) {
        return given().spec(spec).pathParam("doctorId", doctorId)
                .when().get(AppConstants.Endpoints.APPOINTMENTS_BY_DOCTOR);
    }

    public Response create(Appointment body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.APPOINTMENTS);
    }

    public Response update(long id, Appointment body) {
        return given().spec(spec).pathParam("id", id).body(body)
                .when().put(AppConstants.Endpoints.APPOINTMENT_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.APPOINTMENT_BY_ID);
    }
}
