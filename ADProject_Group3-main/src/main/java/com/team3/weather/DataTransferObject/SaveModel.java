package com.team3.weather.DataTransferObject;

public class SaveModel {
    private String weatherStation;
    private String model;
    private double wRMSE;
    private double wMAPE;
    private String startDate;
    private String endDate;
    private String modelNumber;
    private String modelStartDate;
    private String modelEndDate;
    public String getWeatherStation() {
        return weatherStation;
    }
    public void setWeatherStation(String weatherStation) {
        this.weatherStation = weatherStation;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public double getwRMSE() {
        return wRMSE;
    }
    public void setwRMSE(double wRMSE) {
        this.wRMSE = wRMSE;
    }
    public double getwMAPE() {
        return wMAPE;
    }
    public void setwMAPE(double wMAPE) {
        this.wMAPE = wMAPE;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
   
    public String getModelStartDate() {
        return modelStartDate;
    }
    public void setModelStartDate(String modelStartDate) {
        this.modelStartDate = modelStartDate;
    }
    public String getModelEndDate() {
        return modelEndDate;
    }
    public void setModelEndDate(String modelEndDate) {
        this.modelEndDate = modelEndDate;
    }
    public SaveModel(String weatherStation, String model, double wRMSE, double wMAPE, String startDate, String endDate,
            String modelNumber, String modelStartDate, String modelEndDate) {
        this.weatherStation = weatherStation;
        this.model = model;
        this.wRMSE = wRMSE;
        this.wMAPE = wMAPE;
        this.startDate = startDate;
        this.endDate = endDate;
        this.modelNumber = modelNumber;
        this.modelStartDate = modelStartDate;
        this.modelEndDate = modelEndDate;
    }
    public SaveModel() {
    }
    public String getModelNumber() {
        return modelNumber;
    }
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    
}

