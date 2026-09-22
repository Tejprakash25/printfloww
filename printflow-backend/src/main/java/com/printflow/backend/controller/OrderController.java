package com.printflow.backend.controller;

import com.printflow.backend.dto.OrderDtos;
import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }

    @PostMapping public OrderDtos.OrderResponse create(@Valid @RequestBody OrderDtos.CreateOrderRequest request, @AuthenticationPrincipal User user) { return service.create(request, user); }
    @GetMapping public List<OrderDtos.OrderResponse> list(@AuthenticationPrincipal User user) { return service.list(user); }
    @GetMapping("/{id}") public OrderDtos.OrderResponse get(@PathVariable Long id, @AuthenticationPrincipal User user) { return service.get(id, user); }
    @PatchMapping("/{id}/status") public OrderDtos.OrderResponse status(@PathVariable Long id, @Valid @RequestBody OrderDtos.StatusRequest request, @AuthenticationPrincipal User user) { return service.updateStatus(id, request, user); }
    @GetMapping("/{id}/history") public List<ResponseDtos.HistoryResponse> history(@PathVariable Long id, @AuthenticationPrincipal User user) { return service.history(id, user); }
}
