package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import com.project.finance_api.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankStatement extends EntityDetails {
    private LocalDateTime date;

    private String particular;

    private String refNumber;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
