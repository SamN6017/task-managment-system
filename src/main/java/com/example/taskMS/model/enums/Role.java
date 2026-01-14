package com.example.taskMS.model.enums;

public enum Role {
    ADMIN,    // Full access to all projects and system settings
    MANAGER,  // Can create projects and assign tasks
    USER      // Can view assigned tasks and update status
}