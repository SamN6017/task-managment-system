package com.example.taskMS.model.enums;

public enum Status {
    TODO,           // Task is created but not started
    IN_PROGRESS,    // Task is currently being worked on
    IN_REVIEW,      // Task is finished but waiting for approval
    DONE            // Task is completed
}