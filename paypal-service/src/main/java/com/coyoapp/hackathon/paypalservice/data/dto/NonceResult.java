package com.coyoapp.hackathon.paypalservice.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NonceResult {
    private String nonce;
}
