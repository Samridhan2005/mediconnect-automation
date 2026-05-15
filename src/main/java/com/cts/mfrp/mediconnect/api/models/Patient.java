package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.UserRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient {

    private Long patientId;
    private UserRef user;
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;

    public Long getPatientId() { return patientId; }
    public Patient setPatientId(Long patientId) { this.patientId = patientId; return this; }

    public UserRef getUser() { return user; }
    public Patient setUser(UserRef user) { this.user = user; return this; }

    public String getDateOfBirth() { return dateOfBirth; }
    public Patient setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }

    public String getGender() { return gender; }
    public Patient setGender(String gender) { this.gender = gender; return this; }

    public String getBloodGroup() { return bloodGroup; }
    public Patient setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }

    public String getAddress() { return address; }
    public Patient setAddress(String address) { this.address = address; return this; }

    public String getEmergencyContact() { return emergencyContact; }
    public Patient setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; return this; }
}
