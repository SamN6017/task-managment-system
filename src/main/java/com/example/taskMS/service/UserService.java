package com.example.taskMS.service;

import com.example.taskMS.dto.CompanySignupDTO;
import com.example.taskMS.dto.UserProfileDTO;
import com.example.taskMS.dto.UserRegistrationDTO;
import com.example.taskMS.model.Company;
import com.example.taskMS.model.User;
import com.example.taskMS.model.enums.Role;
import com.example.taskMS.repository.CompanyRepository;
import com.example.taskMS.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // Use this interface
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository; // Add this line
    private final PasswordEncoder passwordEncoder;

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

    public User createEmployee(UserRegistrationDTO dto) {
        // 1. Get the Admin's info from the JWT
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 2. Security Check (Manual check if not using @PreAuthorize)
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only Admins can create users");
        }

        // 3. Create the new User and link to the Admin's Company
        User newUser = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) // Admin sets temporary password
                .role(Role.USER) // Default role for new hires
                .company(admin.getCompany()) // <--- This creates the "Company Wall"
                .build();

        return userRepository.save(newUser);
    }

    @Transactional
    public String registerNewCompany(CompanySignupDTO dto) {
        // 1. Create and Save the Company
        Company company = Company.builder()
                .name(dto.getCompanyName())
                .build();
        company = companyRepository.save(company);

        // 2. Create and Save the first Admin for that company
        User admin = User.builder()
                .name(dto.getAdminName())
                .email(dto.getAdminEmail())
                .password(passwordEncoder.encode(dto.getAdminPassword()))
                .role(Role.ADMIN) // Hardcoded as ADMIN
                .company(company) // Linked to the new company
                .build();
        userRepository.save(admin);

        return "Company and Admin registered successfully!";
    }
}