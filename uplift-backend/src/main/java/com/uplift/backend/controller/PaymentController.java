package com.uplift.backend.controller;

import com.uplift.backend.dto.common.ApiResponse;
import com.uplift.backend.dto.payment.PaymentRequestDto;
import com.uplift.backend.dto.payment.RefundRequestDto;
import com.uplift.backend.entity.Payment;
import com.uplift.backend.entity.User;
import com.uplift.backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ApiResponse<Payment> createPayment(@AuthenticationPrincipal User user,
                                              @Valid @RequestBody PaymentRequestDto dto) {
        Payment p = paymentService.createPayment(user, dto);
        return ApiResponse.ok("Payment created", p);
    }

    @PostMapping("/{id}/success")
    public ApiResponse<Payment> markSuccess(@PathVariable Long id) {
        Payment p = paymentService.markSuccess(id);
        return ApiResponse.ok("Payment marked as success", p);
    }

    @PostMapping("/refund")
    public ApiResponse<Payment> refund(@Valid @RequestBody RefundRequestDto dto) {
        Payment p = paymentService.markRefunded(dto.getPaymentId());
        return ApiResponse.ok("Payment refunded", p);
    }

    @GetMapping("/me")
    public ApiResponse<List<Payment>> myPayments(@AuthenticationPrincipal User user) {
        List<Payment> payments = paymentService.paymentsForUser(user);
        return ApiResponse.ok("Payments for current user", payments);
    }
}
