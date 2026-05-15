package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.MedicalRecord;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class MedicalRecordEndpoints {

    private final RequestSpecification spec;

    public MedicalRecordEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.MEDICAL_RECORDS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.MEDICAL_RECORD_BY_ID);
    }

    public Response getByPatient(long patientId) {
        return given().spec(spec).pathParam("patientId", patientId)
                .when().get(AppConstants.Endpoints.MEDICAL_RECORDS_BY_PATIENT);
    }

    public Response create(MedicalRecord body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.MEDICAL_RECORDS);
    }

    public Response update(long id, MedicalRecord body) {
        return given().spec(spec).pathParam("id", id).body(body)
                .when().put(AppConstants.Endpoints.MEDICAL_RECORD_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.MEDICAL_RECORD_BY_ID);
    }
}
