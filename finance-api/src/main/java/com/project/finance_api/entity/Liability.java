package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import com.project.finance_api.enums.RiskClass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Liability extends EntityDetails {

    private String name;

    private Double amount;

    private Double interest;

    private Integer months;

    private Double expense;

    private String note;

    private RiskClass riskClass;

    private String aiResponse;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
