package com.team3.weather.service.impl;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.weather.DataTransferObject.AdminLecturerRequest;
import com.team3.weather.model.Admin;
import com.team3.weather.repository.AdminRepository;
import com.team3.weather.security.SecurityUtil;
import com.team3.weather.service.AdminService;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

  @Autowired
  private AdminRepository adminRepository;

  
  public AdminServiceImpl(AdminRepository adminRepository) {
    this.adminRepository = adminRepository;

  }

  public List<Admin> findAllAdmins() {
    return adminRepository.findByIsDeleted(false);
  }

  public Admin findAdminByAdminId(int adminId) {
    return adminRepository.findByIdAndIsDeleted(adminId, false);
  }

  public Admin findAdminByAdminEmail(String email) {
    return adminRepository.findByEmailAndIsDeleted(email, false);
  }

  @Override
  public List<Admin> getAllAdmin() {
    return adminRepository.findAll();
  }


  @Override
  public Admin createAdmin(Admin admin) {
    adminRepository.save(admin);
    return admin;
  }

  @Override
  public Admin updateAdmin(int adminId, Admin admin) {
    Admin ad = findAdminByAdminId(adminId);
    // Update the lecturer's information
    ad.setFirstName(admin.getFirstName());
    ad.setLastName(admin.getLastName());
    ad.setEmail(admin.getEmail());
    ad.setPhone(admin.getPhone());
    ad.setLastUpdatedBy(SecurityUtil.getSessionUser());
    ad.setLastUpdatedTime(LocalDateTime.now());
    adminRepository.save(admin);
    return ad;
  }

  @Override
  public void deleteAdmin(int adminId) {
    Admin existingAdmin = findAdminByAdminId(adminId);
    existingAdmin.setDeleted(true);
    existingAdmin.setLastUpdatedBy(SecurityUtil.getSessionUser());
    existingAdmin.setLastUpdatedTime(LocalDateTime.now());
    adminRepository.save(existingAdmin);
  }
}
