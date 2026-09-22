package com.printflow.backend.service;

import com.printflow.backend.dto.ResponseDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.InvoiceRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.UUID;

@Service
public class InvoiceService {
    private final InvoiceRepository repository;
    private final OrderService orderService;
    private final Path invoiceRoot;

    public InvoiceService(InvoiceRepository repository, OrderService orderService, @Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.repository = repository; this.orderService = orderService;
        invoiceRoot = Paths.get(uploadDir, "invoices").toAbsolutePath().normalize(); Files.createDirectories(invoiceRoot);
    }

    public ResponseDtos.InvoiceResponse create(Long orderId, User admin) throws IOException {
        if (admin.getRole() != Role.ADMIN) throw new BadRequestException("Only admin can create invoices.");
        PrintOrder order = orderService.getEntity(orderId);
        Invoice invoice = repository.findByOrderId(orderId).orElseGet(Invoice::new);
        invoice.setOrder(order); invoice.setInvoiceNumber(invoice.getInvoiceNumber() == null ? "INV/" + order.getOrderNumber() : invoice.getInvoiceNumber());
        invoice.setAmount(order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount());
        invoice = repository.save(invoice);
        Path pdf = invoiceRoot.resolve(invoice.getInvoiceNumber().replaceAll("[^a-zA-Z0-9_-]", "_") + ".pdf");
        generatePdf(invoice, pdf);
        invoice.setPdfPath(pdf.toString()); repository.save(invoice);
        return response(invoice);
    }

    public Invoice getEntity(Long orderId) { return repository.findByOrderId(orderId).orElseThrow(() -> new ResourceNotFoundException("Invoice not found.")); }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseDtos.InvoiceResponse get(Long orderId, User user) {
        Invoice i = getEntity(orderId); orderService.checkAccess(i.getOrder(), user); return response(i);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ByteArrayResource pdf(Long orderId, User user) throws IOException {
        Invoice i = getEntity(orderId); orderService.checkAccess(i.getOrder(), user);
        if (i.getPdfPath() == null) throw new ResourceNotFoundException("Invoice PDF has not been generated.");
        return new ByteArrayResource(Files.readAllBytes(Paths.get(i.getPdfPath())));
    }

    private void generatePdf(Invoice invoice, Path path) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(); doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText(); cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20); cs.newLineAtOffset(60, 740); cs.showText("PrintFlow Invoice"); cs.endText();
                cs.beginText(); cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12); cs.newLineAtOffset(60, 700);
                cs.showText("Invoice: " + invoice.getInvoiceNumber()); cs.newLineAtOffset(0, -22);
                cs.showText("Order: " + invoice.getOrder().getOrderNumber()); cs.newLineAtOffset(0, -22);
                cs.showText("Customer: " + invoice.getOrder().getCustomer().getFullName()); cs.newLineAtOffset(0, -22);
                cs.showText("Product: " + invoice.getOrder().getProductType()); cs.newLineAtOffset(0, -22);
                cs.showText("Quantity: " + invoice.getOrder().getQuantity()); cs.newLineAtOffset(0, -22);
                cs.showText("Total: INR " + invoice.getAmount()); cs.endText();
            }
            doc.save(path.toFile());
        }
    }

    private ResponseDtos.InvoiceResponse response(Invoice i) {
        return new ResponseDtos.InvoiceResponse(i.getId(), i.getInvoiceNumber(), i.getOrder().getId(), i.getOrder().getOrderNumber(), i.getAmount(), i.getIssuedAt());
    }
}
