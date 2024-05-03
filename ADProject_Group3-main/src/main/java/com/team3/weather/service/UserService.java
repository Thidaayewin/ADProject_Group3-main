package com.team3.weather.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.team3.weather.model.AccountHolder;
import com.team3.weather.model.Admin;
import com.team3.weather.model.Registration;
import com.team3.weather.repository.AccountHolderRepository;
import com.team3.weather.repository.AdminRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private AdminRepository adminRepository;
    private AccountHolderRepository accountHolderRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            AdminRepository adminRepository,
            AccountHolderRepository accountHolderRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountHolder findByEmail(String email) {
        AccountHolder accountHolder = accountHolderRepository.findByEmail(email);
        if (accountHolder != null) {
            return accountHolder;
        }
        return null;
    }

    @Transactional
    public Boolean createAdmin(Registration newUser, String password) {
        Admin newAdmin = (Admin) createUser(new Admin(), newUser, password);
        adminRepository.save(newAdmin);
        createUserNumber(newAdmin);
        return true;
    }

    

    private AccountHolder createUser(AccountHolder newAccount, Registration newUser, String password) {
        LocalDateTime now = LocalDateTime.now();
        newAccount.setCreatedBy(newUser.getCreatedBy());
        newAccount.setCreatedTime(newUser.getCreatedTime());
        newAccount.setLastUpdatedBy(newUser.getEmail());
        newAccount.setLastUpdatedTime(now);
        newAccount.setFirstName(newUser.getFirstName());
        newAccount.setLastName(newUser.getLastName());
        newAccount.setEmail(newUser.getEmail());
        newAccount.setPassword(passwordEncoder.encode(password));
        newAccount.setPhone(newUser.getPhone());
        return newAccount;
    }

    @Transactional
    private void createUserNumber(AccountHolder newAccountHolder) {
        AccountHolder savedNewAccountHolder = accountHolderRepository
                .findByEmailAndIsDeleted(newAccountHolder.getEmail(), false);
        int currentId = savedNewAccountHolder.getId();
        String accountType = newAccountHolder.getClass().getSimpleName();
        switch (accountType) {
            case ("Admin"):
                newAccountHolder.setUserNumber(String.format("A%06d", currentId));
            case ("Researcher"):
                newAccountHolder.setUserNumber(String.format("R%06d", currentId));
            case ("User"):
                newAccountHolder.setUserNumber(String.format("U%06d", currentId));
        }
    }

}
