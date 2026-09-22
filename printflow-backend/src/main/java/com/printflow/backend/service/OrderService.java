package com.printflow.backend.service;

import com.printflow.backend.dto.OrderDtos;
import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final PrintOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderHistoryRepository historyRepository;
    private final NotificationService notificationService;
    private final ProductionJobRepository productionJobRepository;
    private final DeliveryRepository deliveryRepository;

    public OrderService(PrintOrderRepository orderRepository, UserRepository userRepository,
                        OrderHistoryRepository historyRepository, NotificationService notificationService,
                        ProductionJobRepository productionJobRepository, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository; this.userRepository = userRepository;
        this.historyRepository = historyRepository; this.notificationService = notificationService;
        this.productionJobRepository = productionJobRepository; this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public OrderDtos.OrderResponse create(OrderDtos.CreateOrderRequest request, User customer) {
        if (customer.getRole() != Role.CUSTOMER) throw new BadRequestException("Only customers can create orders.");
        PrintOrder order = new PrintOrder();
        order.setOrderNumber("TEMP-" + System.nanoTime());
        order.setCustomer(customer);
        order.setProductType(request.productType()); order.setQuantity(request.quantity());
        order.setMaterial(request.material()); order.setWidth(request.width()); order.setHeight(request.height());
        order.setFinishing(request.finishing()); order.setRequiredDate(request.requiredDate());
        order.setSpecialInstructions(request.specialInstructions()); order.setDeliveryType(request.deliveryType());
        order.setDeliveryAddress(request.deliveryAddress()); order.setTotalAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.ORDER_PLACED);
        order = orderRepository.save(order);
        order.setOrderNumber("PF" + (1000 + order.getId()));
        order = orderRepository.save(order);
        addHistory(order, OrderStatus.ORDER_PLACED, "Order created.", customer);
        notificationService.createForRole(Role.ADMIN, "New order received", order.getOrderNumber() + " needs a quotation.");
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDtos.OrderResponse> list(User user) {
        List<PrintOrder> orders = user.getRole() == Role.CUSTOMER
                ? orderRepository.findByCustomerIdOrderByCreatedAtDesc(user.getId())
                : orderRepository.findAllByOrderByCreatedAtDesc();
        return orders.stream().map(this::toResponse).toList();
    }

    public PrintOrder getEntity(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    @Transactional(readOnly = true)
    public OrderDtos.OrderResponse get(Long id, User user) {
        PrintOrder order = getEntity(id);
        checkAccess(order, user);
        return toResponse(order);
    }

    @Transactional
    public OrderDtos.OrderResponse updateStatus(Long id, OrderDtos.StatusRequest request, User user) {
        PrintOrder order = getEntity(id);
        if (user.getRole() == Role.CUSTOMER && !order.getCustomer().getId().equals(user.getId()))
            throw new ResourceNotFoundException("Order not found.");
        order.setStatus(request.status());
        order = orderRepository.save(order);
        addHistory(order, request.status(), request.message() == null ? "Status updated." : request.message(), user);
        notificationService.create(order.getCustomer(), "Order status updated", order.getOrderNumber() + " is now " + request.status().name().replace('_', ' '));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<ResponseDtos.HistoryResponse> history(Long id, User user) {
        PrintOrder order = getEntity(id); checkAccess(order, user);
        return historyRepository.findByOrderIdOrderByCreatedAtAsc(id).stream()
                .map(h -> new ResponseDtos.HistoryResponse(h.getId(), h.getStatus(), h.getMessage(),
                        h.getChangedBy() == null ? "System" : h.getChangedBy().getFullName(), h.getCreatedAt())).toList();
    }

    public void checkAccess(PrintOrder order, User user) {
        if (user.getRole() == Role.CUSTOMER && !order.getCustomer().getId().equals(user.getId()))
            throw new ResourceNotFoundException("Order not found.");
    }

    public void addHistory(PrintOrder order, OrderStatus status, String message, User user) {
        OrderHistory h = new OrderHistory(); h.setOrder(order); h.setStatus(status); h.setMessage(message); h.setChangedBy(user);
        historyRepository.save(h);
    }

    public OrderDtos.OrderResponse toResponse(PrintOrder o) {
        return new OrderDtos.OrderResponse(o.getId(), o.getOrderNumber(), o.getCustomer().getId(), o.getCustomer().getFullName(),
                o.getProductType(), o.getQuantity(), o.getMaterial(), o.getWidth(), o.getHeight(), o.getFinishing(),
                o.getRequiredDate(), o.getSpecialInstructions(), o.getDeliveryType(), o.getDeliveryAddress(),
                o.getTotalAmount(), o.getStatus(), o.getCreatedAt(), o.getUpdatedAt());
    }
}
