package com.example.taskMS.dto;

import com.example.taskMS.model.enums.Role;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private Role role; // MANAGER, TEAM_LEADER, or TEAM_MEMBER
    private String reportsToEmail;
}