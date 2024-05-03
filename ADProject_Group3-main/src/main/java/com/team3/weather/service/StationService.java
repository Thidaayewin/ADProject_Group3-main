package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.Station;

public interface StationService {
    List<Station> getAllStations();
    Station getStationById(int stationId);
    Station createStation(Station station);
    Station updateStation(int stationId, Station station);
    void deleteStation(int stationId);
    List<Object[]> getDistinctStationValues();

    Station findByStationValue(String stationValue);
}
