package com.coyoapp.donations.location;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ToString
public class Place {

    public Place(){
        address = new PlaceAddress();
    }

    @Id
    private String id;

    private String placeId;

    private String company;

    private PlaceAddress address;

    private Instant created;
}
