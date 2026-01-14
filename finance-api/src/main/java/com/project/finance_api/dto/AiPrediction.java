package com.project.finance_api.dto;

import com.project.finance_api.enums.RiskClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiPrediction {
    private RiskClass riskClass;
    private String description;
}
