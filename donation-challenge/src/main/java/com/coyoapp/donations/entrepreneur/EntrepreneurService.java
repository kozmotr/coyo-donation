package com.coyoapp.donations.entrepreneur;

import com.coyoapp.donations.invitation.InvitationToken;
import com.coyoapp.donations.invitation.InvitationTokenService;
import com.coyoapp.donations.location.PlaceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.coyoapp.donations.paypal.PaypalService;
import com.coyoapp.donations.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * An entrepreneur is an owner of a location. Use this service to update their information or find one.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EntrepreneurService {

    @Autowired
    private  InvitationTokenService invitationTokenService;
    private final EntrepreneurRepository entrepreneurRepository;
    private final PlaceService placeService;
    private final MapperFacade mapperFacade;
    private final TransactionService transactionService;

    public Optional<EntrepreneurResponse> getEntrepreneurByInvitationToken(String invitationToken) {
        return invitationTokenService.getByToken(invitationToken)
                .map(InvitationToken::getGooglePlaceId)
                .map(this::getEntrepreneurByGooglePlaceId)
                .map(this::transform);
    }

    public List<EntrepreneurDto> getAll(int page){
        return entrepreneurRepository.findAll(PageRequest.of(page,10))
                .stream().map(this::transformToDto)
                .collect(Collectors.toList());
    }

    private Entrepreneur getEntrepreneurByGooglePlaceId(String placeId) {
        return entrepreneurRepository.findByGooglePlaceId(placeId).orElseGet(() -> {
            Entrepreneur entrepreneur = new Entrepreneur();
            entrepreneur.setGooglePlaceId(placeId);
            return entrepreneur;
        });
    }

    private EntrepreneurResponse transform(Entrepreneur entrepreneur) {
        EntrepreneurResponse entrepreneurResponse = mapperFacade.map(entrepreneur, EntrepreneurResponse.class);
        placeService.getByGooglePlaceId(entrepreneur.getGooglePlaceId()).ifPresent(place -> {
            mapperFacade.map(place, entrepreneurResponse);
            //entrepreneurResponse.setPlaceId(entrepreneur.getGooglePlaceId()); /*there is no need set again the placeId*/
        });
        return entrepreneurResponse;
    }

    private EntrepreneurDto transformToDto(Entrepreneur entrepreneur) {
        EntrepreneurDto dto = mapperFacade.map(entrepreneur, EntrepreneurDto.class);
        return dto;
    }

    @SneakyThrows
    @Transactional
    public TransferBalanceResponse transferBalance(TransferBalanceRequest request){
        if(request.getAmount().compareTo(BigDecimal.valueOf(1l) ) < 0) {
            throw new IllegalStateException("Requested money can't be less than 1 Euro");
        }
        Optional<Entrepreneur> entrepreneurOptional = entrepreneurRepository.findById(request.getEntrepreneurId());
        entrepreneurOptional.orElseThrow(()-> new IllegalArgumentException("There is no entrepreneur with given id " + request.getEntrepreneurId()));
        Entrepreneur entrepreneur = entrepreneurOptional.get();
        if(request.getAmount().compareTo(entrepreneur.getBalance()) > 0) {
            return TransferBalanceResponse.builder()
                    .remainBalance(entrepreneur.getBalance())
                    .result("Unsufficent balance")
                    .build();
        } else {
            entrepreneur.setBalance(entrepreneur.getBalance().subtract(request.getAmount()));
            entrepreneurRepository.save(entrepreneur);
            transactionService.createTransaction(entrepreneur.getGooglePlaceId(),"balanceCheckoutId", request.getAmount().negate());
            return TransferBalanceResponse.builder()
                    .remainBalance(entrepreneur.getBalance())
                    .result(request.getAmount().toString() + "Euro transfered to iban address" + request.getIban())
                    .build();
        }
    }

    public Entrepreneur updateEntrepreneur(EntrepreneurUpdateRequest request){
        InvitationToken token = invitationTokenService.getByToken(request.getInvitationToken()).orElseThrow(
                () -> new IllegalArgumentException("The invitation token is invalid"));
        Optional<Entrepreneur> optionalEntrepreneur = entrepreneurRepository.findByGooglePlaceId(token.getGooglePlaceId());
        if(optionalEntrepreneur.isPresent()) {
            Entrepreneur e = mapperFacade.map(request, Entrepreneur.class);
            Entrepreneur.combine(e,optionalEntrepreneur.get());
            entrepreneurRepository.save(optionalEntrepreneur.get());
            return e;
        } else {
            throw new IllegalArgumentException(request.getInvitationToken() + " is not valid invitation token");
        }
    }

    public Entrepreneur createEntrepreneur(EntrepreneurCreateRequest request) {
        InvitationToken invitationToken = getAndValidateInvitationTokenAndGooglePlaceId(request);
        Entrepreneur entrepreneur = mapperFacade.map(request, Entrepreneur.class);
        entrepreneur.setGooglePlaceId(invitationToken.getGooglePlaceId());
        return entrepreneurRepository.save(entrepreneur);
    }


    private InvitationToken getAndValidateInvitationTokenAndGooglePlaceId(EntrepreneurCreateRequest request) {
        InvitationToken token = invitationTokenService.getByToken(request.getInvitationToken()).orElseThrow(
                () -> new IllegalArgumentException("The invitation token is invalid"));

        entrepreneurRepository.findByGooglePlaceId(token.getGooglePlaceId()).ifPresent(invitationToken -> {
            throw new IllegalStateException("An entrepreneur is already registered for this google place id");
        });
        return token;
    }

    public void addBalance(String placeId, BigDecimal amount) {
        Optional<Entrepreneur> entrepreneurOptional = entrepreneurRepository.findByGooglePlaceId(placeId);
        if(entrepreneurOptional.isPresent()) {
            Entrepreneur e = entrepreneurOptional.get();
            e.setBalance(e.getBalance().add(amount));
            entrepreneurRepository.save(e);
        } else {
            throw new IllegalArgumentException("for placeId " + placeId +" no entrepreneur ");
        }
    }
    public Optional<Entrepreneur> getByPlaceId(String placeId){
        return entrepreneurRepository.findByGooglePlaceId(placeId);
    }

}
