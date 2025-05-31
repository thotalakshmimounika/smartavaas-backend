package com.smartavaas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RentPaymentResponse {
    private Long id;
    private Double amount;
    private LocalDate paymentDate;
    private String status;
    private String mode;
    private String invoiceUrl;
    private String receiptUrl;
}