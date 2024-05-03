package com.team3.weather.DataTransferObject;

public class RainfallMonthlyRate {

    private String monthly;
    private Double dailyTotalRainfallRate;
    public RainfallMonthlyRate(String monthly, Double dailyTotalRainfallRate){
        this.monthly=monthly;
        this.dailyTotalRainfallRate=dailyTotalRainfallRate;
    }

    public String getMonthly(){
        return monthly;
    }

    public Double getdailyTotalRainfallRate(){
        return dailyTotalRainfallRate;
    }
}
