package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.ModelTrainLog;

public interface ModelTrainLogService {
    List<ModelTrainLog> getAllModelTrainLogs();

    ModelTrainLog getModelTrainLogById(int trainId);

    ModelTrainLog createModelTrainLog(ModelTrainLog modelTrainLog);

    ModelTrainLog updateModelTrainLog(int trainId, ModelTrainLog modelTrainLog);

    void deleteModelTrainLog(int trainId);
}
