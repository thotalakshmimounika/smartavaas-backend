package com.smartavaas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailService emailService;

    // store email-to-otp mapping (in-memory for now)
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public boolean sendOtpToEmail(String email) {
        String otp = generateOtp();
        otpStore.put(email, otp);

        try {
            emailService.sendVerificationCode(email, otp);
            System.out.println("Otp sent for the email: " + email);
            System.out.println("OTP store: " + otpStore);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send OTP: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
