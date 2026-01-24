package com.example.taskMS.controller;

import com.example.taskMS.dto.CompanySignupDTO;
import com.example.taskMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setup")
@RequiredArgsConstructor
public class SetupController {

    private final UserService userService;

    @PostMapping("/register-company")
    public ResponseEntity<String> registerCompany(@RequestBody CompanySignupDTO signupDTO) {
        String response = userService.registerNewCompany(signupDTO);
        return ResponseEntity.ok(response);
    }
}