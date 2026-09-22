package com.printflow.backend.service;

import com.printflow.backend.dto.ApprovalDtos;
import com.printflow.backend.entity.*;
import com.printflow.backend.exception.BadRequestException;
import com.printflow.backend.exception.ResourceNotFoundException;
import com.printflow.backend.repository.ApprovalRepository;
import com.printflow.backend.repository.ProductionJobRepository;
import com.printflow.backend.repository.DesignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalService {
    private final ApprovalRepository approvalRepository;
    private final DesignRepository designRepository;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final ProductionJobRepository productionJobRepository;

    public ApprovalService(ApprovalRepository approvalRepository, DesignRepository designRepository,
                           OrderService orderService, NotificationService notificationService, ProductionJobRepository productionJobRepository) {
        this.approvalRepository = approvalRepository; this.designRepository = designRepository;
        this.orderService = orderService; this.notificationService = notificationService; this.productionJobRepository = productionJobRepository;
    }

    @Transactional
    public void approve(Long orderId, ApprovalDtos.ApprovalRequest request, User customer) {
        process(orderId, request.designId(), ApprovalType.APPROVED, request.comment(), customer);
    }

    @Transactional
    public void changeRequest(Long orderId, ApprovalDtos.ChangeRequest request, User customer) {
        if (request.comment() == null || request.comment().isBlank()) throw new BadRequestException("Change request comment is required.");
        process(orderId, request.designId(), ApprovalType.CHANGE_REQUESTED, request.comment(), customer);
    }

    private void process(Long orderId, Long designId, ApprovalType type, String comment, User user) {
        PrintOrder order = orderService.getEntity(orderId); orderService.checkAccess(order, user);
        if (user.getRole() != Role.CUSTOMER) throw new BadRequestException("Only customer can approve a design.");
        Design design = designRepository.findByIdAndOrderId(designId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Design not found for this order."));
        Approval a = new Approval(); a.setOrder(order); a.setDesign(design); a.setType(type); a.setComment(comment); a.setRequestedBy(user); approvalRepository.save(a);
        design.setApproved(type == ApprovalType.APPROVED); designRepository.save(design);
        order.setStatus(type == ApprovalType.APPROVED ? OrderStatus.APPROVED : OrderStatus.CHANGE_REQUESTED);
        orderService.addHistory(order, order.getStatus(), type == ApprovalType.APPROVED ? "Design approved." : "Customer requested design changes.", user);
        if (type == ApprovalType.APPROVED) {
            productionJobRepository.findByOrderId(orderId).orElseGet(() -> { ProductionJob job = new ProductionJob(); job.setOrder(order); job.setStatus(ProductionStatus.PENDING); return productionJobRepository.save(job); });
        }
        notificationService.createForRole(Role.ADMIN, "Design decision", order.getOrderNumber() + " has a design " + (type == ApprovalType.APPROVED ? "approval." : "change request."));
    }

    @Transactional(readOnly = true)
    public List<ApprovalDtos.ApprovalResponse> list(Long orderId, User user) {
        PrintOrder order = orderService.getEntity(orderId); orderService.checkAccess(order, user);
        return approvalRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(a -> new ApprovalDtos.ApprovalResponse(
                a.getId(), a.getOrder().getId(), a.getDesign().getId(), a.getDesign().getOriginalFileName(),
                a.getType(), a.getComment(), a.getRequestedBy().getFullName(), a.getCreatedAt())).toList();
    }
}
