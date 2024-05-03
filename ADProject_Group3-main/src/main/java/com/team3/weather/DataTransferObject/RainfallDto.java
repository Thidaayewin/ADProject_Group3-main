package com.team3.weather.DataTransferObject;

public class RainfallDto {

    private String startTime;
    private String endTime;
    private String type;
    private String DateType;
    private String stationId;
    private String modelId;
    private String rmse;
    private String mape;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateType() {
        return DateType;
    }

    public void setDateType(String dateType) {
        DateType = dateType;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getRmse() {
        return rmse;
    }

    public void setRmse(String rmse) {
        this.rmse = rmse;
    }

    public String getMape() {
        return mape;
    }

    public void setMape(String mape) {
        this.mape = mape;
    }
}
