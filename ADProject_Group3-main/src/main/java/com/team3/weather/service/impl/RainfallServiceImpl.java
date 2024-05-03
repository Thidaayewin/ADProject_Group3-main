package com.team3.weather.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.weather.DataTransferObject.ApiResponse;
import com.team3.weather.DataTransferObject.DataEntry;
import com.team3.weather.DataTransferObject.DataPoint;
import com.team3.weather.DataTransferObject.DataPointsWrapper;
import com.team3.weather.DataTransferObject.RainfallData;
import com.team3.weather.DataTransferObject.RainfallDto;
import com.team3.weather.DataTransferObject.RainfallMPSE;
import com.team3.weather.DataTransferObject.RainfallMeanSquareError;
import com.team3.weather.DataTransferObject.RainfallMonthlyRate;
import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.RainfallStatistics;
import com.team3.weather.model.Station;
import com.team3.weather.repository.RainfallRepository;
import com.team3.weather.repository.StationRepository;
import com.team3.weather.service.RainfallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map;

@Service
public class RainfallServiceImpl implements RainfallService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private final RainfallRepository rainfallRepository;
    @Autowired
    private final StationRepository stationRepository;

    private final String API_URL = "http://8.222.245.68:8080/researcher/neuralprophet";

    
    public RainfallServiceImpl(RainfallRepository rainfallRepository,StationRepository stationRepository) {
        this.rainfallRepository = rainfallRepository;
        this.stationRepository=stationRepository;
    }

    @Override
    public List<Rainfall> getAllRainfall() {
        return rainfallRepository.findAll();
    }

    @Override
    public Rainfall getRainfallById(int dataId) {
        return rainfallRepository.findById(dataId).orElse(null);
    }

    @Override
    public Rainfall createRainfall(Rainfall rainfall) {
        return rainfallRepository.save(rainfall);
    }

    @Override
    public Rainfall updateRainfall(int dataId, Rainfall rainfall) {
        Optional<Rainfall> existingRainfall = rainfallRepository.findById(dataId);
        if (existingRainfall.isPresent()) {
            Rainfall updatedRainfall = existingRainfall.get();
            updatedRainfall.setStation(rainfall.getStation());
            updatedRainfall.setTimestamp(rainfall.getTimestamp());
            updatedRainfall.setActualRainfallAmount(rainfall.getActualRainfallAmount());
            return rainfallRepository.save(updatedRainfall);
        }
        return null;
    }

    @Override
    public void deleteRainfall(int dataId) {
        rainfallRepository.deleteById(dataId);
    }

    public List<RainfallStatistics> findRainfallInfo(RainfallDto rainfallDto) {
        List<RainfallStatistics> actual = new ArrayList<>();
        String format;
        switch (rainfallDto.getDateType()) {
            case "day":
                format = "%Y-%m-%d";
                break;
            case "month":
                format = "%Y-%m";
                break;
            case "year":
                format = "%Y";
                break;
            default:
                format = "%Y-%m-%d";
                break;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(rainfallDto.getStartTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(rainfallDto.getEndTime(), formatter);
        // List<RainfallStatistics> actual = rainfallRepository.findRainfallInfo(startTime,
        //         endTime, format, rainfallDto.getStationId());
        // call python
        // todo
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("startTime", rainfallDto.getStartTime());
        uriVariables.put("endTime", rainfallDto.getEndTime());
        uriVariables.put("stationId", rainfallDto.getStationId());
        uriVariables.put("modelId", rainfallDto.getModelId());
        uriVariables.put("RMSE", rainfallDto.getRmse());
        uriVariables.put("mape", rainfallDto.getMape());
        List<RainfallStatistics> expectant = restTemplate.getForObject("url", List.class, uriVariables);
        actual.addAll(expectant);
        return actual;
    }

    @Override
    public List<RainfallMonthlyRate> findRainfallMonthlyRate() {
       
        List<RainfallMonthlyRate> rainfallList = new ArrayList<>();
         String[] months = {
            "2009-01", "2009-02", "2009-03", "2009-04", "2009-05", "2009-06",
            "2009-07", "2009-08", "2009-09", "2009-10", "2009-11", "2009-12",
            "2010-01", "2010-02", "2010-03", "2010-04", "2010-05", "2010-06",
            "2010-07", "2010-08", "2010-09", "2010-10", "2010-11", "2010-12"
        };

        

        double[] dailyRainfallRates = {
            0.8, 148, 354.5, 148.8, 205.6, 92, 103, 90.2, 67.6,
            160, 272.6, 100, 108.8, 84.4, 307, 472.6, 176.8, 312.2,
            394.6, 231.4, 184.5, 206.2, 377.2, 129.6
        };

       
        for (int i = 0; i < months.length; i++) {
            RainfallMonthlyRate rainfall = new RainfallMonthlyRate(months[i], dailyRainfallRates[i]);
            rainfallList.add(rainfall);
        }
         return rainfallList;
    }

    @Override
    public List<RainfallMeanSquareError> findRainfallMeanSquareError() {
        List<RainfallMeanSquareError> rainfallList = new ArrayList<>();
         String[] months = {
            "2009-01", "2009-02", "2009-03", "2009-04", "2009-05", "2009-06",
            "2009-07", "2009-08", "2009-09", "2009-10", "2009-11", "2009-12",
            "2010-01", "2010-02", "2010-03", "2010-04", "2010-05", "2010-06",
            "2010-07", "2010-08", "2010-09", "2010-10", "2010-11", "2010-12"
        };

        double[] dailytwelveRainfallRates = {
            0.8, 148, 354.5, 148.8, 205.6, 92, 103, 90.2, 67.6,
            160, 272.6, 100, 108.8, 84.4, 307, 472.6, 176.8, 312.2,
            394.6, 231.4, 184.5, 206.2, 377.2, 129.6
        };

        double[] dailysixtyRainfallRates = {
            0.8, 148, 354.5, 148.8, 205.6, 92, 103, 90.2, 67.6,
            160, 272.6, 100, 108.8, 84.4, 307, 472.6, 176.8, 312.2,
            394.6, 231.4, 184.5, 206.2, 377.2, 129.6
        };

        for (int i = 0; i < months.length; i++) {
            RainfallMeanSquareError rainfall = new RainfallMeanSquareError(months[i], dailytwelveRainfallRates[i],dailysixtyRainfallRates[i]);
            rainfallList.add(rainfall);
        }
         return rainfallList;
    }

    @Override
    public List<RainfallMPSE> findRainfallMPSE() {
       List<RainfallMPSE> rainfallList = new ArrayList<>();
         String[] months = {
            "2009-01", "2009-02", "2009-03", "2009-04", "2009-05", "2009-06",
            "2009-07", "2009-08", "2009-09", "2009-10", "2009-11", "2009-12",
            "2010-01", "2010-02", "2010-03", "2010-04", "2010-05", "2010-06",
            "2010-07", "2010-08", "2010-09", "2010-10", "2010-11", "2010-12"
        };

        double[] dailytwelveRainfallRates = {
            0.8, 148, 354.5, 148.8, 205.6, 92, 103, 90.2, 67.6,
            160, 272.6, 100, 108.8, 84.4, 307, 472.6, 176.8, 312.2,
            394.6, 231.4, 184.5, 206.2, 377.2, 129.6
        };

        for (int i = 0; i < months.length; i++) {
            RainfallMPSE rainfall = new RainfallMPSE(months[i], dailytwelveRainfallRates[i]);
            rainfallList.add(rainfall);
        }
         return rainfallList;
    }

    @Override
    public List<RainfallData> monthlyRainfallRate() {
        List<RainfallData> list = new ArrayList<>();
        List<Rainfall> listRainfall = rainfallRepository.findAll();
        
        Map<String,List<Double>> monthlyDataMap = new HashMap<>();
        
        for(Rainfall rainfall:listRainfall){
            double value = rainfall.getActualRainfallAmount();
            String dateStr = rainfall.getTimestamp().toString().substring(0, 7);
            monthlyDataMap.computeIfAbsent(dateStr, k -> new ArrayList<>()).add(value);
        }
        
        for (Map.Entry<String, List<Double>> entry : monthlyDataMap.entrySet()) {
            String monthlyDate = entry.getKey();
            List<Double> values = entry.getValue();
            double sum = values.stream().mapToDouble(Double::doubleValue).sum();
            RainfallData data = new RainfallData(monthlyDate, sum);
            list.add(data);
        }
        return list;
    }


    //For researcher
    public ApiResponse fetchData(String startDate, String endDate, String modelId, String seriesId,int periods, int wRMSE, int wMAPE) {
                String API_URL="http://8.222.245.68:8080/researcher/neuralprophet";

                String apiUrl = API_URL + "?sDate=" + startDate + "&eDate=" + endDate + "&mId=" + modelId
                + "&sId=" + seriesId +"&wRMSE=" + wRMSE + "&wMAPE=" + wMAPE;

        
        String[] parts = startDate.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        YearMonth yearMonth = YearMonth.of(year, month);
        System.out.println(yearMonth); 
        LocalDate sd = yearMonth.atDay(1);

        String[] partss = endDate.split("-");
        int years = Integer.parseInt(partss[0]);
        int months = Integer.parseInt(partss[1]);

        YearMonth yearMonths = YearMonth.of(years, months);
        System.out.println(yearMonths); 

        LocalDate ed = yearMonths.atDay(1);
                ApiResponse response = new ApiResponse();
                RestTemplate restTemplate = new RestTemplate();
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                List<DataPoint> dataEntries= new ArrayList<>();
                dataEntries = loadDataPointsFromJson(seriesId);
                JSONArray jsonArray = new JSONArray();
                for (DataPoint dataEntry : dataEntries) {
                    String dsStr = dataEntry.getDs();
                    
        String[] partsss = dsStr.split("-");
        int yearss = Integer.parseInt(partsss[0]);
        int monthss = Integer.parseInt(partsss[1]);

        YearMonth yearMonthss = YearMonth.of(yearss, monthss);

        LocalDate dsDate = yearMonthss.atDay(1);
                    if (!dsDate.isBefore(sd) && !dsDate.isAfter(ed)) {
                        jsonArray.add(dataEntry.toJsonObject());
                    }
                }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
    
        String responseBody = responseEntity.getBody();
        responseBody = responseBody.replace(":NaN", ":null");
        
        
        JSONObject result = JSON.parseObject(responseBody);
        if (result != null) {
            JSONArray rainfallArray = result.getJSONArray("data");
            
            List<DataEntry> dataEntryList = new ArrayList<>();
            
            for (int i = 0; i < rainfallArray.size(); i++) {
                JSONObject rainfallTempJson = rainfallArray.getJSONObject(i);

                double actualRainfall = rainfallTempJson.get("Actual Rainfall (mm)") == null ?
                        0.0 : rainfallTempJson.getDouble("Actual Rainfall (mm)");

                String date = rainfallTempJson.get("Date") == null ?
                        "" : rainfallTempJson.getString("Date");

                double predictedRainfall = rainfallTempJson.get("Predicted Rainfall (mm)") == null ?
                        0.0 : rainfallTempJson.getDouble("Predicted Rainfall (mm)");

                double residuals = rainfallTempJson.get("Residuals") == null ?
                        0.0 : rainfallTempJson.getDouble("Residuals");

                double rollingRMSE = rainfallTempJson.get("Rolling RMSE") == null ?
                        0.0 : rainfallTempJson.getDouble("Rolling RMSE");

                double mape = rainfallTempJson.get("MAPE") == null ?
                        0.0 : rainfallTempJson.getDouble("MAPE");

                DataEntry dataEntry = new DataEntry(actualRainfall, date, predictedRainfall, residuals, rollingRMSE, mape);
                dataEntryList.add(dataEntry);
            }
            response.setData(dataEntryList);
            
        }
        return response;
    }

    //FOR USER
    public ApiResponse AndroidfetchData(String startDate, String endDate, String modelId, String seriesId,int periods, int wRMSE, int wMAPE) {
       
        String API_URL="http://8.222.245.68:8080/user/neuralprophet";
         String apiUrl = API_URL + "?sDate=" + startDate + "&eDate=" + endDate + "&mId=" + modelId
                + "&sId=" + seriesId + "&periods="+periods+ "&wRMSE=" + wRMSE + "&wMAPE=" + wMAPE;

        String[] parts = startDate.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        YearMonth yearMonth = YearMonth.of(year, month);
        System.out.println(yearMonth); 
        LocalDate sd = yearMonth.atDay(1);

        String[] partss = endDate.split("-");
        int years = Integer.parseInt(partss[0]);
        int months = Integer.parseInt(partss[1]);

        YearMonth yearMonths = YearMonth.of(years, months);
        System.out.println(yearMonths); 

        LocalDate ed = yearMonths.atDay(1);
                ApiResponse response = new ApiResponse();
                RestTemplate restTemplate = new RestTemplate();
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                List<DataPoint> dataEntries= new ArrayList<>();
                dataEntries = loadDataPointsFromJson(seriesId);
                JSONArray jsonArray = new JSONArray();
                for (DataPoint dataEntry : dataEntries) {
                    String dsStr = dataEntry.getDs();
                    
        String[] partsss = dsStr.split("-");
        int yearss = Integer.parseInt(partsss[0]);
        int monthss = Integer.parseInt(partsss[1]);

        YearMonth yearMonthss = YearMonth.of(yearss, monthss);

        LocalDate dsDate = yearMonthss.atDay(1);
                    if (!dsDate.isBefore(sd) && !dsDate.isAfter(ed)) {
                        jsonArray.add(dataEntry.toJsonObject());
                    }
                }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
    
        String responseBody = responseEntity.getBody();
        responseBody = responseBody.replace(":NaN", ":null");
        
        
        JSONObject result = JSON.parseObject(responseBody);
        if (result != null) {
            JSONArray rainfallArray = result.getJSONArray("data");
            
            List<DataEntry> dataEntryList = new ArrayList<>();
            
            for (int i = 0; i < rainfallArray.size(); i++) {
                JSONObject rainfallTempJson = rainfallArray.getJSONObject(i);

                double actualRainfall = rainfallTempJson.get("Actual Rainfall (mm)") == null ?
                        0.0 : rainfallTempJson.getDouble("Actual Rainfall (mm)");

                String date = rainfallTempJson.get("Date") == null ?
                        "" : rainfallTempJson.getString("Date");

                double predictedRainfall = rainfallTempJson.get("Predicted Rainfall (mm)") == null ?
                        0.0 : rainfallTempJson.getDouble("Predicted Rainfall (mm)");

                double residuals = rainfallTempJson.get("Residuals") == null ?
                        0.0 : rainfallTempJson.getDouble("Residuals");

                double rollingRMSE = rainfallTempJson.get("Rolling RMSE") == null ?
                        0.0 : rainfallTempJson.getDouble("Rolling RMSE");

                double mape = rainfallTempJson.get("MAPE") == null ?
                        0.0 : rainfallTempJson.getDouble("MAPE");

                DataEntry dataEntry = new DataEntry(actualRainfall, date, predictedRainfall, residuals, rollingRMSE, mape);
                dataEntryList.add(dataEntry);
            }
            response.setData(dataEntryList);
            
        }
        return response;
    }

    // public List<DataPoint> loadDataPointsFromJson() throws IOException {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     URL resourceUrl = getClass().getClassLoader().getResource("data.json");
    //     System.out.println("Resource URL: " + resourceUrl);
    //     try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json")) {
    //         if (inputStream != null) {
    //             DataPointsWrapper wrapper = objectMapper.readValue(inputStream, DataPointsWrapper.class);
    //             return wrapper.getData();
    //         } else {
    //             throw new IOException("data.json resource not found");
    //         }
    //     }
    // }

    public List<DataPoint> loadDataPointsFromJson(String stationValue){
        List<DataPoint> dataPoints = new ArrayList<>();
         Station station = stationRepository.findByStationValue(stationValue);
         List<Rainfall> rainfall = rainfallRepository.findAll();

        for(Rainfall data:rainfall){
            if(data.getStation().getStationId() == station.getStationId()){
                DataPoint dataPoint = new DataPoint(data.getTimestamp(), data.getActualRainfallAmount());
                dataPoints.add(dataPoint);
            }
        }
        return dataPoints;
    }

    @Override
    public List<Rainfall> mostRecentMonth(int stationId) {
        return rainfallRepository.findMostRecent12MonthsByStationId(stationId);
    }

    
    @Override
    public String increaseMonth(String start_date){
        String[] parts = start_date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        YearMonth yearMonth = YearMonth.of(year, month);
        
        LocalDate nextMonth = yearMonth.minusMonths(1).atDay(1);
        String[] partss = nextMonth.toString().split("-");
        return partss[0]+"-"+partss[1];
    }
    
}
