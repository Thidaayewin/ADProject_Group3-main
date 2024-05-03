package com.team3.weather.service;

import java.util.List;

import com.team3.weather.model.Admin;

public interface AdminService {
    List<Admin> getAllAdmin();

    Admin findAdminByAdminId(int adminId);

    Admin createAdmin(Admin  admin);

    Admin updateAdmin(int  adminId, Admin  admin);

    void deleteAdmin(int  adminId);

    List<Admin> findAllAdmins();


    
}
