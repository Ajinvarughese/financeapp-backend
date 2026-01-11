package com.project.finance_api.service;

import com.project.finance_api.entity.AiChat;
import com.project.finance_api.entity.User;
import com.project.finance_api.enums.TextFrom;
import com.project.finance_api.repository.AiChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepository aiChatRepository;
    private final UserService userService;

    public List<AiChat> getAllChatByUser(Long id) {
        userService.getUserById(id); // check user exists
        return aiChatRepository.findByUserId(id);
    }

    private AiChat addNewChat(AiChat aiChat) {
        return aiChatRepository.save(aiChat);
    }

    public void deleteChat(Long id) {
        aiChatRepository.deleteById(id);
    }

    public void deleteAllChatsByUserId(Long id) {
        aiChatRepository.deleteByUserId(id);
    }


    public AiChat generateAiResponse(AiChat aiChat) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/ai/chat";

        Map<String, String> request = new HashMap<>();
        request.put("prompt", aiChat.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            addNewChat(aiChat);

            Map<?, ?> response = restTemplate.postForObject(
                url,
                entity,
                Map.class
            );

            AiChat aiResponse = new AiChat();
            aiResponse.setText((String) response.get("text"));
            aiResponse.setTextFrom(TextFrom.AI);
            aiResponse.setUser(aiChat.getUser());

            return aiChatRepository.save(aiResponse);

        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

}
