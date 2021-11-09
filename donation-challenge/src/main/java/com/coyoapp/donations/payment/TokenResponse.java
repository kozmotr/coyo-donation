package com.coyoapp.donations.payment;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class TokenResponse {
    private final String token;
}
