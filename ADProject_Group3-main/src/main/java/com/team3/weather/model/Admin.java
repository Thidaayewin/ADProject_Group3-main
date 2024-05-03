package com.team3.weather.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@Entity
@DiscriminatorValue("Admin")
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Admin extends AccountHolder {

    public Admin() {
    }

    public Admin(
            String createdBy,
            LocalDateTime createdTime,
            String lastUpdatedBy,
            LocalDateTime lastUpdatedTime,
            String firstName,
            String lastName,
            String email,
            String password,
            String phone,
            String userNumber) {
        super(false, createdBy, createdTime, lastUpdatedBy, lastUpdatedTime, firstName, lastName, email, password,
                phone, userNumber);
    }

    @Override
    public String toString() {
        return "Admin" +
                "\n\t" +
                super.toString();
    }
}
