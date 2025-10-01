//CREATED AT:8/18/2025
//BY: UNGUREANU RAZVAN


package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.EmailService;
import com.example.blog.service.OtpService;
import com.example.blog.service.UserService;
import com.example.blog.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;

    public AuthController(UserService userService, EmailService emailService, OtpService otpService) {
        this.userService = userService;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        User user = userService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        if (!userService.getPasswordEncoder().matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Parolă incorectă"));
        }

        String otp = emailService.sendOtp(user.getEmail());
        otpService.storeOtp(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP trimis pe email. Introdu-l pentru autentificare."));
    }



    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        if (otpService.validateOtp(email, otp)) {
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found"));
            }

            String token = JwtUtil.generateToken(email, user.getRole().name());
            return ResponseEntity.ok(Map.of(
                    "message", "Autentificare reușită!",
                    "token", token
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "OTP invalid!"));
    }




}
