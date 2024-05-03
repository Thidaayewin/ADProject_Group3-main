package com.team3.weather.DataTransferObject;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.team3.weather.model.Reference;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RainfallDataResponse {
    private List<String> actualTimestamps;
    private List<Double> actualValues;
    private List<Double> predictedValues;
    private Map<String, List<Reference>> dataDriftPeriods;
    private List<String> pastTrainingDates;
    private String error;

    public RainfallDataResponse(List<String> actualTimestamps, List<Double> actualValues, List<Double> predictedValues) {
        this.actualTimestamps = actualTimestamps;
        this.actualValues = actualValues;
        this.predictedValues = predictedValues;
    }

    public RainfallDataResponse(List<String> actualTimestamps, List<Double> actualValues, List<Double> predictedValues,Map<String, List<Reference>> dataDriftPeriods,List<String>pastTrainingDates) {
        this.actualTimestamps = actualTimestamps;
        this.actualValues = actualValues;
        this.predictedValues = predictedValues;
        this.dataDriftPeriods=dataDriftPeriods;
        this.pastTrainingDates= pastTrainingDates;
    }

    public RainfallDataResponse(String error) {
        this.error = error;
    }
}
