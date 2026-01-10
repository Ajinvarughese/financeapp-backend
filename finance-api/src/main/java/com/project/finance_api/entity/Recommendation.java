package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recommendation extends EntityDetails {

    private String assetName;

    private String assetDescription;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
