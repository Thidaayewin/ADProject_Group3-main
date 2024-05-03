package com.team3.weather.DataTransferObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DefaultModel {

    
    private int modelId;
    private String modelNumber;
    private String modelType;
    private int stationId;
    private String stationName;
    private String stationValue;
    private String start_date;
    private String end_date;
    private boolean isDefault;

    public DefaultModel() {
    }

    public DefaultModel(int modelId, String modelNumber, String modelType, int stationId, String stationName,
            String stationValue, String start_date, String end_date, boolean isDefault) {
        this.modelId = modelId;
        this.modelNumber = modelNumber;
        this.modelType = modelType;
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationValue = stationValue;
        this.start_date = start_date;
        this.end_date = end_date;
        this.isDefault = isDefault;
    }
}
