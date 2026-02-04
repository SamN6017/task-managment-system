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

import java.util.List;

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
        System.out.println("email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserProfileDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public String createEmployee(UserRegistrationDTO dto) {
        // 1. Get current logged-in user (The one performing the creation)
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        // 2. Fetch the assigned Manager/Supervisor by EMAIL instead of ID
        // We use the new reportsToEmail field from your DTO
        User supervisor = userRepository.findByEmail(dto.getReportsToEmail())
                .orElseThrow(() -> new RuntimeException("Supervisor not found with email: " + dto.getReportsToEmail()));

        // 3. Create New User
        User newUser = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .company(creator.getCompany())
                .reportsTo(supervisor) // Hibernate handles the ID mapping automatically here
                .build();

        userRepository.save(newUser);
        return "Added " + newUser.getName() + " reporting to " + supervisor.getName();
    }

    @Transactional
    public String registerNewCompany(CompanySignupDTO dto) {
        // 1. Create Company
        Company company = Company.builder()
                .name(dto.getCompanyName())
                .build();
        company = companyRepository.save(company);

        // 2. Create the CEO (The first user)
        User ceo = User.builder()
                .name(dto.getAdminName())
                .email(dto.getAdminEmail())
                .password(passwordEncoder.encode(dto.getAdminPassword()))
                .role(Role.CEO) // Set role to CEO
                .company(company)
                .build();

        userRepository.save(ceo);
        return "Company registered successfully with CEO: " + ceo.getName();
    }

    public List<UserProfileDTO> getMySubordinates() {
        // 1. Identify the logged-in leader
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User leader = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch people who report directly to this leader
        // You will need to add 'findByReportsToId' to your UserRepository
        List<User> subordinates = userRepository.findByReportsToId(leader.getId());

        // 3. Convert to DTOs for the frontend
        return subordinates.stream()
                .map(sub -> UserProfileDTO.builder()
                        .id(sub.getId())
                        .name(sub.getName())
                        .email(sub.getEmail())
                        .role(sub.getRole().name())
                        .build())
                .toList();
    }
}