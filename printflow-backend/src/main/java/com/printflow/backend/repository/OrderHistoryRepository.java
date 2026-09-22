package com.printflow.backend.repository;
import com.printflow.backend.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByOrderIdOrderByCreatedAtAsc(Long orderId);
}
