package com.printflow.backend.controller;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.User;
import com.printflow.backend.service.InvoiceService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService service;
    public InvoiceController(InvoiceService service) { this.service = service; }
    @PostMapping("/order/{orderId}") public ResponseDtos.InvoiceResponse create(@PathVariable Long orderId, @AuthenticationPrincipal User user) throws IOException { return service.create(orderId, user); }
    @GetMapping("/order/{orderId}") public ResponseDtos.InvoiceResponse get(@PathVariable Long orderId, @AuthenticationPrincipal User user) { return service.get(orderId, user); }
    @GetMapping("/order/{orderId}/pdf") public ResponseEntity<ByteArrayResource> pdf(@PathVariable Long orderId, @AuthenticationPrincipal User user) throws IOException {
        ByteArrayResource resource = service.pdf(orderId, user);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + orderId + ".pdf").body(resource);
    }
}
