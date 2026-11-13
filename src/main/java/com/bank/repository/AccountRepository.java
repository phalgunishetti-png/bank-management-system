package com.bank.repository;

import com.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // 1. Find an account by its unique account number (Used for deposits, withdrawals, and balance checks)
    Optional<Account> findByAccountNumber(String accountNumber);

    // 2. Check if an account already exists by account number (Prevents duplicate account creation)
    boolean existsByAccountNumber(String accountNumber);

    // 3. Delete an account by account number
    void deleteByAccountNumber(String accountNumber);
    // Find accounts by holder name (case-insensitive or exact match)
    List<Account> findByHolderNameContainingIgnoreCase(String holderName);
}
