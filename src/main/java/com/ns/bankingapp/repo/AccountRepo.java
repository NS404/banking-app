package com.ns.bankingapp.repo;

import com.ns.bankingapp.model.Account;
import com.ns.bankingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByIBAN(String iban);

    Optional <List<Account>> findByClient(User user);

    @Query("Update Account a set a.balance = ?2 where a = ?1")
    @Modifying
    void updateBalance(Account fromAccount, BigDecimal newBalance);

    @Query(value = "SELECT iban from account", nativeQuery = true)
    List<String> findAllIBANs();

}