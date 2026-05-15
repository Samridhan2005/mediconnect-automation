package com.cts.mfrp.mediconnect.api.models.refs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorRef {

    private Long doctorId;

    public DoctorRef() {
    }

    public DoctorRef(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public DoctorRef setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
        return this;
    }
}
