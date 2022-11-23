package com.ns.bankingapp.service;

import com.ns.bankingapp.exception.UserNotFoundException;
import  com.ns.bankingapp.model.Role;
import com.ns.bankingapp.model.User;
import com.ns.bankingapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username);

        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found");
        }else {
            log.info("user {} found", username);
        }
        Collection<SimpleGrantedAuthority> authorities =
                new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }


    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User created = userRepo.save(user);
        log.info("User with username: {} and role: {} was successfully created",
                created.getUsername(),created.getRole().toString());
        return created;
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public User getUser(String username) {
        return userRepo.findUserByUsername(username);
    }

    public User saveClient(String username, String password) {
        User user = new User(username, passwordEncoder.encode(password),Role.CLIENT);
        User client = userRepo.save(user);
        log.info("Client with username: {} was successfully created", client.getUsername());
        return client;
    }

    public User saveTeller(String username, String password) {
        User user = new User(username, passwordEncoder.encode(password), Role.TELLER);
        User teller = userRepo.save(user);
        log.info("Teller with username: {} was successfully created", teller.getUsername());
        return teller;
    }

    public List<User> getClients() {
        return userRepo.findByRole(Role.CLIENT);
    }

    public List<User> getTellers() {
        return userRepo.findByRole(Role.TELLER);
    }

    public User getUserById(Long userId) throws UserNotFoundException {
        Optional<User> optionalUser = userRepo.findById(userId);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }else {
            throw new UserNotFoundException("User Not Found!");
        }
    }
}
