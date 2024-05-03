package com.team3.weather.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import com.team3.weather.DataTransferObject.RegistrationDto;
import com.team3.weather.exception.InvalidTokenException;
import com.team3.weather.exception.RegistrationException;
import com.team3.weather.model.Registration;
import com.team3.weather.repository.AccountHolderRepository;
import com.team3.weather.repository.RegistrationRepository;
import com.team3.weather.security.SecurityUtil;
import com.team3.weather.util.Hasher;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class RegistrationService {
    private static final int EXPIRATION_HOURS = 48;

    @Value("${domain.foremail}")
    private String domain;

    private RegistrationRepository registrationRepository;
    private AccountHolderRepository accountHolderRepository;
    private EmailService emailService;
    private UserService userService;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository,
            AccountHolderRepository accountHolderRepository,
            EmailService emailService,
            UserService userService) {
        this.registrationRepository = registrationRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Transactional
    public void inviteUser(Registration newUserRequest)
            throws RegistrationException {
        if (UserAlreadyExists(newUserRequest.getEmail())) {
            throw new RegistrationException("There is an account under this email address");
        }

        String token = UUID.randomUUID().toString();
        String link = domain + "/register/complete?token=" + token;

        invalidatePreviousInvitation(newUserRequest.getEmail());
        try {
            newUserRequest.setToken(Hasher.hashToken(token));
            newUserRequest.setCreatedTime(LocalDateTime.now());
            newUserRequest.setCreatedBy(SecurityUtil.getSessionUser());
            newUserRequest.setLastUpdatedTime(LocalDateTime.now());
            newUserRequest.setLastUpdatedBy(SecurityUtil.getSessionUser());
            registrationRepository.save(newUserRequest);
            emailService.sendAccountCreationEmail(newUserRequest.getEmail(),
                    newUserRequest.getFirstName() + " " + newUserRequest.getLastName(), link);
        } catch (IOException eio) {
            System.out.println("Email template cannot be read");
            throw new RegistrationException("Error sending email");
        } catch (MessagingException emse) {
            System.out.println("Error setting email message");
            throw new RegistrationException("Error sending email");
        } catch (MailException eme) {
            System.out.println("Email service failed");
            throw new RegistrationException("Error sending email");
        }

    }

    private boolean UserAlreadyExists(String email) {
        return accountHolderRepository.existsByEmail(email);
    }

    @Transactional
    private void invalidatePreviousInvitation(String email) {
        Registration registration = registrationRepository.findByEmailAndIsDeleted(email, false);
        if (registration != null) {
            registration.setDeleted(true);
            registration.setLastUpdatedBy("System");
            registration.setLastUpdatedTime(LocalDateTime.now());
            registrationRepository.saveAndFlush(registration);
        }

    }

    @Transactional
    public boolean completeRegistration(RegistrationDto newUserDetails) throws InvalidTokenException {
        String token = newUserDetails.getRegistrationToken();
        if (!isTokenValid(token))
            throw new InvalidTokenException("Token is not valid or has expired");

        Registration newUser = registrationRepository.findByTokenAndIsDeleted(Hasher.hashToken(token), false);

        switch (newUser.getRegistrationType()) {
            case "Admin":
                userService.createAdmin(newUser, newUserDetails.getPassword());
                break;
            // case "Lecturer":
            //     userService.createLecturer(newUser, newUserDetails.getPassword());
            //     break;
            // case "Student":
            //     userService.createStudent(newUser, newUserDetails.getPassword());
            //     break;
        }

        newUser.setDeleted(true);
        newUser.setLastUpdatedBy("Registration System");
        newUser.setLastUpdatedTime(LocalDateTime.now());
        registrationRepository.save(newUser);
        return true;
    }

    public boolean isTokenValid(String token) {
        Registration registration = registrationRepository.findByTokenAndIsDeleted(Hasher.hashToken(token), false);
        if (registration == null)
            return false;

        LocalDateTime expirationTime = registration.getCreatedTime().plusHours(EXPIRATION_HOURS);
        return LocalDateTime.now().isBefore(expirationTime);

    }

}
