package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.Notification;

public interface NotificationService {
    List<Notification> getAllNotifications();
    Notification getNotificationById(int notificationId);
    Notification saveNotification(Notification notification);
    void deleteNotification(int notificationId);
}
