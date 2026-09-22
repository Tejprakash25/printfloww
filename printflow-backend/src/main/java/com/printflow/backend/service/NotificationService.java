package com.printflow.backend.service;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.NotificationRepository;
import com.printflow.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void create(User user, String title, String message) {
        if (user == null) return;
        Notification n = new Notification();
        n.setUser(user); n.setTitle(title); n.setMessage(message); n.setRead(false);
        repository.save(n);
    }

    public void createForRole(Role role, String title, String message) {
        userRepository.findByRole(role).forEach(u -> create(u, title, message));
    }

    @Transactional(readOnly = true)
    public List<ResponseDtos.NotificationResponse> list(User user) {
        return repository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(n -> new ResponseDtos.NotificationResponse(n.getId(), n.getTitle(), n.getMessage(), n.isRead(), n.getCreatedAt()))
                .toList();
    }

    public void markRead(Long id, User user) {
        Notification n = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found."));
        if (!n.getUser().getId().equals(user.getId())) throw new ResourceNotFoundException("Notification not found.");
        n.setRead(true); repository.save(n);
    }
}
