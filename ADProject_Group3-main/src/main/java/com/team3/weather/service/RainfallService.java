package com.team3.weather.service;

import java.io.IOException;
import java.util.List;

import com.team3.weather.DataTransferObject.ApiResponse;
import com.team3.weather.DataTransferObject.RainfallData;
import com.team3.weather.DataTransferObject.RainfallDto;
import com.team3.weather.DataTransferObject.RainfallMPSE;
import com.team3.weather.DataTransferObject.RainfallMeanSquareError;
import com.team3.weather.DataTransferObject.RainfallMonthlyRate;
import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.RainfallStatistics;

public interface RainfallService {
    List<Rainfall> getAllRainfall();
    Rainfall getRainfallById(int dataId);
    Rainfall createRainfall(Rainfall rainfall);
    Rainfall updateRainfall(int dataId, Rainfall rainfall);
    void deleteRainfall(int dataId);
    List<RainfallStatistics> findRainfallInfo(RainfallDto rainfallDto);
    List<RainfallMonthlyRate> findRainfallMonthlyRate();
    List<RainfallMeanSquareError> findRainfallMeanSquareError();
    List<RainfallMPSE> findRainfallMPSE();
    List<RainfallData> monthlyRainfallRate();
    ApiResponse fetchData(String startDate, String endDate, String modelId, String seriesId,int periods, int wRMSE, int wMAPE);
    ApiResponse AndroidfetchData(String startDate, String endDate, String modelId, String seriesId,int periods, int wRMSE, int wMAPE);
    List<Rainfall> mostRecentMonth(int stationId);

    String increaseMonth(String start_date);
    
}
