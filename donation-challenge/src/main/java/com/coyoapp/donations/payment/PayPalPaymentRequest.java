package com.coyoapp.donations.payment;

import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
@Value(staticConstructor = "of")
public class PayPalPaymentRequest {
    String nonce;
    BigDecimal amount;
}
