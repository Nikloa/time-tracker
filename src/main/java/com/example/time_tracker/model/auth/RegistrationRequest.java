package com.example.time_tracker.model.auth;

import com.example.time_tracker.util.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "password", second = "passwordConfirm", message = "The password and passwordConfirm fields should match")
public class RegistrationRequest {

    @Size(min = 3, max = 100, message = "Username should be between 3 and 100 characters")
    @NotBlank(message = "Username should not be empty")
    private String username;

    @Size(min = 5, max = 100, message = "Email should be between 5 and 100 characters")
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be like user@example.com")
    private String email;

    @Size(min = 8, max = 255, message = "Password should be between 8 and 255 characters")
    @NotBlank(message = "Password should not be empty")
    private String password;

    @Size(min = 8, max = 255, message = "Password should be between 8 and 255 characters")
    @NotBlank(message = "Password confirmation should not be empty")
    private String passwordConfirm;

    @NotBlank(message = "Role should not be empty")
    @Pattern(regexp = "ROLE_USER|ROLE_ADMIN", message = "Role should be like ROLE_USER or ROLE_ADMIN")
    private String role;
}
