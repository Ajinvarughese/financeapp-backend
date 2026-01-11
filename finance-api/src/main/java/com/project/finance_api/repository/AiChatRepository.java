package com.project.finance_api.repository;

import com.project.finance_api.entity.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiChatRepository extends JpaRepository<AiChat, Long> {
    List<AiChat> findByUserId(Long id);
    void deleteByUserId(Long id);
}
