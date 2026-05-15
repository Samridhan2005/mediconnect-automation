package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.DoctorRef;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LabReport {

    private Long reportId;
    private PatientRef patient;
    private DoctorRef doctor;
    private HospitalRef hospital;
    private String testName;
    private String result;
    private String reportUrl;
    private String reportDate;

    public Long getReportId() { return reportId; }
    public LabReport setReportId(Long reportId) { this.reportId = reportId; return this; }

    public PatientRef getPatient() { return patient; }
    public LabReport setPatient(PatientRef patient) { this.patient = patient; return this; }

    public DoctorRef getDoctor() { return doctor; }
    public LabReport setDoctor(DoctorRef doctor) { this.doctor = doctor; return this; }

    public HospitalRef getHospital() { return hospital; }
    public LabReport setHospital(HospitalRef hospital) { this.hospital = hospital; return this; }

    public String getTestName() { return testName; }
    public LabReport setTestName(String testName) { this.testName = testName; return this; }

    public String getResult() { return result; }
    public LabReport setResult(String result) { this.result = result; return this; }

    public String getReportUrl() { return reportUrl; }
    public LabReport setReportUrl(String reportUrl) { this.reportUrl = reportUrl; return this; }

    public String getReportDate() { return reportDate; }
    public LabReport setReportDate(String reportDate) { this.reportDate = reportDate; return this; }
}
