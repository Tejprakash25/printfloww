package com.printflow.backend.service;

import com.printflow.backend.dto.AuthDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.repository.UserRepository;
import com.printflow.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email()))
            throw new BadRequestException("Email is already registered.");
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPhone(request.phone());
        user.setCity(request.city());
        user.setAddress(request.address());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user = userRepository.save(user);
        return response(user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password."));
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPassword()))
            throw new BadRequestException("Invalid email or password.");
        return response(user);
    }

    private AuthDtos.AuthResponse response(User user) {
        return new AuthDtos.AuthResponse(user.getId(), user.getFullName(), user.getEmail(),
                user.getRole(), jwtService.generateToken(user));
    }
}
