package com.team3.weather.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.team3.weather.DataTransferObject.RegistrationDto;
import com.team3.weather.exception.InvalidTokenException;
import com.team3.weather.exception.RegistrationException;
import com.team3.weather.model.Registration;
import com.team3.weather.service.RegistrationService;

@Controller
public class RegisterController {

    @Autowired
    private RegistrationService registrationService;

    
    RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/register/invite")
    public ResponseEntity<String> inviteUser(@Valid @RequestBody Registration newUserRequest) {
        System.out.println(newUserRequest);
        try {
            registrationService.inviteUser(newUserRequest);
        } catch (RegistrationException ere) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ere.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Registration request created successfully");
    }

    @GetMapping("/register/complete")
    public String showRegistrationForm(@RequestParam String token, Model model) {
        if (!registrationService.isTokenValid(token)) {
            model.addAttribute("error", "Token is invalid or has expired. Please contact Administrator for assistance");
            return "registration-error";
        }

        RegistrationDto newUser = new RegistrationDto();
        newUser.setRegistrationToken(token);
        model.addAttribute("user", newUser);
        return "registration-form";
    }

    @PostMapping("/register/complete")
    public String completeRegistration(RegistrationDto newUser, Model model) {

        try {
            registrationService.completeRegistration(newUser);
            return "redirect:/login?created";
        } catch (InvalidTokenException eite) {
            model.addAttribute("error", "Token is invalid or has expired. Please contact Administrator for assistance");
            return "registration-error";
        } catch (Exception e) {
            model.addAttribute("error",
                    "An error has occured, please try again. If the error persists, please contact Administrator for assistance");
            return "registration-error";
        }

    }

}
