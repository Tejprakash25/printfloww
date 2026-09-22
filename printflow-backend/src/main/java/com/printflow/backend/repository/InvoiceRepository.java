package com.printflow.backend.repository;
import com.printflow.backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrderId(Long orderId);
}
