package com.printflow.backend.config;

import com.printflow.backend.entity.*;
import com.printflow.backend.repository.PrintOrderRepository;
import com.printflow.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(UserRepository users, PrintOrderRepository orders, PasswordEncoder encoder) {
        return args -> {
            User admin = user(users, encoder, "PrintFlow Admin", "admin@printflow.in", Role.ADMIN);
            User customer = user(users, encoder, "Priya Sharma", "priya@printflow.in", Role.CUSTOMER);
            user(users, encoder, "Production Staff", "production@printflow.in", Role.PRODUCTION);
            user(users, encoder, "Delivery Staff", "delivery@printflow.in", Role.DELIVERY);

            if (orders.count() == 0) {
                PrintOrder order = new PrintOrder();
                order.setOrderNumber("PF1024"); order.setCustomer(customer); order.setProductType("Flex Banner");
                order.setQuantity(2); order.setMaterial("Star Flex"); order.setWidth("6 ft"); order.setHeight("4 ft");
                order.setFinishing("Eyelet"); order.setDeliveryType("Delivery"); order.setDeliveryAddress("Kothrud, Pune");
                order.setTotalAmount(new BigDecimal("1200.00")); order.setStatus(OrderStatus.IN_PRODUCTION);
                orders.save(order);
            }
        };
    }

    private User user(UserRepository repo, PasswordEncoder encoder, String name, String email, Role role) {
        return repo.findByEmailIgnoreCase(email).orElseGet(() -> {
            User u = new User(); u.setFullName(name); u.setEmail(email); u.setPassword(encoder.encode("printflow"));
            u.setRole(role); u.setActive(true); u.setCity("Pune"); return repo.save(u);
        });
    }
}
