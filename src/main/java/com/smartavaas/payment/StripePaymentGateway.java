package com.smartavaas.payment;

import com.smartavaas.dto.RentPaymentRequest;
import com.smartavaas.model.User;
import org.springframework.stereotype.Component;

@Component("STRIPE")
public class StripePaymentGateway implements PaymentGateway {
    @Override
    public String processPayment(User user, RentPaymentRequest request) {
        // Logic to integrate Stripe payment
        return "STRIPE_TXN_" + System.currentTimeMillis();
    }
}