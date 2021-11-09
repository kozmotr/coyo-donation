package com.coyoapp.donations.entrepreneur;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferBalanceResponse {

    private String result;
    private BigDecimal remainBalance;
}
