package com.project.finance_api.service;

import com.project.finance_api.dto.AiDataset;
import com.project.finance_api.dto.AiPrediction;
import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import com.project.finance_api.repository.LiabilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LiabilityService {

    private final LiabilityRepository liabilityRepository;
    private final AssetService assetService;

    public Liability createLiability(Liability liability) {

        List<Asset> existingAssets = assetService.getAssetsByUser(liability.getUser());
        List<Liability> existingLiabilities =
                liabilityRepository.findByUserId(liability.getUser().getId());

        AiDataset aiDataset = getAiDataset(liability, existingAssets, existingLiabilities);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/ai/risk";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiDataset> entity = new HttpEntity<>(aiDataset, headers);

        try {
            AiPrediction response = restTemplate.postForObject(
                    url,
                    entity,
                    AiPrediction.class
            );

            assert response != null;

            liability.setRiskClass(response.getRiskClass());
            liability.setAiResponse(response.getDescription());
            return liabilityRepository.save(liability);

        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }


    private static @NonNull AiDataset getAiDataset(Liability liability, List<Asset> existingAssets, List<Liability> existingLiabilities) {
        double total_assets = 0.0;
        double total_liability = 0.0;

        for(Asset userAsset : existingAssets) {
            total_assets += userAsset.getIncome() - userAsset.getExpense();
        }
        for(Liability userLiability : existingLiabilities) {
            total_liability += userLiability.getEmi();
        }

        return new AiDataset(
                total_assets,
                total_liability,
                liability.getAmount(),
                liability.getEmi()
        );
    }


    public List<Liability> getAllLiability() {
        return liabilityRepository.findAll();
    }


    public Liability getLiabilityById(Long id) {
        return liabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public List<Liability> getLiabilityByUser(User user) {
        return liabilityRepository.findByUserId(user.getId());
    }


    public Liability updateLiability(Long id, Liability liability) {

        Liability existing = getLiabilityById(id);
        existing.setName(liability.getName());
        existing.setAmount(liability.getAmount());
        existing.setInterest(liability.getInterest());
        existing.setMonths(liability.getMonths());
        existing.setEmi(liability.getEmi());
        existing.setRiskClass(liability.getRiskClass());
        existing.setNote(liability.getNote());

        return liabilityRepository.save(existing);
    }


    @Transactional
    public void deleteLiability(Long id) {
        liabilityRepository.deleteById(id);
    }

}

