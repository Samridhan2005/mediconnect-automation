package com.cts.mfrp.mediconnect.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public LoginRequest setEmail(String email) { this.email = email; return this; }

    public String getPassword() { return password; }
    public LoginRequest setPassword(String password) { this.password = password; return this; }
}
