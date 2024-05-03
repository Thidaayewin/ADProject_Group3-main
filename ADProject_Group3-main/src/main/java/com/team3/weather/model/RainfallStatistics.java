package com.team3.weather.model;

import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
public class RainfallStatistics {

    private Object rainfallTime;
    private double rainfallTotal;

    public RainfallStatistics(Object rainfallTime, double rainfallTotal) {
        this.rainfallTime = rainfallTime;
        this.rainfallTotal = rainfallTotal;
    }

    public Object getRainfallTime() {
        return rainfallTime;
    }

    public void setRainfallTime(Object rainfallTime) {
        this.rainfallTime = rainfallTime;
    }

    public double getRainfallTotal() {
        return rainfallTotal;
    }

    public void setRainfallTotal(double rainfallTotal) {
        this.rainfallTotal = rainfallTotal;
    }
}
