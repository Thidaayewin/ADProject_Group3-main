package com.team3.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    public Registration findByTokenAndIsDeleted(String token, boolean isDeleted);

    public Registration findByEmailAndIsDeleted(String email, boolean isDeleted);
}
