package com.uplift.backend.dto.payment;

import jakarta.validation.constraints.NotNull;

public class RefundRequestDto {

    @NotNull
    private Long paymentId;

    private String reason;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
