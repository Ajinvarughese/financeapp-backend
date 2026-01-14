package com.project.finance_api.service;

import com.project.finance_api.dto.AiPrediction;
import com.project.finance_api.entity.AiChat;
import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import com.project.finance_api.enums.TextFrom;
import com.project.finance_api.repository.AssetRepository;
import com.project.finance_api.repository.LiabillityRepository;
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
public class LiabilityService {

    private final LiabillityRepository liabillityRepository;
    private final AssetService assetService;

    public Liability createLiability(Liability liability) {
        List<Asset> existingAssets = assetService.getAssetsByUser(liability.getUser());
        List<Liability> existingLiabilities = liabillityRepository.findByUserId(liability.getUser().getId());

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/ai/liability";

        Map<String, Object> request = new HashMap<>();
        request.put("assets", existingAssets);
        request.put("liabilities", existingLiabilities);
        request.put("newLiability", liability);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {

            AiPrediction response = restTemplate.postForObject(
                    url,
                    entity,
                    AiPrediction.class
            );

            assert response != null;

            liability.setRiskClass(response.getRiskClass());
            liability.setAiResponse(response.getDescription());
            return liabillityRepository.save(liability);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Liability> getAllLiability() {
        return liabillityRepository.findAll();
    }


    public Liability getLiabilityById(Long id) {
        return liabillityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public List<Liability> getLiabilityByUser(User user) {
        return liabillityRepository.findByUserId(user.getId());
    }


    public Liability updateLiability(Long id, Liability liability) {

        Liability existing = getLiabilityById(id);
        existing.setName(liability.getName());
        existing.setAmount(liability.getAmount());
        existing.setInterest(liability.getInterest());
        existing.setMonths(liability.getMonths());
        existing.setExpense(liability.getExpense());
        existing.setRiskClass(liability.getRiskClass());
        existing.setNote(liability.getNote());

        return liabillityRepository.save(existing);
    }


    public void deleteLiability(Long id) {
        liabillityRepository.deleteById(id);
    }

}

