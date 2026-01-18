package com.project.finance_api.service;

import com.project.finance_api.dto.ChatLog;
import com.project.finance_api.entity.AiChat;
import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import com.project.finance_api.enums.TextFrom;
import com.project.finance_api.repository.AiChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepository aiChatRepository;
    private final UserService userService;
    private final AssetService assetService;
    private final LiabilityService liabilityService;

    public List<AiChat> getAllChatByUser(Long id) {
        userService.getUserById(id); // check user exists
        return aiChatRepository.findByUserId(id);
    }

    private void addNewChat(AiChat aiChat) {
        aiChatRepository.save(aiChat);
    }

    @Transactional
    public void deleteChat(Long id) {
        aiChatRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllChatsByUserId(Long id) {
        aiChatRepository.deleteByUserId(id);
    }


    public AiChat generateAiResponse(AiChat aiChat) {

        List<Asset> assets = assetService.getAssetsByUser(aiChat.getUser());
        List<Liability> liabilities = liabilityService.getLiabilityByUser(aiChat.getUser());
        User user = userService.getUserById(aiChat.getUser().getId());

        List<AiChat> existingAiChat =
                aiChatRepository.findByUserId(aiChat.getUser().getId());

        List<ChatLog> chatLogs = new ArrayList<>();

        for (AiChat chat : existingAiChat) {
            String role = chat.getTextFrom() == TextFrom.USER
                    ? "user"
                    : "assistant";

            chatLogs.add(new ChatLog(
                    role,
                    chat.getText(),
                    chat.getCreatedAt()
            ));
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/ai/chat";

        Map<String, Object> request = new HashMap<>();
        request.put("prompt", aiChat.getText());
        request.put("chatLog", chatLogs);
        request.put("asset", assets);
        request.put("liability", liabilities);
        request.put("user", user.getFirstName()+" "+user.getLastName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        try {
            // save user message first
            addNewChat(aiChat);

            String responseText = restTemplate.postForObject(
                    url,
                    entity,
                    String.class
            );

            AiChat aiResponse = new AiChat();
            aiResponse.setText(responseText);
            aiResponse.setTextFrom(TextFrom.ASSISTANT);
            aiResponse.setUser(aiChat.getUser());

            return aiChatRepository.save(aiResponse);

        } catch (RestClientException e) {
            throw new RuntimeException("AI service failed", e);
        }
    }
}