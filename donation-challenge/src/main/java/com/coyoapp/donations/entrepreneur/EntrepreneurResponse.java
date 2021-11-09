package com.coyoapp.donations.entrepreneur;

import com.coyoapp.donations.location.PlaceAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class EntrepreneurResponse {

    private String entrepreneurId;

    private String firstName;

    private String lastName;

    private String email;

    private String placeId;

    private String company;

    private PlaceAddress address;

    private BigDecimal balance;

    private Instant created;
}
