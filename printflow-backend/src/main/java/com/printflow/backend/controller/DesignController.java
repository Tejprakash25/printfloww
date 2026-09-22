package com.printflow.backend.controller;

import com.printflow.backend.dto.DesignDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.DesignService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/designs")
public class DesignController {
    private final DesignService service;
    public DesignController(DesignService service) { this.service = service; }

    @PostMapping(value = "/order/{orderId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DesignDtos.DesignResponse upload(@PathVariable Long orderId, @RequestPart("file") MultipartFile file,
                                            @AuthenticationPrincipal User user) throws IOException { return service.upload(orderId, file, user); }

    @GetMapping("/order/{orderId}") public List<DesignDtos.DesignResponse> list(@PathVariable Long orderId, @AuthenticationPrincipal User user) { return service.list(orderId, user); }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id, @AuthenticationPrincipal User user) throws IOException {
        var design = service.getEntity(id);
        var path = service.file(id, user);
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(design.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : design.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + design.getOriginalFileName().replace("\"", "") + "\"")
                .body(resource);
    }
}
