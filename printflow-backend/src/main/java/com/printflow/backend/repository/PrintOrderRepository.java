package com.printflow.backend.repository;
import com.printflow.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PrintOrderRepository extends JpaRepository<PrintOrder, Long> {
    Optional<PrintOrder> findByOrderNumber(String orderNumber);
    List<PrintOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<PrintOrder> findAllByOrderByCreatedAtDesc();
    long countByStatus(OrderStatus status);
}
