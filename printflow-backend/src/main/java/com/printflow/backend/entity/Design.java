package com.printflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "designs")
@Getter @Setter @NoArgsConstructor
public class Design {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private PrintOrder order;

    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false, unique = true)
    private String storedFileName;
    private String contentType;
    private Long fileSize;
    private Integer version;
    private boolean approved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    void prePersist() { if (uploadedAt == null) uploadedAt = LocalDateTime.now(); }
}
