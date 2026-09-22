package com.printflow.backend.dto;

import com.printflow.backend.entity.ApprovalType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class ApprovalDtos {

    private ApprovalDtos() {}

    public record ApprovalRequest(
            @NotNull Long designId,
            String comment
    ) {}

    public record ChangeRequest(
            @NotNull Long designId,
            String comment
    ) {}

    public record ApprovalResponse(
            Long id,
            Long orderId,
            Long designId,
            String designFileName,
            ApprovalType type,
            String comment,
            String requestedBy,
            LocalDateTime createdAt
    ) {}
}