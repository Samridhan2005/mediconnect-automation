package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.cts.mfrp.mediconnect.api.models.refs.PatientRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bed {

    private Long bedId;
    private HospitalRef hospital;
    private String ward;
    private Integer bedNumber;
    private String status;
    private PatientRef patient;

    public Long getBedId() { return bedId; }
    public Bed setBedId(Long bedId) { this.bedId = bedId; return this; }

    public HospitalRef getHospital() { return hospital; }
    public Bed setHospital(HospitalRef hospital) { this.hospital = hospital; return this; }

    public String getWard() { return ward; }
    public Bed setWard(String ward) { this.ward = ward; return this; }

    public Integer getBedNumber() { return bedNumber; }
    public Bed setBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; return this; }

    public String getStatus() { return status; }
    public Bed setStatus(String status) { this.status = status; return this; }

    public PatientRef getPatient() { return patient; }
    public Bed setPatient(PatientRef patient) { this.patient = patient; return this; }
}
