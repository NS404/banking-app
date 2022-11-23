package com.ns.bankingapp.repo;

import com.ns.bankingapp.model.Role;
import com.ns.bankingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.annotation.Native;
import java.util.Collection;
import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    List<User> findByRole(Role role);
}
