package com.team3.weather.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.DataTransferObject.ApiResponse;
import com.team3.weather.DataTransferObject.DataEntry;
import com.team3.weather.DataTransferObject.DefaultModel;
import com.team3.weather.DataTransferObject.HistoryLog;
import com.team3.weather.DataTransferObject.RainfallDataResponse;
import com.team3.weather.DataTransferObject.RainfallDto;
import com.team3.weather.DataTransferObject.RainfallMPSE;
import com.team3.weather.DataTransferObject.RainfallMeanSquareError;
import com.team3.weather.DataTransferObject.RainfallMonthlyRate;
import com.team3.weather.DataTransferObject.RollingMSESlope;
import com.team3.weather.model.ModelTrainLog;
import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.RainfallStatistics;
import com.team3.weather.model.Reference;
import com.team3.weather.model.Station;
import com.team3.weather.scheduled.ScheduledService;
import com.team3.weather.service.ModelTrainLogService;
import com.team3.weather.service.PredictionModelService;
import com.team3.weather.service.RainfallService;
import com.team3.weather.service.ReferenceService;
import com.team3.weather.service.StationService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/rainfall")
public class RainfallController {

    @Autowired
    private final RainfallService rainfallService;

    @Autowired
    private final ModelTrainLogService modelTrainLogService;

    @Autowired
    private final PredictionModelService predictedModelService;

    @Autowired
    private final ReferenceService referenceService;
    
    @Autowired
    private final ScheduledService scheduledService;

    @Autowired
    private final StationService stationService;

    
    public RainfallController(RainfallService rainfallService,ScheduledService scheduledService, ModelTrainLogService modelTrainLogService,
    PredictionModelService predictionModelService,ReferenceService referenceService,StationService stationService ) {
        this.rainfallService = rainfallService;
        this.scheduledService=scheduledService;
        this.predictedModelService=predictionModelService;
        this.referenceService=referenceService;
        this.modelTrainLogService=modelTrainLogService;
        this.stationService = stationService;
    }

    @GetMapping
    public List<Rainfall> getAllRainfall() {
        return rainfallService.getAllRainfall();
    }

