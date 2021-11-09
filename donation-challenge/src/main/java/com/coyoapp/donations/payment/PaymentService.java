package com.coyoapp.donations.payment;

import com.coyoapp.donations.invitation.InvitationToken;
import com.coyoapp.donations.invitation.InvitationTokenService;
import com.coyoapp.donations.location.PlaceRequest;
import com.coyoapp.donations.location.PlaceService;
import com.coyoapp.donations.mail.MailService;
import com.coyoapp.donations.paypal.PaypalService;
import com.coyoapp.donations.transaction.TransactionService;
import java.math.BigDecimal;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles donations (payments) by users and starts the process for the place that has been donated to if necessary.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaypalService paypalService;
    private final TransactionService transactionService;
    private final InvitationTokenService invitationTokenService;
    private final PlaceService placeService;

    public PaypalService.PaypalResult processPayment(PaymentRequest request) {
        BigDecimal amount = PaymentPlaceRequest.calculateAmountSum(request.getPlaces());
        PaypalService.PaypalResult result = paypalService.processCheckout(request.getNonce(), amount);
        if (result.isSuccess()) {
            processSuccessfullyCheckout(request.getNonce(), request.getPlaces());
            placeService.createPlaces(request);
        }
        return result;
    }

    // this method violates SRP since its moved to PaymentPlaceRequest domain since this is not job of payment service.
   private BigDecimal calculateAmountSum(Collection<PaymentPlaceRequest> places) {
        return places.stream().map(PaymentPlaceRequest::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void processSuccessfullyCheckout(String transactionId, Collection<PaymentPlaceRequest> places) {
        places.forEach(place ->
                createTransactionAndSendInvitation(place.getDetails().getPlaceId(), transactionId, place.getAmount()));
    }

    /* This method violates SRP thus simplified.

       private void createTransactionAndSendInvitation(String placeId, String transactionId, BigDecimal amount) {
        transactionService.createTransaction(placeId, transactionId, amount);
        if (invitationTokenService.getByGooglePlaceId(placeId).isEmpty()) {
            createAndSendInvitationToken(placeId);
        }
    }
     */

    @Transactional
    void createTransactionAndSendInvitation(String placeId, String transactionId, BigDecimal amount) {
        transactionService.createTransaction(placeId, transactionId, amount);
        invitationTokenService.createAndSendInvitation(placeId , amount);
    }

    /* These methods are not related to PaymentService, They moved under the related service.
    This approach violates Single Responsibility Principle of SOLID,
    Thats why it makes PaymentService as GodClass which violates SRP.
       private void createAndSendInvitationToken(String placeId) {
        InvitationToken token = invitationTokenService.createToken(placeId);
        mailService.sendInvitationTokenMail(token);
    }

    private void createPlaces(PaymentRequest request) {
        request.getPlaces()
                .stream()
                .map(PaymentPlaceRequest::getDetails)
                .forEach(this::createPlace);
    }

    private void createPlace(PlaceRequest placeRequest) {
        if (placeService.getByGooglePlaceId(placeRequest.getPlaceId()).isEmpty()) {
            placeService.createPlace(placeRequest);
        }
    }
     */
}
