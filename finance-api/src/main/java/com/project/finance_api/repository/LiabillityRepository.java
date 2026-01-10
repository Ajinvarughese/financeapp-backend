package com.project.finance_api.repository;

import com.project.finance_api.entity.Liability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiabillityRepository extends JpaRepository<Liability, Long> {
    List<Liability> findByUserId(Long id);
}
