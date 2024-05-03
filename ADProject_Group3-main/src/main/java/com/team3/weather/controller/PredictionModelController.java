package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.DataTransferObject.BindData;
import com.team3.weather.model.PredictionModel;
import com.team3.weather.service.PredictionModelService;
import com.team3.weather.service.StationService;

import java.util.List;

@RestController
@RequestMapping("/api/prediction-models")
public class PredictionModelController {

     @Autowired
    private final PredictionModelService predictionModelService;

     @Autowired
    private final StationService stationService;

   
    public PredictionModelController(PredictionModelService predictionModelService, StationService stationService) {
        this.predictionModelService = predictionModelService;
        this.stationService = stationService;
    }

    @GetMapping
    public List<PredictionModel> getAllPredictionModels() {
        return predictionModelService.getAllPredictionModels();
    }

    @GetMapping("/{modelId}")
    public ResponseEntity<PredictionModel> getPredictionModelById(@PathVariable int modelId) {
        PredictionModel predictionModel = predictionModelService.getPredictionModelById(modelId);
        if (predictionModel != null) {
            return new ResponseEntity<>(predictionModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<PredictionModel> createPredictionModel(@RequestBody PredictionModel predictionModel) {
        PredictionModel createdModel = predictionModelService.createPredictionModel(predictionModel);
        return new ResponseEntity<>(createdModel, HttpStatus.CREATED);
    }

    @PutMapping("/{modelId}")
    public ResponseEntity<PredictionModel> updatePredictionModel(@PathVariable int modelId, @RequestBody PredictionModel predictionModel) {
        PredictionModel updatedModel = predictionModelService.updatePredictionModel(modelId, predictionModel);
        if (updatedModel != null) {
            return new ResponseEntity<>(updatedModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{modelId}")
    public ResponseEntity<Void> deletePredictionModel(@PathVariable int modelId) {
        predictionModelService.deletePredictionModel(modelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getBindData")
    public ResponseEntity<BindData> getDistinctModelNumbersAndNames() {
        List<Object[]> modelResults = predictionModelService.getDistinctModelNumbersAndNames();
        List<Object[]> stationResults = stationService.getDistinctStationValues();
        BindData data = new BindData();
        data.setModelResults(modelResults);
        data.setStationResults(stationResults);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getConceptDrift")
    public ResponseEntity<BindData> getDistinctModelNumbersAndNamesforconceptdrift() {
        List<Object[]> modelResults = predictionModelService.getDistinctModelTypes();
        List<Object[]> stationResults = stationService.getDistinctStationValues();
        BindData data = new BindData();
        data.setModelResults(modelResults);
        data.setStationResults(stationResults);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/updateDefaultModel")
    public ResponseEntity<String> updateCheckbox(@RequestParam int modelId, @RequestParam boolean newCheckboxState) {
        PredictionModel optionalModel = predictionModelService.getPredictionModelById(modelId);
        List<PredictionModel> listPredictionModels = predictionModelService.getAllPredictionModels();
        for(PredictionModel pred: listPredictionModels){
            if(optionalModel.getStation().getStationId() == pred.getStation().getStationId()){
                pred.setDefault(false);
                predictionModelService.createPredictionModel(pred);
            }
        }
        if (optionalModel!=null) {
            optionalModel.setDefault(newCheckboxState);
            predictionModelService.createPredictionModel(optionalModel);
            return ResponseEntity.ok("Default state updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Model not found");
        }
    }
}
