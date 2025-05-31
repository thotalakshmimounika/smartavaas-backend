package com.smartavaas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentLinkResponse {
    private String paymentLinkId;
    private String paymentUrl;
}