package com.printflow.backend.controller;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service = service; }
    @GetMapping public ResponseDtos.DashboardResponse dashboard(@AuthenticationPrincipal User user) { return service.get(user); }
}
