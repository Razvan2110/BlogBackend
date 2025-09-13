//CREATED AT:8/18/2025
//BY: UNGUREANU RAZVAN
//CLASS WITH METHODS THAT SEND EMAIL WITH OTP CODE

package com.example.blog.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Random random = new Random();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendOtp(String toEmail) {
        String otp = String.format("%06d", random.nextInt(999999));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Autentificare OTP");
        message.setText("Codul tău de autentificare este: " + otp);

        mailSender.send(message);

        return otp;  // returnează OTP pentru validare ulterioară
    }
}

