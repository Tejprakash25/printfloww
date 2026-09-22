package com.printflow.backend.repository;
import com.printflow.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    List<User> findByRole(Role role);
    boolean existsByEmailIgnoreCase(String email);
}
