package com.printflow.backend.repository;
import com.printflow.backend.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrderId(Long orderId);
    List<Delivery> findAllByOrderByScheduledDateAsc();
}
