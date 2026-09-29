package com.crimewatch.web.dto;

import com.crimewatch.domain.Role;
import jakarta.validation.constraints.*;

public final class AuthDtos {
    private AuthDtos() {}

    public record RegisterRequest(
        @NotBlank @Size(min = 2, max = 100) String fullName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @Pattern(regexp = "^$|^[0-9+() -]{7,20}$") String phone,
        @Size(max = 300) String address
    ) {}

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

    public record UserView(Long id, String fullName, String email, Role role, String phone, String address) {}

    public record AuthResponse(String token, String tokenType, long expiresIn, UserView user) {}
}

