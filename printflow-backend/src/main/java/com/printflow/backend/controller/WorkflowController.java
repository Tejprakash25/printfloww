package com.printflow.backend.controller;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.dto.WorkflowDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.dto.WorkflowResponseDtos;
import com.printflow.backend.service.WorkflowService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkflowController {
    private final WorkflowService service;
    public WorkflowController(WorkflowService service) { this.service = service; }

    @GetMapping("/production/jobs") public List<WorkflowResponseDtos.ProductionJobResponse> production(@AuthenticationPrincipal User user) { return service.productionJobs(user); }
    @PatchMapping("/production/orders/{orderId}") public WorkflowResponseDtos.ProductionJobResponse productionUpdate(@PathVariable Long orderId, @Valid @RequestBody WorkflowDtos.ProductionUpdateRequest request, @AuthenticationPrincipal User user) { return service.updateProduction(orderId, request, user); }

    @GetMapping("/deliveries") public List<WorkflowResponseDtos.DeliveryResponse> deliveries(@AuthenticationPrincipal User user) { return service.deliveries(user); }
    @PatchMapping("/deliveries/orders/{orderId}") public WorkflowResponseDtos.DeliveryResponse deliveryUpdate(@PathVariable Long orderId, @Valid @RequestBody WorkflowDtos.DeliveryUpdateRequest request, @AuthenticationPrincipal User user) { return service.updateDelivery(orderId, request, user); }

    @GetMapping("/payments/order/{orderId}") public List<ResponseDtos.PaymentResponse> payments(@PathVariable Long orderId, @AuthenticationPrincipal User user) { return service.payments(orderId, user); }
    @PostMapping("/payments/order/{orderId}") public ResponseDtos.PaymentResponse payment(@PathVariable Long orderId, @Valid @RequestBody WorkflowDtos.PaymentRequest request, @AuthenticationPrincipal User user) { return service.createPayment(orderId, request, user); }
}
