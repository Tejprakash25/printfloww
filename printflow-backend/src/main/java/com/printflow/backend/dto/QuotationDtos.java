package com.printflow.backend.dto;

import com.printflow.backend.entity.QuotationStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class QuotationDtos {
    private QuotationDtos() {}

    public record CreateQuotationRequest(
            @NotNull @PositiveOrZero BigDecimal subtotal,
            @PositiveOrZero BigDecimal taxPercent,
            LocalDate validUntil,
            String notes
    ) {}

    public record QuotationResponse(
            Long id, String quotationNumber, Long orderId, String orderNumber,
            BigDecimal subtotal, BigDecimal taxPercent, BigDecimal taxAmount,
            BigDecimal total, LocalDate validUntil, String notes,
            QuotationStatus status, LocalDateTime createdAt
    ) {}
}
