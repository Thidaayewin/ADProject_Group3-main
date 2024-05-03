package com.team3.weather.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseModel {


    protected boolean isDeleted;
    
    
    protected String createdBy;
    
    
    protected LocalDateTime createdTime;
    
    
    protected String lastUpdatedBy;
    
    
    protected LocalDateTime lastUpdatedTime;

    public BaseModel() {}

    public BaseModel(boolean isDeleted, String createdBy, LocalDateTime createdTime, String lastUpdatedBy, LocalDateTime lastUpdatedTime) {
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public boolean isActive(){
        return !this.isDeleted;
    }


    @Override
    public String toString() {
        return "Base Attributes(isDeleted=" +
                this.isDeleted() +
                ", createdBy=" +
                this.getCreatedBy() +
                ", createdTime=" +
                this.getCreatedTime() +
                ", lastUpdatedBy=" +
                this.getLastUpdatedBy() +
                ", lastUpdatedTime=" +
                this.getLastUpdatedTime() + ")";
    }
}
