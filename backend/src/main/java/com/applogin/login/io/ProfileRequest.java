package com.applogin.login.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Email(message = "Enter a valid email")
    @NotNull(message = "Email should not be empty")
    private String email;
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
