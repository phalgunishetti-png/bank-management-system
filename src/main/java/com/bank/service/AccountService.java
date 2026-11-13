package com.bank.service;

import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // 1. Create a new account (checks for duplicates first)
    public Account createAccount(Account account) {
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new RuntimeException("Account already exists with number: " + account.getAccountNumber());
        }
        return accountRepository.save(account);
    }

    // 2. Fetch account by account number
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with number: " + accountNumber));
    }

    // 3. Get all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 4. Deposit funds
    @Transactional
    public Account deposit(String accountNumber, Double amount) {
        Account account = getAccountByNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    // 5. Withdraw funds with balance validation
    @Transactional
    public Account withdraw(String accountNumber, Double amount) {
        Account account = getAccountByNumber(accountNumber);
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance in account: " + accountNumber);
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    // 6. Fund Transfer between two accounts (Full Transaction Safety)
    @Transactional
    public void transferFunds(String sourceAccountNum, String targetAccountNum, Double amount) {
        // Withdraw from source account
        Account sourceAccount = getAccountByNumber(sourceAccountNum);
        if (sourceAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds for transfer from account: " + sourceAccountNum);
        }

        // Get target account
        Account targetAccount = getAccountByNumber(targetAccountNum);

        // Execute transfer
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        // Save both states
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
    // 7. Delete an account by account number
    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber); // Ensures it exists first
        accountRepository.delete(account);
    }
    // Find account with Maximum Balance
    public Account getMaxBalanceAccount() {
        return accountRepository.findAll().stream()
                .max(java.util.Comparator.comparing(Account::getBalance))
                .orElseThrow(() -> new RuntimeException("No accounts found"));
    }

    // Find account with Minimum Balance
    public Account getMinBalanceAccount() {
        return accountRepository.findAll().stream()
                .min(java.util.Comparator.comparing(Account::getBalance))
                .orElseThrow(() -> new RuntimeException("No accounts found"));
    }
    // Safe update method that preserves ID and account number
    @Transactional
    public Account updateAccountHolderName(String accountNumber, String newHolderName) {
        Account existingAccount = getAccountByNumber(accountNumber); // Finds it by account number
        existingAccount.setHolderName(newHolderName);
        return accountRepository.save(existingAccount); // JPA sees the ID, so it updates instead of inserting
    }
}
