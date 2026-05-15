package com.cts.mfrp.mediconnect.api.endpoints;

import com.cts.mfrp.mediconnect.api.models.Notification;
import com.cts.mfrp.mediconnect.constants.AppConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class NotificationEndpoints {

    private final RequestSpecification spec;

    public NotificationEndpoints(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getByUser(long userId) {
        return given().spec(spec).pathParam("userId", userId)
                .when().get(AppConstants.Endpoints.NOTIFICATIONS_BY_USER);
    }

    public Response getUnreadByUser(long userId) {
        return given().spec(spec).pathParam("userId", userId)
                .when().get(AppConstants.Endpoints.NOTIFICATIONS_BY_USER_UNREAD);
    }

    public Response create(Notification body) {
        return given().spec(spec).body(body).when().post(AppConstants.Endpoints.NOTIFICATIONS);
    }

    public Response update(long id, Notification body) {
        return given().spec(spec).pathParam("id", id).body(body)
                .when().put(AppConstants.Endpoints.NOTIFICATION_BY_ID);
    }

    public Response delete(long id) {
        return given().spec(spec).pathParam("id", id).when().delete(AppConstants.Endpoints.NOTIFICATION_BY_ID);
    }
}
