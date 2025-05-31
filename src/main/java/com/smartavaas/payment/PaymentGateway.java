package com.smartavaas.payment;

import com.smartavaas.dto.RentPaymentRequest;
import com.smartavaas.model.User;

public interface PaymentGateway {
    String processPayment(User user, RentPaymentRequest request);
}
