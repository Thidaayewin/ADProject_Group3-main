package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.Threshold;

public interface ThresholdService {
    List<Threshold> getAllThresholds();
    Threshold getThresholdById(int thresholdId);
    Threshold createThreshold(Threshold threshold);
    Threshold updateThreshold(int thresholdId, Threshold threshold);
    void deleteThreshold(int thresholdId);
}
