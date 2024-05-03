package com.team3.weather.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Station;
import com.team3.weather.repository.PredictionModelRepository;
import com.team3.weather.service.PredictionModelService;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PredictionModelServiceImpl implements PredictionModelService {

    @Autowired
    private final PredictionModelRepository predictionModelRepository;

    
    public PredictionModelServiceImpl(PredictionModelRepository predictionModelRepository) {
        this.predictionModelRepository = predictionModelRepository;
    }

    @Override
    public List<PredictionModel> getAllPredictionModels() {
        return predictionModelRepository.findAll();
    }

    @Override
    public PredictionModel getPredictionModelById(int modelId) {
        return predictionModelRepository.findById(modelId).orElse(null);
    }

    @Override
    public PredictionModel createPredictionModel(PredictionModel predictionModel) {
        return predictionModelRepository.save(predictionModel);
    }

    @Override
    public PredictionModel updatePredictionModel(int modelId, PredictionModel predictionModel) {
        Optional<PredictionModel> existingModel = predictionModelRepository.findById(modelId);
        if (existingModel.isPresent()) {
            PredictionModel updatedModel = existingModel.get();
            updatedModel.setCreatedBy(predictionModel.getCreatedBy());
            updatedModel.setModelType(predictionModel.getModelType());
            updatedModel.setCreatedDate(predictionModel.getCreatedDate());
            return predictionModelRepository.save(updatedModel);
        }
        return null;
    }

    @Override
    public void deletePredictionModel(int modelId) {
        predictionModelRepository.deleteById(modelId);
    }

    @Override
    public PredictionModel getDefaultPredictionModel() {
        Optional<PredictionModel> optionalDefaultModel = predictionModelRepository.findByIsDefaultTrue();
        return optionalDefaultModel.orElse(null);
    }

    @Override
    public PredictionModel getDefaultPredictionModelByStation(Station station) {

        List<PredictionModel> optionalDefaultModel = predictionModelRepository.findByIsDefaultAndStation(true, station);
        if(optionalDefaultModel.size() == 0){
            return null;
        }
        return optionalDefaultModel.get(0);
    }

    @Override
    public void setDefaultPredictionModel(int modelId) {
        Optional<PredictionModel> optionalModel = predictionModelRepository.findById(modelId);

        if (optionalModel.isPresent()) {
            PredictionModel model = optionalModel.get();
            model.setDefault(true);
            predictionModelRepository.save(model);
        } else {
            throw new IllegalArgumentException("Invalid model ID: " + modelId);
        }
    }

    @Override
    public List<Object[]> getDistinctModelNumbersAndNames() {
        return predictionModelRepository.findDistinctModelNumbersAndTypes();
    }

    @Override
    public List<Object[]> getDistinctModelTypes() {
        return predictionModelRepository.findDistinctModelTypes();
    }

    @Override
    public List<PredictionModel> getModelsByModelTypeAndStationId(String modelType, int stationId) {
        return predictionModelRepository.findByModelTypeAndStationId(modelType, stationId);
    }

        // @Override
        public List<String> findPastTrainingDates(String stationName,String modelType){
            try {
                return predictionModelRepository.findPastTrainingDates(stationName, modelType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        
        // public Integer findMaxModelNumberByStationId(int stationId) {
        //     List<String> modelNumbers = predictionModelRepository.findModelNumbersByStationId(stationId);
        //     int maxNumber = -1;
    
        //     for (String modelNumber : modelNumbers) {
        //         int lastTwoDigits = Integer.parseInt(modelNumber.substring(modelNumber.length() - 2));
        //         if (lastTwoDigits > maxNumber) {
        //             maxNumber = lastTwoDigits;
        //         }
        //     }
    
        //     return maxNumber;
        // }
        @Override
        public String findMaxModelNumberByStationId(int stationId) {
        List<String> modelNumbers = predictionModelRepository.findModelNumbersByStationId(stationId);
        String model = modelNumbers.get(0);
        List<Integer> lastTwoDigits = modelNumbers.stream()
                .map(modelNumber -> Integer.parseInt(modelNumber.substring(modelNumber.length() - 2)))
                .sorted((a, b) -> Integer.compare(b, a)) // Sort in descending order
                .collect(Collectors.toList());
        
        if (lastTwoDigits.isEmpty()) {
            return String.format("%02d", 1);
        }
        
        int nextNumber = lastTwoDigits.get(0) + 1;
        String nex = String.format("%02d", nextNumber);
        return model.substring(0, 2)+nex;
    }

}
