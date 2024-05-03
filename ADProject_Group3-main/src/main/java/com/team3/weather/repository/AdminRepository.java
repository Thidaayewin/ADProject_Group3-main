package com.team3.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    public Admin findByIdAndIsDeleted(int id, boolean isDeleted);

    public Admin findByEmailAndIsDeleted(String email, boolean isDeleted);

    public List<Admin> findAll();

    @Query("SELECT a FROM Admin a WHERE a.isDeleted = false")
    List<Admin> findAllAdmins();

    public List<Admin> findByIsDeleted(boolean isDeleted);

}
