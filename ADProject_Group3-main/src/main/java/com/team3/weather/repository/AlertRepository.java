package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team3.weather.model.Alert;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
    // Add any custom query methods if needed
    @Query("SELECT a FROM Alert a WHERE a.active = ?1")
    List<Alert> findActiveAlert(boolean active);

}
