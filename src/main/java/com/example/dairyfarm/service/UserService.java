package com.example.dairyfarm.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.dairyfarm.entity.User;
import com.example.dairyfarm.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(User user) {

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return false;
        }

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return true;
    }

    public User login(String email, String password) {

        User user = userRepository
                .findUserByEmail(email)
                .orElse(null);

        if (user != null &&
            user.getPassword().equals(password)) {

            return user;
        }

        return null;
    }
}