package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import com.project.finance_api.enums.FeedbackStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends EntityDetails {

    private String title;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String comment;

    private String document;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus feedbackStatus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
