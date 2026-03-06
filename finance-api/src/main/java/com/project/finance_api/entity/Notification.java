package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import com.project.finance_api.enums.NotificationPriority;
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
public class Notification extends EntityDetails {
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationPriority priority;
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
