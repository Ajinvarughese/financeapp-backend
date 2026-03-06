package com.project.finance_api.dto;

import com.project.finance_api.entity.Feedback;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Feedback feedback;
    private String response;
}
