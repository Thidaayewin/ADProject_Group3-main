package com.team3.weather.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team3.weather.DataTransferObject.ApiResponse;
import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.Station;
import com.team3.weather.service.PredictionModelService;
import com.team3.weather.service.RainfallService;
import com.team3.weather.service.StationService;

@RestController
@RequestMapping("/api")
public class PredictionModelApi {

    @Autowired
    private final PredictionModelService predictionModelService;

    @Autowired
    private final StationService stationService;

    @Autowired
    private final RainfallService rainfallService;

    
    public PredictionModelApi(PredictionModelService predictionModelService, StationService stationService,RainfallService rainfallService) {
        this.predictionModelService = predictionModelService;
        this.stationService=stationService;
        this.rainfallService=rainfallService;
    }

    @GetMapping("/getDefaultModel")
    public PredictionModel getDefaultModel() {
        return predictionModelService.getDefaultPredictionModel();
    }

    //  @GetMapping("/getPredictionModel")
    // public PredictionModel getPredictionModel(
    //         @RequestParam("stationId") int stationId,
    //         @RequestParam("predictionPeriod") int predictionPeriod) {
    //     // Call your service method to get the prediction model based on the parameters
    //     // You can use stationId and predictionPeriod to fetch the appropriate prediction model
    //     return predictionModelService.getPredictionModelByStationAndPeriod(stationId, predictionPeriod);
    // }

    @PostMapping("/getPredictionModel")
    public ResponseEntity<ApiResponse> processNeuralProphetRequest(
            @RequestParam("stationId") int stationId,   
            @RequestParam("predictionPeriod") int predictionPeriod) {
        
        
        Station station = stationService.getStationById(stationId);
        PredictionModel model = predictionModelService.getDefaultPredictionModelByStation(station);
        List<Rainfall> mostRecentmonth = rainfallService.mostRecentMonth(stationId);
        Rainfall firstRecord = new Rainfall();
        Rainfall lastRecord = new Rainfall();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM", Locale.US);
        String currentYearMonth = yearMonthFormat.format(calendar.getTime());

        if (!mostRecentmonth.isEmpty()) {
            // Find the index of the record with the current month's timestamp
            int currentIndex = -1;
            for (int i = 0; i < mostRecentmonth.size(); i++) {
                String timestamp = mostRecentmonth.get(i).getTimestamp(); // Assuming timestamp is in "YYYY-MM" format
        
                if (timestamp.equals(currentYearMonth)) {
                    currentIndex = i;
                    break;
                }
            }

            // if (currentIndex != -1) {
            //     int firstRecordIndex = currentIndex - predictionPeriod;
                firstRecord = mostRecentmonth.get(0);
            // }

            int lastIndex = currentIndex + 12 ;            

            lastRecord = mostRecentmonth.get(lastIndex);
        }


        ApiResponse apiResponse=rainfallService.AndroidfetchData(lastRecord.getTimestamp(),firstRecord.getTimestamp() ,model.getModelNumber(), station.getStationValue(),predictionPeriod,12, 12);
        
        return ResponseEntity.ok(apiResponse);
    }
}
