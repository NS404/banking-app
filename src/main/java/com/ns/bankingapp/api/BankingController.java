package com.ns.bankingapp.api;

import com.ns.bankingapp.exception.AccountRequestNotFoundException;
import com.ns.bankingapp.model.*;
import com.ns.bankingapp.model.cardRequest.CreditCardRequest;
import com.ns.bankingapp.model.cardRequest.DebitCardRequest;
import com.ns.bankingapp.service.AccountService;
import com.ns.bankingapp.service.CardService;
import com.ns.bankingapp.service.TransactionService;
import com.ns.bankingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BankingController {

    private final AccountService accountService;
    private final CardService cardService;
    private final UserService userService;
    private final TransactionService transactionService;

    @PostMapping("/account/request")
    public ResponseEntity<AccountRequest> currentAccountRequest(@RequestParam String username,
                                                                @RequestParam String currencyCode) {
        User user = userService.getUser(username);
        Currency currency = Currency.getInstance(currencyCode);
        AccountRequest accountRequest = new AccountRequest(user, currency, LocalDateTime.now());
        return ResponseEntity.ok().body(accountService.saveRequest(accountRequest));
    }

    @PostMapping("/account/request/approve/{id}")
    public ResponseEntity<Account> approveCurrentAccountRequest(@PathVariable Long id) throws AccountRequestNotFoundException {
        return ResponseEntity.ok().body(accountService.approveRequest(id));
    }
    @PutMapping("/account/request/disapprove/{id}")
    public ResponseEntity<?> disapproveCurrentAccountRequest(@PathVariable Long id) throws AccountRequestNotFoundException {
        accountService.disapproveRequest(id);
        Map<String,String> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "Account Request with id: " + id + " was disapproved");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/debit/card/request")
    public ResponseEntity<DebitCardRequest> debitCardRequest(@RequestBody Account account){
        return ResponseEntity.ok().body(cardService.createDebitCardRequest(account));
    }

    @PostMapping("/debit/card/request/approve")
    public ResponseEntity<Card> approveDebitCardRequest(@RequestBody DebitCardRequest cardRequest){
        return ResponseEntity.ok().body(cardService.approveDebitCardRequest(cardRequest));
    }

    @PostMapping("/credit/card/request")
    public ResponseEntity<CreditCardRequest> creditCardRequest(@RequestBody String currencyCode,
                                                               String username,
                                                               BigDecimal monthlyIncome){
        CreditCardRequest creditCardRequest = cardService.createCreditCardRequest(username, monthlyIncome, currencyCode);

        if(creditCardRequest != null) {
            return ResponseEntity.ok().body(creditCardRequest);
        }

        return null;
    }

    @PostMapping("/credit/card/request/approve")
    public ResponseEntity<Card> approveCreditCardRequest(@RequestBody CreditCardRequest cardRequest, Double interest){
        return ResponseEntity.ok().body(cardService.approveCreditCardRequest(cardRequest, interest));
    }

    @PostMapping("transaction/")
    public ResponseEntity<Transaction> makeTransaction(@RequestBody Account account, String IBAN, String amount){
        return ResponseEntity.ok().body(transactionService.makeTransaction(account, IBAN, amount));
    }

}
