package com.applogin.login.services;

import com.applogin.login.io.ProfileRequest;
import com.applogin.login.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest profileRequest);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email, String otp, String newPassword);
    void sendVerificationOtp(String email);
    void verifyOtp(String email, String otp);
}
