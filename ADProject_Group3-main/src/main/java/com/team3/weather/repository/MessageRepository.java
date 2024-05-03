package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.MessageDO;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.RainfallStatistics;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageDO, Integer> {
    // Add custom query methods if needed
}
