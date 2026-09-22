package com.printflow.backend.controller;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }
    @GetMapping public List<ResponseDtos.NotificationResponse> list(@AuthenticationPrincipal User user) { return service.list(user); }
    @PatchMapping("/{id}/read") public ResponseDtos.MessageResponse read(@PathVariable Long id, @AuthenticationPrincipal User user) { service.markRead(id, user); return new ResponseDtos.MessageResponse("Notification marked as read."); }
}
