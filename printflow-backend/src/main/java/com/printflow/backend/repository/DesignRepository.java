package com.printflow.backend.repository;
import com.printflow.backend.entity.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DesignRepository extends JpaRepository<Design, Long> {
    List<Design> findByOrderIdOrderByVersionDesc(Long orderId);
    Optional<Design> findByIdAndOrderId(Long id, Long orderId);
    int countByOrderId(Long orderId);
}
