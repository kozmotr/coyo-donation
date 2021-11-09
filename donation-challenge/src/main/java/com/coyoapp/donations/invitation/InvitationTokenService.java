package com.coyoapp.donations.invitation;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.coyoapp.donations.entrepreneur.Entrepreneur;
import com.coyoapp.donations.entrepreneur.EntrepreneurCreateRequest;
import com.coyoapp.donations.entrepreneur.EntrepreneurService;
import com.coyoapp.donations.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Invitation tokens are lightweight tokens that are send to owners. The owner can use the token to check his balance or
 * set his contact information.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvitationTokenService {

    private final InvitationTokenRepository tokenRepository;
    private final MailService mailService;
    private final EntrepreneurService entrepreneurService;

    public Optional<InvitationToken> getByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Optional<InvitationToken> getByGooglePlaceId(String googlePlaceId) {
        return tokenRepository.findByGooglePlaceId(googlePlaceId);
    }



    public InvitationToken createAndSendInvitation(String placeId, BigDecimal amount){
        Optional<InvitationToken> tokenOptional = getByGooglePlaceId(placeId);
        if (!tokenOptional.isPresent()) {
            InvitationToken token = createToken(placeId);
            EntrepreneurCreateRequest entrepreneurCreateRequest =
                    new EntrepreneurCreateRequest();
            entrepreneurCreateRequest.setBalance(amount);
            entrepreneurCreateRequest.setInvitationToken(token.getToken());
            entrepreneurService.createEntrepreneur(entrepreneurCreateRequest);
            mailService.sendInvitationTokenMail(token);
            return token;
        } else {
            entrepreneurService.addBalance(placeId, amount);
        }
        return tokenOptional.get();
    }




    public InvitationToken createToken(String googlePlaceId) {
        InvitationToken token = new InvitationToken();
        token.setGooglePlaceId(googlePlaceId);
        token.setToken(UUID.randomUUID().toString());
        return tokenRepository.save(token);
    }
}
