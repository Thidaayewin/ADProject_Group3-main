package com.team3.weather.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.team3.weather.model.PredictionModel;
import com.team3.weather.service.PredictionModelService;

@Controller
public class MenuController {
    
    @Autowired
    private final PredictionModelService predictionModelService;

    
    public MenuController(PredictionModelService predictionModelService) {
        this.predictionModelService = predictionModelService;
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("dashboardTitle", "Dashboard");
        model.addAttribute("dashboardActive", true);
        model.addAttribute("content", "dashboard"); 
        model.addAttribute("linePlot", "line_plot"); 
        model.addAttribute("gradientPlot", "gradient_plot"); 
        model.addAttribute("scatterPlot", "residual_scatter"); 
        model.addAttribute("monthlyPlot", "meanSquareError_plot"); 
        model.addAttribute("mpsePlot", "mpse_plot"); 
        return "main-layout"; 
    }

    @GetMapping("/modelDrift")
    public String showModelDrift(Model model){
        model.addAttribute("dashboardTitle", "Dashboard");
        model.addAttribute("modelDriftActive", true);
        model.addAttribute("content", "model_drift"); 
        model.addAttribute("modelPlot", "model_plot"); 
        return "main-layout"; 
    }

    @GetMapping("/conceptDrift")
    public String showConceptDrift(Model model) {
        model.addAttribute("dashboardTitle", "Dashboard");
        model.addAttribute("driftActive", true);
        
        model.addAttribute("driftPlot", "drift_plot"); 
        return "main-layout"; 
    }

    @GetMapping("/defaultmodel")
    public String showDefaultModelSelection(Model model) {
        model.addAttribute("dashboardTitle", "Default Model Selection");
        model.addAttribute("modelSelectionActive", true);
        model.addAttribute("content", "defaultmodel"); 
       
        return "main-layout"; 
    }

    @GetMapping("/userprofile")
    public String showUserProfile(Model model) {
        model.addAttribute("dashboardTitle", "User Profile");
        model.addAttribute("userProfileActive", true);
        model.addAttribute("content", "userprofile"); 
        return "main-layout"; 
    }

    @GetMapping("/modelPrediction")
    public String showModelPrediction(Model model) {
        model.addAttribute("dashboardTitle", "Model Prediction");
        model.addAttribute("modelPredictionActive", true);
        model.addAttribute("content", "model-training"); 
        return "main-layout"; 
    }

    @GetMapping("/modelReview")
    public String showModelReview(Model model) {
        // PredictionModel defaultModel = predictionModelService.getDefaultPredictionModel();
        // List<PredictionModel> allModels = predictionModelService.getAllPredictionModels();
    
        // if (defaultModel == null && !allModels.isEmpty()) {
        //     // If there is no default model, set the first model as the default model
        //     defaultModel = allModels.get(0);
        // }
        model.addAttribute("dashboardTitle", "Model Review");
        model.addAttribute("modelReviewActive", true);
        model.addAttribute("content", "training-result"); 
    
        return "main-layout"; 
    }

    // @PostMapping("/modelReview")
    // public String setDefaultModel(@RequestParam(name = "modelId") int modelId) {
    //     predictionModelService.setDefaultPredictionModel(modelId);
    //     return "main-layout"; 
    // }

    @GetMapping("/notify")
    public String showNotifications(Model model) {
        model.addAttribute("dashboardTitle", "Notifications");
        model.addAttribute("notificationsActive", true);
        model.addAttribute("content", "notification"); 
        return "main-layout"; 
    }

    @GetMapping("/alert")
    public String showAlertSettings(Model model) {
        model.addAttribute("dashboardTitle", "Alert Settings");
        model.addAttribute("alertActive", true);
        model.addAttribute("content", "alert-setting"); 
        return "main-layout"; 
    }

    @GetMapping("/userAccountManagement")
    public String showUserAccountManagement(Model model) {
        model.addAttribute("dashboardTitle", "User Account Management");
        model.addAttribute("userAccountManagementActive", true);
        model.addAttribute("content", "user-account-manage"); 
        return "main-layout"; 
    }

    
}
