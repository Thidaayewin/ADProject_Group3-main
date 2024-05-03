package com.team3.weather.model;

import jakarta.persistence.*;


@Entity
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int refId;
    
    private String refName;
    private String monthly;
    private String type;
    public Reference(){}
    public int getRefId() {
        return refId;
    }
    public void setRefId(int refId) {
        this.refId = refId;
    }
    public String getRefName() {
        return refName;
    }
    public void setRefName(String refName) {
        this.refName = refName;
    }
    public String getMonthly() {
        return monthly;
    }
    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Reference(String refName, String monthly, String type) {
        this.refName = refName;
        this.monthly = monthly;
        this.type = type;
    }
}
