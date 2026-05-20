package com.cts.mfrp.mediconnect.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
    private String dateOfBirth;

    public String getFirstName() { return firstName; }
    public RegisterRequest setFirstName(String firstName) { this.firstName = firstName; return this; }

    public String getLastName() { return lastName; }
    public RegisterRequest setLastName(String lastName) { this.lastName = lastName; return this; }

    public String getEmail() { return email; }
    public RegisterRequest setEmail(String email) { this.email = email; return this; }

    public String getPassword() { return password; }
    public RegisterRequest setPassword(String password) { this.password = password; return this; }

    public String getPhone() { return phone; }
    public RegisterRequest setPhone(String phone) { this.phone = phone; return this; }

    public String getRole() { return role; }
    public RegisterRequest setRole(String role) { this.role = role; return this; }

    public String getGender() { return gender; }
    public RegisterRequest setGender(String gender) { this.gender = gender; return this; }

    public String getBloodGroup() { return bloodGroup; }
    public RegisterRequest setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }

    public String getAddress() { return address; }
    public RegisterRequest setAddress(String address) { this.address = address; return this; }

    public String getEmergencyContact() { return emergencyContact; }
    public RegisterRequest setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; return this; }

    public String getDateOfBirth() { return dateOfBirth; }
    public RegisterRequest setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }

    /**
     * Convenience setter that accepts a full name and splits it into
     * firstName + lastName on the first whitespace. Useful for callers that
     * still pass a single "Auto Admin"-style string.
     */
    public RegisterRequest setName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            this.firstName = null;
            this.lastName = null;
            return this;
        }
        String[] parts = fullName.trim().split("\\s+", 2);
        this.firstName = parts[0];
        this.lastName  = parts.length > 1 ? parts[1] : "";
        return this;
    }
}
