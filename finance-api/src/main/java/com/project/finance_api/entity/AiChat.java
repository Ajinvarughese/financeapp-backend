package com.project.finance_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.finance_api.component.EntityDetails;
import com.project.finance_api.enums.TextFrom;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AiChat extends EntityDetails {

    @Column(columnDefinition = "MEDIUMTEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    private TextFrom textFrom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

}
