package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.Threshold;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, Integer> {
    // Add custom query methods if needed
}
