package com.ns.bankingapp.repo;

import com.ns.bankingapp.model.AccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRequestRepo extends JpaRepository<AccountRequest, Long> {

}

