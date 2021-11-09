package com.coyoapp.donations.transaction;

import com.coyoapp.donations.aop.TrackMe;
import com.coyoapp.donations.invitation.InvitationToken;
import com.coyoapp.donations.invitation.InvitationTokenRepository;
import com.coyoapp.donations.location.PlaceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Handles the creations of transactions raised by donation processes. A transaction can be used to track all donations
 * and interact with the payment provider status later on.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final InvitationTokenRepository invitationTokenRepository;
    private final PlaceRepository placeRepository;


    @TrackMe
    public Transaction createTransaction(String googlePlaceId, String brainTreeTransactionId, BigDecimal amount) {
        log.trace("createTransaction(placeId={}, brainTreeTransactionId={})", googlePlaceId, brainTreeTransactionId);
        Transaction transaction = new Transaction();
        transaction.setGooglePlaceId(googlePlaceId);
        transaction.setBrainTreeTransactionId(brainTreeTransactionId);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction);
    }

    public TransactionsSummary getTransactionSummaryByInvitationToken(String invitationToken) {
        return invitationTokenRepository.findByToken(invitationToken)
                .map(InvitationToken::getGooglePlaceId)
                .map(this::sumTransactionsByGooglePlaceId)
                .orElse(new TransactionsSummary());
    }

    private TransactionsSummary sumTransactionsByGooglePlaceId(String placeId) {
        List<Transaction> transactions = transactionRepository.findByGooglePlaceId(placeId);
        BigDecimal amount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new TransactionsSummary(amount, transactions.size());
    }

    public List<TransactionResponse> getLast5Transactions() {
        return transactionRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "_id")))
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransaction(){
        return transactionRepository.findAll();
    }

    private TransactionResponse transform(Transaction transaction) {
        return placeRepository.findByPlaceId(transaction.getGooglePlaceId())
                .map(place -> new TransactionResponse(transaction.getAmount(), place.getCompany(), place.getAddress().getCity()))
                .orElse(new TransactionResponse());
    }
}
