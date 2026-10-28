package com.applogin.login.controller;

import com.applogin.login.io.ProfileRequest;
import com.applogin.login.io.ProfileResponse;
import com.applogin.login.services.ProfileService;
import com.applogin.login.services.impl.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
// we don't need the @RequestMapping since we added the same path as a context-path in the application,properties file
//@RequestMapping("/api/v1") //Can handle GET, POST, PUT, DELETE, we need to specify, if not specified, it defaults to GET
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final EmailService emailService;

    @RequestMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest profileRequest) {
        ProfileResponse profileResponse = profileService.createProfile(profileRequest);
        //TODO create email trigger
        emailService.sendWelcomeEmail(profileResponse.getEmail(), profileResponse.getName());
        return profileResponse;
    }

    @GetMapping("/test")
    public String test() {
        return "Auth is working";
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) { // CurrentSecurityContext(expression = "authentication?.name") checks the current security context and finds if the user is authenticated
        return profileService.getProfile(email); //A custom error handler is created under /config/CustomAuthenticationEntryPoint
    }

}
