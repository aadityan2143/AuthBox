package com.applogin.login.services.impl;

import com.applogin.login.entity.UserEntity;
import com.applogin.login.io.ProfileRequest;
import com.applogin.login.io.ProfileResponse;
import com.applogin.login.repository.UserRepository;
import com.applogin.login.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        UserEntity newProfile = convertToUserEntity(profileRequest);

        if (!userRepository.existsByEmail(newProfile.getEmail())) {
            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " +email));
        return convertToProfileResponse(userEntity);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " +email));

        //Generate 6 digit otp
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        //Calculate expiry time - current time + 15 minutes
        Long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expiryTime);

        //save into database
        userRepository.save(existingUser);

        try {
            emailService.sendResetOtp(existingUser.getEmail(), otp, existingUser.getName());
        } catch (Exception e) {
            throw new RuntimeException("Unable to send otp: " +e);
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " +email));
        if (userEntity.getResetOtp() == null || !userEntity.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        } else if (userEntity.getResetOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP Expired");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setResetOtp(null);
        userEntity.setResetOtpExpireAt(0L);

        userRepository.save(userEntity);

        try {
            emailService.sendPasswordResetSuccessEmail(userEntity.getEmail(), userEntity.getName());
        } catch (Exception e) {
            throw new RuntimeException("Unable to send password reset success email: " +e);
        }
    }

    @Override
    public void sendVerificationOtp(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " +email));

        if (userEntity.isAccountVerified()) {
            return;
        }

        //Generate 6 digit otp
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        //Calculate expiry time - current time + 15 minutes
        Long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

        userEntity.setVerifyOtp(otp);
        userEntity.setVerifyOtpExpireAt(expiryTime);

        userRepository.save(userEntity);

        try {
            emailService.sendVerificationOtp(userEntity.getEmail(), otp, userEntity.getName());
        } catch (Exception e) {
            throw new RuntimeException("Unable to send otp: " +e);
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " +email));

        if (userEntity.getVerifyOtp() == null || !userEntity.getVerifyOtp().equals(otp))
            throw new RuntimeException("Invalid OTP");

        if (userEntity.getVerifyOtpExpireAt() < System.currentTimeMillis())
            throw new RuntimeException("OTP Expired");

        userEntity.setAccountVerified(true);
        userEntity.setVerifyOtp(null);
        userEntity.setVerifyOtpExpireAt(0L);

        userRepository.save(userEntity);

        //Verification email
        try {
            emailService.sendVerificationSuccessEmail(userEntity.getEmail(), userEntity.getName());
        } catch (Exception e) {
            throw new RuntimeException("Unable to send Verification email: " +e);
        }
    }


    private UserEntity convertToUserEntity(ProfileRequest profileRequest) {
        return UserEntity.builder()
                .email(profileRequest.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(profileRequest.getName())
                .password(passwordEncoder.encode(profileRequest.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();

    }

    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
        return ProfileResponse.builder()
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .userId(newProfile.getUserId())
                .isAccountVerified(newProfile.isAccountVerified() )
                .build();
    }

}
