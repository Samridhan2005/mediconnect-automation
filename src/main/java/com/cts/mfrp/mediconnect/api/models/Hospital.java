package com.cts.mfrp.mediconnect.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hospital {

    private Long hospitalId;
    private String hospitalName;
    private String address;
    private String city;
    private String phone;
    private Integer totalBeds;
    private Integer availableBeds;

    public Long getHospitalId() { return hospitalId; }
    public Hospital setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; return this; }

    public String getHospitalName() { return hospitalName; }
    public Hospital setHospitalName(String hospitalName) { this.hospitalName = hospitalName; return this; }

    public String getAddress() { return address; }
    public Hospital setAddress(String address) { this.address = address; return this; }

    public String getCity() { return city; }
    public Hospital setCity(String city) { this.city = city; return this; }

    public String getPhone() { return phone; }
    public Hospital setPhone(String phone) { this.phone = phone; return this; }

    public Integer getTotalBeds() { return totalBeds; }
    public Hospital setTotalBeds(Integer totalBeds) { this.totalBeds = totalBeds; return this; }

    public Integer getAvailableBeds() { return availableBeds; }
    public Hospital setAvailableBeds(Integer availableBeds) { this.availableBeds = availableBeds; return this; }
}
