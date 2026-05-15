package com.cts.mfrp.mediconnect.api.models.refs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentRef {

    private Long departmentId;

    public DepartmentRef() {
    }

    public DepartmentRef(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public DepartmentRef setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
        return this;
    }
}
