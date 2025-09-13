//CREATED AT:8/18/2025
//BY: UNGUREANU RAZVAN


package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.EmailService;
import com.example.blog.service.OtpService;
import com.example.blog.service.UserService;
import com.example.blog.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

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
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return "User not found";
        }

        // verifică parola
        if (!userService.getPasswordEncoder().matches(password, user.getPassword())) {
            return "Parolă incorectă";
        }

        // trimite OTP pe email
        String otp = emailService.sendOtp(user.getEmail());
        otpService.storeOtp(username, otp);

        return "OTP trimis pe email. Introdu-l pentru autentificare.";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String username, @RequestParam String otp) {
        if (otpService.validateOtp(username, otp)) {
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                return "User not found";
            }

            // generează token cu rolul userului
            String token = JwtUtil.generateToken(username, user.getRole().name());

            return "Autentificare reușită! Token JWT: " + token;
        }
        return "OTP invalid!";
    }

}
