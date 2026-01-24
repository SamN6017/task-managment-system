package com.example.taskMS.dto;

import lombok.Data;

@Data
public class CompanySignupDTO {
    // Company Info
    private String companyName;

    // Admin Info
    private String adminName;
    private String adminEmail;
    private String adminPassword;
}