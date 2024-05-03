package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.model.ModelTrainLog;
import com.team3.weather.service.ModelTrainLogService;

import java.util.List;

@RestController
@RequestMapping("/api/model-train-logs")
public class ModelTrainLogController {

    @Autowired
    private final ModelTrainLogService modelTrainLogService;

    
    public ModelTrainLogController(ModelTrainLogService modelTrainLogService) {
        this.modelTrainLogService = modelTrainLogService;
    }

    @GetMapping
    public ResponseEntity<List<ModelTrainLog>> getAllModelTrainLogs() {
        List<ModelTrainLog> modelTrainLogs = modelTrainLogService.getAllModelTrainLogs();
        return new ResponseEntity<>(modelTrainLogs, HttpStatus.OK);
    }

    @GetMapping("/{trainId}")
    public ResponseEntity<ModelTrainLog> getModelTrainLogById(@PathVariable int trainId) {
        ModelTrainLog modelTrainLog = modelTrainLogService.getModelTrainLogById(trainId);
        if (modelTrainLog != null) {
            return new ResponseEntity<>(modelTrainLog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ModelTrainLog> createModelTrainLog(@RequestBody ModelTrainLog modelTrainLog) {
        ModelTrainLog createdModelTrainLog = modelTrainLogService.createModelTrainLog(modelTrainLog);
        return new ResponseEntity<>(createdModelTrainLog, HttpStatus.CREATED);
    }

    @PutMapping("/{trainId}")
    public ResponseEntity<ModelTrainLog> updateModelTrainLog(@PathVariable int trainId,
                                                              @RequestBody ModelTrainLog modelTrainLog) {
        ModelTrainLog updatedModelTrainLog = modelTrainLogService.updateModelTrainLog(trainId, modelTrainLog);
        if (updatedModelTrainLog != null) {
            return new ResponseEntity<>(updatedModelTrainLog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{trainId}")
    public ResponseEntity<Void> deleteModelTrainLog(@PathVariable int trainId) {
        modelTrainLogService.deleteModelTrainLog(trainId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
