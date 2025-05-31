//package com.smartavaas.controller;
//
//import com.smartavaas.dto.PaymentLinkResponse;
//import com.smartavaas.service.RentPaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/rent-payments")
//@RequiredArgsConstructor
//public class RentPaymentController {
//
//    private final RentPaymentService rentPaymentService;
//
//    @PostMapping("/pay-now/{rentId}")
//    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long rentId) {
//        return ResponseEntity.ok(rentPaymentService.createPaymentLinkForRent(rentId));
//    }
//}