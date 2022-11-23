package com.ns.bankingapp.api;

import com.ns.bankingapp.model.User;
import com.ns.bankingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class UserController {

    private final UserService userService;


    @PostMapping("user/client/create")
    public ResponseEntity<User> createClient(@RequestParam String username, @RequestParam String password){
         return ResponseEntity.ok().body(userService.saveClient(username, password));
    }

    @GetMapping("user/client/all")
    public ResponseEntity<List<User>> getAllClients(){
        return ResponseEntity.ok().body(userService.getClients());
    }


    @PostMapping("user/teller/create")
    public ResponseEntity<User> createTeller(@RequestParam String username, @RequestParam String password){
        return ResponseEntity.ok().body(userService.saveTeller(username, password));
    }

    @GetMapping("user/teller/all")
    public ResponseEntity<List<User>> getAllTellers(){
        return ResponseEntity.ok().body(userService.getTellers());
    }
}
