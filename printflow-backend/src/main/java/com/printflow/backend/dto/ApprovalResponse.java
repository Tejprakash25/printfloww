package com.printflow.backend.dto;

import com.printflow.backend.entity.ApprovalType;
import java.time.LocalDateTime;

public record ApprovalResponse(Long id, Long orderId, Long designId, String designFileName,
                               ApprovalType type, String comment, String requestedBy, LocalDateTime createdAt) {}
