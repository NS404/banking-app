package com.ns.bankingapp.service;

import com.ns.bankingapp.exception.AccountNotFoundException;
import com.ns.bankingapp.exception.TransactionException;
import com.ns.bankingapp.exception.TransactionNotFoundException;
import com.ns.bankingapp.model.*;
import com.ns.bankingapp.repo.AccountRepo;
import com.ns.bankingapp.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service @Slf4j @RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    public TransactionDetails makeTransaction(Long accountId, String toIBAN, BigDecimal amount)
            throws TransactionException, AccountNotFoundException {

        Account fromAccount = accountService.getAccount(accountId);
        Account toAccount = accountService.getAccount(toIBAN);


        if(fromAccount.getCurrency().equals(toAccount.getCurrency())) {

            if (accountService.hasCard(fromAccount) && accountService.hasCard(toAccount)) {

                if (fromAccount.getAccountType().equals(AccountType.TECHNICAL)) {
                     return transfer(fromAccount, toAccount, amount);

                }else if(fromAccount.getAccountType().equals(AccountType.CURRENT)){

                    if (fromAccount.getBalance().compareTo(amount) >= 0) {
                        return transfer(fromAccount, toAccount, amount);

                    }else throw new TransactionException("Transaction failed! Insufficient funds!");
                }
            }else throw new TransactionException("Transaction failed! Accounts must have cards");

        }else throw new TransactionException("Transaction failed! Accounts have different currencies");

        return null;
    }

    @Transactional
    public TransactionDetails transfer(Account fromAccount, Account toAccount, BigDecimal amount){

        BigDecimal interestRate = BigDecimal.valueOf(fromAccount.getInterest());
        BigDecimal interestValue = amount.multiply(interestRate);
        BigDecimal remainedBalance = fromAccount.getBalance().subtract(amount.add(interestValue));

        accountRepo.updateBalance(fromAccount, remainedBalance);

        Transaction debitTransaction = new Transaction(fromAccount, amount, fromAccount.getCurrency(), TransactionType.DEBIT);
        transactionRepo.save(debitTransaction);

        log.info("Debit transaction from Account: {} was successfully saved",  fromAccount.getIBAN());

        BigDecimal newBalance = toAccount.getBalance().add(amount);
        accountRepo.updateBalance(toAccount, newBalance);

        Transaction creditTransaction = new Transaction(toAccount, amount, toAccount.getCurrency(), TransactionType.CREDIT);
        transactionRepo.save(creditTransaction);

        log.info("Credit transaction to Account: {} was successfully saved",  toAccount.getIBAN());

        return new TransactionDetails(fromAccount.getIBAN(), toAccount.getIBAN(), amount, fromAccount.getCurrency());
    }

    public List<Transaction> getTransactions(Long clientId) throws TransactionNotFoundException {
        Optional<List<Transaction>> optionalTransactions = transactionRepo.findByClient(clientId);

        if(optionalTransactions.isPresent()){
            return optionalTransactions.get();
        }else throw new TransactionNotFoundException("client doesnt have any transaction");
    }

    public List<Transaction> getAllTransactions() {
       return transactionRepo.findAll();
    }
}
