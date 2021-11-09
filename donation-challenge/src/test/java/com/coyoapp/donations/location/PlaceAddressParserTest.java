package com.coyoapp.donations.location;


import com.coyoapp.donations.entrepreneur.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceAddressParserTest {

    @InjectMocks
    private PlaceAddressParser placeAddressParser;

    @Test
    void shouldParseAddress() {
        // given
        String formattedAddress = "Pflugacker 7, 22523 Hamburg, Deutschland";

        // when
        PlaceAddress placeAddress = placeAddressParser.parseAddress(formattedAddress);

        // then
        assertThat(placeAddress.getAddress()).isEqualTo("Pflugacker 7");
        assertThat(placeAddress.getZip()).isEqualTo("22523");
        assertThat(placeAddress.getCity()).isEqualTo("Hamburg");
        assertThat(placeAddress.getCountry()).isEqualTo("Deutschland");
    }


}
