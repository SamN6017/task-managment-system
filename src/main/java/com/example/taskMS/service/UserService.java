package com.example.taskMS.service;

import com.example.taskMS.dto.UserProfileDTO;
import com.example.taskMS.dto.UserRegistrationDTO;
import com.example.taskMS.model.User;
import com.example.taskMS.model.enums.Role;
import com.example.taskMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // Use this interface
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Spring picks up your BCrypt Bean

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(registrationDTO.getName())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword())) // ENCODE HERE
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public UserProfileDTO getCurrentUserProfile() {
        // Get the email from the authenticated token
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserProfileDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}