package com.ns.bankingapp.service;

import com.ns.bankingapp.exception.CardNotFoundException;
import com.ns.bankingapp.exception.UserNotFoundException;
import com.ns.bankingapp.model.*;
import com.ns.bankingapp.model.cardRequest.CreditCardRequest;
import com.ns.bankingapp.model.cardRequest.DebitCardRequest;
import com.ns.bankingapp.repo.CardRepo;
import com.ns.bankingapp.repo.CardRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public Card saveCard(Card card) {
        return cardRepo.save(card);
    }

    public DebitCardRequest createDebitCardRequest(Account account) {
        DebitCardRequest debitCardRequest = new DebitCardRequest(account, LocalDateTime.now());
        return cardRequestRepo.save(debitCardRequest);
    }

    public Card approveDebitCardRequest(DebitCardRequest cardRequest) {
        Account account = cardRequest.getAccount();
        Card debitCard = new Card(CardType.DEBIT,account);
        return saveCard(debitCard);
    }

    public CreditCardRequest createCreditCardRequest(String username, BigDecimal monthlyIncome, String currencyCode) {
        if(monthlyIncome.compareTo(BigDecimal.valueOf(500.0)) >= 0) {
            Currency currency = Currency.getInstance(currencyCode);
            User user = userService.getUser(username);
            return new CreditCardRequest(user, currency);
        }else {
            return null;
        }
    }

    public Card approveCreditCardRequest(CreditCardRequest creditCardRequest, Double interest ) {
        Account account = accountService.createTechnicalAccount(creditCardRequest.getUser(),
                creditCardRequest.getCurrency(), interest);

        return cardRepo.save(new Card(CardType.CREDIT, account));
    }

    public List<Card> getClientCards(Long clientId) throws UserNotFoundException, CardNotFoundException {
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
