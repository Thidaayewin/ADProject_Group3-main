package com.team3.weather.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.weather.model.Station;
import com.team3.weather.repository.StationRepository;
import com.team3.weather.service.StationService;

import java.util.List;
import java.util.Optional;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private final StationRepository stationRepository;

    
    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @Override
    public Station getStationById(int stationId) {
        return stationRepository.findById(stationId).orElse(null);
    }

    @Override
    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    @Override
    public Station updateStation(int stationId, Station station) {
        Optional<Station> existingStation = stationRepository.findById(stationId);
        if (existingStation.isPresent()) {
            Station updatedStation = existingStation.get();
            updatedStation.setStationName(station.getStationName());
            updatedStation.setLatitude(station.getLatitude());
            updatedStation.setLongitude(station.getLongitude());
            return stationRepository.save(updatedStation);
        }
        return null;
    }

    @Override
    public void deleteStation(int stationId) {
        stationRepository.deleteById(stationId);
    }

    @Override
    public List<Object[]> getDistinctStationValues() {
        return stationRepository.findDistinctStationValues();
    }

    @Override
    public Station findByStationValue(String stationValue) {
        return stationRepository.findByStationValue(stationValue);
    }
}
