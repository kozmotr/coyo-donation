package com.coyoapp.hackathon.paypalservice.service;

import com.coyoapp.hackathon.paypalservice.data.dto.NonceResult;
import com.coyoapp.hackathon.paypalservice.data.dto.PaymentRequest;
import com.coyoapp.hackathon.paypalservice.data.dto.PaymentResult;

public interface PaypalService {
    PaymentResult processPayment(PaymentRequest request);
    NonceResult createNonce();
}
