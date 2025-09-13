//CREATED AT:8/18/2025
//BY: UNGUREANU RAZVAN
//CLASS FOR STORING AND VERIFYING OTP CODE


package com.example.blog.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();

    public void storeOtp(String username, String otp) {
        otpStorage.put(username, otp);
    }

    public boolean validateOtp(String username, String otp) {
        String stored = otpStorage.get(username);
        if (stored != null && stored.equals(otp)) {
            otpStorage.remove(username);
            return true;
        }
        return false;
    }
}
