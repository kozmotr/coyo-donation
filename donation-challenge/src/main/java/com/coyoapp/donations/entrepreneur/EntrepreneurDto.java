package com.coyoapp.donations.entrepreneur;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
public class EntrepreneurDto {

    @Id
    private String entrepreneurId;

    private String googlePlaceId;

    private String firstName;

    private String lastName;

}
