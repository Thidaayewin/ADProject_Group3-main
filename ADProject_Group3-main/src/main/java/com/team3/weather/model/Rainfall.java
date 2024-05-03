package com.team3.weather.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Rainfall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dataId;

    @ManyToOne
    @JoinColumn(name = "stationId", referencedColumnName = "stationId")
    private Station station;

    private String timestamp;
    private double actualRainfallAmount;

    // Constructors, Getters, and Setters

    // Default constructor
    public Rainfall() {
    }

    // Parameterized constructor
    public Rainfall(Station station, String timestamp, double actualRainfallAmount) {
        this.station = station;
        this.timestamp = timestamp;
        this.actualRainfallAmount = actualRainfallAmount;
    }

    // Getters and Setters

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getActualRainfallAmount() {
        return actualRainfallAmount;
    }

    public void setActualRainfallAmount(double actualRainfallAmount) {
        this.actualRainfallAmount = actualRainfallAmount;
    }

}
