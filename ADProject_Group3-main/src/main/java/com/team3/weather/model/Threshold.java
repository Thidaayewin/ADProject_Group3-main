package com.team3.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import com.team3.weather.util.ThresholdType;

@Entity
@Data
public class Threshold extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private ThresholdType type;
    private String metric;
    private double statistic;

    // Default constructor
    public Threshold() {
    }

    // Parameterized constructor
    public Threshold(
            String createdBy,
            LocalDateTime createdTime,
            String lastUpdatedBy,
            LocalDateTime lastUpdatedTime,
            ThresholdType type,
            String metric,
            double statistic
            ) {
        super(false, createdBy, createdTime, lastUpdatedBy, lastUpdatedTime);
        this.type = type;
        this.metric = metric;
        this.statistic = statistic;
    }

    // TODO: tostring
    @Override
    public String toString() {
        return "Threshold{" +
                "id=" + id +
                ", type=" + type +
                ", metric='" + metric + '\'' +
                ", statistic=" + statistic +
                ", isDeleted=" + isDeleted +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedTime=" + lastUpdatedTime +
                '}';
    }
}
