package com.printflow.backend.dto;

import com.printflow.backend.entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ResponseDtos {
    private ResponseDtos() {}
    public record MessageResponse(String message) {}
    public record NotificationResponse(Long id, String title, String message, boolean read, LocalDateTime createdAt) {}
    public record InvoiceResponse(Long id, String invoiceNumber, Long orderId, String orderNumber,
                                  BigDecimal amount, LocalDateTime issuedAt) {}
    public record PaymentResponse(Long id, Long orderId, BigDecimal amount, PaymentMethod method,
                                  PaymentStatus status, String transactionId, LocalDateTime paidAt, LocalDateTime createdAt) {}
    public record HistoryResponse(Long id, OrderStatus status, String message, String changedBy, LocalDateTime createdAt) {}
    public record DashboardResponse(long totalOrders, long pendingOrders, long productionOrders,
                                    long readyOrders, long deliveredOrders, BigDecimal totalRevenue,
                                    long unreadNotifications) {}
    public record UserResponse(Long id, String fullName, String email, String phone, String city,
                               String address, Role role, boolean active) {}
}
