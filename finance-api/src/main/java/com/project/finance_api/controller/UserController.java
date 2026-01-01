package com.project.finance_api.controller;

import com.project.finance_api.dto.Login;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }


    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }


    @GetMapping("/auth/token")
    public ResponseEntity<User> getUserByToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer", "");
        return ResponseEntity.ok(userService.getUserByToken(token));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authExistingUser(@RequestBody Login login) {
        return ResponseEntity.ok(userService.authExistingUser(login));
    }

}
