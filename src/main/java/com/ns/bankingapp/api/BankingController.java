package com.ns.bankingapp.api;

import com.ns.bankingapp.exception.*;
import com.ns.bankingapp.model.*;
import com.ns.bankingapp.model.cardRequest.CreditCardRequest;
import com.ns.bankingapp.model.cardRequest.DebitCardRequest;
import com.ns.bankingapp.service.*;
import lombok.Data;
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

    @PostMapping("/request/account")
    public ResponseEntity<AccountRequest> currentAccountRequest(@RequestParam String username, @RequestParam String currencyCode) {
        User user = userService.getUser(username);
        Currency currency = Currency.getInstance(currencyCode);
        AccountRequest accountRequest = new AccountRequest(user, currency, LocalDateTime.now());
        return ResponseEntity.ok().body(accountService.saveRequest(accountRequest));
    }

    @PostMapping("/approve/account/request")
    public ResponseEntity<Account> approveCurrentAccountRequest(@RequestParam Long id) throws AccountRequestNotFoundException {
        return ResponseEntity.ok().body(accountService.approveRequest(id));
    }

    @PutMapping("/disapprove/account/request")
    public ResponseEntity<?> disapproveCurrentAccountRequest(@RequestParam Long id) throws AccountRequestNotFoundException {
        accountService.disapproveRequest(id);
        Map<String,String> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "Account Request with id: " + id + " was disapproved");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/request/debit/card")
    public ResponseEntity<DebitCardRequest> debitCardRequest(@RequestParam Long accountId) throws AccountNotFoundException {
        return ResponseEntity.ok().body(cardService.createDebitCardRequest(accountId));
    }

    @PostMapping("/approve/debit/card/request")
    public ResponseEntity<Card> approveDebitCardRequest(@RequestParam Long cardRequestId) throws CardRequestNotFoundException {
        return ResponseEntity.ok().body(cardService.approveDebitCardRequest(cardRequestId));
    }

    @PostMapping("/request/credit/card")
    public ResponseEntity<CreditCardRequest> creditCardRequest(@RequestBody CreditCardRequestForm form)
            throws UserNotFoundException, CreditCardRequestException {
            return ResponseEntity.ok().body(
                    cardService.createCreditCardRequest(form.getClientId(), form.getMonthlyIncome(), form.getCurrencyCode()));

    }

    @PostMapping("/approve/credit/card/request")
    public ResponseEntity<Card> approveCreditCardRequest(@RequestParam Long cardRequestId,@RequestParam Double interest)
            throws CardRequestNotFoundException {
        return ResponseEntity.ok().body(cardService.approveCreditCardRequest(cardRequestId, interest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDetails> makeTransaction(@RequestBody TransactionForm form)
            throws TransactionException, AccountNotFoundException {
        return ResponseEntity.ok().body(transactionService.makeTransaction(form.getAccountId(), form.getToIBAN(), form.getAmount()));
    }

}

@Data
class CreditCardRequestForm{
    private String currencyCode;
    private BigDecimal monthlyIncome;
    private Long clientId;
}
@Data
class TransactionForm{
     private Long accountId;
     private String toIBAN;
     private BigDecimal amount;
}
