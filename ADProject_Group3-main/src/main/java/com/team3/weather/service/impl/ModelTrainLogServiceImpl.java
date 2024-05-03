package com.team3.weather.service.impl;

import org.springframework.beans.factory.annotation.Autowired;


import com.team3.weather.model.ModelTrainLog;
import com.team3.weather.repository.ModelTrainLogRepository;
import com.team3.weather.service.ModelTrainLogService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModelTrainLogServiceImpl implements ModelTrainLogService {

    @Autowired
    private final ModelTrainLogRepository modelTrainLogRepository;
    
    public ModelTrainLogServiceImpl(ModelTrainLogRepository modelTrainLogRepository) {
        this.modelTrainLogRepository = modelTrainLogRepository;
    }

    @Override
    public List<ModelTrainLog> getAllModelTrainLogs() {
        return modelTrainLogRepository.findAll();
    }

    @Override
    public ModelTrainLog getModelTrainLogById(int trainId) {
        return modelTrainLogRepository.findById(trainId).orElse(null);
    }

    @Override
    public ModelTrainLog createModelTrainLog(ModelTrainLog modelTrainLog) {
        return modelTrainLogRepository.save(modelTrainLog);
    }

    @Override
    public ModelTrainLog updateModelTrainLog(int trainId, ModelTrainLog modelTrainLog) {
        if (modelTrainLogRepository.existsById(trainId)) {
            modelTrainLog.setTrainId(trainId);
            return modelTrainLogRepository.save(modelTrainLog);
        }
        return null;
    }

    @Override
    public void deleteModelTrainLog(int trainId) {
        modelTrainLogRepository.deleteById(trainId);
    }
}
