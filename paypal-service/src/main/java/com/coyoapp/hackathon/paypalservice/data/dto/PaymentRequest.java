package com.coyoapp.hackathon.paypalservice.data.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    String nonce;
    BigDecimal amount;
}
