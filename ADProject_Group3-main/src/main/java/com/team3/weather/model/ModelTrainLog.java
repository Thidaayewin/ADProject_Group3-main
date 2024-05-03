package com.team3.weather.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ModelTrainLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trainId;

    @ManyToOne
    @JoinColumn(name = "modelId")
    private PredictionModel predictionModel;

    private String start_date;
    private String end_date;
    private int wRMSE;
    private int wMAPE;
    private String createdBy;
    private LocalDateTime createdDate;

    public ModelTrainLog() {
    }

    public ModelTrainLog(PredictionModel predictionModel, String start_date, String end_date,
            int wRMSE, int wMAPE, String createdBy, LocalDateTime createdDate) {
       
        this.predictionModel = predictionModel;
        this.start_date = start_date;
        this.end_date = end_date;
        this.wRMSE = wRMSE;
        this.wMAPE = wMAPE;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public PredictionModel getPredictionModel() {
        return predictionModel;
    }

    public void setPredictionModel(PredictionModel predictionModel) {
        this.predictionModel = predictionModel;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getwRMSE() {
        return wRMSE;
    }

    public void setwRMSE(int wRMSE) {
        this.wRMSE = wRMSE;
    }

    public int getwMAPE() {
        return wMAPE;
    }

    public void setwMAPE(int wMAPE) {
        this.wMAPE = wMAPE;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ModelTrainLog{" +
                "trainId=" + trainId +
                ", predictionModel=" + predictionModel +
                ", createdDate=" + createdDate +
                '}';
    }
}
