package com.ns.bankingapp.service;

import com.ns.bankingapp.exception.AccountNotFoundException;
import com.ns.bankingapp.exception.CardNotFoundException;
import com.ns.bankingapp.exception.CreditCardRequestException;
import com.ns.bankingapp.exception.UserNotFoundException;
import com.ns.bankingapp.model.*;
import com.ns.bankingapp.model.cardRequest.CardRequest;
import com.ns.bankingapp.model.cardRequest.CreditCardRequest;
import com.ns.bankingapp.model.cardRequest.DebitCardRequest;
import com.ns.bankingapp.repo.CardRepo;
import com.ns.bankingapp.repo.CardRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service @Slf4j @RequiredArgsConstructor
public class CardService {

    private final CardRepo cardRepo;
    private final CardRequestRepo cardRequestRepo;
    private final AccountService accountService;
    private final UserService userService;


    public DebitCardRequest createDebitCardRequest(Long accountId) throws AccountNotFoundException {
        Account account = accountService.getAccount(accountId);
        DebitCardRequest debitCardRequest = new DebitCardRequest(account, LocalDateTime.now());
        DebitCardRequest savedRequest = cardRequestRepo.save(debitCardRequest);
        log.info("Debit card request with id: {} was successfully saved", savedRequest.getId());
        return savedRequest;
    }

    public CardRequest getCardRequest(Long requestId) throws CardRequestNotFoundException {
        Optional<CardRequest> optionalCardRequest = cardRequestRepo.findById(requestId);

        if(optionalCardRequest.isPresent()){
            return optionalCardRequest.get();
        }else {
            throw new CardRequestNotFoundException("Card Request doesn't exist");
        }
    }


    @Transactional
    public Card approveDebitCardRequest(Long requestId) throws CardRequestNotFoundException {
        DebitCardRequest cardRequest = (DebitCardRequest) getCardRequest(requestId);
        cardRequest.setStatus(RequestStatus.APPROVED);
        log.info("Debit card request with id: {} was approved", cardRequest.getId());
        return createCard(cardRequest.getAccount(), CardType.DEBIT);
    }

    public Card createCard(Account account, CardType cardType){
        Card debitCard = new Card(cardType,account);
        Card savedCard = cardRepo.save(debitCard);
        log.info("{} card with id: {} was successfully saved", savedCard.getCardType(), savedCard.getId());
        return savedCard;
    }



    public CreditCardRequest createCreditCardRequest(Long clientId, BigDecimal monthlyIncome, String currencyCode)
            throws UserNotFoundException, CreditCardRequestException {

        if(monthlyIncome.compareTo(BigDecimal.valueOf(500.0)) >= 0) {
            Currency currency = Currency.getInstance(currencyCode);
            User user = userService.getUserById(clientId);
            CreditCardRequest creditCardRequest = new CreditCardRequest(user, currency,LocalDateTime.now());
            CreditCardRequest savedRequest = cardRequestRepo.save(creditCardRequest);
            log.info("Credit card Request was successfully saved!");
            return savedRequest;
        }else {
            throw new CreditCardRequestException("Monthly income must be greater than 500 EUR");
        }
    }

    @Transactional
    public Card approveCreditCardRequest(Long requestId, Double interest) throws CardRequestNotFoundException {

        CreditCardRequest cardRequest = (CreditCardRequest) getCardRequest(requestId);
        cardRequest.setStatus(RequestStatus.APPROVED);

        Account account = accountService.createTechnicalAccount(cardRequest.getUser(), cardRequest.getCurrency(), interest);
        return createCard(account,CardType.CREDIT);
    }

    public List<Card> getCards(Long clientId) throws UserNotFoundException, CardNotFoundException {
        User client = userService.getUserById(clientId);
        Optional<List<Card>> optionalCards = cardRepo.findByClient(client);

        if(optionalCards.isPresent()){
            return optionalCards.get();
        }else {
            throw new CardNotFoundException("User doesn't have any cards");
        }
    }

    public List<Card> getAllCards() {
        return cardRepo.findAll();
    }
}
