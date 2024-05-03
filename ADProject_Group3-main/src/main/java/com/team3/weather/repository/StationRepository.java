package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.Station;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    Station findByStationName(String stationName);
    Station findByStationValue(String stationValue);
    Station findByStationId(int stationId);

     @Query("SELECT DISTINCT s.stationName,s.stationValue FROM Station s")
    List<Object[]> findDistinctStationValues();
} 
