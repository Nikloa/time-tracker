package com.example.time_tracker.model.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Size(min = 3, max = 100, message = "Username should be between 3 and 100 characters")
    @NotBlank(message = "Username should not be empty")
    private String username;

    @Size(min = 8, max = 255, message = "Password should be between 8 and 255 characters")
    @NotBlank(message = "Password should not be empty")
    private String password;

}
