package com.example.taskMS.controller;

import com.example.taskMS.dto.LoginRequest;
import com.example.taskMS.dto.AuthResponse;
import com.example.taskMS.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenFromUsername(authentication.getName());

            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUsername(), role));

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            System.out.println("DEBUG: Invalid Password for " + loginRequest.getEmail());
            return ResponseEntity.status(401).body("Error: Invalid password.");
        } catch (Exception e) {
            System.out.println("DEBUG: Authentication Error: " + e.getMessage());
            e.printStackTrace(); // This will show the full error in your Java console
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}