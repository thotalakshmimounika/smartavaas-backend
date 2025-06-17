package com.smartavaas.controller;

import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.dto.BaseApiResponse;
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
    public ResponseEntity<BaseApiResponse<PaymentLinkResponse>> createPaymentLink(@PathVariable Long rentId) {
        PaymentLinkResponse paymentLink = paymentService.createPaymentLinkForRent(rentId);
        return ResponseEntity.ok(ApiResponseBuilder.success("Payment link created successfully", paymentLink));
    }


    @PostMapping("/callback")
    public ResponseEntity<BaseApiResponse<String>> handleCallback(@RequestBody Map<String, Object> payload,
                                                                  @RequestHeader("X-Razorpay-Signature") String signature) {
        paymentService.handleWebhook(payload, signature);
        return ResponseEntity.ok(ApiResponseBuilder.success("Webhook processed successfully", "OK"));
    }
}