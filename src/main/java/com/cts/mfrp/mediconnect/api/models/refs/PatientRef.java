package com.cts.mfrp.mediconnect.api.models.refs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientRef {

    private Long patientId;

    public PatientRef() {
    }

    public PatientRef(Long patientId) {
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public PatientRef setPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }
}
