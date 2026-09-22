package com.printflow.backend.repository;
import com.printflow.backend.entity.ProductionJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ProductionJobRepository extends JpaRepository<ProductionJob, Long> {
    Optional<ProductionJob> findByOrderId(Long orderId);
    List<ProductionJob> findAllByOrderByCreatedAtDesc();
}
