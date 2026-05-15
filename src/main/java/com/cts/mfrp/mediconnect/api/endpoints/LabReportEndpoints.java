package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.LabReport;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class LabReportEndpoints {

    private final RequestSpecification spec;

    public LabReportEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAll() {
        return given().spec(spec).when().get(AppConstants.Endpoints.LAB_REPORTS);
    }

    public Response getById(long id) {
        return given().spec(spec).pathParam("id", id).when().get(AppConstants.Endpoints.LAB_REPORT_BY_ID);
    }

    public Response getByPatient(long patientId) {
        return given().spec(spec).pathParam("patientId", patientId)
                .when().get(AppConstants.Endpoints.LAB_REPORTS_BY_PATIENT);
    }

    public Response create(LabReport body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.LAB_REPORTS);
    }

    public Response update(long id, LabReport body) {
        return given().spec(spec).pathParam("id", id).body(body)
                .when().put(AppConstants.Endpoints.LAB_REPORT_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.LAB_REPORT_BY_ID);
    }
}
