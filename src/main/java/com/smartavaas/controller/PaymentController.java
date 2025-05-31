package com.smartavaas.controller;

import com.smartavaas.dto.PaymentLinkResponse;
import com.smartavaas.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-link/{rentId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long rentId) {
        return ResponseEntity.ok(paymentService.createPaymentLinkForRent(rentId));
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody Map<String, Object> payload,
                                                 @RequestHeader("X-Razorpay-Signature") String signature) {
        paymentService.handleWebhook(payload, signature);
        System.out.println("Webhook Payload: " + payload);
        return ResponseEntity.ok("Webhook processed");
    }
}