    @GetMapping("/{dataId}")
    public ResponseEntity<Rainfall> getRainfallById(@PathVariable int dataId) {
        Rainfall rainfall = rainfallService.getRainfallById(dataId);
        if (rainfall != null) {
            return new ResponseEntity<>(rainfall, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Rainfall> createRainfall(@RequestBody Rainfall rainfall) {
        Rainfall createdRainfall = rainfallService.createRainfall(rainfall);
        return new ResponseEntity<>(createdRainfall, HttpStatus.CREATED);
    }

    @PutMapping("/{dataId}")
    public ResponseEntity<Rainfall> updateRainfall(@PathVariable int dataId, @RequestBody Rainfall rainfall) {
        Rainfall updatedRainfall = rainfallService.updateRainfall(dataId, rainfall);
        if (updatedRainfall != null) {
            return new ResponseEntity<>(updatedRainfall, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{dataId}")
    public ResponseEntity<Void> deleteRainfall(@PathVariable int dataId) {
        rainfallService.deleteRainfall(dataId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @PostMapping("/modelDrift")
    // @ResponseBody
    // public ResponseEntity<RollingMSESlope> setModelDriftParams(
    //     @RequestParam String modelType,
    //     @RequestParam String modelName,
    //     @RequestParam String station,
    //     @RequestParam String type,
    //     @RequestParam int window,
    //     @RequestParam double rollingMSEslope
    // ){
    //         RollingMSESlope res = new RollingMSESlope(modelType,window,rollingMSEslope);
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    // }

    @GetMapping("/getmodelDrift")
    @ResponseBody
    public ResponseEntity<RollingMSESlope> getModelDriftSlopes(
        @RequestParam String modelType,
        @RequestParam String modelName,
        @RequestParam String station,
        @RequestParam String type,
        @RequestParam int window,
        @RequestParam double rollingMSEslope
    ){
            RollingMSESlope res = new RollingMSESlope(modelType,window,rollingMSEslope);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @GetMapping("/getPredictedAndActualRainfallData")
    @ResponseBody
    public ResponseEntity<RainfallDataResponse> getAllRainfallData(
    @RequestParam String modelNum,
    @RequestParam String station,
    @RequestParam String start_date,
    @RequestParam String end_date,
    @RequestParam int periods,
    @RequestParam int wRMSE,
    @RequestParam int wMAPE) {
        // List<Rainfall> rainfallList = rainfallService.getAllRainfall();
    
        PredictionModel pred = predictedModelService.getPredictionModelById(1);
    ModelTrainLog m2 = new ModelTrainLog(pred, start_date, end_date, wRMSE,wMAPE,"Admin",LocalDateTime.now());
    
    modelTrainLogService.createModelTrainLog(m2);
        
        List<String> actualTimestamps = new ArrayList<>();
        List<Double> actualValues = new ArrayList<>();
        List<Double> predictedValues = new ArrayList<>();
        //datadrift periods
        Map<String, List<Reference>> dataDriftPeriods= new HashMap<>();
        dataDriftPeriods=referenceService.getDataDriftPeriods();
        //past training dates
        List<String> pastTrainingDatesList =predictedModelService.findPastTrainingDates(station, modelNum);
        List<String> pastTrainingDates = new ArrayList<>();
        for (String pastTrainingDate : pastTrainingDatesList) {
            pastTrainingDates.add(pastTrainingDate);
        }
        ApiResponse apiResponse=new ApiResponse();
       
        try{
                apiResponse=fetchDataFromApiAndPrint(rainfallService.increaseMonth(start_date), end_date,
                                         modelNum, station,periods,
                                         wRMSE, wMAPE);
        }catch(Exception ex){
            Station s = stationService.findByStationValue(station);
            List<PredictionModel> listpredict = predictedModelService.getAllPredictionModels();
            String start="";
            String end = "";
            for(PredictionModel predictionModel: listpredict){
                if(predictionModel.getModelNumber().equals(modelNum) && predictionModel.getStation().getStationId() == s.getStationId()){
                    start = predictionModel.getStart_date();
                    end = predictionModel.getEnd_date();
                    break;
                }
            }
            RainfallDataResponse res = new RainfallDataResponse("Please select between "+start+" and "+end);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        
        
        for(DataEntry entry : apiResponse.getData()){
            actualTimestamps.add(entry.getDate());
            actualValues.add(entry.getActualRainfall());
            predictedValues.add(entry.getPredictedRainfall());
        }

        // Create a response object containing both actual and predicted data
        RainfallDataResponse response = new RainfallDataResponse(actualTimestamps, actualValues, predictedValues,dataDriftPeriods,pastTrainingDates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getDefaultPredictedAndActualRainfallData")
    @ResponseBody
    public ResponseEntity<RainfallDataResponse> getDefaultModel() {
        PredictionModel optionalDefaultModel = predictedModelService.getDefaultPredictionModel();
        List<String> actualTimestamps = new ArrayList<>();
        List<Double> actualValues = new ArrayList<>();
        List<Double> predictedValues = new ArrayList<>();
        if(optionalDefaultModel == null){
            PredictionModel pred = predictedModelService.getPredictionModelById(1);
            pred.setDefault(true);
            predictedModelService.updatePredictionModel( 1, pred);
        }
        optionalDefaultModel = predictedModelService.getDefaultPredictionModel();

        // ApiResponse apiResponse=fetchDataFromApiAndPrint(optionalDefaultModel.getStart_date(), optionalDefaultModel.getEnd_date(),
        //                                  optionalDefaultModel.getmodelType(), optionalDefaultModel.getStation(),optionalDefaultModel.getPeriods(),
        //                                  optionalDefaultModel.getwRMSE(), optionalDefaultModel.getwMAPE());
        ApiResponse apiResponse=fetchDataFromApiAndPrint("1983-01", "1984-12",
                                         "0600", "clementi",12,
                                         12, 12);
        
        for(DataEntry entry : apiResponse.getData()){
            actualTimestamps.add(entry.getDate());
            actualValues.add(entry.getActualRainfall());
            predictedValues.add(entry.getPredictedRainfall());
        }

        // Create a response object containing both actual and predicted data
        RainfallDataResponse response = new RainfallDataResponse(actualTimestamps, actualValues, predictedValues);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/plot")
    public String showDashboard(Model model) throws MessagingException, IOException {
        scheduledService.scheduledTasks();
        model.addAttribute("message", "Hello, this is your dashboard!");
        return "plot";
    }

    @PostMapping("/queryRainfallInfo")
    public ResponseEntity<List<RainfallStatistics>> updateAdmin(@RequestBody RainfallDto rainfallDto) {
        rainfallService.findRainfallInfo(rainfallDto);
        return ResponseEntity.ok().body(rainfallService.findRainfallInfo(rainfallDto));
    }

    @PostMapping("/queryMonthlyRainfall")
    public ResponseEntity<List<RainfallMonthlyRate>> getAllMonthlyRainfall(@RequestBody RainfallMonthlyRate rainfallDto) {
        rainfallService.findRainfallMonthlyRate();
        return ResponseEntity.ok().body(rainfallService.findRainfallMonthlyRate());
    }

    @GetMapping("/getMonthlyRainfall")
    @ResponseBody
    public ResponseEntity<RainfallDataResponse> getAllMonthlyRainfall() {
        List<RainfallMonthlyRate> rainfallList = rainfallService.findRainfallMonthlyRate();
        
        List<String> timestamp = new ArrayList<>();
        List<Double> rate = new ArrayList<>();
        List<Double> predictedValues = new ArrayList<>();
       
        for (RainfallMonthlyRate rainfall : rainfallList) {
            timestamp.add(rainfall.getMonthly().toString());
            rate.add( rainfall.getdailyTotalRainfallRate());
        }

        // Create a response object containing both actual and predicted data
        RainfallDataResponse response = new RainfallDataResponse(timestamp, rate, predictedValues);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getRainfallMeanSquareError")
    @ResponseBody
    public ResponseEntity<RainfallDataResponse> getAllRainfallMeanSquareError(
    @RequestParam String modelNum,
    @RequestParam String station,
    @RequestParam String start_date,
    @RequestParam String end_date,
    @RequestParam int periods,
    @RequestParam int wRMSE,
    @RequestParam int wMAPE
    ) {
        List<RainfallMeanSquareError> rainfallList = rainfallService.findRainfallMeanSquareError();
        
        List<String> timestamp = new ArrayList<>();
        List<Double> twelveValues = new ArrayList<>();
        List<Double> sixtyValues = new ArrayList<>();
         //datadrift periods
        Map<String, List<Reference>> dataDriftPeriods= new HashMap<>();
        dataDriftPeriods=referenceService.getDataDriftPeriods();
        //past training dates
        List<String> pastTrainingDatesList =predictedModelService.findPastTrainingDates(station, modelNum);
        List<String> pastTrainingDates = new ArrayList<>();
        for (String pastTrainingDate : pastTrainingDatesList) {
            pastTrainingDates.add(pastTrainingDate);
            
        }
        ApiResponse apiResponse=fetchDataFromApiAndPrint(rainfallService.increaseMonth(start_date), end_date,
                                         modelNum, station,periods,
                                         wRMSE, wMAPE);
        
        for(DataEntry entry : apiResponse.getData()){
            timestamp.add(entry.getDate());
            twelveValues.add(entry.getRollingRMSE());
        }
       
        // for (RainfallMeanSquareError rainfall : rainfallList) {
        //     timestamp.add(rainfall.getMonthly().toString());
        //     twelveValues.add( rainfall.gettwelveRainfallRate());
        //     sixtyValues.add( rainfall.getsixtyRainfallRate());
        // }

        // Create a response object containing both actual and predicted data
        RainfallDataResponse response = new RainfallDataResponse(timestamp, twelveValues, sixtyValues,dataDriftPeriods,pastTrainingDates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getResiduals")
    @ResponseBody
    public ResponseEntity<RainfallDataResponse> getAllResiduals(
    @RequestParam String modelNum,
    @RequestParam String station,
    @RequestParam String start_date,
    @RequestParam String end_date,
    @RequestParam int periods,
    @RequestParam int wRMSE,
    @RequestParam int wMAPE
    ) {
        List<String> timestamp = new ArrayList<>();
        List<Double> residuals = new ArrayList<>();
        List<Double> values = new ArrayList<>();
         //datadrift periods
        Map<String, List<Reference>> dataDriftPeriods= new HashMap<>();
        dataDriftPeriods=referenceService.getDataDriftPeriods();
        //past training dates
        List<String> pastTrainingDatesList =predictedModelService.findPastTrainingDates(station, modelNum);
        List<String> pastTrainingDates = new ArrayList<>();
        for (String pastTrainingDate : pastTrainingDatesList) {
            pastTrainingDates.add(pastTrainingDate);
            
        }
        ApiResponse apiResponse=fetchDataFromApiAndPrint(rainfallService.increaseMonth(start_date), end_date,
                                         modelNum, station,periods,
                                         wRMSE, wMAPE);
        
        for(DataEntry entry : apiResponse.getData()){
            timestamp.add(entry.getDate());
            residuals.add(entry.getResiduals());
        }
        RainfallDataResponse response = new RainfallDataResponse(timestamp, residuals, values,dataDriftPeriods,pastTrainingDates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getRainfallMPSE")
    @ResponseBody
    public ResponseEntity<RainfallDataResponse> getAllRainfallMPSE(
    @RequestParam String modelNum,
    @RequestParam String station,
    @RequestParam String start_date,
    @RequestParam String end_date,
    @RequestParam int periods,
    @RequestParam int wRMSE,
    @RequestParam int wMAPE
    )  {
        List<RainfallMPSE> rainfallList = rainfallService.findRainfallMPSE();
        
        List<String> timestamp = new ArrayList<>();
        List<Double> mpseScore = new ArrayList<>();
        List<Double> otherValue = new ArrayList<>();
        //datadrift periods
        Map<String, List<Reference>> dataDriftPeriods= new HashMap<>();
        dataDriftPeriods=referenceService.getDataDriftPeriods();
        //past training dates
        List<String> pastTrainingDatesList =predictedModelService.findPastTrainingDates(station, modelNum);
        List<String> pastTrainingDates = new ArrayList<>();
        for (String pastTrainingDate : pastTrainingDatesList) {
            pastTrainingDates.add(pastTrainingDate);
            
        }

        ApiResponse apiResponse=fetchDataFromApiAndPrint(rainfallService.increaseMonth(start_date), end_date,
                                         modelNum, station,periods,
                                         wRMSE, wMAPE);
        
        for(DataEntry entry : apiResponse.getData()){
            timestamp.add(entry.getDate());
            mpseScore.add(entry.getMape());
        }
        // for (RainfallMPSE rainfall : rainfallList) {
        //     timestamp.add(rainfall.getMonthly().toString());
        //     mpseScore.add( rainfall.getmpseScore());
        // }
        
        RainfallDataResponse response = new RainfallDataResponse(timestamp, mpseScore, otherValue,dataDriftPeriods,pastTrainingDates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetch-data")
    public ApiResponse fetchDataFromApiAndPrint(@RequestParam String startDate, @RequestParam String endDate,
                                         @RequestParam String modelId, @RequestParam String seriesId,
                                         @RequestParam int periods,@RequestParam int wRMSE, @RequestParam int wMAPE) {
        ApiResponse response = rainfallService.fetchData(rainfallService.increaseMonth(startDate), endDate, modelId, seriesId,periods, wRMSE, wMAPE);

        for (DataEntry entry : response.getData()) {
            System.out.println("Date: " + entry.getDate());
            System.out.println("Actual Rainfall: " + entry.getActualRainfall());
            System.out.println("Predicted Rainfall: " + entry.getPredictedRainfall());
            System.out.println("Residuals: " + entry.getResiduals());
            System.out.println("Rolling RMSE: " + entry.getRollingRMSE());
            System.out.println("MAPE: " + entry.getMape());
            System.out.println("--------------------------------------");
        }
        return response;
    }
    
    @GetMapping("/getHistoryLog")
    @ResponseBody
    public  ResponseEntity<List<HistoryLog>> getAllHistoryLog(){
        List<HistoryLog> historyList= new ArrayList<>();
        
        // List<ModelTrainLog> modelTrainLogs = new ArrayList<>();
        // modelTrainLogs = modelTrainLogService.getAllModelTrainLogs();
        List<PredictionModel> modelLogs = new ArrayList<>();
        modelLogs=predictedModelService.getAllPredictionModels();

       for(PredictionModel data : modelLogs){
            HistoryLog log = new HistoryLog(data.getModelId(),data.getStation(), data.getModelNumber(),data.getModelType(), data.getStart_date(), data.getEnd_date(),data.isDefault());
            historyList.add(log);
        }
        return ResponseEntity.ok(historyList);
    }

    @GetMapping("/getDefaultModel")
    @ResponseBody
    public  ResponseEntity<List<DefaultModel>> getAllPredictedModels(){
        List<DefaultModel> defaultModels= new ArrayList<>();
        
        List<PredictionModel> predictionModels = new ArrayList<>();
        predictionModels = predictedModelService.getAllPredictionModels();
        
       for(PredictionModel data : predictionModels){
            DefaultModel log = new DefaultModel(data.getModelId(),data.getModelNumber(),data.getModelType(),data.getStation().getStationId(),data.getStation().getStationName(),data.getStation().getStationValue(),data.getStart_date(),data.getEnd_date(),data.isDefault());
            defaultModels.add(log);
        }
        return ResponseEntity.ok(defaultModels);
    }

}
