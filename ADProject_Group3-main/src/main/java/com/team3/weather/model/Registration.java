package com.team3.weather.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Registration extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email(message = "Invalid email format")
    private String email;

    private String token;

    @NotNull
    private String registrationType;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @Pattern(regexp = "\\d{8}", message = "Phone number must be 8 digits")
    private String phone;
    // last name

    public Registration(
            String createdBy,
            LocalDateTime createdTime,
            String lastUpdatedBy,
            LocalDateTime lastUpdatedTime,
            String email,
            String registrationType,
            String firstName,
            String lastName,
            String phone) {
        super(false, createdBy, createdTime, lastUpdatedBy, lastUpdatedTime);
        this.email = email;
        this.registrationType = registrationType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
