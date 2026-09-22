package com.printflow.backend.service;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {
    private final PrintOrderRepository orders;
    private final PaymentRepository payments;
    private final NotificationRepository notifications;

    public DashboardService(PrintOrderRepository orders, PaymentRepository payments, NotificationRepository notifications) {
        this.orders = orders; this.payments = payments; this.notifications = notifications;
    }

    public ResponseDtos.DashboardResponse get(User user) {
        long total = user.getRole() == Role.CUSTOMER ? orders.findByCustomerIdOrderByCreatedAtDesc(user.getId()).size() : orders.count();
        long pending = orders.countByStatus(OrderStatus.QUOTATION_PENDING) + orders.countByStatus(OrderStatus.PROOF_PENDING) + orders.countByStatus(OrderStatus.PAYMENT_PENDING);
        long production = orders.countByStatus(OrderStatus.IN_PRODUCTION) + orders.countByStatus(OrderStatus.QUALITY_CHECK);
        long ready = orders.countByStatus(OrderStatus.READY) + orders.countByStatus(OrderStatus.PICKUP_READY);
        long delivered = orders.countByStatus(OrderStatus.DELIVERED) + orders.countByStatus(OrderStatus.PICKED_UP) + orders.countByStatus(OrderStatus.COMPLETED);
        BigDecimal revenue = payments.findAll().stream().filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (user.getRole() == Role.CUSTOMER) {
            revenue = payments.findAll().stream().filter(p -> p.getStatus() == PaymentStatus.SUCCESS && p.getOrder().getCustomer().getId().equals(user.getId()))
                    .map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return new ResponseDtos.DashboardResponse(total, pending, production, ready, delivered, revenue,
                notifications.countByUserIdAndReadFalse(user.getId()));
    }
}
