package com.coyoapp.hackathon.paypalservice.data.dto;

import lombok.Data;
import lombok.Value;

@Data
@Value(staticConstructor = "of")
public class PaymentResult {
        String transactionId;
        boolean success;

}
