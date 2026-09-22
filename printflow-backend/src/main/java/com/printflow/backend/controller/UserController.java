package com.printflow.backend.controller;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping("/me") public ResponseDtos.UserResponse me(@AuthenticationPrincipal User u) { return new ResponseDtos.UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getCity(), u.getAddress(), u.getRole(), u.isActive()); }
    @GetMapping("/customers") @PreAuthorize("hasRole('ADMIN')") public List<ResponseDtos.UserResponse> customers() { return service.customers(); }
    @GetMapping("/employees") @PreAuthorize("hasRole('ADMIN')") public List<ResponseDtos.UserResponse> employees() { return service.employees(); }
}
