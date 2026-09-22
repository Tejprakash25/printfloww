package com.printflow.backend.dto;

import com.printflow.backend.entity.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public final class WorkflowDtos {
    private WorkflowDtos() {}

    public record ProductionUpdateRequest(@NotNull ProductionStatus status, Long assignedToId, String notes) {}
    public record DeliveryUpdateRequest(@NotNull DeliveryStatus status, Long assignedToId, LocalDate scheduledDate, String address, String notes) {}
    public record PaymentRequest(@NotNull java.math.BigDecimal amount, @NotNull PaymentMethod method, String transactionId) {}
}
