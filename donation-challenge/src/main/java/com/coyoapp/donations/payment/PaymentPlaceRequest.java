package com.coyoapp.donations.payment;

import com.coyoapp.donations.location.PlaceRequest;
import java.math.BigDecimal;
import java.util.Collection;

import lombok.Data;

@Data
public class PaymentPlaceRequest {

    private PlaceRequest details;

    private BigDecimal amount;

    public static BigDecimal calculateAmountSum(Collection<PaymentPlaceRequest> places) {
        return places.stream().map(PaymentPlaceRequest::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
