package com.printflow.backend.controller;

import com.printflow.backend.dto.QuotationDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {
    private final QuotationService service;
    public QuotationController(QuotationService service) { this.service = service; }
    @PostMapping("/order/{orderId}") public QuotationDtos.QuotationResponse create(@PathVariable Long orderId, @Valid @RequestBody QuotationDtos.CreateQuotationRequest request, @AuthenticationPrincipal User user) { return service.create(orderId, request, user); }
    @GetMapping("/order/{orderId}") public QuotationDtos.QuotationResponse get(@PathVariable Long orderId, @AuthenticationPrincipal User user) { return service.get(orderId, user); }
    @PostMapping("/order/{orderId}/accept") public QuotationDtos.QuotationResponse accept(@PathVariable Long orderId, @AuthenticationPrincipal User user) { return service.decision(orderId, true, user); }
    @PostMapping("/order/{orderId}/reject") public QuotationDtos.QuotationResponse reject(@PathVariable Long orderId, @AuthenticationPrincipal User user) { return service.decision(orderId, false, user); }
}
