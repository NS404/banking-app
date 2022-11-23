package com.ns.bankingapp.service;

import com.ns.bankingapp.model.Account;
import com.ns.bankingapp.model.AccountType;
import com.ns.bankingapp.model.Transaction;
import com.ns.bankingapp.model.TransactionType;
import com.ns.bankingapp.repo.AccountRepo;
import com.ns.bankingapp.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service @Slf4j @RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    public Transaction makeTransaction(Account fromAccount, String IBAN, String value){

        Account toAccount = accountService.getAccount(IBAN);

        if(fromAccount.getCurrency().equals(toAccount.getCurrency())) {

            if (accountService.hasCard(fromAccount) && accountService.hasCard(toAccount)) {

                if (fromAccount.getAccountType().equals(AccountType.CURRENT)) {

                    BigDecimal amount = new BigDecimal(value);

                    if (fromAccount.getBalance().compareTo(amount) >= 0) {

                        Transaction creditTransaction = new Transaction(toAccount, amount, fromAccount.getCurrency(), TransactionType.CREDIT);
                        Transaction debitTransaction = new Transaction(fromAccount, amount, toAccount.getCurrency(), TransactionType.DEBIT);
                        transfer(debitTransaction, creditTransaction);

                        return debitTransaction;
                    }
                }

            }
        }
        // balance availability

        //each fromAccount must have a card

        // interest calculation
            return null;
    }

    @Transactional
    public void transfer(Transaction debitTransaction, Transaction creditTransaction) {

        if(debitTransaction.getAccount().getAccountType().equals(AccountType.CURRENT)){

            BigDecimal amount = debitTransaction.getAmount();

            Account fromAccount = debitTransaction.getAccount();
            BigDecimal remainedBalance = fromAccount.getBalance().subtract(amount);
            accountRepo.updateBalance(fromAccount, remainedBalance);

            transactionRepo.save(debitTransaction);

            Account toAccount = creditTransaction.getAccount();
            BigDecimal newBalance = toAccount.getBalance().add(amount);
            accountRepo.updateBalance(toAccount, newBalance);

            transactionRepo.save(creditTransaction);


        }else {

            Account fromAccount = debitTransaction.getAccount();
            BigDecimal amount = debitTransaction.getAmount();
            BigDecimal interestRate = BigDecimal.valueOf(debitTransaction.getAccount().getInterest());
            BigDecimal interestValue = amount.multiply(interestRate);
            BigDecimal remainedBalance = fromAccount.getBalance().subtract(amount.add(interestValue));

            accountRepo.updateBalance(fromAccount, remainedBalance);

            transactionRepo.save(debitTransaction);


            Account toAccount = creditTransaction.getAccount();
            BigDecimal newBalance = toAccount.getBalance().add(amount);
            accountRepo.updateBalance(toAccount, newBalance);

            transactionRepo.save(creditTransaction);

        }
    }
}
