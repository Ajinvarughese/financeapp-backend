package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Asset extends EntityDetails {

    private String name;

    private Double income;

    // total amount spent on this asset
    private Double expense;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String notes;
}
