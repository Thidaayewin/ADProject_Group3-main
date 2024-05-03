package com.team3.weather.DataTransferObject;

import java.util.List;

public class BindData {
    private List<Object[]> modelResults;
    private List<Object[]> stationResults;
    public List<Object[]> getModelResults() {
        return modelResults;
    }
    public void setModelResults(List<Object[]> modelResults) {
        this.modelResults = modelResults;
    }
    public List<Object[]> getStationResults() {
        return stationResults;
    }
    public void setStationResults(List<Object[]> stationResults) {
        this.stationResults = stationResults;
    }
    public BindData() {
    }

    
    
    
}
