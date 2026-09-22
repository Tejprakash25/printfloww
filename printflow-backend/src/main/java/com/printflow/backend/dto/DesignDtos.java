package com.printflow.backend.dto;

import java.time.LocalDateTime;

public final class DesignDtos {
    private DesignDtos() {}
    public record DesignResponse(Long id, Long orderId, String orderNumber, String originalFileName,
                                 String contentType, Long fileSize, Integer version,
                                 boolean approved, LocalDateTime uploadedAt) {}
}
