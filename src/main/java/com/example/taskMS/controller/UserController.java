package com.example.taskMS.controller;

import com.example.taskMS.dto.UserRegistrationDTO;
import com.example.taskMS.model.User;
import com.example.taskMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserRegistrationDTO registrationDTO) {
        return userService.registerUser(registrationDTO);
    }
}