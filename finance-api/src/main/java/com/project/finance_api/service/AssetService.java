package com.project.finance_api.service;

import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.User;
import com.project.finance_api.repository.AssetRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }


    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }


    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public List<Asset> getAssetsByUser(User user) {
        return assetRepository.findByUserId(user.getId());
    }


    public Asset updateAsset(Long id, Asset asset) {
        Asset existing = getAssetById(id);

        existing.setName(asset.getName());
        existing.setIncome(asset.getIncome());
        existing.setExpense(asset.getExpense());
        existing.setNotes(asset.getNotes());
        existing.setUser(asset.getUser());

        return assetRepository.save(existing);
    }


    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

}
