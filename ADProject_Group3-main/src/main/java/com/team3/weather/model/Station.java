package com.team3.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stationId;
    private String stationName;
    private String stationValue;
    private double latitude;
    private double longitude;
   
    // Default constructor
    public Station() {
    }

    public Station(String stationName, String stationValue, double latitude, double longitude) {
       
        this.stationName = stationName;
        this.stationValue = stationValue;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    // Getters and Setters

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

   

    // toString() method
    @Override
    public String toString() {
        return "Station{" +
                "stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getStationValue() {
        return stationValue;
    }

    public void setStationValue(String stationValue) {
        this.stationValue = stationValue;
    }

    public Station orElse(Object object) {
        return null;
    }
}
