package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.NotificationEndpoints;
import com.cts.mfrp.mediconnect.api.models.Notification;
import com.cts.mfrp.mediconnect.api.models.refs.UserRef;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class NotificationApiTest extends ApiBaseTest {

    private static final long SEED_USER_ID = 7L;

    private NotificationEndpoints notifications;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        notifications = new NotificationEndpoints(spec);
    }

    @Test(groups = {"sanity", "regression"}, priority = 1)
    public void getByUser_shouldReturnList() {
        notifications.getByUser(SEED_USER_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(groups = {"regression"}, priority = 2)
    public void getUnreadByUser_shouldReturnList() {
        notifications.getUnreadByUser(SEED_USER_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(groups = {"regression"}, priority = 3)
    public void create_reminder_shouldSucceed() {
        Notification body = new Notification()
                .setUser(new UserRef(SEED_USER_ID))
                .setNotificationType("REMINDER")
                .setMessage("Your follow-up appointment is tomorrow at 10:00 AM.")
                .setIsRead(false)
                .setCreatedAt("2026-04-02T09:00:00");

        Response response = notifications.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("notificationId", notNullValue())
                .body("notificationType", equalTo("REMINDER"))
                .body("isRead", equalTo(false));

        createdId = response.jsonPath().getLong("notificationId");
    }

    @Test(groups = {"regression"}, priority = 4, dependsOnMethods = "create_reminder_shouldSucceed")
    public void update_markAsRead_shouldReflectStatus() {
        Notification body = new Notification()
                .setUser(new UserRef(SEED_USER_ID))
                .setNotificationType("APPOINTMENT")
                .setMessage("Your appointment with Dr. Arun Kumar is confirmed.")
                .setIsRead(true)
                .setCreatedAt("2026-04-01T09:00:00");

        notifications.update(createdId, body).then()
                .statusCode(200)
                .body("isRead", equalTo(true));
    }

    @Test(groups = {"regression"}, priority = 5, dependsOnMethods = "create_reminder_shouldSucceed")
    public void delete_shouldReturnNoContent() {
        notifications.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
