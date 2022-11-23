package com.ns.bankingapp.repo;

import com.ns.bankingapp.model.cardRequest.CardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRequestRepo extends JpaRepository<CardRequest, Long> {
}
