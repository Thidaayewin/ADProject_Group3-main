package com.team3.weather.model;

import jakarta.persistence.*;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alertId;

    private String alertName;
    private double thresholdLimit;
    private String alertPriority;
    private String alertType;
    private boolean active;

    // Constructors, Getters, and Setters

    // Default constructor
    public Alert() {
    }

    // Parameterized constructor
    public Alert(String alertName, double thresholdLimit, String alertPriority, String alertType, boolean active) {
        this.alertName = alertName;
        this.thresholdLimit = thresholdLimit;
        this.alertPriority = alertPriority;
        this.alertType = alertType;
        this.active = active;
    }

    // Getters and Setters

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public double getThresholdLimit() {
        return thresholdLimit;
    }

    public void setThresholdLimit(double thresholdLimit) {
        this.thresholdLimit = thresholdLimit;
    }

    public String getAlertPriority() {
        return alertPriority;
    }

    public void setAlertPriority(String alertPriority) {
        this.alertPriority = alertPriority;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertId=" + alertId +
                ", alertName='" + alertName + '\'' +
                ", thresholdLimit=" + thresholdLimit +
                ", alertPriority='" + alertPriority + '\'' +
                ", alertType='" + alertType + '\'' +
                ", active=" + active +
                '}';
    }
}
