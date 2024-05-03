package com.team3.weather.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@DiscriminatorValue("User")
@Data
public class User extends AccountHolder {

    public User() {
    }

    public User(
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
        return "User" +
                "\n\t" +
                super.toString();
    }
}
