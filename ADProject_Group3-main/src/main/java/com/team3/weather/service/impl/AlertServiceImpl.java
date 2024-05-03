package com.team3.weather.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.weather.model.Alert;
import com.team3.weather.repository.AlertRepository;
import com.team3.weather.service.AlertService;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private final AlertRepository alertRepository;

    
    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    @Override
    public Alert getAlertById(int alertId) {
        return alertRepository.findById(alertId).orElse(null);
    }

    @Override
    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    @Override
    public Alert updateAlert(int alertId, Alert alert) {
        if (alertRepository.existsById(alertId)) {
            alert.setAlertId(alertId);
            return alertRepository.save(alert);
        }
        return null;
    }

    @Override
    public void deleteAlert(int alertId) {
        alertRepository.deleteById(alertId);
    }
}
