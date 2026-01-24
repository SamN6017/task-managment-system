package com.example.taskMS.controller;

import com.example.taskMS.dto.UserProfileDTO;
import com.example.taskMS.dto.UserRegistrationDTO;
import com.example.taskMS.model.User;
import com.example.taskMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }
}