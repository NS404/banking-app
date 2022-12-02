package com.ns.bankingapp.repo;

import com.ns.bankingapp.model.Account;
import com.ns.bankingapp.model.Card;
import com.ns.bankingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepo extends JpaRepository<Card, Long> {

    public Optional<Card> findCardByAccount(Account account);

    @Query(value = "SELECT c From Card c JOIN c.account a JOIN a.client where a.client = :user")
    Optional<List<Card>> findByClient(@Param("user") User user);
}
