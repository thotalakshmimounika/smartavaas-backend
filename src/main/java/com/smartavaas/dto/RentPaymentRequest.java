package com.smartavaas.dto;

import lombok.Data;

@Data
public class RentPaymentRequest {
    private Double amount;
    private String mode;
    private boolean autoPayEnabled;
}