package com.cts.mfrp.mediconnect.api.models.refs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRef {

    private Long userId;

    public UserRef() {
    }

    public UserRef(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public UserRef setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
