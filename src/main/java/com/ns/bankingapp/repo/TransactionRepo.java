package com.ns.bankingapp.repo;

import com.ns.bankingapp.model.Transaction;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    //@Query(value = "SELECT c From Card c JOIN c.account a JOIN a.client where a.client.id = :user")
    @Query("SELECT t from Transaction t JOIN t.account a JOIN a.client c where c.id = ?1")
    Optional<List<Transaction>> findByClient(Long clientId);
}
