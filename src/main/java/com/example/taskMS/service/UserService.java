package com.example.taskMS.service;

import com.example.taskMS.dto.UserRegistrationDTO;
import com.example.taskMS.model.User;
import com.example.taskMS.model.enums.Role;
import com.example.taskMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(registrationDTO.getName())
                .email(registrationDTO.getEmail())
                .password(registrationDTO.getPassword()) // Plain text for now
                .role(Role.USER) // Default role
                .build();

        return userRepository.save(user);
    }
}