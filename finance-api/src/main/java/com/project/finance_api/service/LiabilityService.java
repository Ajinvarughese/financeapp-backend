package com.project.finance_api.service;

import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import com.project.finance_api.repository.AssetRepository;
import com.project.finance_api.repository.LiabillityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LiabilityService {

    private final LiabillityRepository liabillityRepository;

    public Liability createLiability(Liability liability) {
        return liabillityRepository.save(liability);
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
        existing.setRiskPercent(liability.getRiskPercent());
        existing.setNote(liability.getNote());

        return liabillityRepository.save(existing);
    }


    public void deleteLiability(Long id) {
        liabillityRepository.deleteById(id);
    }

}

