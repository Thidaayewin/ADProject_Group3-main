package com.team3.weather.DataTransferObject;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.team3.weather.model.Station;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HistoryLog {

    private int trainId;
    private String modelType;
    private String modelNumber;
    private Station station;
    private String startDate;
    private String endDate;
    private boolean isDefault;
    public HistoryLog(int trainId, Station station,String modelNumber, String modelType,
    String startDate, String endDate, Boolean isDefault) {
        this.trainId=trainId;
        this.station=station;
        this.modelType=modelType;
        this.modelNumber=modelNumber;
        this.startDate=startDate;
        this.endDate=endDate;
        this.isDefault=isDefault;
    }
}
