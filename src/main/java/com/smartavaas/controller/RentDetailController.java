package com.smartavaas.controller;

import com.smartavaas.dto.RentPaymentRequest;
import com.smartavaas.dto.RentPaymentResponse;
import com.smartavaas.service.RentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rent")
@RequiredArgsConstructor
public class RentDetailController {

    @Autowired
    private RentDetailService rentDetailService;

    @GetMapping("/my-payments")
    public ResponseEntity<List<RentPaymentResponse>> getMyPayments(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(rentDetailService.getPaymentsForUser(email));
    }

    @PostMapping("/pay")
    public ResponseEntity<RentPaymentResponse> makePayment(@RequestBody RentPaymentRequest request, Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(rentDetailService.payRent(request, email));
    }
}