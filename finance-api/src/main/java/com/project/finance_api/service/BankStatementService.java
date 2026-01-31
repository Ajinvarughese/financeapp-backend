package com.project.finance_api.service;

import com.project.finance_api.exceptions.DuplicateResourceException;
import com.project.finance_api.entity.BankStatement;
import com.project.finance_api.entity.User;
import com.project.finance_api.repository.BankStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankStatementService {
    private final BankStatementRepository bankStatementRepository;

    public List<BankStatement> getStatementsOfUser(User user) {
        return bankStatementRepository.findByUserId(user.getId());
    }

    public List<BankStatement> getAllStatements() {
        return bankStatementRepository.findAll();
    }

    public BankStatement addNewStatement(BankStatement bankStatement) {
        return bankStatementRepository.save(bankStatement);
    }

    public List<BankStatement> addStatementFromDoc(
            MultipartFile file,
            User user
    ) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/extractPdf";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        BankStatement[] response = restTemplate.postForObject(
                url,
                request,
                BankStatement[].class
        );

        if (response == null || response.length == 0) {
            return Collections.emptyList();
        }

        List<BankStatement> extracted = Arrays.asList(response);

        /* ---------------- CHECK DUPLICATION ---------------- */

        int firstNewIndex = -1;

        for (int i = 0; i < extracted.size(); i++) {
            BankStatement stmt = extracted.get(i);

            boolean exists = bankStatementRepository
                    .findByRefNumberAndUser(stmt.getRefNumber(), user)
                    .isPresent();

            if (!exists) {
                firstNewIndex = i;
                break;
            }
        }

        // CASE 1: FULL DUPLICATE
        if (firstNewIndex == -1) {
            throw new DuplicateResourceException(
                    "Bank statement already uploaded"
            );
        }

        // CASE 2 or 3: PARTIAL / FRESH
        List<BankStatement> newStatements =
                extracted.subList(firstNewIndex, extracted.size());

        // attach user
        for (BankStatement stmt : newStatements) {
            stmt.setUser(user);
        }

        return bankStatementRepository.saveAll(newStatements);
    }
    
    public void deleteStatementByUser(User user) {
        bankStatementRepository.deleteByUser(user);
    }
}

