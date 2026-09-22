package com.printflow.backend.repository;
import com.printflow.backend.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Optional<Quotation> findByOrderId(Long orderId);
}
