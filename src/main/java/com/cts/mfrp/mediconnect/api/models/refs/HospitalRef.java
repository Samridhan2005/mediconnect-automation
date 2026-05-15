package com.cts.mfrp.mediconnect.api.models.refs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalRef {

    private Long hospitalId;

    public HospitalRef() {
    }

    public HospitalRef(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public HospitalRef setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
        return this;
    }
}
