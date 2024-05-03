package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team3.weather.model.ModelTrainLog;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelTrainLogRepository extends JpaRepository<ModelTrainLog, Integer> {
    
}
