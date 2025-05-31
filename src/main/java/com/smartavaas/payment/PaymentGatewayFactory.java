package com.smartavaas.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentGatewayFactory {

    @Autowired
    private ApplicationContext context;

    public PaymentGateway getGateway(String mode) {
        return context.getBean(mode.toUpperCase(), PaymentGateway.class);
    }
}
