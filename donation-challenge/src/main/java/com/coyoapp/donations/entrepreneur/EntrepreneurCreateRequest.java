package com.coyoapp.donations.entrepreneur;

import com.coyoapp.donations.invitation.InvitationToken;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EntrepreneurCreateRequest extends EntrepreneurUpdateRequest{
    private BigDecimal balance;
}
