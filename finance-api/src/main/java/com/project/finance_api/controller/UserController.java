package com.project.finance_api.controller;

import com.project.finance_api.dto.Login;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }


    @PostMapping("/register")
    public ResponseEntity<Login> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }


    @GetMapping("/auth/token")
    public ResponseEntity<?> getUserByToken(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();


        try {
            User user = userService.getUserByToken(token);
            return ResponseEntity.ok(user);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(
                Map.of(
                    "status", 401,
                    "error", "UNAUTHORIZED",
                    "message", "Token expired. Please login again."
                )
            );
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Login> authExistingUser(@RequestBody Login login) {
        return ResponseEntity.ok(userService.authExistingUser(login));
    }

}
