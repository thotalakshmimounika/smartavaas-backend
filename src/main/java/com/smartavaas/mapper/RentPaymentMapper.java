package com.smartavaas.mapper;

import com.smartavaas.dto.RentPaymentResponse;
import com.smartavaas.model.RentDetail;

public class RentPaymentMapper {
    public static RentPaymentResponse toResponse(RentDetail payment) {
        RentPaymentResponse res = new RentPaymentResponse();
        res.setId(payment.getId());
        res.setAmount(payment.getAmount());
        res.setPaymentDate(payment.getPaymentDate());
        res.setStatus(payment.getStatus());
        res.setMode(payment.getMode());
        res.setInvoiceUrl(payment.getInvoiceUrl());
        res.setReceiptUrl(payment.getReceiptUrl());
        return res;
    }
}
