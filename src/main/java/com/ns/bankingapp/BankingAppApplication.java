package com.ns.bankingapp;

import com.ns.bankingapp.model.Role;
import com.ns.bankingapp.model.User;
import com.ns.bankingapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BankingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAppApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {


            userService.saveUser(new User("admin","password", Role.ADMIN));
            userService.saveClient("client","password");
            userService.saveTeller("teller","password");

        };
    }



}
