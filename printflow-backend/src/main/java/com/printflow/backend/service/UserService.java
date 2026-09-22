package com.printflow.backend.service;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) { this.repository = repository; }
    public List<ResponseDtos.UserResponse> customers() { return repository.findByRole(Role.CUSTOMER).stream().map(this::map).toList(); }
    public List<ResponseDtos.UserResponse> employees() { return repository.findAll().stream().filter(u -> u.getRole() != Role.CUSTOMER).map(this::map).toList(); }
    private ResponseDtos.UserResponse map(User u) { return new ResponseDtos.UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getCity(), u.getAddress(), u.getRole(), u.isActive()); }
}
