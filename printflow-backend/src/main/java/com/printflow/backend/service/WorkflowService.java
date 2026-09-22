package com.printflow.backend.service;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.dto.WorkflowDtos;
import com.printflow.backend.dto.WorkflowResponseDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WorkflowService {
    private final ProductionJobRepository productionRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final NotificationService notificationService;

    public WorkflowService(ProductionJobRepository productionRepository, DeliveryRepository deliveryRepository,
                           PaymentRepository paymentRepository, UserRepository userRepository, OrderService orderService,
                           NotificationService notificationService) {
        this.productionRepository = productionRepository; this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository; this.userRepository = userRepository; this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponseDtos.ProductionJobResponse> productionJobs(User user) {
        requireProduction(user);
        return productionRepository.findAllByOrderByCreatedAtDesc().stream().map(this::productionResponse).toList();
    }

    @Transactional
    public WorkflowResponseDtos.ProductionJobResponse updateProduction(Long orderId, WorkflowDtos.ProductionUpdateRequest request, User actor) {
        requireProduction(actor);
        PrintOrder order = orderService.getEntity(orderId);
        ProductionJob job = productionRepository.findByOrderId(orderId).orElseGet(ProductionJob::new);
        job.setOrder(order); job.setStatus(request.status()); job.setNotes(request.notes());
        if (request.assignedToId() != null) job.setAssignedTo(userRepository.findById(request.assignedToId()).orElseThrow(() -> new ResourceNotFoundException("Staff member not found.")));
        if (request.status() == ProductionStatus.IN_PRODUCTION && job.getStartedAt() == null) job.setStartedAt(LocalDateTime.now());
        if (request.status() == ProductionStatus.READY || request.status() == ProductionStatus.COMPLETED) job.setCompletedAt(LocalDateTime.now());
        job = productionRepository.save(job);
        OrderStatus orderStatus = switch (request.status()) {
            case PENDING -> OrderStatus.APPROVED; case IN_PRODUCTION -> OrderStatus.IN_PRODUCTION;
            case QUALITY_CHECK -> OrderStatus.QUALITY_CHECK; case READY -> OrderStatus.READY; case COMPLETED -> OrderStatus.COMPLETED;
        };
        order.setStatus(orderStatus); orderService.addHistory(order, orderStatus, "Production status updated to " + request.status() + ".", actor);
        if (request.status() == ProductionStatus.READY) {
            deliveryRepository.findByOrderId(orderId).orElseGet(() -> { Delivery d = new Delivery(); d.setOrder(order); d.setType("PICKUP".equalsIgnoreCase(order.getDeliveryType()) ? DeliveryType.PICKUP : DeliveryType.DELIVERY); d.setStatus(d.getType() == DeliveryType.PICKUP ? DeliveryStatus.PICKUP_READY : DeliveryStatus.PENDING); d.setAddress(order.getDeliveryAddress()); return deliveryRepository.save(d); });
        }
        notificationService.create(order.getCustomer(), "Production update", order.getOrderNumber() + " is now " + request.status().name().replace('_', ' '));
        return productionResponse(job);
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponseDtos.DeliveryResponse> deliveries(User user) {
        requireDelivery(user);
        return deliveryRepository.findAllByOrderByScheduledDateAsc().stream().map(this::deliveryResponse).toList();
    }

    @Transactional
    public WorkflowResponseDtos.DeliveryResponse updateDelivery(Long orderId, WorkflowDtos.DeliveryUpdateRequest request, User actor) {
        requireDelivery(actor);
        PrintOrder order = orderService.getEntity(orderId);
        Delivery d = deliveryRepository.findByOrderId(orderId).orElseGet(Delivery::new);
        d.setOrder(order); d.setStatus(request.status());
        if (d.getType() == null) d.setType("PICKUP".equalsIgnoreCase(order.getDeliveryType()) ? DeliveryType.PICKUP : DeliveryType.DELIVERY);
        if (request.assignedToId() != null) d.setAssignedTo(userRepository.findById(request.assignedToId()).orElseThrow(() -> new ResourceNotFoundException("Delivery staff not found.")));
        if (request.scheduledDate() != null) d.setScheduledDate(request.scheduledDate());
        if (request.address() != null) d.setAddress(request.address());
        d.setNotes(request.notes());
        if (request.status() == DeliveryStatus.OUT_FOR_DELIVERY && d.getTrackingNumber() == null) d.setTrackingNumber("PF-TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        if (request.status() == DeliveryStatus.DELIVERED || request.status() == DeliveryStatus.PICKED_UP) d.setDeliveredAt(LocalDateTime.now());
        d = deliveryRepository.save(d);
        OrderStatus orderStatus = switch (request.status()) {
            case PENDING -> OrderStatus.READY; case OUT_FOR_DELIVERY -> OrderStatus.OUT_FOR_DELIVERY;
            case DELIVERED -> OrderStatus.DELIVERED; case PICKUP_READY -> OrderStatus.PICKUP_READY; case PICKED_UP -> OrderStatus.PICKED_UP;
        };
        order.setStatus(orderStatus); orderService.addHistory(order, orderStatus, "Delivery status updated to " + request.status() + ".", actor);
        notificationService.create(order.getCustomer(), "Delivery update", order.getOrderNumber() + " is now " + request.status().name().replace('_', ' '));
        return deliveryResponse(d);
    }

    @Transactional(readOnly = true)
    public List<ResponseDtos.PaymentResponse> payments(Long orderId, User user) {
        PrintOrder order = orderService.getEntity(orderId); orderService.checkAccess(order, user);
        return paymentRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(this::paymentResponse).toList();
    }

    @Transactional
    public ResponseDtos.PaymentResponse createPayment(Long orderId, WorkflowDtos.PaymentRequest request, User user) {
        PrintOrder order = orderService.getEntity(orderId); orderService.checkAccess(order, user);
        if (user.getRole() != Role.CUSTOMER && user.getRole() != Role.ADMIN) throw new BadRequestException("Payment access required.");
        Payment p = new Payment(); p.setOrder(order); p.setAmount(request.amount()); p.setMethod(request.method());
        p.setTransactionId(request.transactionId() == null || request.transactionId().isBlank() ? "TXN-" + UUID.randomUUID() : request.transactionId());
        p.setStatus(PaymentStatus.SUCCESS); p.setPaidAt(LocalDateTime.now()); paymentRepository.save(p);
        order.setStatus(OrderStatus.COMPLETED); orderService.addHistory(order, OrderStatus.COMPLETED, "Payment received: ₹" + request.amount(), user);
        notificationService.createForRole(Role.ADMIN, "Payment received", order.getOrderNumber() + " payment received.");
        return paymentResponse(p);
    }

    private void requireProduction(User user) { if (user.getRole() != Role.ADMIN && user.getRole() != Role.PRODUCTION) throw new BadRequestException("Production access required."); }
    private void requireDelivery(User user) { if (user.getRole() != Role.ADMIN && user.getRole() != Role.DELIVERY) throw new BadRequestException("Delivery access required."); }
    private ResponseDtos.PaymentResponse paymentResponse(Payment p) { return new ResponseDtos.PaymentResponse(p.getId(), p.getOrder().getId(), p.getAmount(), p.getMethod(), p.getStatus(), p.getTransactionId(), p.getPaidAt(), p.getCreatedAt()); }
    private WorkflowResponseDtos.ProductionJobResponse productionResponse(ProductionJob j) { return new WorkflowResponseDtos.ProductionJobResponse(j.getId(), j.getOrder().getId(), j.getOrder().getOrderNumber(), j.getAssignedTo() == null ? null : j.getAssignedTo().getId(), j.getAssignedTo() == null ? null : j.getAssignedTo().getFullName(), j.getStatus(), j.getNotes(), j.getStartedAt(), j.getCompletedAt(), j.getCreatedAt()); }
    private WorkflowResponseDtos.DeliveryResponse deliveryResponse(Delivery d) { return new WorkflowResponseDtos.DeliveryResponse(d.getId(), d.getOrder().getId(), d.getOrder().getOrderNumber(), d.getType(), d.getStatus(), d.getAddress(), d.getTrackingNumber(), d.getScheduledDate(), d.getDeliveredAt(), d.getAssignedTo() == null ? null : d.getAssignedTo().getId(), d.getAssignedTo() == null ? null : d.getAssignedTo().getFullName(), d.getNotes()); }
}
