package com.printflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter @Setter @NoArgsConstructor
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String invoiceNumber;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", unique = true)
    private PrintOrder order;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    private String pdfPath;
    private LocalDateTime issuedAt;
    @PrePersist void prePersist() { if (issuedAt == null) issuedAt = LocalDateTime.now(); }
}
