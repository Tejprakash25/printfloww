package com.printflow.backend.controller;

import com.printflow.backend.dto.ApprovalDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService service;

    public ApprovalController(ApprovalService service) {
        this.service = service;
    }

    @PostMapping("/order/{orderId}/approve")
    public void approve(
            @PathVariable Long orderId,
            @Valid @RequestBody ApprovalDtos.ApprovalRequest request,
            @AuthenticationPrincipal User user) {
        service.approve(orderId, request, user);
    }

    @PostMapping("/order/{orderId}/change-request")
    public void change(
            @PathVariable Long orderId,
            @Valid @RequestBody ApprovalDtos.ChangeRequest request,
            @AuthenticationPrincipal User user) {
        service.changeRequest(orderId, request, user);
    }

    @GetMapping("/order/{orderId}")
    public List<ApprovalDtos.ApprovalResponse> list(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) {
        return service.list(orderId, user);
    }
}