package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.model.Threshold;
import com.team3.weather.service.ThresholdService;

import java.util.List;

@RestController
@RequestMapping("/api/thresholds")
public class ThresholdController {

    @Autowired
    private final ThresholdService thresholdService;

    
    public ThresholdController(ThresholdService thresholdService) {
        this.thresholdService = thresholdService;
    }

    @GetMapping
    public List<Threshold> getAllThresholds() {
        return thresholdService.getAllThresholds();
    }

    @GetMapping("/{thresholdId}")
    public ResponseEntity<Threshold> getThresholdById(@PathVariable int thresholdId) {
        Threshold threshold = thresholdService.getThresholdById(thresholdId);
        if (threshold != null) {
            return ResponseEntity.ok(threshold);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Threshold createThreshold(@RequestBody Threshold threshold) {
        return thresholdService.createThreshold(threshold);
    }

    @PutMapping("/{thresholdId}")
    public ResponseEntity<Threshold> updateThreshold(
            @PathVariable int thresholdId,
            @RequestBody Threshold threshold) {
        Threshold updatedThreshold = thresholdService.updateThreshold(thresholdId, threshold);
        if (updatedThreshold != null) {
            return ResponseEntity.ok(updatedThreshold);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{thresholdId}")
    public ResponseEntity<Void> deleteThreshold(@PathVariable int thresholdId) {
        thresholdService.deleteThreshold(thresholdId);
        return ResponseEntity.noContent().build();
    }
}
