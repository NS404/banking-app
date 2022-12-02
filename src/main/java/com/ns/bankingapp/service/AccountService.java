package com.ns.bankingapp.service;

import com.ns.bankingapp.exception.AccountNotFoundException;
import com.ns.bankingapp.exception.AccountRequestNotFoundException;
import com.ns.bankingapp.exception.UserNotFoundException;
import com.ns.bankingapp.model.*;
import com.ns.bankingapp.repo.AccountRepo;
import com.ns.bankingapp.repo.AccountRequestRepo;
import com.ns.bankingapp.repo.CardRepo;
import com.ns.bankingapp.util.IBANUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service @Slf4j @RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountRequestRepo accountRequestRepo;
    private final CardRepo cardRepo;

    private final UserService userService;

    public Account getAccount(String IBAN) {
        return accountRepo.findByIBAN(IBAN);
    }



    public Account saveAccount(Account account) {
        return accountRepo.save(account);
    }

    public void deleteAccount(Account account) {
        accountRepo.delete(account);
    }

    public Account createCurrentAccount(User user, Currency currency) {
        Account account = new Account(user,currency,0.0, AccountType.CURRENT,generateIBAN());
        account = saveAccount(account);
        log.info("Current Account with IBAN: {} was successfully created",account.getIBAN());
        return account;
    }

    public String generateIBAN() {
        String IBAN = IBANUtil.generateIBAN();
        List<String> allIBANs = accountRepo.findAllIBANs();

        boolean IBANExist = allIBANs.stream().anyMatch(existingIBAN -> existingIBAN.equals(IBAN));
        if(IBANExist)
            generateIBAN();

        return IBAN;
    }

    public Account createTechnicalAccount(User user, Currency currency, Double interest) {
        Account account = new Account(user, currency, interest, AccountType.TECHNICAL, generateIBAN());
        account = saveAccount(account);
        log.info("Technical Account with IBAN: {} was successfully created", account.getIBAN());
        return account;
    }

    public AccountRequest saveRequest(AccountRequest accountRequest) {
        AccountRequest savedRequest = accountRequestRepo.save(accountRequest);
        log.info("Account request with id: {} was saved", savedRequest.getId());
        return savedRequest;
    }

    public void deleteRequest(AccountRequest accountRequest) {
        accountRequestRepo.delete(accountRequest);
    }

    @Transactional
    public Account approveRequest(Long id) throws AccountRequestNotFoundException {
        Optional<AccountRequest> optionalAccountRequest = accountRequestRepo.findById(id);

        if(optionalAccountRequest.isPresent()){
            AccountRequest accountRequest = optionalAccountRequest.get();
            accountRequest.setStatus(RequestStatus.APPROVED);
            log.info("Request with id {} was approved", accountRequest.getId());

            return createCurrentAccount(accountRequest.getUser(), accountRequest.getCurrency());
        }else {
            throw new AccountRequestNotFoundException("Account Request Not Found!");
        }
    }

    @Transactional
    public void disapproveRequest(Long id) throws AccountRequestNotFoundException {
        Optional<AccountRequest> optionalAccountRequest = accountRequestRepo.findById(id);

        if(optionalAccountRequest.isPresent()){
            AccountRequest accountRequest = optionalAccountRequest.get();
            accountRequest.setStatus(RequestStatus.DISAPPROVED);
            log.info("Request with id: {} was disapproved", accountRequest.getId());
        }else {
            throw new AccountRequestNotFoundException("Account Request Not Found!");
        }
    }

    public boolean hasCard(Account account) {
        Optional<Card> cardByAccount = cardRepo.findCardByAccount(account);
        return cardByAccount.isPresent();
    }


    public List<Account> getAccounts(Long clientId) throws UserNotFoundException, AccountNotFoundException {
        User user = userService.getUserById(clientId);
        Optional<List<Account>> optionalAccounts = accountRepo.findByClient(user);
        if (optionalAccounts.isPresent()){
            return optionalAccounts.get();
        }else {
            throw new AccountNotFoundException("User doesn't have any accounts");
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Account getAccount(Long accountId) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepo.findById(accountId);
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }else {
            throw new AccountNotFoundException("Account doesn't exist");
        }

    }
}
