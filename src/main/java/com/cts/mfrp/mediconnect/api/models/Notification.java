package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.UserRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

    private Long notificationId;
    private UserRef user;
    private String notificationType;
    private String message;
    private Boolean isRead;
    private String createdAt;

    public Long getNotificationId() { return notificationId; }
    public Notification setNotificationId(Long notificationId) { this.notificationId = notificationId; return this; }

    public UserRef getUser() { return user; }
    public Notification setUser(UserRef user) { this.user = user; return this; }

    public String getNotificationType() { return notificationType; }
    public Notification setNotificationType(String notificationType) { this.notificationType = notificationType; return this; }

    public String getMessage() { return message; }
    public Notification setMessage(String message) { this.message = message; return this; }

    public Boolean getIsRead() { return isRead; }
    public Notification setIsRead(Boolean isRead) { this.isRead = isRead; return this; }

    public String getCreatedAt() { return createdAt; }
    public Notification setCreatedAt(String createdAt) { this.createdAt = createdAt; return this; }
}
