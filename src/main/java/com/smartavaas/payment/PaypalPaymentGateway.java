package com.smartavaas.payment;

import com.smartavaas.dto.RentPaymentRequest;
import com.smartavaas.model.User;
import org.springframework.stereotype.Component;

@Component("PAYPAL")
public class PaypalPaymentGateway implements PaymentGateway {
    @Override
    public String processPayment(User user, RentPaymentRequest request) {
        // Logic to integrate PayPal payment
        return "PAYPAL_TXN_" + System.currentTimeMillis();
    }
}