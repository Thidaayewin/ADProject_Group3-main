package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // You can add custom query methods here if needed
}
