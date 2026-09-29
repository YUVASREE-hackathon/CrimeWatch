package com.crimewatch.service;

import com.crimewatch.domain.*;
import com.crimewatch.repository.*;
import com.crimewatch.security.JwtService;
import com.crimewatch.web.dto.AuthDtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwt;
    private final AuditService audit;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        var user = users.save(AppUser.builder().fullName(request.fullName().trim()).email(request.email().trim().toLowerCase())
            .password(encoder.encode(request.password())).role(Role.CITIZEN)
            .roleDefinition(roles.findByCode(Role.CITIZEN).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Citizen role is not configured")))
            .phone(request.phone()).address(request.address()).build());
        audit.record(user, "USER_REGISTERED", "USER", user.getId().toString(), "Citizen account created");
        return token(user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        var user = users.findByEmailIgnoreCase(request.email()).orElseThrow();
        audit.record(user, "USER_LOGIN", "USER", user.getId().toString(), "Successful login");
        return token(user);
    }

    private AuthResponse token(AppUser user) {
        var principal = User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build();
        return new AuthResponse(jwt.generate(principal), "Bearer", jwt.expirationMs() / 1000, view(user));
    }

    public static UserView view(AppUser u) { return new UserView(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.getPhone(), u.getAddress()); }
}
