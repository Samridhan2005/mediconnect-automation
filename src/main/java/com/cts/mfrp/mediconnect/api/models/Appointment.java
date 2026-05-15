package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.DoctorRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment {

    private Long appointmentId;
    private PatientRef patient;
    private DoctorRef doctor;
    private HospitalRef hospital;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String appointmentType;
    private String sessionUrl;

    public Long getAppointmentId() { return appointmentId; }
    public Appointment setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; return this; }

    public PatientRef getPatient() { return patient; }
    public Appointment setPatient(PatientRef patient) { this.patient = patient; return this; }

    public DoctorRef getDoctor() { return doctor; }
    public Appointment setDoctor(DoctorRef doctor) { this.doctor = doctor; return this; }

    public HospitalRef getHospital() { return hospital; }
    public Appointment setHospital(HospitalRef hospital) { this.hospital = hospital; return this; }

    public String getAppointmentDate() { return appointmentDate; }
    public Appointment setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; return this; }

    public String getAppointmentTime() { return appointmentTime; }
    public Appointment setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; return this; }

    public String getStatus() { return status; }
    public Appointment setStatus(String status) { this.status = status; return this; }

    public String getAppointmentType() { return appointmentType; }
    public Appointment setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; return this; }

    public String getSessionUrl() { return sessionUrl; }
    public Appointment setSessionUrl(String sessionUrl) { this.sessionUrl = sessionUrl; return this; }
}
