package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.Alert;


public interface AlertService {
    List<Alert> getAllAlerts();

    Alert getAlertById(int alertId);

    Alert createAlert(Alert alert);

    Alert updateAlert(int alertId, Alert alert);

    void deleteAlert(int alertId);
}
