package com.printflow.backend.service;

import com.printflow.backend.dto.DesignDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class DesignService {
    private final DesignRepository designRepository;
    private final OrderService orderService;
    private final Path uploadRoot;

    public DesignService(DesignRepository designRepository, OrderService orderService,
                         @Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.designRepository = designRepository;
        this.orderService = orderService;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadRoot);
    }

    public DesignDtos.DesignResponse upload(Long orderId, MultipartFile file, User user) throws IOException {
        if (file == null || file.isEmpty()) throw new BadRequestException("Please select a file.");
        if (file.getSize() > 25L * 1024 * 1024) throw new BadRequestException("File exceeds 25 MB.");
        PrintOrder order = orderService.getEntity(orderId); orderService.checkAccess(order, user);
        String original = Optional.ofNullable(file.getOriginalFilename()).orElse("design");
        String stored = UUID.randomUUID() + "-" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
        Files.copy(file.getInputStream(), uploadRoot.resolve(stored), StandardCopyOption.REPLACE_EXISTING);
        Design d = new Design(); d.setOrder(order); d.setOriginalFileName(original); d.setStoredFileName(stored);
        d.setContentType(file.getContentType()); d.setFileSize(file.getSize()); d.setVersion(designRepository.countByOrderId(orderId) + 1); d.setUploadedBy(user);
        d = designRepository.save(d);
        return toResponse(d);
    }

    @Transactional(readOnly = true)
    public List<DesignDtos.DesignResponse> list(Long orderId, User user) {
        PrintOrder order = orderService.getEntity(orderId); orderService.checkAccess(order, user);
        return designRepository.findByOrderIdOrderByVersionDesc(orderId).stream().map(this::toResponse).toList();
    }

    public Design getEntity(Long id) { return designRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Design not found.")); }
    @Transactional(readOnly = true)
    public Path file(Long id, User user) {
        Design d = getEntity(id);
        orderService.checkAccess(d.getOrder(), user);
        return uploadRoot.resolve(d.getStoredFileName()).normalize();
    }

    public DesignDtos.DesignResponse toResponse(Design d) {
        return new DesignDtos.DesignResponse(d.getId(), d.getOrder().getId(), d.getOrder().getOrderNumber(), d.getOriginalFileName(),
                d.getContentType(), d.getFileSize(), d.getVersion(), d.isApproved(), d.getUploadedAt());
    }
}
