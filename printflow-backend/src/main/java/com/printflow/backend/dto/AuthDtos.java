package com.printflow.backend.dto;

import com.printflow.backend.entity.Role;
import jakarta.validation.constraints.*;

public final class AuthDtos {
    private AuthDtos() {}

    public record RegisterRequest(
            @NotBlank String fullName,
            @Email @NotBlank String email,
            String phone,
            String city,
            String address,
            @NotBlank @Size(min = 8, max = 100) String password
    ) {}

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    public record AuthResponse(Long id, String fullName, String email, Role role, String token) {}
}
