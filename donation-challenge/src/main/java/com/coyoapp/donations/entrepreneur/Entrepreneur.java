package com.coyoapp.donations.entrepreneur;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class Entrepreneur {

    @Id
    private String entrepreneurId;

    private String googlePlaceId;

    private String firstName;

    private String lastName;

    private String email;

    private Instant created;

    private BigDecimal balance;

    /*helper function to save */
    public static void combine(Entrepreneur source, Entrepreneur destination) {
        if(destination.getBalance() == null) destination.setBalance(source.getBalance());
        if(destination.getEmail() == null) destination.setEmail(source.getEmail());
        if(destination.getGooglePlaceId() == null) destination.setEmail(source.getGooglePlaceId());
        if(destination.getFirstName() == null) destination.setFirstName(source.getFirstName());
        if(destination.getLastName() == null) destination.setLastName(source.getLastName());

    }
}
