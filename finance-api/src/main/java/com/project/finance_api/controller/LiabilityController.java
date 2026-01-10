package com.project.finance_api.controller;


import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.LiabilityService;
import com.project.finance_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/liability")
@RequiredArgsConstructor
public class LiabilityController {
    private final LiabilityService liabilityService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Liability> createLiability(@RequestBody Liability liability) {
        return ResponseEntity.ok(liabilityService.createLiability(liability));
    }

    @GetMapping
    public ResponseEntity<List<Liability>> getAllLiability() {
        return ResponseEntity.ok(liabilityService.getAllLiability());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Liability> getLiabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(liabilityService.getLiabilityById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Liability>> getAssetsByUser(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();
        User user = userService.getUserByToken(token);
        return ResponseEntity.ok(liabilityService.getLiabilityByUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Liability> updateLiability(
            @PathVariable Long id,
            @RequestBody Liability liability
    ) {
        return ResponseEntity.ok(liabilityService.updateLiability(id, liability));
    }

    @DeleteMapping("/{id}")
    public void deleteLiability(@PathVariable Long id) {
        liabilityService.deleteLiability(id);
    }

}
