package com.example.dairyfarm.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dairyfarm.entity.Admin;
import com.example.dairyfarm.repository.AdminRepository;

@Service
public class LoginService {

    private final AdminRepository adminRepository;

    public LoginService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin login(String username, String password) {

        Optional<Admin> admin = adminRepository.findByUsername(username);

        if (admin.isPresent()) {

            Admin existingAdmin = admin.get();

            if (existingAdmin.getPassword().equals(password)) {
                return existingAdmin;
            }
        }

        return null;
    }
}