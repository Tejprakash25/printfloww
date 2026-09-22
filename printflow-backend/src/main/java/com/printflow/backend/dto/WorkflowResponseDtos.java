package com.printflow.backend.dto;

import com.printflow.backend.entity.*;
import java.time.*;

public final class WorkflowResponseDtos {
    private WorkflowResponseDtos() {}
    public record ProductionJobResponse(Long id, Long orderId, String orderNumber, Long assignedToId,
                                        String assignedToName, ProductionStatus status, String notes,
                                        LocalDateTime startedAt, LocalDateTime completedAt, LocalDateTime createdAt) {}
    public record DeliveryResponse(Long id, Long orderId, String orderNumber, DeliveryType type,
                                   DeliveryStatus status, String address, String trackingNumber,
                                   LocalDate scheduledDate, LocalDateTime deliveredAt, Long assignedToId,
                                   String assignedToName, String notes) {}
}
