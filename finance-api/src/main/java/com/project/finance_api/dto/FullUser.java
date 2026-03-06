package com.project.finance_api.dto;

import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullUser {
    private User user;
    private List<Asset> assets;
    private List<Liability> liabilities;
}
