package com.ns.bankingapp.api;

import com.ns.bankingapp.exception.AccountNotFoundException;
import com.ns.bankingapp.exception.CardNotFoundException;
import com.ns.bankingapp.exception.TransactionNotFoundException;
import com.ns.bankingapp.exception.UserNotFoundException;
import com.ns.bankingapp.model.Account;
import com.ns.bankingapp.model.Card;
import com.ns.bankingapp.model.Transaction;
import com.ns.bankingapp.service.AccountService;
import com.ns.bankingapp.service.CardService;
import com.ns.bankingapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final CardService cardService;
    private final TransactionService transactionService;


    @GetMapping("/accounts/{clientId}")
    public ResponseEntity<List<Account>> getClientAccounts(@PathVariable Long clientId) throws UserNotFoundException, AccountNotFoundException {
        return ResponseEntity.ok().body(accountService.getAccounts(clientId));
    }

    @GetMapping("/cards/{clientId}")
    public ResponseEntity<List<Card>> getClientCards(@PathVariable String clientId) throws UserNotFoundException, CardNotFoundException {
        return ResponseEntity.ok().body(cardService.getCards(Long.valueOf(clientId)));
    }

    @GetMapping("/transactions/{clientId}")
    public ResponseEntity<List<Transaction>> getClientTransactions(@PathVariable Long clientId) throws TransactionNotFoundException {
        return ResponseEntity.ok().body(transactionService.getTransactions(clientId));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok().body(accountService.getAllAccounts());
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok().body(cardService.getAllCards());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok().body(transactionService.getAllTransactions());

    }



}
