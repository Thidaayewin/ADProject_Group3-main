package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.model.Alert;
import com.team3.weather.service.AlertService;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        List<Alert> alerts = alertService.getAllAlerts();
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<Alert> getAlertById(@PathVariable int alertId) {
        Alert alert = alertService.getAlertById(alertId);
        if (alert != null) {
            return new ResponseEntity<>(alert, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        Alert createdAlert = alertService.createAlert(alert);
        return new ResponseEntity<>(createdAlert, HttpStatus.CREATED);
    }

    @PutMapping("/{alertId}")
    public ResponseEntity<Alert> updateAlert(@PathVariable int alertId, @RequestBody Alert alert) {
        Alert updatedAlert = alertService.updateAlert(alertId, alert);
        if (updatedAlert != null) {
            return new ResponseEntity<>(updatedAlert, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> deleteAlert(@PathVariable int alertId) {
        alertService.deleteAlert(alertId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
