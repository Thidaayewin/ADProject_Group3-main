package com.team3.weather.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team3.weather.DataTransferObject.AdminLecturerRequest;
import com.team3.weather.DataTransferObject.UserUpdateRequest;
import com.team3.weather.model.*;
import com.team3.weather.service.AdminService;
import com.team3.weather.service.impl.AdminServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_Admin')")
public class AdminController {

  static int pageSize = 5;

  private AdminServiceImpl adminServiceImpl;

  @Autowired
  private AdminService adminService;

  @Autowired
  public AdminController(AdminServiceImpl adminServiceImpl,AdminService adminService) {
    this.adminServiceImpl = adminServiceImpl;
    this.adminService = adminService;
  }

 
  @GetMapping("/admin/list")
  public String showAdmins(Model model) {

    List<Admin> admins = adminServiceImpl.findAllAdmins();
    ResponseEntity<List<Admin>> responseEntity = new ResponseEntity<>(admins, HttpStatus.OK);

    model.addAttribute("admins", responseEntity.getBody());
    return "admin/admin-view";
  }

  @GetMapping("/list")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.findAllAdmins();
        return ResponseEntity.ok(admins);
    }

  // @PostMapping("/admin/update")
  // public ResponseEntity<String> updateAdmin(@Valid @RequestBody AdminLecturerRequest adminRequest) {
  //   try {
  //     int adminId = adminRequest.getId();
  //     String email = adminRequest.getEmail();

  //     // Retrieve the existing lecturer from the repository based on the ID
  //     Admin optionalAdmin = adminServiceImpl.findAdminByAdminId(adminId);

  //     if (optionalAdmin == null) {
  //       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
  //     }

  //     if (!email.equals(optionalAdmin.getEmail())
  //         && adminServiceImpl.findAdminByAdminEmail(adminRequest.getEmail()) != null) {
  //       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
  //     }
  //     adminServiceImpl.updateAdmin(adminRequest);
  //     return ResponseEntity.status(HttpStatus.OK).body("Admin updated successfully");
  //   } catch (Exception e) {
  //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
  //   }
  // }

  // @DeleteMapping("/admin/delete")
  // public ResponseEntity<String> deleteAdmin(@RequestBody AdminLecturerRequest adminRequest) {
  //   try {
  //     Admin existingAdmin = adminServiceImpl.findAdminByAdminId(adminRequest.getId());
  //     if (existingAdmin == null) {
  //       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
  //     }
  //     adminService.deleteAdmin(adminRequest);
  //     return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully");
  //   } catch (Exception e) {
  //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
  //   }
  // }

  

    // @PutMapping("/updateUser")
    // public ResponseEntity<String> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
    //     Admin admin = adminService.getAdminById(userUpdateRequest.getUserId());
    //         if(admin != null){
    //           admin.setFirstName(userUpdateRequest.getFirstname());
    //           admin.setLastName(userUpdateRequest.getLastname());
    //           admin.setEmail(userUpdateRequest.getEmail());
    //           admin.setPhone(userUpdateRequest.getPhone());
    //           Admin admin2=adminService.updateAdmin(userUpdateRequest.getUserId(),admin);
    //         }
    //         return ResponseEntity.ok("created successfully");
    // }

    @PutMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestParam int userId, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String phone) {
        Admin admin = adminService.findAdminByAdminId(userId);
            if(admin != null){
              admin.setFirstName(firstname);
              admin.setLastName(lastname);
              admin.setEmail(email);
              admin.setPhone(phone);
              Admin admin2=adminService.updateAdmin(userId,admin);
            }
            return ResponseEntity.ok("created successfully");
    }

    // @DeleteMapping("/deleteUser/{userId}")
    // public ResponseEntity<String> deleteUser(@PathVariable int userId) {
    //     adminService.deleteAdmin(userId);
    //     return ResponseEntity.ok("User deleted successfully");
    // }

    @DeleteMapping("/deleteUser/{userId}")
  public ResponseEntity<String> deleteAdmin(@PathVariable int userId) {
    try {
      Admin existingAdmin = adminServiceImpl.findAdminByAdminId(userId);
      if (existingAdmin == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
      }
      adminServiceImpl.deleteAdmin(userId);
      return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
  }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String phone) {
        Admin admin = new Admin();
            if(admin != null){
              admin.setFirstName(firstname);
              admin.setLastName(lastname);
              admin.setEmail(email);
              admin.setPhone(phone);
              Admin admin2=adminService.createAdmin(admin);
            }
            return ResponseEntity.ok("created successfully");
    }
}
