package com.example.time_tracker.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Positive
    private Long id;

    @Size(min = 3, max = 100, message = "Username should be between 3 and 100 characters")
    @NotBlank(message = "Username should not be empty")
    private String username;

    @Size(min = 5, max = 100, message = "Email should be between 5 and 100 characters")
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be like user@example.com")
    private String email;

    @NotBlank(message = "Role should not be empty")
    @Pattern(regexp = "ROLE_USER|ROLE_ADMIN", message = "Role should be like ROLE_USER or ROLE_ADMIN")
    private String role;
}
