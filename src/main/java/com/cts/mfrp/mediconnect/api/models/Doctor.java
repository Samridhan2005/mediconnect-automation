package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.DepartmentRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.UserRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Doctor {

    private Long doctorId;
    private UserRef user;
    private DepartmentRef department;
    private HospitalRef hospital;
    private String specialization;
    private String availabilityStatus;

    public Long getDoctorId() { return doctorId; }
    public Doctor setDoctorId(Long doctorId) { this.doctorId = doctorId; return this; }

    public UserRef getUser() { return user; }
    public Doctor setUser(UserRef user) { this.user = user; return this; }

    public DepartmentRef getDepartment() { return department; }
    public Doctor setDepartment(DepartmentRef department) { this.department = department; return this; }

    public HospitalRef getHospital() { return hospital; }
    public Doctor setHospital(HospitalRef hospital) { this.hospital = hospital; return this; }

    public String getSpecialization() { return specialization; }
    public Doctor setSpecialization(String specialization) { this.specialization = specialization; return this; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public Doctor setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; return this; }
}
