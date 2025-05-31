package com.smartavaas.service;

import com.smartavaas.dto.RentPaymentRequest;
import com.smartavaas.dto.RentPaymentResponse;
import com.smartavaas.mapper.RentPaymentMapper;
import com.smartavaas.model.RentDetail;
import com.smartavaas.model.User;
import com.smartavaas.payment.PaymentGatewayFactory;
import com.smartavaas.repository.RentDetailRepository;
import com.smartavaas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentDetailService {
    @Autowired
    private final RentDetailRepository rentPaymentRepo;
    private final UserRepository userRepository;
    private final PaymentGatewayFactory gatewayFactory;

    public List<RentPaymentResponse> getPaymentsForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return rentPaymentRepo.findByUser(user)
                .stream()
                .map(RentPaymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RentPaymentResponse payRent(RentPaymentRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Choose gateway based on mode
        String transactionId = gatewayFactory
                .getGateway(request.getMode())
                .processPayment(user, request);

        RentDetail payment = new RentDetail();
        payment.setUser(user);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus("PAID");
        payment.setMode(request.getMode());
        payment.setAutoPayEnabled(request.isAutoPayEnabled());
        payment.setInvoiceUrl("https://example.com/invoice/" + transactionId);
        payment.setReceiptUrl("https://example.com/receipt/" + transactionId);

        rentPaymentRepo.save(payment);

        return RentPaymentMapper.toResponse(payment);
    }
}