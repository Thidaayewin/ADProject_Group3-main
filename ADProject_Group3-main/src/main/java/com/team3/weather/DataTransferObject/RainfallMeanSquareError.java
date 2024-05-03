package com.team3.weather.DataTransferObject;

public class RainfallMeanSquareError {

    private String monthly;
    private Double twelveRainfallRate;
    private Double sixtyRainfallRate;
    public RainfallMeanSquareError(String monthly, Double twelveRainfallRate,Double sixtyRainfallRate){
        this.monthly=monthly;
        this.twelveRainfallRate=twelveRainfallRate;
        this.sixtyRainfallRate=sixtyRainfallRate;
    }

    public String getMonthly(){
        return monthly;
    }

    public Double gettwelveRainfallRate(){
        return twelveRainfallRate;
    }
    public Double getsixtyRainfallRate(){
        return sixtyRainfallRate;
    }
}
