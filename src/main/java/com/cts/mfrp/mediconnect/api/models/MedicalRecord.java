package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.DoctorRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicalRecord {

    private Long recordId;
    private PatientRef patient;
    private DoctorRef doctor;
    private HospitalRef hospital;
    private String recordDate;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;

    public Long getRecordId() { return recordId; }
    public MedicalRecord setRecordId(Long recordId) { this.recordId = recordId; return this; }

    public PatientRef getPatient() { return patient; }
    public MedicalRecord setPatient(PatientRef patient) { this.patient = patient; return this; }

    public DoctorRef getDoctor() { return doctor; }
    public MedicalRecord setDoctor(DoctorRef doctor) { this.doctor = doctor; return this; }

    public HospitalRef getHospital() { return hospital; }
    public MedicalRecord setHospital(HospitalRef hospital) { this.hospital = hospital; return this; }

    public String getRecordDate() { return recordDate; }
    public MedicalRecord setRecordDate(String recordDate) { this.recordDate = recordDate; return this; }

    public String getDiagnosis() { return diagnosis; }
    public MedicalRecord setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; return this; }

    public String getTreatment() { return treatment; }
    public MedicalRecord setTreatment(String treatment) { this.treatment = treatment; return this; }

    public String getPrescription() { return prescription; }
    public MedicalRecord setPrescription(String prescription) { this.prescription = prescription; return this; }

    public String getNotes() { return notes; }
    public MedicalRecord setNotes(String notes) { this.notes = notes; return this; }
}
