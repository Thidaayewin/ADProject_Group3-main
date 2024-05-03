package com.team3.weather.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.weather.model.Threshold;
import com.team3.weather.repository.ThresholdRepository;
import com.team3.weather.service.ThresholdService;

import java.util.List;
import java.util.Optional;

@Service
public class ThresholdServiceImpl implements ThresholdService {

    @Autowired
    private final ThresholdRepository thresholdRepository;

    public ThresholdServiceImpl(ThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    public List<Threshold> getAllThresholds() {
        return thresholdRepository.findAll();
    }

    @Override
    public Threshold getThresholdById(int thresholdId) {
        return thresholdRepository.findById(thresholdId).orElse(null);
    }

    @Override
    public Threshold createThreshold(Threshold threshold) {
        return thresholdRepository.save(threshold);
    }

    @Override
    public Threshold updateThreshold(int thresholdId, Threshold threshold) {
        Optional<Threshold> existingThreshold = thresholdRepository.findById(thresholdId);
        if (existingThreshold.isPresent()) {
            Threshold updatedThreshold = existingThreshold.get();

            updatedThreshold.setDeleted(threshold.isDeleted());
            updatedThreshold.setCreatedBy(threshold.getCreatedBy());
            updatedThreshold.setCreatedTime(threshold.getCreatedTime());
            updatedThreshold.setLastUpdatedBy(threshold.getLastUpdatedBy());
            updatedThreshold.setLastUpdatedTime(threshold.getLastUpdatedTime());

            updatedThreshold.setType(threshold.getType());
            updatedThreshold.setMetric(threshold.getMetric());
            updatedThreshold.setStatistic(threshold.getStatistic());

            return thresholdRepository.save(updatedThreshold);
        } else {
            return null;
        }
    }

    @Override
    public void deleteThreshold(int thresholdId) {
        thresholdRepository.deleteById(thresholdId);
    }
}
