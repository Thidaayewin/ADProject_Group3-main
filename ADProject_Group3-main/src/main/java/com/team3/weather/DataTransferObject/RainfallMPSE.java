package com.team3.weather.DataTransferObject;

public class RainfallMPSE {

    private String monthly;
    private Double mpseScore;
    public RainfallMPSE(String monthly, Double mpseScore){
        this.monthly=monthly;
        this.mpseScore=mpseScore;
    }

    public String getMonthly(){
        return monthly;
    }

    public Double getmpseScore(){
        return mpseScore;
    }
}
