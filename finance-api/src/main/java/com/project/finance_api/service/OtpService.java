package com.project.finance_api.service;

import com.project.finance_api.entity.OTP;
import com.project.finance_api.exceptions.OtpExpiredException;
import com.project.finance_api.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public OTP createOTP(OTP otp) {
        String generatedOtp = generateOTP();
        Optional<OTP> existingOtp =
                otpRepository.findByEmail(otp.getEmail());

        if (existingOtp.isPresent()) {
            OTP oldOtp = existingOtp.get();
            OTP responseOtp = new OTP(generatedOtp, oldOtp.getEmail());
            oldOtp.setOtp(passwordEncoder.encode(generatedOtp));
            otpRepository.save(oldOtp);
            return responseOtp;
        }

        OTP responseOtp = new OTP(generatedOtp, otp.getEmail());
        otp.setOtp(passwordEncoder.encode(generatedOtp));

        otpRepository.save(otp);
        return  responseOtp;
    }

    public OTP validateOTP(OTP otp) {
        OTP existing = otpRepository.findByEmail(otp.getEmail())
                .orElseThrow(() -> new OtpExpiredException("Otp Expired"));
        if(passwordEncoder.matches(otp.getOtp(), existing.getOtp())) {
            return existing;
        }
        throw new OtpExpiredException("OTP is wrong");
    }

    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // runs every 1 minute
    public void deleteExpiredOTPs() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);
        otpRepository.deleteByCreatedAtBefore(expiryTime);
    }
}
