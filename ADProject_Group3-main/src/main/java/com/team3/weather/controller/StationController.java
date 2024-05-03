package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.model.Station;
import com.team3.weather.service.StationService;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    @Autowired
    private final StationService stationService;

    
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<Station> getStationById(@PathVariable int stationId) {
        Station station = stationService.getStationById(stationId);
        if (station != null) {
            return new ResponseEntity<>(station, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        Station createdStation = stationService.createStation(station);
        return new ResponseEntity<>(createdStation, HttpStatus.CREATED);
    }

    @PutMapping("/{stationId}")
    public ResponseEntity<Station> updateStation(@PathVariable int stationId, @RequestBody Station station) {
        Station updatedStation = stationService.updateStation(stationId, station);
        if (updatedStation != null) {
            return new ResponseEntity<>(updatedStation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable int stationId) {
        stationService.deleteStation(stationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
