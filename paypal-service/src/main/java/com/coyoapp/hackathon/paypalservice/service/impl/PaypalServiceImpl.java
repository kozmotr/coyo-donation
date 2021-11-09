package com.coyoapp.hackathon.paypalservice.service.impl;

import com.coyoapp.hackathon.paypalservice.data.dto.NonceResult;
import com.coyoapp.hackathon.paypalservice.data.dto.PaymentRequest;
import com.coyoapp.hackathon.paypalservice.data.dto.PaymentResult;
import com.coyoapp.hackathon.paypalservice.service.PaypalService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaypalServiceImpl implements PaypalService {

    public PaymentResult processPayment(PaymentRequest request) {
        PaymentResult paymentResult = PaymentResult.of(UUID.randomUUID().toString() , true);
        return paymentResult;
    }

    @Override
    public NonceResult createNonce() {
        return NonceResult.builder()
                .nonce(UUID.randomUUID().toString())
                .build();

    }
}
