package com.cts.mfrp.mediconnect.api.models;

import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Inventory {

    private Long itemId;
    private HospitalRef hospital;
    private String itemName;
    private String category;
    private Integer quantity;
    private Integer reorderLevel;

    public Long getItemId() { return itemId; }
    public Inventory setItemId(Long itemId) { this.itemId = itemId; return this; }

    public HospitalRef getHospital() { return hospital; }
    public Inventory setHospital(HospitalRef hospital) { this.hospital = hospital; return this; }

    public String getItemName() { return itemName; }
    public Inventory setItemName(String itemName) { this.itemName = itemName; return this; }

    public String getCategory() { return category; }
    public Inventory setCategory(String category) { this.category = category; return this; }

    public Integer getQuantity() { return quantity; }
    public Inventory setQuantity(Integer quantity) { this.quantity = quantity; return this; }

    public Integer getReorderLevel() { return reorderLevel; }
    public Inventory setReorderLevel(Integer reorderLevel) { this.reorderLevel = reorderLevel; return this; }
}
