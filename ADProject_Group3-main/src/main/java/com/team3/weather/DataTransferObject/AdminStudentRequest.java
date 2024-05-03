package com.team3.weather.DataTransferObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AdminStudentRequest {
    
    private int id;
    
    @NotBlank(message="First Name is required")
    private String firstName;
    
    @NotBlank(message="Last Name is required")
    private String lastName;
    
    @Email(message="Invalid email format")
    private String email;
    
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;
    
    @Pattern(regexp = "\\d{8}", message = "Phone number must be 8 digits")
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AdminStudentRequest(){

    }
    
    public AdminStudentRequest(int id, String firstName, String lastName, String email, String password, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
