package com.applogin.login.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.email.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String name) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Welcome to our platform");
        simpleMailMessage.setText("Hello "+name+",\n\nThanks for registering with us!\n\nRegards,\nAuthify Team");
        mailSender.send(simpleMailMessage);
    }

    public void sendResetOtp(String toEmail, String otp, String name) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Password Reset OTP");
        simpleMailMessage.setText("Hi " + name + "!" + "\n\nOTP for Resetting password is: " +otp+ "\nPlease don't share this OTP to anyone else. OTP is valid for 15 minutes");
        mailSender.send(simpleMailMessage);
    }

    public void sendVerificationOtp(String toEmail, String otp, String name) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Account Verification OTP");
        simpleMailMessage.setText("Hi " + name + "!" + "\n\nPlease find the OTP to verify your account: " +otp+ ".\nPlease do not share the OTP with anyone else.\nThis otp is valid for 15 minutes");
        mailSender.send(simpleMailMessage);
    }

    public void sendVerificationSuccessEmail(String toEmail, String name) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Congrats! Your Account is Verified");
        simpleMailMessage.setText("Hi " + name + "!" + "\n\nPlease be informed that your account is now verified against your email.\n\nThanks, and Regards,\nTeam Authify.");
        mailSender.send(simpleMailMessage);
    }

    public void sendPasswordResetSuccessEmail(String toEmail, String name) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Update: Password reset successfully");
        simpleMailMessage.setText("Hi " + name + "!" + "\n\nPlease be informed that your account password is now updated.\n\nThanks, and Regards,\nTeam Authify.");
        mailSender.send(simpleMailMessage);
    }
}
