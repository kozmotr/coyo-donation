package com.coyoapp.donations.entrepreneur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrepreneurUpdateRequest {

    protected String invitationToken;

    protected String firstName;

    protected String lastName;

    protected String email;
}
