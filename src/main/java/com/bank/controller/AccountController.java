
package com.bank.controller;

import com.bank.model.Account;
import com.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // 1. Create Account Endpoint (POST: /api/accounts)
    @PostMapping
    public ResponseEntity<Account> createAccount( @Valid  @RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // 2. Get Account by Number Endpoint (GET: /api/accounts/{accountNumber})
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    // 3. Get All Accounts Endpoint (GET: /api/accounts)
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // 4. Deposit Endpoint (POST: /api/accounts/{accountNumber}/deposit)
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        Account updatedAccount = accountService.deposit(accountNumber, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    // 5. Withdraw Endpoint (POST: /api/accounts/{accountNumber}/withdraw)
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        Account updatedAccount = accountService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    // 6. Fund Transfer Endpoint (POST: /api/accounts/transfer)
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody Map<String, Object> request) {
        String sourceAccount = (String) request.get("sourceAccount");
        String targetAccount = (String) request.get("targetAccount");
        Double amount = Double.valueOf(request.get("amount").toString());

        accountService.transferFunds(sourceAccount, targetAccount, amount);
        return ResponseEntity.ok("Transfer completed successfully!");
    }
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber); // Implement delete lookup in service if needed
        return ResponseEntity.ok("Account deleted successfully!");
    }
    @GetMapping("/max-balance")
    public ResponseEntity<Account> getMaxBalanceAccount() {
        return ResponseEntity.ok(accountService.getMaxBalanceAccount());
    }

    // Get Account with Minimum Balance
    @GetMapping("/min-balance")
    public ResponseEntity<Account> getMinBalanceAccount() {
        return ResponseEntity.ok(accountService.getMinBalanceAccount());
    }
    // Update account holder name or details
    @PutMapping("/{accountNumber}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber, @RequestBody Account updatedData) {
        Account updatedAccount = accountService.updateAccountHolderName(accountNumber, updatedData.getHolderName());
        return ResponseEntity.ok(updatedAccount);
    }
}