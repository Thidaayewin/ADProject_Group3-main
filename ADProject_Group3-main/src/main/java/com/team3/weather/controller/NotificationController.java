package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.model.Notification;
import com.team3.weather.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private final NotificationService notificationService;

    
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{notificationId}")
    public Notification getNotificationById(@PathVariable int notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

    @PostMapping
    public Notification saveNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable int notificationId) {
        notificationService.deleteNotification(notificationId);
    }
	
	@GetMapping("/notifications")
    public String showNotifications(Model model) {
        List<Notification> notifications = notificationService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        return "notification";
    }
}
