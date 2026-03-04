package com.project.finance_api.repository;

import com.project.finance_api.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Long> {
    void deleteByCreatedAtBefore(LocalDateTime time);
    Optional<OTP> findByEmail(String email);
}