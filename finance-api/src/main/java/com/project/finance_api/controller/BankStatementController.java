package com.project.finance_api.controller;

import com.project.finance_api.entity.BankStatement;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.BankStatementService;
import com.project.finance_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/bankstatement")
@RequiredArgsConstructor
public class BankStatementController {
    private final BankStatementService bankStatementService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<BankStatement>> getAllStatements() {
        return ResponseEntity.ok(bankStatementService.getAllStatements());
    }

    @GetMapping("/user")
    public ResponseEntity<List<BankStatement>> getStatementsOfUser(
            @RequestHeader("Authorization") String authHeader) {
            String token = authHeader.replace("Bearer ", "").trim();
            User user = userService.getUserByToken(token);

            return ResponseEntity.ok(bankStatementService.getStatementsOfUser(user));
    }

    @PostMapping(path = "/file/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<BankStatement>> uploadDoc(@RequestParam("file") MultipartFile file, @RequestParam("user") User user) throws IOException {
            return ResponseEntity.ok(bankStatementService.addStatementFromDoc(file, user));
    }

    @PostMapping
    public ResponseEntity<BankStatement> addNewStatement(@RequestBody BankStatement statement) {
        return ResponseEntity.ok(bankStatementService.addNewStatement(statement));
    }

    @DeleteMapping
    public void deleteStatements(
            @RequestHeader("Authorization") String authHeader) {
            String token = authHeader.replace("Bearer ", "").trim();
            User user = userService.getUserByToken(token);
            bankStatementService.deleteStatementByUser(user);
    }
}
