package com.cts.mfrp.mediconnect.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Long userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
    private String dateOfBirth;

    public Long getUserId() { return userId; }
    public User setUserId(Long userId) { this.userId = userId; return this; }

    public String getName() { return name; }
    public User setName(String name) { this.name = name; return this; }

    public String getEmail() { return email; }
    public User setEmail(String email) { this.email = email; return this; }

    public String getPassword() { return password; }
    public User setPassword(String password) { this.password = password; return this; }

    public String getPhone() { return phone; }
    public User setPhone(String phone) { this.phone = phone; return this; }

    public String getRole() { return role; }
    public User setRole(String role) { this.role = role; return this; }

    public String getGender() { return gender; }
    public User setGender(String gender) { this.gender = gender; return this; }

    public String getBloodGroup() { return bloodGroup; }
    public User setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }

    public String getAddress() { return address; }
    public User setAddress(String address) { this.address = address; return this; }

    public String getEmergencyContact() { return emergencyContact; }
    public User setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; return this; }

    public String getDateOfBirth() { return dateOfBirth; }
    public User setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
}
