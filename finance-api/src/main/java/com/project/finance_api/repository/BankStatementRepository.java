package com.project.finance_api.repository;

import com.project.finance_api.entity.BankStatement;
import com.project.finance_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    List<BankStatement> findByUserId(Long id);
    Optional<BankStatement> findTopByUserOrderByDateDesc(User user);
    Optional<BankStatement> findByRefNumberAndUser(String refNumber, User user);
    void deleteByUser(User user);
}
