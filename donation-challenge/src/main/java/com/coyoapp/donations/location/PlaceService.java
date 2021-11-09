package com.coyoapp.donations.location;

import java.time.Instant;
import java.util.Optional;

import com.coyoapp.donations.payment.PaymentPlaceRequest;
import com.coyoapp.donations.payment.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;

/**
 * A place is a location that has been donated towards. This service manages the places.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceAddressParser placeAddressParser;
    private final MapperFacade mapperFacade;

    public void createPlaces(PaymentRequest request) {
        request.getPlaces()
                .stream()
                .map(PaymentPlaceRequest::getDetails)
                .forEach(this::createPlace);
    }

    public Optional<Place> getByGooglePlaceId(String placeId) {
        return placeRepository.findByPlaceId(placeId);
    }

    public void createPlace(PlaceRequest placeRequest) {
        if (getByGooglePlaceId(placeRequest.getPlaceId()).isEmpty()) {
            createNewPlace(placeRequest);
        }
    }

    public Place createNewPlace(PlaceRequest placeRequest) {
        Place place = new Place();
        place.setAddress(placeAddressParser.parseAddress(placeRequest.getFormattedAddress()));
        place.setCompany(placeRequest.getName());
        place.setPlaceId(placeRequest.getPlaceId());
        place.setCreated(Instant.now());
        return placeRepository.save(place);
    }
}
