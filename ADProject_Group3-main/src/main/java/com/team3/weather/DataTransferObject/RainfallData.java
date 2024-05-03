package com.team3.weather.DataTransferObject;

public class RainfallData {
        private final String timestamp;
        private final double actualRainfallAmount;

        public RainfallData(String timestamp, double actualRainfallAmount) {
            this.timestamp = timestamp;
            this.actualRainfallAmount = actualRainfallAmount;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public double getActualRainfallAmount() {
            return actualRainfallAmount;
        }
    }