package com.printflow.backend.service;

import com.printflow.backend.dto.QuotationDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class QuotationService {
    private final QuotationRepository repository;
    private final OrderService orderService;
    private final NotificationService notificationService;

    public QuotationService(QuotationRepository repository, OrderService orderService, NotificationService notificationService) {
        this.repository = repository; this.orderService = orderService; this.notificationService = notificationService;
    }

    @Transactional
    public QuotationDtos.QuotationResponse create(Long orderId, QuotationDtos.CreateQuotationRequest request, User admin) {
        if (admin.getRole() != Role.ADMIN) throw new BadRequestException("Only admin can create quotations.");
        PrintOrder order = orderService.getEntity(orderId);
        Quotation q = repository.findByOrderId(orderId).orElseGet(Quotation::new);
        q.setOrder(order); q.setQuotationNumber(q.getQuotationNumber() == null ? "TEMP-" + System.nanoTime() : q.getQuotationNumber());
        q.setSubtotal(request.subtotal());
        q.setTaxPercent(request.taxPercent() == null ? BigDecimal.valueOf(18) : request.taxPercent());
        q.setTaxAmount(q.getSubtotal().multiply(q.getTaxPercent()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        q.setTotal(q.getSubtotal().add(q.getTaxAmount()));
        q.setValidUntil(request.validUntil() == null ? LocalDate.now().plusDays(7) : request.validUntil());
        q.setNotes(request.notes()); q.setStatus(QuotationStatus.SENT);
        q = repository.save(q);
        q.setQuotationNumber("QT/" + order.getOrderNumber()); q = repository.save(q);
        order.setTotalAmount(q.getTotal()); order.setStatus(OrderStatus.QUOTATION_SENT);
        orderService.addHistory(order, OrderStatus.QUOTATION_SENT, "Quotation sent to customer.", admin);
        notificationService.create(order.getCustomer(), "Quotation ready", "Quotation " + q.getQuotationNumber() + " is ready for review.");
        return toResponse(q);
    }

    public Quotation getEntity(Long orderId) { return repository.findByOrderId(orderId).orElseThrow(() -> new ResourceNotFoundException("Quotation not found.")); }

    @Transactional(readOnly = true)
    public QuotationDtos.QuotationResponse get(Long orderId, User user) {
        Quotation q = getEntity(orderId); orderService.checkAccess(q.getOrder(), user); return toResponse(q);
    }

    @Transactional
    public QuotationDtos.QuotationResponse decision(Long orderId, boolean accepted, User customer) {
        Quotation q = getEntity(orderId); orderService.checkAccess(q.getOrder(), customer);
        if (customer.getRole() != Role.CUSTOMER) throw new BadRequestException("Only customer can decide on quotation.");
        q.setStatus(accepted ? QuotationStatus.ACCEPTED : QuotationStatus.REJECTED); repository.save(q);
        PrintOrder order = q.getOrder();
        order.setStatus(accepted ? OrderStatus.QUOTATION_ACCEPTED : OrderStatus.QUOTATION_REJECTED);
        orderService.addHistory(order, order.getStatus(), accepted ? "Quotation accepted." : "Quotation rejected.", customer);
        notificationService.createForRole(Role.ADMIN, "Quotation decision", order.getOrderNumber() + " quotation was " + (accepted ? "accepted." : "rejected."));
        return toResponse(q);
    }

    private QuotationDtos.QuotationResponse toResponse(Quotation q) {
        return new QuotationDtos.QuotationResponse(q.getId(), q.getQuotationNumber(), q.getOrder().getId(), q.getOrder().getOrderNumber(),
                q.getSubtotal(), q.getTaxPercent(), q.getTaxAmount(), q.getTotal(), q.getValidUntil(), q.getNotes(), q.getStatus(), q.getCreatedAt());
    }
}
