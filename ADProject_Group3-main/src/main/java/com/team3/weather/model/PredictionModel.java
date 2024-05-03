package com.team3.weather.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class PredictionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int modelId;
    
    private String modelNumber;
    private String modelType;
    
    @ManyToOne
    @JoinColumn(name = "stationId")
    private Station station;
    
    private String start_date;
    private String end_date;
    private String createdBy;
    private LocalDateTime createdDate;

    @Column(columnDefinition = "boolean default false")
    private boolean isDefault;

    public PredictionModel() {
    }

    public PredictionModel(String modelNumber, String modelType, Station station, String start_date,
            String end_date, String createdBy, LocalDateTime createdDate, boolean isDefault) {
        this.modelNumber = modelNumber;
        this.modelType = modelType;
        this.station = station;
        this.start_date = start_date;
        this.end_date = end_date;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.isDefault = isDefault;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

   

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDefault() {
        return isDefault;
    }
    

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "PredictionModel{" +
                "modelId=" + modelId +
                ", createdBy='" + createdBy + '\'' +
                ", modelType='" + modelType + '\'' +
                ", createdDate=" + createdDate +
                ", isDefault='" + isDefault + '\'' +
                '}';
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
