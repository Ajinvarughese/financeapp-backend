package com.project.finance_api.controller;

import com.project.finance_api.dto.AiChatRequest;
import com.project.finance_api.entity.AiChat;
import com.project.finance_api.entity.User;
import com.project.finance_api.enums.TextFrom;
import com.project.finance_api.service.AiChatService;
import com.project.finance_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ai/chat")
@RequiredArgsConstructor
public class AiChatController {
    private final AiChatService aiChatService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AiChat> newChatWithAI(@RequestBody AiChatRequest req) {

        User user = userService.getUserById(req.userId());

        AiChat aiChat = new AiChat();
        aiChat.setText(req.text());
        aiChat.setUser(user);
        aiChat.setTextFrom(TextFrom.USER);

        return ResponseEntity.ok(aiChatService.generateAiResponse(aiChat));
    }

    @GetMapping
    public ResponseEntity<List<AiChat>> getUserChat(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        User user = userService.getUserByToken(token);
        return ResponseEntity.ok(aiChatService.getAllChatByUser(user.getId()));
    }

    @DeleteMapping
    public void deleteChatsByUser(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        User user = userService.getUserByToken(token);
        aiChatService.deleteAllChatsByUserId(user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        aiChatService.deleteChat(id);
    }
}
