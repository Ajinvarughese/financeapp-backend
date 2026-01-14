package com.project.finance_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatLog {
    private String role;
    private String content;
    private LocalDateTime time;
}
