package com.uplift.backend.service;

import com.uplift.backend.dto.payment.PaymentRequestDto;
import com.uplift.backend.entity.Payment;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.PaymentStatus;
import com.uplift.backend.enums.RideType;
import com.uplift.backend.repository.PaymentRepository;
import com.uplift.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public Payment createPayment(User payer, PaymentRequestDto dto) {
        User payee = userRepository.findById(dto.getPayeeId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Payee not found"));

        Payment p = new Payment();
        p.setRideType(RideType.valueOf(dto.getRideType()));
        p.setRideId(dto.getRideId());
        p.setPayer(payer);
        p.setPayee(payee);
        p.setAmount(dto.getAmount());
        p.setStatus(PaymentStatus.PENDING);

        return paymentRepository.save(p);
    }

    public Payment markSuccess(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Payment not found"));
        p.setStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(p);
    }

    public Payment markRefunded(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Payment not found"));
        p.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(p);
    }

    public List<Payment> paymentsForUser(User user) {
        List<Payment> asPayer = paymentRepository.findByPayer(user);
        List<Payment> asPayee = paymentRepository.findByPayee(user);
        asPayer.addAll(asPayee);
        return asPayer;
    }
}
