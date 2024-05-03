package com.team3.weather.DataTransferObject;

public class DataEntry {
    private double actualRainfall;
    private String date;
    private double predictedRainfall;
    private double residuals;
    private double rollingRMSE;
    private double mape;
    public double getActualRainfall() {
        return actualRainfall;
    }
    public void setActualRainfall(double actualRainfall) {
        this.actualRainfall = actualRainfall;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public double getPredictedRainfall() {
        return predictedRainfall;
    }
    public void setPredictedRainfall(double predictedRainfall) {
        this.predictedRainfall = predictedRainfall;
    }
    public double getResiduals() {
        return residuals;
    }
    public void setResiduals(double residuals) {
        this.residuals = residuals;
    }
    public double getRollingRMSE() {
        return rollingRMSE;
    }
    public void setRollingRMSE(double rollingRMSE) {
        this.rollingRMSE = rollingRMSE;
    }
    public double getMape() {
        return mape;
    }
    public void setMape(double mape) {
        this.mape = mape;
    }
    public DataEntry(double actualRainfall, String date, double predictedRainfall, double residuals, double rollingRMSE,
            double mape) {
        this.actualRainfall = actualRainfall;
        this.date = date;
        this.predictedRainfall = predictedRainfall;
        this.residuals = residuals;
        this.rollingRMSE = rollingRMSE;
        this.mape = mape;
    }

    
    
}

