package com.printflow.backend.repository;
import com.printflow.backend.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByOrderIdOrderByCreatedAtDesc(Long orderId);
}
