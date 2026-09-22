package com.printflow.backend.dto;

import com.printflow.backend.entity.OrderStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class OrderDtos {
    private OrderDtos() {}

    public record CreateOrderRequest(
            @NotBlank String productType,
            @NotNull @Positive Integer quantity,
            @NotBlank String material,
            String width,
            String height,
            String finishing,
            LocalDate requiredDate,
            String specialInstructions,
            String deliveryType,
            String deliveryAddress
    ) {}

    public record StatusRequest(@NotNull OrderStatus status, String message) {}

    public record OrderResponse(
            Long id, String orderNumber, Long customerId, String customerName,
            String productType, Integer quantity, String material, String width,
            String height, String finishing, LocalDate requiredDate,
            String specialInstructions, String deliveryType, String deliveryAddress,
            BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
