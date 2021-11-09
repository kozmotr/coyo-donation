package com.coyoapp.donations.entrepreneur;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferBalanceRequest {
    private String entrepreneurId;
    private BigDecimal amount;
    private String iban;
}
