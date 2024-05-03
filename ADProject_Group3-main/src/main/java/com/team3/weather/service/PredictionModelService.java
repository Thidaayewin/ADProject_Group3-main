package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Station;

public interface PredictionModelService {
    List<PredictionModel> getAllPredictionModels();
    PredictionModel getPredictionModelById(int modelId);
    PredictionModel createPredictionModel(PredictionModel predictionModel);
    PredictionModel updatePredictionModel(int modelId, PredictionModel predictionModel);
    void deletePredictionModel(int modelId);
    PredictionModel getDefaultPredictionModel();
    void setDefaultPredictionModel(int modelId);
    PredictionModel getDefaultPredictionModelByStation(Station station);
    List<Object[]> getDistinctModelNumbersAndNames();
    List<PredictionModel> getModelsByModelTypeAndStationId(String modelType, int stationId);
    List<String> findPastTrainingDates(String stationName,String modelType);
    List<Object[]> getDistinctModelTypes();
    String findMaxModelNumberByStationId(int stationId);
}
