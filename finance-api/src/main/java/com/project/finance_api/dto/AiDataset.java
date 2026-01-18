package com.project.finance_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AiDataset {
    private double total_assets;
    private double total_liabilities;
    private double new_liability;
    private double monthly_emi;
}
